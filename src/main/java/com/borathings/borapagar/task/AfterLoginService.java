package com.borathings.borapagar.task;

import com.borathings.borapagar.classroom.ClassroomService;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AfterLoginService {


    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomService classroomService;


    @Async
    public void completeProfileAfterLogin(StudentEntity student) {
        CompletableFuture.allOf(
                        studentService.fetchIndexes(student),
                        studentService.fetchWorkload(student),
                        studentService.fetchAcademicRecord(student),
                        classroomService.fetchClassroom(student))
                .exceptionally(ex -> {
                    throw new Error(ex);
                });
    }
}
