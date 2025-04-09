package com.mvfiscal.api.taskservice.repository;

import com.mvfiscal.api.taskservice.dto.TaskDTO;
import com.mvfiscal.api.taskservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {

    @Query("SELECT new com.mvfiscal.api.taskservice.dto.TaskDTO( " +
            " t.id, t.title, t.description, t.status, t.createdDate, t.limitDate, t.userId ) " +
            " FROM Task t " +
            " WHERE t.id = :taskId")
    Optional<TaskDTO> findTaskById(@Param("taskId") Long taskId);

    @Query("SELECT new com.mvfiscal.api.taskservice.dto.TaskDTO( " +
            " t.id, t.title, t.description, t.status, t.createdDate, t.limitDate, t.userId ) " +
            " FROM Task t " +
            " WHERE t.userId = :userId")
    Set<TaskDTO> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.mvfiscal.api.taskservice.dto.TaskDTO( " +
            " t.id, t.title, t.description, t.status, t.createdDate, t.limitDate, t.userId ) " +
            " FROM Task t " +
            " WHERE t.status = :status")
    Set<TaskDTO> findAllByStatus(@Param("status") String status);
}
