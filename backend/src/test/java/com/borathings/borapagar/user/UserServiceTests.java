package com.borathings.borapagar.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.core.user.OAuth2User;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private OAuth2User oauth2User;

    @InjectMocks
    private UserService userService;

    private UserEntity existingUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingUser = UserEntity.builder()
                .userId(1)
                .email("test@example.com")
                .personName("Test User")
                .login("testuser")
                .institutionalId(67890L)
                .cpf("12345678901L")
                .imageUrl("http://image.url")
                .build();
    }

    @Test
    void upsert_existingUser_updatesUser() {
        String username = "migracao";
        when(oauth2User.getName()).thenReturn(username);
        when(oauth2User.getAttribute("id-usuario")).thenReturn(1);
        when(userRepository.findByUserId(1)).thenReturn(Optional.of(existingUser));
        when(oauth2User.getAttribute("imagem_url")).thenReturn("http://newimage.url");

        userService.upsert(oauth2User);

        verify(userRepository, times(1)).save(existingUser);
        assertThat(existingUser.getImageUrl()).isEqualTo("http://newimage.url");
    }

    @Test
    void findByUserIdOrError_userFound_returnsUser() {
        int userId = 1;
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(existingUser));

        UserEntity result = userService.findByIdUserOrError(userId);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(existingUser.getEmail());
        assertThat(result.getUserId()).isEqualTo(existingUser.getUserId());
    }

    @Test
    void findByUserIdOrError_userNotFound_throwsEntityNotFoundException() {
        int userId = 99;
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByIdUserOrError(userId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    void findByLoginOrError_userFound_returnsUser() {
        String login = "testuser";
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(existingUser));

        UserEntity result = userService.findByLoginOrError(login);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(existingUser.getEmail());
        assertThat(result.getLogin()).isEqualTo(existingUser.getLogin());
    }

    @Test
    void findByLoginOrError_userNotFound_throwsEntityNotFoundException() {
        String login = "nonexistentuser";
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByLoginOrError(login))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }
}
