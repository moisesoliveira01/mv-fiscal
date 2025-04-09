package com.mvfiscal.api.taskservice.http;

import com.mvfiscal.api.taskservice.dto.TaskDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTOApiResponse extends GenericApiResponse<TaskDTO> {

    protected TaskDTO responseBody;
}
