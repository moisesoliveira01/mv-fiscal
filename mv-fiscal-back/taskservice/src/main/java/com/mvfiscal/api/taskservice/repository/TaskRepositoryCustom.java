package com.mvfiscal.api.taskservice.repository;

import com.mvfiscal.api.taskservice.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface TaskRepositoryCustom {
    Page<TaskDTO> findPageByFilter(
            Long id, String title, String description, String status,
            Date createdDate, Date limitDate, Long userId, Pageable pageable);
}
