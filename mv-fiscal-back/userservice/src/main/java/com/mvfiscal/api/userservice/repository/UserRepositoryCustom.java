package com.mvfiscal.api.userservice.repository;

import com.mvfiscal.api.userservice.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface UserRepositoryCustom {
    Page<UserDTO> findPageByFilter(Long id, String name, String email, Date createdDate, Pageable pageable);
}
