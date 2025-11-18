package com.borathings.borapagar.enrollmentRank;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.academicCalendar.AcademicCalendarService;
import com.borathings.borapagar.academicCalendar.dto.AcademicCalendarResponseDTO;
import com.borathings.borapagar.classroom.ClassroomService;
import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import com.borathings.borapagar.enrollmentRank.dto.EnrollmentRequestDTO;
import com.borathings.borapagar.enrollmentRank.dto.EnrollmentResponseDTO;
import com.borathings.borapagar.enrollmentRank.enums.PriorityType;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.borathings.borapagar.student.interest.StudentSubjectInterestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class EnrollmentRankService {
    @Qualifier("serviceRestClient")
    private final RestClient serviceRestClient;

    private final ClassroomService classroomService;
    private final EnrollmentRankRepository enrollmentRankRepository;
    private final StudentService studentService;
    private final AcademicCalendarService calendarService;

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentRankService.class);

    private static final Comparator<EnrollmentRequestDTO> PRIORITY_SORTER =
            Comparator.comparing(req -> PriorityType.fromId(req.priorityTypeId()));
    private final ComponentService componentService;
    private final StudentSubjectInterestService studentSubjectInterestService;

    public Map<Long, List<EnrollmentRequestDTO>> getEnrollmentRequests(List<Long> studentsIds) {

        AcademicCalendarResponseDTO calendar = calendarService.getCurrentCalendar();
        Integer year = calendar.year();
        Integer period = calendar.period();
        Boolean reenrollment;
        Instant instantNow = Instant.now();
        if (instantNow.isAfter(calendar.reEnrollmentStart()) && instantNow.isBefore(calendar.reEnrollmentEnd())) {
            reenrollment = true;
        } else {
            reenrollment = false;
        }

        logger.info("Buscando {}", reenrollment ? "Re-Matriculas" : "Matriculas");
        Map<Long, List<EnrollmentRequestDTO>> map = new HashMap<>();
        logger.info("Buscando enrollments para {} estudantes", studentsIds.size());
        for (int i = 0; i < studentsIds.size(); i++) {
            Long studentId = studentsIds.get(i);
            logger.info("Enrollment requests for student " + studentId + " posicao: " + (i + 1));

            try {
                List<EnrollmentRequestDTO> list = serviceRestClient
                        .get()
                        .uri("/matricula/v1/solicitacoes-matriculas?id-discente=" + studentId + "&ano=" + year
                                + "&periodo=" + period)
                        .attributes(clientRegistrationId("sigaa"))
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<EnrollmentRequestDTO>>() {});

                List<EnrollmentRequestDTO> filteredList = list != null
                        ? list.stream()
                                .filter(item -> item.priorityTypeId() != null && item.isReEnrollment() == reenrollment)
                                .toList()
                        : new ArrayList<>();

                map.put(studentId, filteredList);

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

    public Map<Long, List<EnrollmentRequestDTO>> getRankedEnrollmentsByClass(List<Long> studentsIds) {
        Map<Long, List<EnrollmentRequestDTO>> requestsByClass = fetchAllRequestsGroupedByClass(studentsIds);
        Map<Long, ClassroomDTO> classroomMap = fetchClassroomMap(requestsByClass.keySet());
        AcademicCalendarResponseDTO calendar = calendarService.getCurrentCalendar();

        Map<Long, List<EnrollmentRequestDTO>> finalRequestsByClass = new HashMap<>();
        Map<Long, Integer> participantsCountByClass;

        Boolean reenrollment =
                requestsByClass.values().stream().findFirst().get().getFirst().isReEnrollment();
        if (reenrollment) {
            participantsCountByClass = classroomService.fetchClassroomsParticipants(
                    requestsByClass.keySet(), calendar.reEnrollmentStart());
        } else {
            participantsCountByClass = null;
        }

        requestsByClass.forEach((classId, requestList) -> {
            ClassroomDTO classroom = classroomMap.get(classId);
            int participantsCount = reenrollment ? participantsCountByClass.getOrDefault(classId, 0) : 0;
            if (classroom != null) {
                List<EnrollmentRequestDTO> processedList =
                        processClassEnrollments(requestList, classroom, participantsCount);
                finalRequestsByClass.put(classId, processedList);
            }
        });

        return finalRequestsByClass;
    }

    private Map<Long, List<EnrollmentRequestDTO>> fetchAllRequestsGroupedByClass(List<Long> studentsIds) {
        Map<Long, List<EnrollmentRequestDTO>> requestsByStudent = getEnrollmentRequests(studentsIds);

        List<EnrollmentRequestDTO> allRequests =
                requestsByStudent.values().stream().flatMap(List::stream).toList();

        return allRequests.stream().collect(Collectors.groupingBy(EnrollmentRequestDTO::classId));
    }

    private Map<Long, ClassroomDTO> fetchClassroomMap(Set<Long> classIds) {
        if (classIds == null || classIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<ClassroomDTO> classrooms = classroomService.fetchClassrooms(classIds);

        return classrooms.stream().collect(Collectors.toMap(ClassroomDTO::classroomId, classroom -> classroom));
    }

    private List<EnrollmentRequestDTO> processClassEnrollments(
            List<EnrollmentRequestDTO> requestList, ClassroomDTO classroom, int participantsCount) {
        requestList.sort(PRIORITY_SORTER);

        Integer capacity = classroom.capacity() - participantsCount;

        if (requestList.size() <= capacity) {
            return requestList;
        }
        if (capacity <= 0) {
            return List.of();
        }

        PriorityType lastInPriority =
                PriorityType.fromId(requestList.get(capacity - 1).priorityTypeId());
        PriorityType firstOutPriority =
                PriorityType.fromId(requestList.get(capacity).priorityTypeId());

        if (lastInPriority.ordinal() < firstOutPriority.ordinal()) {
            return requestList;
        }

        return handleBubbleCase(requestList, lastInPriority);
    }

    private List<EnrollmentRequestDTO> handleBubbleCase(
            List<EnrollmentRequestDTO> sortedRequestList, PriorityType bubblePriority) {
        List<EnrollmentRequestDTO> safeApproved = new ArrayList<>();
        List<EnrollmentRequestDTO> flaggedBubbleGroup = new ArrayList<>();
        List<EnrollmentRequestDTO> autoRejected = new ArrayList<>();

        int bubbleOrdinal = bubblePriority.ordinal();

        for (EnrollmentRequestDTO request : sortedRequestList) {
            int requestOrdinal = PriorityType.fromId(request.priorityTypeId()).ordinal();

            if (requestOrdinal < bubbleOrdinal) {
                safeApproved.add(request);
            } else if (requestOrdinal == bubbleOrdinal) {
                flaggedBubbleGroup.add(new EnrollmentRequestDTO(request, true)); // true = uncertainRanking
            } else {
                autoRejected.add(request);
            }
        }

        List<EnrollmentRequestDTO> finalClassList = new ArrayList<>(safeApproved);
        finalClassList.addAll(flaggedBubbleGroup);
        finalClassList.addAll(autoRejected);

        return finalClassList;
    }

    @Transactional
    @CacheEvict(value = "enrollmentRankByComponent", allEntries = true)
    public void processAndSaveEnrollmentRanks() {
        logger.info("Iniciando tarefa agendada: Processando e salvando ranking de matrículas...");

        try {
            List<Long> studentsIds = studentService.fetchAllStudentsFromTI();

            Map<Long, List<EnrollmentRequestDTO>> rankedMap = getRankedEnrollmentsByClass(studentsIds);

            if (rankedMap.isEmpty()) {
                logger.info("Nenhum ranking para processar.");
                return;
            }

            EnrollmentRequestDTO first =
                    rankedMap.values().stream().findFirst().get().getFirst();
            logger.info("Limpando rankings antigos para {} turmas.");
            enrollmentRankRepository.deleteAllByYearAndPeriod(first.year(), first.period());

            List<EnrollmentRankEntity> entitiesToSave = mapRankedRequestsToEntities(rankedMap);

            studentSubjectInterestService.softDeleteAllInterestsByYearAndPeriod(2026, 1);

            if (entitiesToSave.isEmpty()) {
                logger.info("Nenhuma entidade de ranking para salvar.");
                return;
            }

            logger.info("Salvando {} novos registros de ranking no banco de dados.", entitiesToSave.size());
            enrollmentRankRepository.saveAll(entitiesToSave);

            logger.info("Ranking de matrículas atualizado e salvo com sucesso!");

        } catch (Exception e) {
            logger.error("Falha crítica ao processar e salvar o ranking de matrículas.", e);
        }
    }

    private List<EnrollmentRankEntity> mapRankedRequestsToEntities(Map<Long, List<EnrollmentRequestDTO>> rankedMap) {
        List<EnrollmentRankEntity> entitiesToSave = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Map.Entry<Long, List<EnrollmentRequestDTO>> entry : rankedMap.entrySet()) {
            Long classId = entry.getKey();
            List<EnrollmentRequestDTO> rankedRequests = entry.getValue();
            if (rankedRequests.isEmpty()) {
                continue;
            }

            for (int i = 0; i < rankedRequests.size(); i++) {
                EnrollmentRequestDTO requestDTO = rankedRequests.get(i);
                int rankPosition = i + 1;

                EnrollmentRankEntity rankEntity = new EnrollmentRankEntity();
                rankEntity.setClassId(classId);
                rankEntity.setReenrollment(requestDTO.isReEnrollment());
                rankEntity.setStudentId(requestDTO.studentId());
                rankEntity.setRankPosition(rankPosition);
                rankEntity.setPriorityTypeId(requestDTO.priorityTypeId());
                rankEntity.setProcessingTimestamp(now);
                rankEntity.setUncertainRanking(requestDTO.uncertainRanking());
                rankEntity.setYear(requestDTO.year());
                rankEntity.setPeriod(requestDTO.period());
                entitiesToSave.add(rankEntity);
            }
        }
        return entitiesToSave;
    }

    public List<EnrollmentResponseDTO> getEnrollmentRanksByStudent(String userLogin, Boolean reEnrollment) {
        StudentEntity student = studentService.findByUserLoginOrError(userLogin);
        AcademicCalendarResponseDTO calendar = calendarService.getCurrentCalendar();
        Integer year = calendar.year();
        Integer period = calendar.period();

        List<EnrollmentRankEntity> enrollments =
                enrollmentRankRepository.findAllByStudentIdAndYearAndPeriodAndReenrollment(
                        student.getStudentId(), year, period, reEnrollment);

        if (enrollments.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> allClassIds =
                enrollments.stream().map(EnrollmentRankEntity::getClassId).collect(Collectors.toSet());

        Map<Long, ClassroomDTO> enrollmentClassroomsMap = fetchClassroomMap(allClassIds);

        Set<String> classComponentsCode = enrollmentClassroomsMap.values().stream()
                .map(item -> item.componentCode())
                .collect(Collectors.toSet());

        Map<String, ComponentResponseDTO> componentsMap =
                componentService.findComponentMapPriorityMatrix(classComponentsCode, student.getCurricularMatrix());

        List<EnrollmentRankEntity> uncertainRanks = enrollments.stream()
                .filter(EnrollmentRankEntity::isUncertainRanking)
                .toList();
        List<EnrollmentRankEntity> certainRanks =
                enrollments.stream().filter(item -> !item.isUncertainRanking()).toList();

        Set<Long> uncertainClassIds =
                uncertainRanks.stream().map(EnrollmentRankEntity::getClassId).collect(Collectors.toSet());

        Map<Long, Map<Long, Long>> concurrenceMap = buildConcurrenceMap(uncertainClassIds);
        Map<Long, Integer> classroomsParticipants;
        if (reEnrollment) {
            classroomsParticipants =
                    classroomService.fetchClassroomsParticipants(allClassIds, calendar.reEnrollmentStart());
        } else {
            classroomsParticipants = null;
        }

        List<EnrollmentResponseDTO> uncertainEnrollmentsResponseDtos = new ArrayList<>();
        for (EnrollmentRankEntity uncertainRank : uncertainRanks) {
            Long classId = uncertainRank.getClassId();
            Long studentPriorityId = uncertainRank.getPriorityTypeId();

            ClassroomDTO classroom = enrollmentClassroomsMap.get(classId);

            int totalSlots = reEnrollment
                    ? classroom.capacity() - classroomsParticipants.getOrDefault(classId, 0)
                    : classroom.capacity();

            Map<Long, Long> priorityCounts = concurrenceMap.getOrDefault(classId, Collections.emptyMap());

            int concurrenceCount =
                    priorityCounts.getOrDefault(studentPriorityId, 0L).intValue();

            long higherPriorityRequestsCount = 0;
            for (Map.Entry<Long, Long> entry : priorityCounts.entrySet()) {
                if (entry.getKey() < studentPriorityId) {
                    higherPriorityRequestsCount += entry.getValue();
                }
            }

            int remainingSlots = Math.max(0, (int) (totalSlots - higherPriorityRequestsCount));

            ComponentResponseDTO component = componentsMap.get(classroom.componentCode());

            EnrollmentResponseDTO response = new EnrollmentResponseDTO(
                    year, period, concurrenceCount, remainingSlots, uncertainRank, classroom, component);
            uncertainEnrollmentsResponseDtos.add(response);
        }

        List<EnrollmentResponseDTO> certainEnrollmentResponseDTOs = certainRanks.stream()
                .map(item -> {
                    ClassroomDTO classroom = enrollmentClassroomsMap.get(item.getClassId());
                    int participantsCount = reEnrollment ? classroomsParticipants.get(item.getClassId()) : 0;
                    int remainingSlots = reEnrollment ? classroom.capacity() - participantsCount : classroom.capacity
                            ();
                    ComponentResponseDTO component = componentsMap.get(classroom.componentCode());
                    return new EnrollmentResponseDTO(year, period, 0, remainingSlots, item, classroom, component);
                })
                .toList();

        List<EnrollmentResponseDTO> responses = new ArrayList<>(certainEnrollmentResponseDTOs);
        responses.addAll(uncertainEnrollmentsResponseDtos);

        return responses;
    }

    private Map<Long, Map<Long, Long>> buildConcurrenceMap(Set<Long> uncertainClassIds) {
        if (uncertainClassIds == null || uncertainClassIds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, Map<Long, Long>> concurrenceMap = new HashMap<>();
        List<Object[]> concurrenceCounts = enrollmentRankRepository.countRanksForClasses(uncertainClassIds);

        for (Object[] result : concurrenceCounts) {
            Long classId = (Long) result[0];
            Long priorityId = (Long) result[1];
            Long count = (Long) result[2];

            concurrenceMap.computeIfAbsent(classId, k -> new HashMap<>()).put(priorityId, count);
        }
        return concurrenceMap;
    }
}
