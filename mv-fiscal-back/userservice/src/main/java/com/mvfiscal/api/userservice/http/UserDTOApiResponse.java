package com.mvfiscal.api.userservice.http;

import com.mvfiscal.api.userservice.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOApiResponse extends GenericApiResponse<UserDTO> {

    protected UserDTO responseBody;
}
