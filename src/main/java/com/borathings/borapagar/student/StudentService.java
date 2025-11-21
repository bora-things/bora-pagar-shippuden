package com.borathings.borapagar.student;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.classroom.dto.ClassroomResponseDTO;
import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.component.ComponentService;
import com.borathings.borapagar.component.mapper.ComponentMapper;
import com.borathings.borapagar.friendRequest.FriendRequestEntity;
import com.borathings.borapagar.friendRequest.FriendRequestService;
import com.borathings.borapagar.student.dto.SearchedStudentResponseDTO;
import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.student.dto.StudentResponseDTO;
import com.borathings.borapagar.student.enums.FriendStatus;
import com.borathings.borapagar.student.enums.StudentSituation;
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
import org.flywaydb.core.internal.util.StringUtils;
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
    @Qualifier("serviceRestClient")
    RestClient serviceRestClient;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserService userService;

    @Autowired
    StudentMapper studentMapper;

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

    @Autowired
    private FriendRequestService friendRequestService;

    public Set<Long> findAllStudents() {
        return studentRepository.findAll().stream().map(StudentEntity::getId).collect(Collectors.toSet());
    }

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

    private Integer mapCurricularMatrix(Integer matrix) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(102199826, 133804382); // TI-C
        map.put(134044402, 134044403); // TI-DS
        map.put(92127271, 133795010); // TI-MT
        map.put(92127278, 133797961); // TI-N

        return map.get(matrix);
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
            StudentSituation studentSituation = StudentSituation.getById(studentDto.studentStatusId());
            StudentEntity studentEntity = studentMapper.toEntity(
                    studentDto, mapCurricularMatrix(studentDto.curricularMatrix()), studentSituation);
            studentEntity.setImageUrl(userEntity.getImageUrl());
            studentEntity.setLogin(userEntity.getLogin());
            studentEntity.setUser(userEntity);
            return studentRepository.save(studentEntity);
        }
        return student.get();
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

    public Page<SearchedStudentResponseDTO> findFriendsByName(String userLogin, String name, Pageable page) {

        StudentEntity student = findByUserLoginOrError(userLogin);
        UserEntity currentUser = student.getUser();

        Page<StudentEntity> studentPage;

        if (StringUtils.hasText(name)) {
            studentPage = studentRepository.findFriendsByName(name, student.getStudentId(), page);
        } else {
            studentPage = studentRepository.findFriends(student.getStudentId(), page);
        }

        return studentPage.map(item -> {
            return studentMapper.toSearchedResponseDTO(item, FriendStatus.FRIENDS, null);
        });
    }

    public Page<SearchedStudentResponseDTO> findStudentsByName(String userLogin, String name, Pageable page) {

        StudentEntity student = findByUserLoginOrError(userLogin);
        UserEntity currentUser = student.getUser();

        Page<StudentEntity> studentPage;

        if (StringUtils.hasText(name)) {
            studentPage = studentRepository.findByStudentNameContainingIgnoreCaseAndIdNot(name, student.getId(), page);
        } else {
            studentPage = studentRepository.findFriendsOfFriends(student.getStudentId(), page);
        }

        List<Long> targetUserIds = studentPage.getContent().stream()
                .map(item -> item.getUser().getId())
                .toList();

        Set<Long> friendIds = userService.findFriendsFromList(currentUser.getId(), targetUserIds);

        Map<Long, FriendRequestEntity> sentRequestsMap =
                friendRequestService.findSentRequestsToUsers(currentUser, targetUserIds).stream()
                        .collect(Collectors.toMap(req -> req.getToUser().getId(), Function.identity()));

        Map<Long, FriendRequestEntity> receivedRequestsMap =
                friendRequestService.findReceivedRequestsFromUsers(currentUser, targetUserIds).stream()
                        .collect(Collectors.toMap(req -> req.getFromUser().getId(), Function.identity()));

        return studentPage.map(item -> {
            Long targetUserId = item.getUser().getId();
            FriendStatus status;
            Long requestId = null;

            if (targetUserId.equals(currentUser.getId())) {
                status = FriendStatus.SELF;

            } else if (friendIds.contains(targetUserId)) {
                status = FriendStatus.FRIENDS;

            } else if (sentRequestsMap.containsKey(targetUserId)) {
                status = FriendStatus.REQUEST_SENT;
                requestId = sentRequestsMap.get(targetUserId).getId();

            } else if (receivedRequestsMap.containsKey(targetUserId)) {
                status = FriendStatus.REQUEST_RECEIVED;
                requestId = receivedRequestsMap.get(targetUserId).getId();

            } else {
                status = FriendStatus.NOT_FRIENDS;
            }

            return studentMapper.toSearchedResponseDTO(item, status, requestId);
        });
    }

    public SearchedStudentResponseDTO findStudentResponseDTOById(String userLogin, Long studentId) {
        StudentEntity student = findByIdOrError(studentId);
        UserEntity currentUser = userService.findByLoginOrError(userLogin);
        FriendStatus status;
        Long requestId = null;

        if (student.getUser().getId().equals(currentUser.getId())) {
            status = FriendStatus.SELF;

        } else if (userService.areFriends(currentUser.getId(), student.getUser().getId())) {
            status = FriendStatus.FRIENDS;

        } else {
            Optional<FriendRequestEntity> sentRequest =
                    friendRequestService.findRequest(currentUser, student.getUser());

            if (sentRequest.isPresent()) {
                status = FriendStatus.REQUEST_SENT;
                requestId = sentRequest.get().getId();

            } else {
                Optional<FriendRequestEntity> receivedRequest =
                        friendRequestService.findRequest(student.getUser(), currentUser);

                if (receivedRequest.isPresent()) {
                    status = FriendStatus.REQUEST_RECEIVED;
                    requestId = receivedRequest.get().getId();
                } else {
                    status = FriendStatus.NOT_FRIENDS;
                }
            }
        }

        return studentMapper.toSearchedResponseDTO(student, status, requestId);
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

    public List<Long> fetchAllStudentsFromTI() {
        final int LIMIT = 100;
        int idCursoES = 17848940;
        int idCursoTI = 92127264;
        int idCursoCS = 2000013;
        List<Long> studentIds = new ArrayList<>();

        Set<Integer> coursesId = Set.of(idCursoCS, idCursoTI, idCursoES);

        Iterator<Integer> courseIterator = coursesId.iterator();
        while (courseIterator.hasNext()) {
            int offset = 0;
            Integer courseId = courseIterator.next();
            boolean hasMoreStudents = true;
            while (hasMoreStudents) {
                logger.info("Buscando estudantes de {}, partindo de {} até {}", courseId, offset, offset + LIMIT);
                int finalOffset = offset;
                List<StudentDTO> currentPageStudents = serviceRestClient
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .path("discente/v1/discentes")
                                .queryParam("id-curso", courseId)
                                .queryParam("id-tipo-discente", "1")
                                .queryParam("offset", finalOffset)
                                .queryParam("limit", LIMIT)
                                .build())
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {});

                if (currentPageStudents != null && !currentPageStudents.isEmpty()) {
                    studentIds.addAll(currentPageStudents.stream()
                            .filter(item -> StudentSituation.getById(item.studentStatusId())
                                    .isActive())
                            .map(item -> item.studentId())
                            .toList());
                    offset += LIMIT;
                } else {
                    hasMoreStudents = false;
                }
            }
        }

        return studentIds;
    }

    public Map<Long, StudentEntity> getAllStudents() {

        List<StudentEntity> students = studentRepository.findAll();
        Map<Long, StudentEntity> studentsMap = new HashMap<>();
        students.forEach(student -> {
            studentsMap.put(student.getStudentId(), student);
        });
        return studentsMap;
    }
}
