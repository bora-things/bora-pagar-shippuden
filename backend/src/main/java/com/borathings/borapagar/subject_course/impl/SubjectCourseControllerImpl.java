package com.borathings.borapagar.subject_course.impl;

import com.borathings.borapagar.subject_course.SubjectCourseController;
import com.borathings.borapagar.subject_course.SubjectCourseEntity;
import com.borathings.borapagar.subject_course.SubjectCourseService;
import com.borathings.borapagar.subject_course.dto.CreateSubjectCourseRequest;
import com.borathings.borapagar.subject_course.dto.UpdateSubjectCourseRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubjectCourseControllerImpl implements SubjectCourseController {

    @Autowired private SubjectCourseService subjectCourseService;

    @Override
    public ResponseEntity<SubjectCourseEntity> addSubjectToCourseSchedule(
            Long courseId, @Valid CreateSubjectCourseRequest createSubjectCourseRequest) {
        SubjectCourseEntity subjectCourseEntity =
                subjectCourseService.addSubjectToCourseSchedule(
                        courseId, createSubjectCourseRequest.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED).body(subjectCourseEntity);
    }

    @Override
    public ResponseEntity<List<SubjectCourseEntity>> getAllSubjectsFromCourseSchedule(
            Long courseId) {
        List<SubjectCourseEntity> courseSchedule =
                subjectCourseService.getAllSubjectsFromCourseSchedule(courseId);
        return ResponseEntity.ok().body(courseSchedule);
    }

    @Override
    public ResponseEntity<SubjectCourseEntity> getSubjectInfoFromCourseSchedule(
            Long courseId, Long subjectId) {
        SubjectCourseEntity subjectCourseEntity =
                subjectCourseService.getSubjectInfoFromCourseScheduleOrError(courseId, subjectId);

        return ResponseEntity.ok().body(subjectCourseEntity);
    }

    @Override
    public ResponseEntity<SubjectCourseEntity> updateSubjectInfoFromCourseSchedule(
            Long courseId,
            Long subjectId,
            @Valid UpdateSubjectCourseRequest updateSubjectCourseRequest) {
        SubjectCourseEntity subjectCourseEntity =
                subjectCourseService.updateSubjectInfoFromCourseSchedule(
                        courseId, subjectId, updateSubjectCourseRequest.toEntity());

        return ResponseEntity.ok().body(subjectCourseEntity);
    }

    @Override
    public ResponseEntity<Void> removeSubjectFromCourseSchedule(Long courseId, Long subjectId) {
        subjectCourseService.deleteSubjectFromCourseSchedule(courseId, subjectId);
        return ResponseEntity.ok().build();
    }
}
