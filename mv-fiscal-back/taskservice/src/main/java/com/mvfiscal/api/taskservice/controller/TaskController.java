package com.mvfiscal.api.taskservice.controller;

import com.mvfiscal.api.taskservice.annotation.ApiPageable;
import com.mvfiscal.api.taskservice.dto.TaskDTO;
import com.mvfiscal.api.taskservice.dto.UpdatableTaskDTO;
import com.mvfiscal.api.taskservice.http.*;
import com.mvfiscal.api.taskservice.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@Api(tags = "Tarefas")
@RestController
@RequestMapping("tarefa")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @ApiOperation(value = "Busca uma tarefa pelo ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a tarefa correspondente", response = TaskDTOApiResponse.class),
            @ApiResponse(code = 204, message = "Nenhuma tarefa com o ID correspondente", response = VoidApiResponse.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro interno no servidor", response = ServerErrorApiResponse.class),
    })
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDTOApiResponse> getTaskById(@PathVariable("id") Long taskId) {
        return this.taskService.getTaskById(taskId)
                .map(dto -> ResponseEntity.ok()
                        .body(new TaskDTOApiResponse(dto)))
                .orElseGet(
                        () -> ResponseEntity.noContent().build());
    }

    @ApiOperation(value = "Busca uma página de tarefas por filtros")
    @ApiPageable
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna as tarefas correspondentes", response = PageTaskDTOApiResponse.class),
            @ApiResponse(code = 204, message = "Nenhuma tarefa encontrada", response = VoidApiResponse.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro interno no servidor", response = ServerErrorApiResponse.class),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageTaskDTOApiResponse> getAllTasksByPage(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "createdDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date createdDate,
            @RequestParam(name = "limitDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date limitDate,
            @RequestParam(name = "userId", required = false) Long userId) {
        Page<TaskDTO> tasksDTOPage = this.taskService.getPageByFilter(
                id, title, description, status, createdDate, limitDate, userId, pageable);
        if (tasksDTOPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(new PageTaskDTOApiResponse(tasksDTOPage));
    }

    @ApiOperation(value = "Salva uma nova tarefa")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Retorna a tarefa criada", response = TaskDTOApiResponse.class),
            @ApiResponse(code = 400, message = "Dados enviados inválidos", response = ValidationErrorApiResponse.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro interno no servidor", response = ServerErrorApiResponse.class),
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDTOApiResponse> saveTask(@RequestBody @Valid TaskDTO taskDTO) {
        return new ResponseEntity<>(new TaskDTOApiResponse(
                TaskDTO.fromEntity(this.taskService.saveTask(taskDTO))),
                HttpStatus.CREATED);
    }

    @ApiOperation(value = "Atualiza uma tarefa")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a tarefa atualizada", response = TaskDTOApiResponse.class),
            @ApiResponse(code = 400, message = "Dados enviados inválidos", response = ValidationErrorApiResponse.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro interno no servidor", response = ServerErrorApiResponse.class),
    })
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDTOApiResponse> updateTask(@RequestBody @Valid UpdatableTaskDTO taskDTO) {
        return ResponseEntity.ok().body(new TaskDTOApiResponse(
                TaskDTO.fromEntity(this.taskService.updateTask(taskDTO))));
    }

    @ApiOperation(value = "Exclui uma tarefa")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Tarefa excluída com sucesso", response = VoidApiResponse.class),
            @ApiResponse(code = 404, message = "Nenhuma tarefa com o ID correspondente", response = ValidationErrorApiResponse.class),
            @ApiResponse(code = 500, message = "Ocorreu um erro interno no servidor", response = ServerErrorApiResponse.class),
    })
    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoidApiResponse> deleteTaskById(@PathVariable("id") @NotNull Long taskId) {
        this.taskService.deleteTaskById(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
