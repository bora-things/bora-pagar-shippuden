package com.borathings.borapagar.classroom;

import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import com.borathings.borapagar.classroom.dto.ClassroomResponseDTO;
import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import com.borathings.borapagar.component.mapper.ComponentMapper;
import com.borathings.borapagar.component.repository.ComponentRepository;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.student.dto.StudentClassResponseDTO;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class ClassroomService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("userRestClient")
    RestClient userRestClient;

    @Autowired
    private ClassroomMapper classroomMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private ComponentRepository componentRepository;
    @Autowired
    private ComponentMapper componentMapper;


    @Async
    public CompletableFuture<Void> fetchClassroom(StudentEntity student) {
        try {

            List<ClassroomDTO> classroomDTOs = userRestClient
                    .get()
                    .uri("https://api.info.ufrn.br/turma/v1/turmas?id-discente=" + student.getStudentId())
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ClassroomDTO>>() {
                    });

            if (classroomDTOs != null) {

                List<ClassroomEntity> classrooms = classroomDTOs.stream().map(item ->
                        toEntity(item, student)
                ).toList();
                classroomRepository.deleteAllByStudent(student);
                classroomRepository.saveAll(classrooms);
            }

        } catch (Exception ex) {
            logger.error("Exception at fetchWorkload: {}", ex.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }


    public List<ClassroomResponseDTO> findClassroomByStudent(String login){
        StudentEntity student = studentService.findByUserLoginOrError(login);

        List<ClassroomEntity> classrooms = classroomRepository.findAllByStudent(student);
        List<String> componentCodes=classrooms.stream().map(ClassroomEntity::getComponentCode).toList();
        List<ComponentEntity> components=componentRepository.findAllByCodeIn(componentCodes);
        Map<String, ComponentResponseDTO> componentMap = components.stream()
                .collect(Collectors.toMap(
                        ComponentEntity::getCode,
                        component ->componentMapper.toResponseDTO(component),
                        (existing, replacement) -> existing // mantém o primeiro, ignora os duplicados
                ));
        try{
        List<CompletableFuture<ClassroomResponseDTO>> futures = classrooms.stream()
                .map(item -> {
                    if (item.getComponentCode() != null) {
                        CompletableFuture<List<UserResponseDTO>> friendsFuture = studentService.findFriendsInClass(student.getUser(), item);
                        ComponentResponseDTO component = componentMap.get(item.getComponentCode());

                        return friendsFuture.thenApply(friends ->
                                classroomMapper.toResponseDTO(item, component, friends)
                        );
                    }
                    return CompletableFuture.completedFuture((ClassroomResponseDTO)null);
                })
                .toList();

        CompletableFuture<List<ClassroomResponseDTO>> allDoneFuture = sequence(futures);
        return allDoneFuture.get(); // .get() bloqueia aqui (só agora)
        }
        catch (Exception ex){
            logger.error("Erro",ex.getMessage());
        return null;
        }
    }

    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList()
                );
    }



    public ClassroomEntity toEntity(ClassroomDTO dto, StudentEntity student) {
        return ClassroomEntity.builder()
                .classroomId(dto.classroomId())
                .year(dto.year())
                .studentCapacity(dto.studentCapacity())
                .componentCode(dto.componentCode())
                .classroomCode(dto.classroomCode())
                .scheduleDescription(dto.scheduleDescription())
                .externalTeacherId(dto.externalTeacherId())
                .educationModeId(dto.educationModeId())
                .classroomStatusId(dto.classroomStatusId())
                .groupingClassroomId(dto.groupingClassroomId())
                .unitId(dto.unitId())
                .location(dto.location())
                .componentName(dto.componentName())
                .period(dto.period())
                .levelAbbreviation(dto.levelAbbreviation())
                .subgroup(dto.isSubClassroom())
                .type(dto.type())
                .usesNewVirtualClassroom(dto.usesNewVirtualClassroom())
                .student(student)
                .build();
    }

}
