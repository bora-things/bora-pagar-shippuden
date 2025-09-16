package com.borathings.borapagar.classroom;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import com.borathings.borapagar.classroom.dto.ClassroomResponseDTO;
import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import com.borathings.borapagar.component.mapper.ComponentMapper;
import com.borathings.borapagar.component.repository.ComponentRepository;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
public class ClassroomService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("serviceRestClient")
    RestClient serviceRestClient;

    @Autowired
    private ClassroomMapper classroomMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private ComponentMapper componentMapper;

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
        List<String> componentCodes =
                classrooms.stream().map(ClassroomEntity::getComponentCode).toList();
        List<ComponentEntity> components = componentRepository.findAllByCodeInAndCurricularMatrixId(
                componentCodes, Integer.valueOf(student.getCurricularMatrix()));
        Map<String, ComponentResponseDTO> componentMap = components.stream()
                .collect(Collectors.toMap(
                        ComponentEntity::getCode,
                        component -> componentMapper.toResponseDTO(component),
                        (existing, replacement) -> existing));
        try {
            List<CompletableFuture<ClassroomResponseDTO>> futures = classrooms.stream()
                    .map(item -> {
                        if (item.getComponentCode() != null) {
                            CompletableFuture<List<UserResponseDTO>> friendsFuture = studentService.findFriendsInClass(
                                    student.getUser(), item, student.getUser().getFriends());
                            ComponentResponseDTO component = componentMap.get(item.getComponentCode());

                            return friendsFuture.thenApply(
                                    friends -> classroomMapper.toResponseDTO(item, component, friends));
                        }
                        return CompletableFuture.completedFuture((ClassroomResponseDTO) null);
                    })
                    .toList();

            CompletableFuture<List<ClassroomResponseDTO>> allDoneFuture = sequence(futures);
            return allDoneFuture.get();
        } catch (Exception ex) {
            logger.error("Erro", ex.getMessage());
            return null;
        }
    }

    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).toList());
    }
}
