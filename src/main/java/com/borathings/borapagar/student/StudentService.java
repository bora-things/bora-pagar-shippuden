package com.borathings.borapagar.student;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.student.index.IndexDTO;
import com.borathings.borapagar.student.index.IndexEnum;
import com.borathings.borapagar.student.index.StudentIndexEntity;
import com.borathings.borapagar.student.index.StudentIndexRepository;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserService;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class StudentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestClient restClient;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserService userService;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    StudentIndexRepository studentIndexRepository;

    public StudentEntity createFromInstitutionalId(Long institutionalId, int userId) {
        Optional<StudentEntity> student = studentRepository.findByUserId(userId);
        if (student.isEmpty()) {
            UserEntity userEntity = userService.findByIdUserOrError(userId);
            logger.info("Creating Student from User {}", userEntity);
            List<StudentDTO> students = restClient
                    .get()
                    .uri("/discente/v1/discentes?id-institucional=" + institutionalId)
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            StudentDTO studentDto = students.getFirst();

            StudentEntity studentEntity = studentMapper.toEntity(studentDto);

            studentEntity.setUser(userEntity);
            return studentRepository.save(studentEntity);
        }
        return student.get();
    }

    @Async
    public CompletableFuture<Void> fetchIndexes(StudentEntity student) {
        try {

            List<IndexDTO> indexes = restClient
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
}
