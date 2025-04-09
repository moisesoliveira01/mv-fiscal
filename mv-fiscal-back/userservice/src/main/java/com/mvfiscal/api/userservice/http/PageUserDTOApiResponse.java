package com.mvfiscal.api.userservice.http;

import com.mvfiscal.api.userservice.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageUserDTOApiResponse extends GenericApiResponse<Page<UserDTO>> {

    protected Page<UserDTO> responseBody;
}
