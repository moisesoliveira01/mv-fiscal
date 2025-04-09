package com.mvfiscal.api.userservice.repository;

import com.mvfiscal.api.userservice.dto.UserDTO;
import com.mvfiscal.api.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long>, UserRepositoryCustom {

    @Query("SELECT new com.mvfiscal.api.userservice.dto.UserDTO( " +
            " u.id, u.name, u.email, u.createdDate ) " +
            " FROM User u " +
            " WHERE u.id = :userId")
    Optional<UserDTO> findUserById(@Param("userId") Long userId);
}
