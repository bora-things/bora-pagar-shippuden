package com.borathings.borapagar.auth;

import static org.mockito.Mockito.*;

import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.task.AfterLoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

@ExtendWith(MockitoExtension.class)
class OAuth2AuthenticationEventListenerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private AfterLoginService afterLoginTasks;

    @InjectMocks
    private OAuth2AuthenticationEventListener eventListener;

    @Test
    void onAuthenticationSuccess_WhenPrincipalIsOAuth2User_CallsStudentService() {
        OAuth2User oauth2User = mock(OAuth2User.class);
        StudentEntity studentEntity = mock(StudentEntity.class);
        when(oauth2User.getAttribute("id-institucional")).thenReturn(12345L);
        when(oauth2User.getAttribute("id-usuario")).thenReturn(678);
        when(oauth2User.getName()).thenReturn("migracao");
        when(studentService.createFromInstitutionalId(12345L, 678)).thenReturn(studentEntity);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(oauth2User);

        InteractiveAuthenticationSuccessEvent event =
                new InteractiveAuthenticationSuccessEvent(authentication, this.getClass());

        eventListener.onAuthenticationSuccess(event);

        verify(studentService, times(1)).createFromInstitutionalId(12345L, 678);
        verify(afterLoginTasks, times(1)).completeProfileAfterLogin(any(StudentEntity.class));
    }

    @Test
    void onAuthenticationSuccess_WhenPrincipalIsNotOAuth2User_DoesNotCallStudentService() {
        Object principal = new Object();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);

        InteractiveAuthenticationSuccessEvent event =
                new InteractiveAuthenticationSuccessEvent(authentication, this.getClass());

        eventListener.onAuthenticationSuccess(event);

        verifyNoInteractions(studentService);
    }
}
