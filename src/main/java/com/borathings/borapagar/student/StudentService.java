package com.borathings.borapagar.student;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.classroom.dto.ClassroomResponseDTO;
import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.component.mapper.ComponentMapper;
import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.student.dto.StudentResponseDTO;
import com.borathings.borapagar.student.index.IndexDTO;
import com.borathings.borapagar.student.index.IndexEnum;
import com.borathings.borapagar.student.index.StudentIndexEntity;
import com.borathings.borapagar.student.index.StudentIndexRepository;
import com.borathings.borapagar.student.interest.StudentSubjectInterestEntity;
import com.borathings.borapagar.student.interest.StudentSubjectInterestService;
import com.borathings.borapagar.student.interest.exception.PreRequisitesNotCompletedException;
import com.borathings.borapagar.student.takenComponent.TakenComponentEntity;
import com.borathings.borapagar.student.takenComponent.TakenComponentService;
import com.borathings.borapagar.student.takenComponent.dto.TakenComponentDTO;
import com.borathings.borapagar.student.takenComponent.enums.TakenComponentSituationEnum;
import com.borathings.borapagar.student.util.RequisiteParser;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserMapper;
import com.borathings.borapagar.user.UserService;
import com.borathings.borapagar.user.dto.FriendClassUserDTO;
import com.borathings.borapagar.user.dto.response.UserResponseDTO;
import com.borathings.borapagar.workload.WorkloadDto;
import com.borathings.borapagar.workload.WorkloadEntity;
import com.borathings.borapagar.workload.WorkloadRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private TakenComponentService takenComponentService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private ComponentMapper componentMapper;

    @Autowired
    private StudentSubjectInterestService studentSubjectInterestService;

    public StudentEntity findByIdWithClassrooms(Long studentId) {
        StudentEntity student = studentRepository
                .findByIdWithClassrooms(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return student;
    }

    public List<ClassroomResponseDTO> getPossibleSubjectsForStudent(String studentLogin, Pageable pageable) {

        StudentEntity student = findByUserLoginOrError(studentLogin);

        // Buscar componentes disponíveis com paginação
        Page<ComponentEntity> componentPage = componentService.getAllComponentsPageable(pageable);
        List<ComponentEntity> components = componentPage.getContent();

        // Buscar histórico do aluno (disciplinas cursadas)
        List<TakenComponentEntity> transcriptComponents = student.getTakenComponents();
        // Mapear turmas do aluno por código da disciplina
        Map<String, ClassroomEntity> classroomMap = student.getClassrooms().stream()
                .collect(
                        Collectors.toMap(ClassroomEntity::getComponentCode, Function.identity(), (prev, next) -> prev));

        // Mapear interesses do aluno por código da disciplina
        Map<String, StudentSubjectInterestEntity> interestMap = student.getInterests().stream()
                .collect(Collectors.toMap(StudentSubjectInterestEntity::getSubjectCode, Function.identity()));

        // Mapear componentes que o aluno não foi aprovado ainda
        Map<Integer, TakenComponentEntity> notApprovedTranscriptMap = transcriptComponents.stream()
                .filter(tc ->
                        !TakenComponentSituationEnum.fromId(tc.getSituation()).isApproved())
                .collect(Collectors.toMap(
                        TakenComponentEntity::getComponentId, Function.identity(), (first, second) -> first));

        List<StudentEntity> studentFriends = student.getUser().getFriends().stream()
                .map(item -> findByUserIdOrError(item.getUserId()))
                .toList();

        Map<Long, Set<String>> friendInterestsMap = studentFriends.stream()
                .collect(Collectors.toMap(StudentEntity::getId, friend -> friend.getInterests().stream()
                        .map(StudentSubjectInterestEntity::getSubjectCode)
                        .collect(Collectors.toSet())));

        List<ComponentEntity> takenComponentsEntity =
                componentService.findAllByComponentId(student.getTakenComponents().stream()
                        .map(TakenComponentEntity::getComponentId)
                        .toList());

        Set<String> completedOrEnrolledCodes = Stream.concat(
                        takenComponentsEntity.stream().map(ComponentEntity::getCode),
                        student.getClassrooms().stream().map(ClassroomEntity::getComponentCode))
                .collect(Collectors.toSet());

        return components.stream()
                .filter(component -> {
                    RequisiteParser parser = new RequisiteParser(completedOrEnrolledCodes);
                    try {
                        parser.assertRequisitesAreMet(component.getPreRequisites());
                        return !notApprovedTranscriptMap.containsKey(component.getComponentId())
                                && !interestMap.containsKey(component.getCode())
                                && !classroomMap.containsKey(component.getCode());
                    } catch (PreRequisitesNotCompletedException ex) {
                        return false;
                    }
                })
                .map(component -> {
                    List<UserResponseDTO> interestedFriends = studentFriends.stream()
                            .filter(friend ->
                                    friendInterestsMap.get(friend.getId()).contains(component.getCode()))
                            .map(item -> userMapper.toUserResponseDTO(item.getUser()))
                            .toList();

                    return new ClassroomResponseDTO(
                            0, 0, 0, componentMapper.toResponseDTO(component), interestedFriends);
                })
                .toList();
    }

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
            // Atualmente a api não retorna o id da matriz curricular, por isso setamos como padrão a matriz de T.I - MT
            studentEntity.setCurricularMatrix("133795010");
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
    @Transactional
    public CompletableFuture<Void> fetchWorkload(StudentEntity student) {
        try {

            WorkloadDto workloadDto = userRestClient
                    .get()
                    .uri("/discente/v1/discentes/" + student.getStudentId() + "/carga-horaria")
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<WorkloadDto>() {});

            if (workloadDto != null) {
                WorkloadEntity workload = workloadRepository
                        .findByStudent(student)
                        .orElse(WorkloadEntity.builder().student(student).build());

                workload.setPendingWorkload(workloadDto.pendingWorkload());
                workload.setTotalMinimumWorkload(workloadDto.totalMinimumWorkload());
                workload.setTotalWorkloadCompleted(workloadDto.totalWorkloadCompleted());

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

    public StudentEntity findByIdOrError(Long studentId) {

        return studentRepository.findById(studentId).orElseThrow(() -> {
            return new EntityNotFoundException("Estudante com id: " + studentId + " não foi encontrado");
        });
    }

    public StudentResponseDTO findStudentResponseDTOById(Long studentId) {
        StudentEntity student = findByIdOrError(studentId);
        return studentMapper.toResponseDTO(student);
    }

    public void saveStudent(StudentEntity student) {
        studentRepository.save(student);
    }

    @Async
    public CompletableFuture<Void> fetchAcademicRecord(StudentEntity student) {
        try {

            List<TakenComponentDTO> components = userRestClient
                    .get()
                    .uri("/matricula/v1/matriculas-componentes?id-discente=" + student.getStudentId())
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<TakenComponentDTO>>() {});

            takenComponentService.batchInsertDTOs(components, student);
            return CompletableFuture.completedFuture(null);

        } catch (Exception ex) {
            logger.error("Exception at fetchAcademicRecord {}", ex.getMessage());

            return CompletableFuture.failedFuture(ex);
        }
    }

    @Async
    public CompletableFuture<List<UserResponseDTO>> findFriendsInClass(
            UserEntity user, ClassroomEntity classroom, Set<UserEntity> userFriends) {
        try {
            List<FriendClassUserDTO> studentsDto = userRestClient
                    .get()
                    .uri("/turma/v1/participantes?limit=100&id-turma=" + classroom.getClassroomId())
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<FriendClassUserDTO>>() {});

            if (studentsDto != null && !studentsDto.isEmpty()) {
                Map<Long, UserEntity> userFriendsMap = userFriends.stream()
                        .collect(Collectors.toMap(UserEntity::getInstitutionalId, Function.identity()));

                List<UserResponseDTO> result = studentsDto.stream()
                        .filter(item -> userFriendsMap.containsKey(item.institutionalId()))
                        .map(item -> userMapper.toUserResponseDTO(userFriendsMap.get(item.institutionalId())))
                        .toList();

                return CompletableFuture.completedFuture(result);
            }
        } catch (Exception ex) {
            logger.info("Exception at findFriendsInClass {}", ex.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }
}
