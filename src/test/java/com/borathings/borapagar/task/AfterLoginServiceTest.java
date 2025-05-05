package com.borathings.borapagar.task;

import static org.mockito.Mockito.*;

import com.borathings.borapagar.classroom.ClassroomService;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AfterLoginServiceTest {

    @Mock
    private StudentService studentService;

    @Mock
    private ClassroomService classroomService;

    @InjectMocks
    private AfterLoginService afterLoginService;

    @Test
    void completeProfileAfterLogin_CallsAllMethods() {
        StudentEntity student = new StudentEntity();
        when(studentService.fetchIndexes(student)).thenReturn(CompletableFuture.completedFuture(null));
        when(studentService.fetchWorkload(student)).thenReturn(CompletableFuture.completedFuture(null));
        when(studentService.fetchAcademicRecord(student)).thenReturn(CompletableFuture.completedFuture(null));
        when(classroomService.fetchClassroom(student)).thenReturn(CompletableFuture.completedFuture(null));

        afterLoginService.completeProfileAfterLogin(student);

        verify(studentService, times(1)).fetchIndexes(student);
        verify(studentService, times(1)).fetchWorkload(student);
        verify(studentService, times(1)).fetchAcademicRecord(student);
    }
}
