package com.borathings.borapagar.student;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.student.dto.StudentClassResponseDTO;
import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.student.dto.StudentResponseDTO;
import com.borathings.borapagar.student.index.IndexDTO;
import com.borathings.borapagar.student.index.IndexEnum;
import com.borathings.borapagar.student.index.StudentIndexEntity;
import com.borathings.borapagar.student.index.StudentIndexRepository;
import com.borathings.borapagar.student.transcript.TranscriptComponentService;
import com.borathings.borapagar.student.transcript.dto.TranscriptComponentDTO;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserMapper;
import com.borathings.borapagar.user.UserService;
import com.borathings.borapagar.user.dto.FriendClassUserDTO;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import com.borathings.borapagar.workload.WorkloadDto;
import com.borathings.borapagar.workload.WorkloadEntity;
import com.borathings.borapagar.workload.WorkloadRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class StudentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("userRestClient")
    RestClient userRestClient;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserService userService;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    private StudentIndexRepository studentIndexRepository;

    @Autowired
    private WorkloadRepository workloadRepository;

    @Autowired
    private TranscriptComponentService transcriptComponentService;
    @Autowired
    private UserMapper userMapper;

    public StudentEntity createFromInstitutionalId(Long institutionalId, int userId) {
        Optional<StudentEntity> student = studentRepository.findByUserId(userId);
        if (student.isEmpty()) {
            UserEntity userEntity = userService.findByIdUserOrError(userId);
            logger.info("Creating Student from User {}", userEntity);
            List<StudentDTO> students = userRestClient
                    .get()
                    .uri("/discente/v1/discentes?id-curso=92127264&id-institucional=" + institutionalId)
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<StudentDTO>>() {});

            StudentDTO studentDto = students.getFirst();
            StudentEntity studentEntity = studentMapper.toEntity(studentDto);
            studentEntity.setImageUrl(userEntity.getImageUrl());
            studentEntity.setLogin(userEntity.getLogin());
            studentEntity.setUser(userEntity);
            return studentRepository.save(studentEntity);
        }
        return student.get();
    }

    @Async
    public CompletableFuture<Void> fetchIndexes(StudentEntity student) {
        try {

            List<IndexDTO> indexes = userRestClient
                    .get()
                    .uri("/discente/v1/indices-discentes?id-discente=" + student.getStudentId())
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<IndexDTO>>() {});

            List<StudentIndexEntity> studentIndexEntities = indexes.stream()
                    .map(idx -> StudentIndexEntity.builder()
                            .student(student)
                            .value(idx.value())
                            .name(IndexEnum.fromId(idx.indexId().intValue()).name())
                            .indexId(idx.indexId())
                            .studentIndexId(idx.studentIndexId())
                            .build())
                    .collect(Collectors.toList());

            studentIndexRepository.saveAll(studentIndexEntities);

        } catch (Exception ex) {
            logger.error("Exception at fetchIndexes: {}", ex.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> fetchWorkload(StudentEntity student) {
        try {

            WorkloadDto workloadDto = userRestClient
                    .get()
                    .uri("https://api.info.ufrn.br/discente/v1/discentes/" + student.getStudentId() + "/carga-horaria")
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<WorkloadDto>() {});

            if (workloadDto != null) {
                WorkloadEntity workload = WorkloadEntity.builder()
                        .pendingWorkload(workloadDto.pendingWorkload())
                        .totalMinimumWorkload(workloadDto.totalMinimumWorkload())
                        .totalWorkloadCompleted(workloadDto.totalWorkloadCompleted())
                        .student(student)
                        .build();
                workloadRepository.deleteAllByStudent(student);
                workloadRepository.save(workload);
            }

        } catch (Exception ex) {
            logger.error("Exception at fetchWorkload: {}", ex.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    public List<StudentEntity> findAllStudentsById(List<UserEntity> users) {
        return studentRepository.findAllByUserIn(users);
    }

    public StudentEntity findByUserLoginOrError(String userLogin) {
        return studentRepository.findByUserLogin(userLogin).orElseThrow(() -> {
            return new EntityNotFoundException("Estudante com login : " + userLogin + "não foi encontrado");
        });
    }

    public StudentResponseDTO getCurrentStudent(String userLogin) {
        StudentEntity student = findByUserLoginOrError(userLogin);
        return studentMapper.toResponseDTO(student);
    }

    public StudentEntity findByUserIdOrError(int userId) {
        return studentRepository.findByUserId(userId).orElseThrow(() -> {
            return new EntityNotFoundException("Estudante com id usuário : " + userId + " não foi encontrado");
        });
    }

    public StudentResponseDTO findByIdOrError(Long studentId) {
        StudentEntity student = studentRepository.findById(studentId).orElseThrow(() -> {
            return new EntityNotFoundException("Estudante com id: " + studentId + " não foi encontrado");
        });

        return studentMapper.toResponseDTO(student);
    }

    @Async
    public CompletableFuture<Void> fetchAcademicRecord(StudentEntity student) {
        try {

            List<TranscriptComponentDTO> components = userRestClient
                    .get()
                    .uri("/matricula/v1/matriculas-componentes?id-discente=" + student.getStudentId())
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<TranscriptComponentDTO>>() {});

            transcriptComponentService.batchInsertDTOs(components, student);
            return CompletableFuture.completedFuture(null);

        } catch (Exception ex) {
            logger.error("Exception at fetchAcademicRecord {}", ex.getMessage());

            return CompletableFuture.failedFuture(ex);
        }
    };


    @Async
    public CompletableFuture<List<UserResponseDTO>> findFriendsInClass(UserEntity user, ClassroomEntity classroom) {
        try {
            List<FriendClassUserDTO> studentsDto = userRestClient
                    .get()
                    .uri("/turma/v1/participantes?limit=100&id-turma=" + classroom.getClassroomId())
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<FriendClassUserDTO>>() {});

            if (studentsDto != null && !studentsDto.isEmpty()) {
                Set<UserEntity> userFriends = user.getFriends();
                Map<Long, UserEntity> userFriendsMap = userFriends.stream()
                        .collect(Collectors.toMap(UserEntity::getInstitutionalId, Function.identity()));

                List<UserResponseDTO> result = studentsDto.stream()
                        .filter(item -> userFriendsMap.containsKey(item.institutionalId()))
                        .map(item->userMapper.toUserResponseDTO(userFriendsMap.get(item.institutionalId())))
                        .toList();

                return CompletableFuture.completedFuture(result);
            }
        } catch (Exception ex) {
            logger.info("Exception at findFriendsInClass {}", ex.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }
}
