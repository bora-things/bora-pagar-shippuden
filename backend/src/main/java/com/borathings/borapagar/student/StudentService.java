package com.borathings.borapagar.student;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.user.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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

    public void createFromInstitutionalId(Long institutionalId, int userId) {
        boolean studentExists = studentRepository.existsByUserId(userId);
        if (!studentExists) {
            UserEntity userEntity = userService.findByIdUserOrError(userId);
            logger.info("Creating Student from User {}", userEntity);
            List<StudentDTO> students = restClient
                    .get()
                    .uri("/discente/v1/discentes?id-institucional=" + institutionalId)
                    .attributes(clientRegistrationId("sigaa"))
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            StudentDTO student = students.getFirst();

            StudentEntity studentEntity = studentMapper.toEntity(student);

            studentEntity.setUser(userEntity);
            studentRepository.save(studentEntity);
        }
    }
}
