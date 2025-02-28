package com.borathings.borapagar.user;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.borathings.borapagar.student.StudentService;
import com.borathings.borapagar.user.dto.response.UserFriendResponseDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
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

    @Mock
    private StudentService studentService;

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
                .hasMessage("Usuário com ID : " + userId + " não encontrado");
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
                .hasMessage("Usuário com Login : " + login + " não encontrado");
    }

    @Test
    void createFriendship_successfullyCreatesFriendship() {
        UserEntity user1 = UserEntity.builder()
                .userId(1)
                .login("user1")
                .personName("User One")
                .friends(new HashSet<>())
                .email("user1@example.com")
                .build();
        UserEntity user2 = UserEntity.builder()
                .userId(2)
                .login("user2")
                .friends(new HashSet<>())
                .personName("User Two")
                .email("user2@example.com")
                .build();

        when(userRepository.findByUserId(1)).thenReturn(Optional.of(user1));
        when(userRepository.findByUserId(2)).thenReturn(Optional.of(user2));

        userService.createFriendship(user1, user2);

        assertTrue(user1.getFriends().contains(user2));
        assertTrue(user2.getFriends().contains(user1));

        verify(userRepository, times(1)).saveAll(List.of(user1, user2));
    }

    @Test
    void getFriends_returnsListOfFriends() {
        UserEntity user1 = UserEntity.builder()
                .userId(1)
                .login("user1")
                .personName("User One")
                .email("user1@example.com")
                .friends(new HashSet<>())
                .build();

        UserEntity user2 = UserEntity.builder()
                .userId(2)
                .login("user2")
                .personName("User Two")
                .email("user2@example.com")
                .friends(new HashSet<>())
                .build();

        UserEntity user3 = UserEntity.builder()
                .userId(3)
                .login("user3")
                .personName("User Three")
                .email("user3@example.com")
                .friends(new HashSet<>())
                .build();

        user1.addFriend(user2);
        user1.addFriend(user3);

        when(userRepository.findByLogin("user1")).thenReturn(Optional.of(user1));
        when(userRepository.findByLogin("user2")).thenReturn(Optional.of(user2));
        when(userRepository.findByLogin("user3")).thenReturn(Optional.of(user3));

        UserFriendResponseDto userFriendDto1 = new UserFriendResponseDto(user2.getPersonName(), "course", 1, "");
        UserFriendResponseDto userFriendDto2 = new UserFriendResponseDto(user3.getPersonName(), "course", 1, "");

        when(userMapper.toUserFriendResponseDto(user2, null)).thenReturn(userFriendDto1);
        when(userMapper.toUserFriendResponseDto(user3, null)).thenReturn(userFriendDto2);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user1");

        when(studentService.findAllStudentsById(anyList())).thenReturn(new ArrayList<>());

        List<UserFriendResponseDto> result = userService.getFriends(authentication);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("User Two", result.get(0).personName());
        assertEquals("User Three", result.get(1).personName());
    }
}
