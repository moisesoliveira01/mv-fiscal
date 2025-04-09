package com.mvfiscal.api.userservice.service;

import com.mvfiscal.api.userservice.dto.SetTaskDTO;
import com.mvfiscal.api.userservice.dto.UserDTO;
import com.mvfiscal.api.userservice.exception.UserDataValidationException;
import com.mvfiscal.api.userservice.model.User;
import com.mvfiscal.api.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TaskRestService taskRestService;
    private final UserRepository userRepository;

    public Optional<UserDTO> getUserById(Long userId) {
        return this.userRepository.findUserById(userId);
    }

    public Page<UserDTO> getPageByFilter(Long id, String name, String email, Date createdDate, Pageable pageable) {
        return this.userRepository.findPageByFilter(id, name, email, createdDate, pageable);
    }

    public User saveUser(UserDTO userDTO) {
        return this.userRepository.save(userDTO.asEntity());
    }

    public void validateUserExistence(Long userId) {
        if (!this.userRepository.existsById(userId)) {
            throw new UserDataValidationException(
                    String.format("Nenhum usuário encontrado com o ID: %d", userId));
        }
    }

    private boolean userHasAssociatedTasks(Long userId) {
        return this.taskRestService.userHasAssociatedTasks(userId);
    }

    public User updateUser(UserDTO userDTO) {
        if (isNull(userDTO.getId())) {
            throw new UserDataValidationException("O ID do usuário deve ser informado");
        }
        this.validateUserExistence(userDTO.getId());

        return this.userRepository.save(userDTO.asEntity());
    }

    public void deleteUserById(Long userId) {
        this.validateUserExistence(userId);

        if (this.userHasAssociatedTasks(userId)) {
            throw new UserDataValidationException("O usuário possui tarefas associadas, portanto não pode ser excluído");
        }

        this.userRepository.deleteById(userId);
    }
}
