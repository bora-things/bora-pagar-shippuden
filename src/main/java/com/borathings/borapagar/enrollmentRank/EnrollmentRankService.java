package com.borathings.borapagar.enrollmentRank;

import com.borathings.borapagar.classroom.ClassroomService;
import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import com.borathings.borapagar.enrollmentRank.dto.EnrollmentRequestDTO;
import com.borathings.borapagar.enrollmentRank.dto.EnrollmentRequestEnriched;
import com.borathings.borapagar.enrollmentRank.dto.EnrollmentResponseDTO;
import com.borathings.borapagar.enrollmentRank.enums.PriorityType;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.student.index.StudentIndexService;
import com.borathings.borapagar.student.takenComponent.TakenComponentService;
import com.nimbusds.jose.util.Pair;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
@RequiredArgsConstructor
public class EnrollmentRankService {
    @Qualifier("serviceRestClient")
    private final RestClient serviceRestClient;

    private final ClassroomService classroomService;

    private final EnrollmentRankRepository enrollmentRankRepository;
    private final StudentService studentService;


    private static final Logger logger = LoggerFactory.getLogger(EnrollmentRankService.class);

    public Map<Long, List<EnrollmentRequestDTO>> getEnrollmentRequests(List<Long> studentsIds, Integer year) {
        Map<Long, List<EnrollmentRequestDTO>> map = new HashMap<>();
        logger.info("Buscando enrollments para {} estudantes", studentsIds.size());
        for (int i = 0; i < studentsIds.size(); i++) {
            Long studentId = studentsIds.get(i);
            logger.info("Enrollment requests for student " + studentId + " posicao: " + (i + 1));

            try {
                List<EnrollmentRequestDTO> list = serviceRestClient
                        .get()
                        .uri("/matricula/v1/solicitacoes-matriculas?id-discente=" + studentId + "&ano=" + year + "&periodo=2")
                        .attributes(clientRegistrationId("sigaa"))
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<EnrollmentRequestDTO>>() {
                        });

                map.put(studentId, list != null ? list : new ArrayList<>());

            } catch (HttpClientErrorException.TooManyRequests e) {
                logger.warn("Rate limit (429) atingido ao buscar estudante {}. Pausando por 1 minuto...", studentId);

                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    logger.error("Pausa do rate limit interrompida.", ie);
                    throw new RuntimeException("Pausa interrompida", ie);
                }

                logger.info("Retomando... Retentando estudante {} (posicao {}).", studentId, (i + 1));
                i--;

            } catch (Exception e) {
                logger.error("Falha ao buscar dados para o estudante {}. Pulando este estudante.", studentId, e);
                map.put(studentId, new ArrayList<>());
            }
        }
        return map;
    }

    public Map<Long, List<EnrollmentRequestDTO>> getRankedEnrollmentsByClass(List<Long> studentsIds, Integer year) {
        Map<Long, List<EnrollmentRequestDTO>> requestsByStudent = getEnrollmentRequests(studentsIds, year);

        List<EnrollmentRequestDTO> allRequests = requestsByStudent.values().stream()
                .flatMap(List::stream)
                .toList();

        Map<Long, List<EnrollmentRequestDTO>> requestsByClass = allRequests.stream()
                .collect(Collectors.groupingBy(EnrollmentRequestDTO::classId));

        List<ClassroomDTO> classrooms = classroomService.fetchClassrooms(requestsByClass.keySet().stream().collect(Collectors.toSet()));

        Map<Long, ClassroomDTO> enrollmentClassroomsMap = classrooms.stream().collect(Collectors.toMap(
                ClassroomDTO::classroomId,
                classroom -> classroom
        ));

        Map<Long, List<EnrollmentRequestDTO>> finalRequestsByClass = new HashMap<>();

        Comparator<EnrollmentRequestDTO> cheapSorter =
                Comparator.comparing(req -> PriorityType.fromId(req.priorityTypeId()));


        requestsByClass.forEach((classId, requestList) -> {
            ClassroomDTO classroom = enrollmentClassroomsMap.get(classId);
            if (classroom == null) {
                return;
            }
            Integer capacity = classroom.capacity();

            requestList.sort(cheapSorter);

            if (requestList.size() <= capacity) {
                finalRequestsByClass.put(classId, requestList);
                return;
            }


            PriorityType lastInPriority = PriorityType.fromId(requestList.get(capacity - 1).priorityTypeId());

            PriorityType firstOutPriority = PriorityType.fromId(requestList.get(capacity).priorityTypeId());

            if (lastInPriority.ordinal() < firstOutPriority.ordinal()) {
                finalRequestsByClass.put(classId, requestList);
                return;
            }

            List<EnrollmentRequestDTO> safeApproved = requestList.stream()
                    .filter(req -> PriorityType.fromId(req.priorityTypeId()).ordinal() < lastInPriority.ordinal())
                    .collect(toList());

            List<EnrollmentRequestDTO> bubbleGroup = requestList.stream()
                    .filter(req -> PriorityType.fromId(req.priorityTypeId()) == lastInPriority)
                    .collect(toList());

            List<EnrollmentRequestDTO> autoRejected = requestList.stream()
                    .filter(req -> PriorityType.fromId(req.priorityTypeId()).ordinal() > lastInPriority.ordinal())
                    .collect(toList());


            List<EnrollmentRequestDTO> flaggedBubbleGroup = bubbleGroup.stream()
                    .map(request -> new EnrollmentRequestDTO(request, true)) // true = uncertainRanking
                    .collect(toList());


            List<EnrollmentRequestDTO> finalClassList = new ArrayList<>(safeApproved);
            finalClassList.addAll(flaggedBubbleGroup);
            finalClassList.addAll(autoRejected);

            finalRequestsByClass.put(classId, finalClassList);
        });

        return finalRequestsByClass;
    }


    @Transactional
    @CacheEvict(value = "enrollmentRankByComponent", allEntries = true) // Limpa o cache após a execução
    public void processAndSaveEnrollmentRanks() {
        logger.info("Iniciando tarefa agendada: Processando e salvando ranking de matrículas...");

        try {
            int currentYear = 2024;
            List<Long> studentsIds = studentService.fetchAllStudentsFromTI();

            Map<Long, List<EnrollmentRequestDTO>> rankedMap = getRankedEnrollmentsByClass(studentsIds, currentYear);

            if (rankedMap.isEmpty()) {
                logger.info("Nenhum ranking para processar.");
                return;
            }

            List<Long> classIdsToUpdate = new ArrayList<>(rankedMap.keySet());
            logger.info("Limpando rankings antigos para {} turmas.", classIdsToUpdate.size());
            enrollmentRankRepository.deleteAllByClassIdIn(classIdsToUpdate);

            List<EnrollmentRankEntity> entitiesToSave = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            for (Map.Entry<Long, List<EnrollmentRequestDTO>> entry : rankedMap.entrySet()) {
                Long classId = entry.getKey(); // ID da Turma
                List<EnrollmentRequestDTO> rankedRequests = entry.getValue();
                if (rankedRequests.isEmpty()) {
                    continue;
                }

                String componentCode = rankedRequests.get(0).componentCode();

                for (int i = 0; i < rankedRequests.size(); i++) {
                    EnrollmentRequestDTO requestDTO = rankedRequests.get(i);
                    int rankPosition = i + 1;

                    EnrollmentRankEntity rankEntity = new EnrollmentRankEntity();
                    rankEntity.setComponentCode(componentCode); // Salva o código do componente
                    rankEntity.setClassId(classId); // Salva o ID da turma
                    rankEntity.setStudentId(requestDTO.studentId());
                    rankEntity.setRankPosition(rankPosition);
                    rankEntity.setPriorityTypeId(requestDTO.priorityTypeId());
                    rankEntity.setComponentName(requestDTO.componentName());
                    rankEntity.setEnrollmentComponentId(requestDTO.enrollmentComponentId());
                    rankEntity.setProcessingTimestamp(now);
                    rankEntity.setUncertainRanking(requestDTO.uncertainRanking());
                    entitiesToSave.add(rankEntity);
                }
            }

            logger.info("Salvando {} novos registros de ranking no banco de dados.", entitiesToSave.size());
            enrollmentRankRepository.saveAll(entitiesToSave);

            logger.info("Ranking de matrículas atualizado e salvo com sucesso!");

        } catch (Exception e) {
            logger.error("Falha crítica ao processar e salvar o ranking de matrículas.", e);
        }
    }

    public List<EnrollmentResponseDTO> getEnrollmentRanksByStudent(String userLogin) {
        StudentEntity student = studentService.findByUserLoginOrError(userLogin);

        int year = 2025;
        int period = 2;

        List<EnrollmentRankEntity> enrollments = enrollmentRankRepository.findAllByStudentId(student.getStudentId());

        Set<Long> allClassIds = enrollments.stream()
                .map(EnrollmentRankEntity::getClassId)
                .collect(Collectors.toSet());

        List<ClassroomDTO> enrollmentClassrooms = classroomService.fetchClassrooms(allClassIds);

        Map<Long, ClassroomDTO> enrollmentClassroomsMap = enrollmentClassrooms.stream()
                .collect(Collectors.toMap(
                        ClassroomDTO::classroomId,
                        classroom -> classroom
                ));

        List<EnrollmentRankEntity> uncertainRanks = enrollments.stream()
                .filter(item -> item.isUncertainRanking())
                .toList();

        List<EnrollmentRankEntity> certainRanks = enrollments.stream()
                .filter(item -> !item.isUncertainRanking())
                .toList();

        Set<Long> uncertainClassIds = uncertainRanks.stream()
                .map(EnrollmentRankEntity::getClassId)
                .collect(Collectors.toSet());

        Map<Long, Map<Long, Long>> concurrenceMap = new HashMap<>();

        if (!uncertainClassIds.isEmpty()) {
            List<Object[]> concurrenceCounts = enrollmentRankRepository.countRanksForClasses(uncertainClassIds);

            for (Object[] result : concurrenceCounts) {
                Long classId = (Long) result[0];
                Long priorityId = (Long) result[1];
                Long count = (Long) result[2];

                concurrenceMap
                        .computeIfAbsent(classId, k -> new HashMap<>())
                        .put(priorityId, count);
            }
        }

        List<EnrollmentResponseDTO> uncertainEnrollmentsResponseDtos = new ArrayList<>();
        for (EnrollmentRankEntity uncertainRank : uncertainRanks) {

            Long concorrence = concurrenceMap
                    .getOrDefault(uncertainRank.getClassId(), Collections.emptyMap())
                    .getOrDefault(uncertainRank.getPriorityTypeId(), 0L);

            EnrollmentResponseDTO response = new EnrollmentResponseDTO(
                    year,
                    period,
                    concorrence.intValue(),
                    uncertainRank,
                    enrollmentClassroomsMap.get(uncertainRank.getClassId())
            );
            uncertainEnrollmentsResponseDtos.add(response);
        }
        List<EnrollmentResponseDTO> certainEnrollmentResponseDTOs = certainRanks.stream()
                .map(item -> new EnrollmentResponseDTO(year, period, 0, item, enrollmentClassroomsMap.get(item.getClassId())))
                .toList();

        List<EnrollmentResponseDTO> responses = new ArrayList<>(certainEnrollmentResponseDTOs);
        responses.addAll(uncertainEnrollmentsResponseDtos);

        return responses;
    }


}
