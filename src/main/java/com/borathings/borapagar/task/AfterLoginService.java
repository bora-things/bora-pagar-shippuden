package com.borathings.borapagar.task;

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

    @Async
    public void completeProfileAfterLogin(StudentEntity student) {
        CompletableFuture.allOf(
                        studentService.fetchIndexes(student),
                        studentService.fetchWorkload(student),
                        studentService.fetchAcademicRecord(student))
                .exceptionally(ex -> {
                    throw new Error(ex);
                });
    }
}
