package com.borathings.borapagar.component;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.classroom.ClassroomHelperService;
import com.borathings.borapagar.component.dto.ComponentDTO;
import com.borathings.borapagar.component.dto.ComponentDetailsDTO;
import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import com.borathings.borapagar.component.dto.ComponentResponseDetailsDTO;
import com.borathings.borapagar.component.mapper.ComponentMapper;
import com.borathings.borapagar.component.repository.ComponentRepository;
import com.borathings.borapagar.docent.DocentService;
import com.borathings.borapagar.docent.dto.DocentEvaluationDTO;
import com.borathings.borapagar.docent.dto.DocentResponseDTO;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentRepository;
import com.borathings.borapagar.student.interest.StudentSubjectInterestHelperService;
import com.borathings.borapagar.user.UserService;
import com.borathings.borapagar.user.dto.UserDTO;
import com.borathings.borapagar.user.dto.response.UserFriendResponseDto;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ComponentService {

    @Autowired
    ComponentRepository componentRepository;

    @Autowired
    private ComponentFetchService componentFetchService;

    @Autowired
    ComponentMapper componentMapper;

    @Autowired
    @Qualifier("serviceRestClient")
    RestClient serviceRestClient;

    @Autowired
    private DocentService docentService;

    @Autowired
    private StudentSubjectInterestHelperService studentSubjectInterestService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassroomHelperService classroomHelperService;

    @Autowired
    private UserService userService;

    public Page<ComponentEntity> getAllComponentsPageable(Pageable pageable) {
        return componentRepository.findAll(pageable);
    }

    public List<ComponentEntity> findAllDiscinctByCodeIn(List<String> codes) {
        return componentRepository.findDistinctByCodeIn(codes);
    }

    public Map<String, ComponentResponseDTO> findComponentMapPriorityMatrix(Set<String> codes, Integer studentMatrix) {
        List<ComponentEntity> allComponents = componentRepository.findAllByCodeIn(codes);

        Map<String, ComponentEntity> prioritizedComponentMap = allComponents.stream()
                .collect(Collectors.toMap(ComponentEntity::getCode, component -> component, (existing, replacement) -> {
                    if (studentMatrix == null) {
                        return existing;
                    }

                    boolean existingIsPreferred = studentMatrix.equals(existing.getCurricularMatrixId());
                    boolean replacementIsPreferred = studentMatrix.equals(replacement.getCurricularMatrixId());

                    if (existingIsPreferred && !replacementIsPreferred) {
                        return existing;
                    } else if (!existingIsPreferred && replacementIsPreferred) {
                        return replacement;
                    } else {
                        return existing;
                    }
                }));

        Map<String, ComponentResponseDTO> componentMap = prioritizedComponentMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    ComponentEntity entity = entry.getValue();
                    boolean mandatorySubject = entity.getCurricularMatrixId().equals(studentMatrix)
                            ? Boolean.TRUE.equals(entity.getMandatorySubject())
                            : false;
                    entity.setMandatorySubject(mandatorySubject);
                    return componentMapper.toResponseDTO(entity);
                }));

        return componentMap;
    }

    public List<ComponentEntity> findAllByComponentId(List<Integer> ids) {
        return componentRepository.findAllByComponentIdIn(ids);
    }

    @Async
    public void fetchComponents() {
        List<Integer> curricularMatrixIdList = List.of(134044403, 133795010, 133797961, 133804382);

        curricularMatrixIdList.forEach(componentFetchService::fetchComponentsByMatrix);
    }

    public List<ComponentDTO> findSearchedComponents(String searched) {
        List<ComponentEntity> components = componentRepository.searchByNameOrCode(searched);
        return components.stream().map(componentMapper::toDto).toList();
    }

    public Optional<ComponentEntity> findByCode(String code) {
        return componentRepository.findFirstByCode(code);
    }

    public ComponentResponseDetailsDTO findComponentDetails(String code, String studentLogin) {
        StudentEntity student =
                studentRepository.findByUserLogin(studentLogin).orElseThrow(EntityNotFoundException::new);
        Optional<ComponentEntity> component = componentRepository.findFirstByCode(code);

        if (component.isPresent()) {
            ComponentEntity componentEntity = component.get();

            List<ComponentDetailsDTO> detailsList = serviceRestClient
                    .get()
                    .uri("/curso/v1/componentes-curriculares/" + componentEntity.getComponentId() + "/programas")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ComponentDetailsDTO>>() {});

            ComponentDetailsDTO componentDetails =
                    detailsList == null || detailsList.isEmpty() ? null : detailsList.getFirst();

            List<DocentResponseDTO> docentes = getTeachersReviews(code);
            List<UserFriendResponseDto> friendsInterests =
                    studentSubjectInterestService.getFriendsInterestsInComponent(student, code).stream()
                            .map(item -> {
                                StudentEntity studentFriend = item.getStudent();
                                return new UserFriendResponseDto(
                                        studentFriend.getStudentName(),
                                        studentFriend.getCourseName(),
                                        studentFriend.getUserPeriod(),
                                        studentFriend.getImageUrl());
                            })
                            .toList();
            return componentMapper.toDetailsDTO(componentEntity, componentDetails, docentes, friendsInterests);
        }

        return null;
    }

    private List<DocentResponseDTO> getTeachersReviews(String code) {

        List<ClassroomEntity> classrooms = classroomHelperService.findClassroomsByComponentCode(code);
        int currentYear = LocalDateTime.now().getYear();

        List<ClassroomEntity> filteredClassrooms = classrooms.stream()
                .filter(item -> item.getYear() > currentYear - 2)
                .toList();

        // Passa a lista filtrada para o método do service (ajuste se o método espera outro tipo)
        List<DocentEvaluationDTO> evaluationDTOS = docentService.findDocentsEvaluation(code, filteredClassrooms);

        Map<Long, Accumulator> teacherMap = new HashMap<>();

        for (DocentEvaluationDTO dto : evaluationDTOS) {
            Long teacherId = dto.getTeacherId();

            teacherMap
                    .computeIfAbsent(teacherId, id -> {
                        Accumulator acc = new Accumulator();
                        acc.name = dto.getTeacherName();
                        acc.cpf = dto.getCpf();
                        return acc;
                    })
                    .add(dto.getGeneralAverage());
        }

        List<DocentResponseDTO> response = teacherMap.values().stream()
                .map(acc -> {
                    UserDTO user = userService.fetchUserByCpf(acc.cpf); // busca aqui, uma vez por professor

                    return new DocentResponseDTO(acc.name, acc.average(), user.imageUrl());
                })
                .toList();

        return response;
    }
}

class Accumulator {
    double sum = 0;
    int count = 0;
    String name;
    String cpf;

    void add(double value) {
        sum += value;
        count++;
    }

    double average() {
        return count == 0 ? 0 : sum / count;
    }
}
