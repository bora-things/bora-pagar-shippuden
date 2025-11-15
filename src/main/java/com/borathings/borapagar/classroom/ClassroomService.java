package com.borathings.borapagar.classroom;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import com.borathings.borapagar.classroom.dto.ClassroomResponseDTO;
import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Qualifier("serviceRestClient")
    final RestClient serviceRestClient;

    private final ClassroomMapper classroomMapper;

    private final StudentService studentService;

    private final ClassroomRepository classroomRepository;

    private final ComponentService componentService;

    @Async
    public CompletableFuture<Void> fetchClassroomAsync(StudentEntity student) {
        fetchClassroom(student.getId());
        return CompletableFuture.completedFuture(null);
    }

    @Transactional
    public void fetchClassroom(Long studentId) {
        try {
            StudentEntity student = studentService.findByIdWithClassrooms(studentId);

            List<ClassroomDTO> classroomDTOs = serviceRestClient
                    .get()
                    .uri("/turma/v1/turmas?id-discente=" + student.getStudentId())
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ClassroomDTO>>() {});

            if (classroomDTOs != null && !classroomDTOs.isEmpty()) {

                for (ClassroomDTO dto : classroomDTOs) {
                    ClassroomEntity classroom = classroomRepository
                            .findByClassroomId(dto.classroomId())
                            .orElseGet(() -> {
                                ClassroomEntity newClassroom = classroomMapper.toEntity(dto);
                                return classroomRepository.save(newClassroom);
                            });

                    if (!student.getClassrooms().contains(classroom)) {
                        student.getClassrooms().add(classroom);
                    }
                }

                studentService.saveStudent(student);
            }

        } catch (Exception ex) {
            logger.error("Exception at fetchClassrooms", ex);
        }
    }

    public List<ClassroomResponseDTO> findClassroomByStudent(String login) {
        StudentEntity student = studentService.findByUserLoginOrError(login);
        Set<ClassroomEntity> classrooms = student.getClassrooms();
        Set<String> componentCodes =
                classrooms.stream().map(ClassroomEntity::getComponentCode).collect(Collectors.toSet());

        final Integer studentMatrix = student.getCurricularMatrix();

        Map<String, ComponentResponseDTO> componentMap =
                componentService.findComponentMapPriorityMatrix(componentCodes, studentMatrix);

        try {
            List<CompletableFuture<ClassroomResponseDTO>> futures = classrooms.stream()
                    .map(item -> {
                        if (item.getComponentCode() != null) {
                            CompletableFuture<List<UserResponseDTO>> friendsFuture = studentService.findFriendsInClass(
                                    student.getUser(), item, student.getUser().getFriends());

                            ComponentResponseDTO component = componentMap.get(item.getComponentCode());

                            if (component == null) {
                                logger.warn(
                                        "Componente com código {} não encontrado para a turma {}",
                                        item.getComponentCode(),
                                        item.getId());
                                return CompletableFuture.completedFuture((ClassroomResponseDTO) null);
                            }

                            return friendsFuture.thenApply(
                                    friends -> classroomMapper.toResponseDTO(item, component, friends));
                        }
                        return CompletableFuture.completedFuture((ClassroomResponseDTO) null);
                    })
                    .toList();

            CompletableFuture<List<ClassroomResponseDTO>> allDoneFuture = sequence(futures);

            return allDoneFuture.get().stream().filter(Objects::nonNull).toList();

        } catch (Exception ex) {
            logger.error("Erro ao buscar turmas do aluno: {}", ex.getMessage(), ex);
            return null;
        }
    }

    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).toList());
    }

    public List<ClassroomResponseDTO> findClassroomByStudentId(Long studentId) {
        StudentEntity student = studentService.findByIdWithClassrooms(studentId);
        return findClassroomByStudent(student.getUser().getLogin());
    }

    public List<ClassroomDTO> fetchClassrooms(Set<Long> classroomIds) {

        List<ClassroomDTO> classrooms = new ArrayList<>();

        for (Long classroomId : classroomIds) {
            ClassroomDTO classroomDTO = serviceRestClient
                    .get()
                    .uri("/turma/v1/turmas/" + classroomId)
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<ClassroomDTO>() {});
            if (classroomDTO != null) {
                classrooms.add(classroomDTO);
            }
        }
        return classrooms;
    }

    public Map<Long, Integer> fetchClassroomsParticipants(Set<Long> classroomIds, Instant reenrollmentStart) {

        Map<Long, Integer> participantCounts = new HashMap<>();

        ParameterizedTypeReference<List<Map<String, Object>>> responseType = new ParameterizedTypeReference<>() {};

        final long reenrollmentStartMillis = reenrollmentStart.toEpochMilli();

        for (Long classroomId : classroomIds) {
            try {
                List<Map<String, Object>> participantsList = serviceRestClient
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/turma/v1/participantes")
                                .queryParam("id-turma", classroomId)
                                .queryParam("id-tipo-participante", 4)
                                .queryParam("limit", 100)
                                .build())
                        .attributes(clientRegistrationId("sigaa"))
                        .retrieve()
                        .body(responseType);

                if (participantsList != null) {
                    long count = participantsList.stream()
                            .filter(participant -> {
                                Object entryDateObj = participant.get("data-entrada-participante");

                                if (entryDateObj == null) {
                                    return false;
                                }

                                try {
                                    long entryDateMillis;
                                    if (entryDateObj instanceof Number) {
                                        entryDateMillis = ((Number) entryDateObj).longValue();
                                    } else {
                                        entryDateMillis = Long.parseLong(entryDateObj.toString());
                                    }

                                    return entryDateMillis < reenrollmentStartMillis;

                                } catch (Exception e) {
                                    logger.warn(
                                            "Não foi possível parsear 'data-entrada-participante': {} para turma {}",
                                            entryDateObj,
                                            classroomId);
                                    return false;
                                }
                            })
                            .count();

                    participantCounts.put(classroomId, (int) count);

                } else {
                    participantCounts.put(classroomId, 0);
                }

            } catch (Exception e) {
                logger.error("Erro ao buscar participantes da turma {}: {}", classroomId, e.getMessage());
                participantCounts.put(classroomId, 0);
            }
        }
        return participantCounts;
    }
}
