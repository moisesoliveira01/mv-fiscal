package com.mvfiscal.api.userservice.controller;

import com.mvfiscal.api.userservice.annotations.ApiPageable;
import com.mvfiscal.api.userservice.dto.*;
import com.mvfiscal.api.userservice.http.*;
import com.mvfiscal.api.userservice.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Api(tags = "Usuários")
@RestController
@RequestMapping("usuario")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "Busca um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna o usuário correspondente", response = UserDTOApiResponse.class),
            @ApiResponse(code = 204, message = "Nenhum usuário com o ID correspondente", response = VoidApiResponse.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro interno no servidor", response = ServerErrorApiResponse.class),
    })
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTOApiResponse> getUserById(@PathVariable("id") Long userId) {
        return this.userService.getUserById(userId)
                .map(dto -> ResponseEntity.ok()
                        .body(new UserDTOApiResponse(dto)))
                .orElseGet(
                        () -> ResponseEntity.noContent().build());
    }

    @ApiOperation(value = "Busca uma página de usuários por filtros")
    @ApiPageable
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna os usuários correspondentes", response = PageUserDTOApiResponse.class),
            @ApiResponse(code = 204, message = "Nenhum usuário encontrado", response = VoidApiResponse.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro interno no servidor", response = ServerErrorApiResponse.class),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageUserDTOApiResponse> getAllUsersByPage(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "createdDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date createdDate) {
        Page<UserDTO> usersDTOPage = this.userService.getPageByFilter(id, name, email, createdDate, pageable);
        if (usersDTOPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(new PageUserDTOApiResponse(usersDTOPage));
    }

    @ApiOperation(value = "Salva um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Retorna o usuário criado", response = UserDTOApiResponse.class),
            @ApiResponse(code = 400, message = "Dados enviados inválidos", response = ValidationErrorApiResponse.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro interno no servidor", response = ServerErrorApiResponse.class),
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTOApiResponse> saveUser(@RequestBody @Valid UserDTO userDTO) {
        return new ResponseEntity<>(new UserDTOApiResponse(
                UserDTO.fromEntity(this.userService.saveUser(userDTO))),
                HttpStatus.CREATED);
    }

    @ApiOperation(value = "Atualiza um usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna o usuário atualizado", response = UserDTOApiResponse.class),
            @ApiResponse(code = 400, message = "Dados enviados inválidos", response = ValidationErrorApiResponse.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro interno no servidor", response = ServerErrorApiResponse.class),
    })
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTOApiResponse> updateUser(@RequestBody @Valid UpdatableUserDTO userDTO) {
        return ResponseEntity.ok().body(new UserDTOApiResponse(
                UserDTO.fromEntity(this.userService.updateUser(userDTO))));
    }

    @ApiOperation(value = "Exclui um usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Usuário excluído com sucesso", response = VoidApiResponse.class),
            @ApiResponse(code = 404, message = "Nenhum usuário com o ID correspondente", response = ValidationErrorApiResponse.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro interno no servidor", response = ServerErrorApiResponse.class),
    })
    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoidApiResponse> deleteUserById(@PathVariable("id") @NotNull Long userId) {
        this.userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
