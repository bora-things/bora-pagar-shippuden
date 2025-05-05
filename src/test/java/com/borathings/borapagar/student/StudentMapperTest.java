package com.borathings.borapagar.student;

import static org.assertj.core.api.Assertions.assertThat;

import com.borathings.borapagar.student.dto.StudentDTO;
import com.borathings.borapagar.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class StudentMapperTest {

    private final StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

    @Test
    void testToDto() {
        // Create a UserEntity (required field)
        UserEntity user = new UserEntity();
        user.setId(1L);

        // Build StudentEntity with test data
        StudentEntity entity = new StudentEntity();
        entity.setId(2L);
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
}
