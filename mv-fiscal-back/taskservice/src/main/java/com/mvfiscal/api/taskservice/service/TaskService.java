package com.mvfiscal.api.taskservice.service;

import com.mvfiscal.api.taskservice.dto.TaskDTO;
import com.mvfiscal.api.taskservice.exception.TaskDataConflictException;
import com.mvfiscal.api.taskservice.exception.TaskDataValidationException;
import com.mvfiscal.api.taskservice.model.Task;
import com.mvfiscal.api.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserRestService userRestService;
    private final TaskRepository taskRepository;
    public static final String TASK_PENDING_STATUS = "P";
    public static final String TASK_IN_PROGRESS_STATUS = "A";

    public Optional<TaskDTO> getTaskById(Long taskId) {
        return this.taskRepository.findTaskById(taskId);
    }

    public Page<TaskDTO> getPageByFilter(
            Long id, String title, String description, String status,
            Date createdDate, Date limitDate, Long userId, Pageable pageable) {
        return this.taskRepository.findPageByFilter(
                id, title, description, status, createdDate, limitDate, userId, pageable);
    }

    public Set<TaskDTO> getAllTasksByUserId(Long userId) {
        return this.taskRepository.findAllByUserId(userId);
    }

    public Set<TaskDTO> getAllTasksByStatus(String status) {
        return this.taskRepository.findAllByStatus(status);
    }

    public Task saveTask(@Valid TaskDTO taskDTO) {
        this.validateUserExistence(taskDTO.getUserId());
        return this.taskRepository.save(taskDTO.asEntity());
    }

    public void validateTaskExistence(Long taskId) {
        if (!this.taskRepository.existsById(taskId)) {
            throw new TaskDataValidationException(
                    String.format("Nenhuma terefa encontrada com o ID: %d", taskId));
        }
    }

    private void validateUserExistence(Long userId) {
        if (isNull(this.userRestService.getUserById(userId))) {
            throw new TaskDataValidationException(
                    String.format("Não existe nenhum usuário com o ID: %d", userId));
        }
    }

    public void validateTaskExistenceAndStatusForUpdate(Long taskId) {
        Task databaseTask = this.taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskDataValidationException(
                        String.format("Nenhuma terefa encontrada com o ID: %d", taskId)))
                .asEntity();

        if (!databaseTask.getStatus().equals(TASK_PENDING_STATUS)
                && !databaseTask.getStatus().equals(TASK_IN_PROGRESS_STATUS)) {
            throw new TaskDataConflictException("A tarefa já foi concluída, portanto não pode ser editada");
        }
    }

    public Task updateTask(@Valid TaskDTO taskDTO) {
        if (isNull(taskDTO.getId())) {
            throw new TaskDataValidationException("O ID da tarefa deve ser informado");
        }
        this.validateTaskExistenceAndStatusForUpdate(taskDTO.getId());
        this.validateUserExistence(taskDTO.getUserId());

        return this.taskRepository.save(taskDTO.asEntity());
    }

    public void deleteTaskById(@NotNull Long taskId) {
        this.validateTaskExistence(taskId);

        this.taskRepository.deleteById(taskId);
    }
}
