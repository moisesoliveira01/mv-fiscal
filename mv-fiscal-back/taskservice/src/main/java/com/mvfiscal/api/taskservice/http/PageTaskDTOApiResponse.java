package com.mvfiscal.api.taskservice.http;

import com.mvfiscal.api.taskservice.dto.TaskDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageTaskDTOApiResponse extends GenericApiResponse<Page<TaskDTO>> {

    protected Page<TaskDTO> responseBody;
}
