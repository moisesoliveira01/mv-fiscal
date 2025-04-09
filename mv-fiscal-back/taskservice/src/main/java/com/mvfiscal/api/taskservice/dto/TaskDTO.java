package com.mvfiscal.api.taskservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvfiscal.api.taskservice.annotation.OneOf;
import com.mvfiscal.api.taskservice.model.Task;
import com.mvfiscal.api.taskservice.service.TaskService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static java.util.Objects.nonNull;

@ApiModel("Tarefa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    @ApiModelProperty(example = "0", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Long id;

    @ApiModelProperty(example = "Levantamento de requisitos do projeto")
    @NotBlank(message = "Título da tarefa é obrigatório")
    protected String title;

    @ApiModelProperty(example = "Criação do documento de levantamento de requisitos para o projeto A")
    @NotBlank(message = "Descrição da tarefa é obrigatória")
    protected String description;

    @ApiModelProperty(example = "P", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneOf(
            values = {"P", "A", "C"},
            message = "Status inválido. Valores possíveis são: P(Pendente), A(Em Andamento) e C(Concluída)")
    protected String status;

    @ApiModelProperty(example = "2025-04-03T00:00", readOnly = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Date createdDate;

    @ApiModelProperty(example = "2025-04-10T00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    protected Date limitDate;

    @ApiModelProperty(example = "0")
    @NotNull
    protected Long userId;

    public Task asEntity() {
        return Task.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .status(nonNull(this.status) ? this.status : TaskService.TASK_PENDING_STATUS)
                .createdDate(nonNull(this.createdDate) ? this.createdDate : new Date())
                .limitDate(this.limitDate)
                .userId(this.userId)
                .build();
    }

    public static TaskDTO fromEntity(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedDate(),
                task.getLimitDate(),
                task.getUserId());
    }
}
