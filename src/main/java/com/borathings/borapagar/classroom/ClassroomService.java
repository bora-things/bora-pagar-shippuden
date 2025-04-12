package com.borathings.borapagar.classroom;

import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class ClassroomService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestClient restClient;

    @Autowired
    private ClassroomMapper classroomMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Async
    public CompletableFuture<Void> fetchClassroom(StudentEntity student) {
        try {

            List<ClassroomDTO> classroomDTOs = restClient
                    .get()
                    .uri("https://api.info.ufrn.br/turma/v1/turmas?id-discente=" + student.getStudentId())
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ClassroomDTO>>() {});

            if(classroomDTOs != null){

            List<ClassroomEntity> classrooms=classroomDTOs.stream().map(item->
                toEntity(item,student)
            ).toList();
                classroomRepository.deleteAllByStudent(student);
                classroomRepository.saveAll(classrooms);
            }

        } catch (Exception ex) {
            logger.error("Exception at fetchWorkload: {}", ex.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    public List<ClassroomDTO> findClassroomByStudent(String login) {
       StudentEntity student= studentService.findByUserLoginOrError(login);
        List<ClassroomEntity> classrooms= classroomRepository.findAllByStudent(student);
        return classrooms.stream().map(item->classroomMapper.toDTO(item)).toList();
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
