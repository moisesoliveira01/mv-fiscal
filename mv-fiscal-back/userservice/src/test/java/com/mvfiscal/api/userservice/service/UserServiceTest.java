package com.mvfiscal.api.userservice.service;

import com.mvfiscal.api.userservice.dto.UserDTO;
import com.mvfiscal.api.userservice.exception.UserDataValidationException;
import com.mvfiscal.api.userservice.model.User;
import com.mvfiscal.api.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRestService taskRestService;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private User userEntity;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("Test User");
        userDTO.setEmail("test@example.com");
        userDTO.setCreatedDate(new Date());

        userEntity = new User();
        userEntity.setId(userId);
        userEntity.setName("Test User");
        userEntity.setEmail("test@example.com");
        userEntity.setCreatedDate(new Date());
    }

    @Test
    void shouldReturnUserWhenUserExists() {
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(userDTO));

        Optional<UserDTO> result = userService.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository, times(1)).findUserById(userId);
    }

    @Test
    void shouldReturnEmptyWhenUserDoesNotExist() {
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.getUserById(userId);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findUserById(userId);
    }

    @Test
    void shouldReturnPageOfUsers() {
        Page<UserDTO> expectedPage = new PageImpl<>(Collections.singletonList(userDTO));
        when(userRepository.findPageByFilter(any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(expectedPage);

        Page<UserDTO> result = userService.getPageByFilter(null, null, null, null, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1))
                .findPageByFilter(any(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void shouldSaveAndReturnUser() {
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        User result = userService.saveUser(userDTO);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldUpdateUserWhenUserExists() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        User result = userService.updateUser(userDTO);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNullDoingUpdate() {
        userDTO.setId(null);

        assertThrows(UserDataValidationException.class, () -> userService.updateUser(userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExistDoingUpdate() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserDataValidationException.class, () -> userService.updateUser(userDTO));
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldNotThrowExceptionWhenUserExists() {
        when(userRepository.existsById(userId)).thenReturn(true);

        assertDoesNotThrow(() -> userService.validateUserExistence(userId));
        verify(userRepository, times(1)).existsById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExistValidatingExistence() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserDataValidationException.class, () -> userService.validateUserExistence(userId));
        verify(userRepository, times(1)).existsById(userId);
    }
}
