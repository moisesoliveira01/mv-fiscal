package com.mvfiscal.api.taskservice.http;

import com.mvfiscal.api.taskservice.dto.TaskDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetTaskDTOApiResponse extends GenericApiResponse<Set<TaskDTO>> {

    protected Set<TaskDTO> responseBody;
}
