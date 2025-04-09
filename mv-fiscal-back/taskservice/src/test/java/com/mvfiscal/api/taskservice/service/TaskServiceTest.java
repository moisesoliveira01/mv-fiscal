package com.mvfiscal.api.taskservice.service;

import com.mvfiscal.api.taskservice.dto.TaskDTO;
import com.mvfiscal.api.taskservice.exception.TaskDataConflictException;
import com.mvfiscal.api.taskservice.exception.TaskDataValidationException;
import com.mvfiscal.api.taskservice.model.Task;
import com.mvfiscal.api.taskservice.repository.TaskRepository;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRestService userRestService;

    @InjectMocks
    private TaskService taskService;

    private TaskDTO taskDTO;
    private Task taskEntity;
    private final Long taskId = 1L;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        taskDTO = new TaskDTO();
        taskDTO.setId(taskId);
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Test Description");
        taskDTO.setStatus(TaskService.TASK_PENDING_STATUS);
        taskDTO.setCreatedDate(new Date());
        taskDTO.setLimitDate(new Date());
        taskDTO.setUserId(userId);

        taskEntity = new Task();
        taskEntity.setId(taskId);
        taskEntity.setTitle("Test Task");
        taskEntity.setDescription("Test Description");
        taskEntity.setStatus(TaskService.TASK_PENDING_STATUS);
        taskEntity.setCreatedDate(new Date());
        taskEntity.setLimitDate(new Date());
        taskEntity.setUserId(userId);
    }

    @Test
    void getTaskByIdShouldReturnTaskWhenTaskExists() {
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(taskDTO));

        Optional<TaskDTO> result = taskService.getTaskById(taskId);

        assertTrue(result.isPresent());
        assertEquals(taskId, result.get().getId());
        verify(taskRepository, times(1)).findTaskById(taskId);
    }

    @Test
    void getTaskByIdShouldReturnEmptyWhenTaskDoesNotExist() {
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        Optional<TaskDTO> result = taskService.getTaskById(taskId);

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findTaskById(taskId);
    }

    @Test
    void getPageByFilterShouldReturnPageOfTasks() {
        Page<TaskDTO> expectedPage = new PageImpl<>(Collections.singletonList(taskDTO));
        when(taskRepository.findPageByFilter(any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(expectedPage);

        Page<TaskDTO> result = taskService.getPageByFilter(null, null, null, null, null, null, null, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(taskRepository, times(1))
                .findPageByFilter(any(), any(), any(), any(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void getAllTasksByUserIdShouldReturnTasks() {
        Set<TaskDTO> expectedTasks = Set.of(taskDTO);
        when(taskRepository.findAllByUserId(userId)).thenReturn(expectedTasks);

        Set<TaskDTO> result = taskService.getAllTasksByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void getAllTasksByStatusShouldReturnTasks() {
        Set<TaskDTO> expectedTasks = Set.of(taskDTO);
        when(taskRepository.findAllByStatus(TaskService.TASK_PENDING_STATUS)).thenReturn(expectedTasks);

        Set<TaskDTO> result = taskService.getAllTasksByStatus(TaskService.TASK_PENDING_STATUS);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findAllByStatus(TaskService.TASK_PENDING_STATUS);
    }

    @Test
    void saveTaskShouldThrowExceptionWhenUserDoesNotExist() {
        when(userRestService.getUserById(userId)).thenReturn(null);

        assertThrows(TaskDataValidationException.class, () -> taskService.saveTask(taskDTO));
        verify(userRestService, times(1)).getUserById(userId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTaskShouldThrowExceptionWhenTaskIdIsNull() {
        taskDTO.setId(null);

        assertThrows(TaskDataValidationException.class, () -> taskService.updateTask(taskDTO));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTaskShouldThrowExceptionWhenTaskDoesNotExist() {
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskDataValidationException.class, () -> taskService.updateTask(taskDTO));
        verify(taskRepository, times(1)).findTaskById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTaskShouldThrowExceptionWhenTaskIsCompleted() {
        taskDTO.setStatus("C");
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(taskDTO));

        assertThrows(TaskDataConflictException.class, () -> taskService.updateTask(taskDTO));
        verify(taskRepository, times(1)).findTaskById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTaskShouldThrowExceptionWhenUserNotExists() {
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(taskDTO));
        when(userRestService.getUserById(userId)).thenReturn(null);

        assertThrows(TaskDataValidationException.class, () -> taskService.updateTask(taskDTO));
        verify(taskRepository, times(1)).findTaskById(taskId);
        verify(userRestService, times(1)).getUserById(userId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTaskByIdShouldDeleteTaskWhenTaskExists() {
        when(taskRepository.existsById(taskId)).thenReturn(true);

        assertDoesNotThrow(() -> taskService.deleteTaskById(taskId));
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void deleteTaskByIdShouldThrowExceptionWhenTaskDoesNotExist() {
        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThrows(TaskDataValidationException.class, () -> taskService.deleteTaskById(taskId));
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    void validateTaskExistenceShouldNotThrowExceptionWhenTaskExists() {
        when(taskRepository.existsById(taskId)).thenReturn(true);

        assertDoesNotThrow(() -> taskService.validateTaskExistence(taskId));
        verify(taskRepository, times(1)).existsById(taskId);
    }

    @Test
    void validateTaskExistenceShouldThrowExceptionWhenTaskDoesNotExist() {
        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThrows(TaskDataValidationException.class, () -> taskService.validateTaskExistence(taskId));
        verify(taskRepository, times(1)).existsById(taskId);
    }

    @Test
    void validateTaskExistenceAndStatusForUpdateShouldNotThrowExceptionWhenTaskIsPending() {
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(taskDTO));

        assertDoesNotThrow(() -> taskService.validateTaskExistenceAndStatusForUpdate(taskId));
        verify(taskRepository, times(1)).findTaskById(taskId);
    }

    @Test
    void validateTaskExistenceAndStatusForUpdateShouldThrowExceptionWhenTaskDoesNotExist() {
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskDataValidationException.class,
                () -> taskService.validateTaskExistenceAndStatusForUpdate(taskId));
        verify(taskRepository, times(1)).findTaskById(taskId);
    }

    @Test
    void validateTaskExistenceAndStatusForUpdateShouldThrowExceptionWhenTaskIsCompleted() {
        taskDTO.setStatus("C");
        when(taskRepository.findTaskById(taskId)).thenReturn(Optional.of(taskDTO));

        assertThrows(TaskDataConflictException.class,
                () -> taskService.validateTaskExistenceAndStatusForUpdate(taskId));
        verify(taskRepository, times(1)).findTaskById(taskId);
    }
}