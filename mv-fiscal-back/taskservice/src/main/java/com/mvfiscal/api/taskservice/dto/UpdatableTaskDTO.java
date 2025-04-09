package com.mvfiscal.api.taskservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvfiscal.api.taskservice.annotation.OneOf;
import com.mvfiscal.api.taskservice.model.Task;
import com.mvfiscal.api.taskservice.service.TaskService;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static java.util.Objects.nonNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatableTaskDTO extends TaskDTO {

    @ApiModelProperty(example = "0")
    @JsonProperty(access = JsonProperty.Access.AUTO)
    protected Long id;

    @ApiModelProperty(example = "2025-04-03T00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty(access = JsonProperty.Access.AUTO)
    protected Date createdDate;

    @ApiModelProperty(example = "P")
    @JsonProperty(access = JsonProperty.Access.AUTO)
    @OneOf(
            values = {"P", "A", "C"},
            message = "Status inválido para a tarefa. Valores possíveis são: P(Pendente), A(Em Andamento) e C(Concluída)")
    protected String status;

    @Override
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
}
