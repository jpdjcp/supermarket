package com.supermarket.supermarket_api.service;

import com.supermarket.supermarket_api.dto.user.ChangePasswordRequest;
import com.supermarket.supermarket_api.dto.user.CreateUserRequest;
import com.supermarket.supermarket_api.dto.user.UserResponse;
import com.supermarket.supermarket_api.exception.UserNotFoundException;
import com.supermarket.supermarket_api.mapper.UserMapper;
import com.supermarket.supermarket_api.model.User;
import com.supermarket.supermarket_api.model.UserRole;
import com.supermarket.supermarket_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Captor
    ArgumentCaptor<User> userCaptor;

    private User user;
    private CreateUserRequest request;
    private ChangePasswordRequest pwdRequest;
    private UserResponse response;
    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        String username = "John";
        String password = "abcd1234";
        String newPassword = "newPassword";
        UserRole role = UserRole.ROLE_USER;

        user = new User(username, password, role);
        request = new CreateUserRequest(username, password, role);
        pwdRequest = new ChangePasswordRequest(newPassword);
        response = new UserResponse(userId, username, role, true, Instant.now());
    }

    @Test
    void createUser_shouldCreate() {
        when(repository.existsByUsername(request.username()))
                .thenReturn(false);
        when(repository.save(any(User.class)))
                .thenReturn(user);
        when(mapper.toResponse(any(User.class)))
                .thenReturn(response);

        UserResponse returned = service.createUser(request);

        verify(repository).existsByUsername(request.username());
        verify(repository).save(userCaptor.capture());
        verify(mapper).toResponse(user);
        verifyNoMoreInteractions(repository);

        User captured = userCaptor.getValue();

        assertThat(captured.getUsername()).isEqualTo(request.username());
        assertThat(captured.getRole()).isEqualTo(request.role());
        assertThat(captured.isEnabled()).isTrue();
        assertThat(returned).isSameAs(response);
    }

    @Test
    void createUser_withNullRequestDTO_shouldThrow() {
        assertThatThrownBy(()-> service.createUser(null))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(repository);
    }

    @Test
    void createUser_withInexistentId_shouldThrow() {
        when(repository.existsByUsername(request.username()))
                .thenReturn(true);

        assertThatThrownBy(()-> service.createUser(request))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_shouldFindUser() {
        when(repository.findById(userId))
                .thenReturn(Optional.of(user));
        when(mapper.toResponse(any(User.class)))
                .thenReturn(response);

        UserResponse returned = service.findById(userId);

        verify(repository).findById(userId);
        verify(mapper).toResponse(any(User.class));
        verifyNoMoreInteractions(repository);
        assertThat(returned).isSameAs(response);
    }

    @Test
    void findById_withInexistentId_shouldThrow() {
        when(repository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()-> service.findById(userId))
                .isInstanceOf(UserNotFoundException.class);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_withNullId_shouldThrow() {
        assertThatThrownBy(()-> service.findById(null))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(repository);
    }

    @Test
    void changePassword_shouldChange() {
        when(repository.findById(userId))
                .thenReturn(Optional.of(user));

        service.changePassword(userId, pwdRequest);

        verify(repository).findById(userId);
        verifyNoMoreInteractions(repository);
        assertThat(user.getPassword())
                .isEqualTo(pwdRequest.password());
    }

    @Test
    void changePassword_withNullRequest_shouldTrow() {
        assertThatThrownBy(()-> service.changePassword(userId, null))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(repository);
    }

    @Test
    void changePassword_withNullId_shouldThrow() {
        assertThatThrownBy(()-> service.changePassword(null, null))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(repository);
    }

    @Test
    void changePassword_withInexistentId_shouldThrow() {
        when(repository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()-> service.changePassword(userId, pwdRequest))
                .isInstanceOf(UserNotFoundException.class);
        verify(repository).findById(userId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void enable_shouldEnable() {
        when(repository.findById(userId))
                .thenReturn(Optional.of(user));
        user.disable();

        service.enable(userId);

        verify(repository).findById(userId);
        verifyNoMoreInteractions(repository);
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void enable_withNullId_shouldThrow() {
        assertThatThrownBy(()-> service.enable(null))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(repository);
    }

    @Test
    void enable_withInexistentId_shouldThrow() {
        when(repository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()-> service.enable(userId))
                .isInstanceOf(UserNotFoundException.class);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void disable_shouldDisable() {
        when(repository.findById(userId))
                .thenReturn(Optional.of(user));

        service.disable(userId);

        assertThat(user.isEnabled()).isFalse();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void disable_withNullId_shouldThrow() {
        assertThatThrownBy(()-> service.disable(null))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(repository);
    }

    @Test
    void disable_withInexistentId_shouldThrow() {
        when(repository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()-> service.disable(userId))
                .isInstanceOf(UserNotFoundException.class);
        verifyNoMoreInteractions(repository);
    }
}
