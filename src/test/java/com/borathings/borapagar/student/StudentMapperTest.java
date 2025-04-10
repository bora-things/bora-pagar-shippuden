package com.borathings.borapagar.student;

import static org.assertj.core.api.Assertions.assertThat;

import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StudentMapperTest {

    @Autowired
    private StudentMapper studentMapper;

    @Test
    void testToDto() {
        // Create a UserEntity (required field)
        UserEntity user = new UserEntity();
        user.setId(1L);

        // Build StudentEntity with test data
        StudentEntity entity = new StudentEntity();
        entity.setStudentId(123L);
        entity.setStudentName("John Doe");
        entity.setEnrollmentId("456");
        entity.setAdmissionYear(2020);
        entity.setAcademicManagerId(789);
        entity.setParticipantTypeId(10);
        entity.setCourseId(112);
        entity.setCourseName("Computer Science");
        entity.setLevel("BSc");
        entity.setEducationalInstitution("University");
        entity.setCampus("Main Campus");
        entity.setAdmissionSemester(1); // Required, but not in DTO
        entity.setUser(user); // Required

        // Add optional fields that can be mapped
        entity.setIngressMethodDescription("Admission Description");
        entity.setEducationalInstitutionId(101);
        entity.setCampusId(2); // Maps to DTO's campusId (different name, not mapped)

        // Map to DTO
        StudentDTO dto = studentMapper.toDto(entity);

        // Assert mapped fields
        assertThat(dto.studentName()).isEqualTo("John Doe");
        assertThat(dto.courseName()).isEqualTo("Computer Science");
    }

//    @Test
//    void testToEntity() {
//        // Create DTO with test data
//        StudentDTO dto = new StudentDTO(
//                123L, // studentId
//                2020, // admissionYear
//                99999999999L, // cpf (not mapped)
//                "Admission Description", // admissionTypeDescription
//                "john.doe@example.com", // email
//                112L, // courseId
//                456L, // enrollmentId
//                null, // photoId
//                789L, // academicManagerId
//                null, // institutionalId
//                101, // educationalInstitutionId
//                2, // campusId (maps to idPolo if @Mapping exists)
//                null, // studentStatusId (not mapped)
//                null, // studentTypeId (not mapped)
//                10, // participantTypeId
//                null, // unitId
//                "University", // educationalInstitution
//                "john.doe", // login (not mapped)
//                12345L, // registrationNumber (not mapped)
//                "Computer Science", // courseName
//                "John Doe", // studentName
//                1, // admissionPeriod (not mapped)
//                "Main Campus", // campus
//                "BSc" // level
//                );
//
//        StudentEntity entity = studentMapper.toEntity(dto);
//
//        assertThat(entity.getStudentId()).isEqualTo(123);
//        assertThat(entity.getStudentName()).isEqualTo("John Doe");
//        assertThat(entity.getEnrollmentId()).isEqualTo("456"); // Long 456L to String "456"
//        assertThat(entity.getAdmissionYear()).isEqualTo(2020);
//        assertThat(entity.getAcademicManagerId()).isEqualTo(789);
//        assertThat(entity.getParticipantTypeId()).isEqualTo(10);
//        assertThat(entity.getCourseId()).isEqualTo(112);
//        assertThat(entity.getCourseName()).isEqualTo("Computer Science");
//        assertThat(entity.getLevel()).isEqualTo("BSc");
//        assertThat(entity.getEducationalInstitution()).isEqualTo("University");
//        assertThat(entity.getCampus()).isEqualTo("Main Campus");
//        assertThat(entity.getEducationalInstitutionId()).isEqualTo(101);
//
//        assertThat(entity.getIngressMethodDescription()).isEqualTo("Admission Description");
//        assertThat(entity.getCampusId()).isEqualTo(2);
//    }
}
