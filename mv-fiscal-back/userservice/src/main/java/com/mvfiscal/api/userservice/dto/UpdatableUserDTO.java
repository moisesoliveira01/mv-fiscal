package com.mvfiscal.api.userservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvfiscal.api.userservice.model.User;
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
public class UpdatableUserDTO extends UserDTO {

    @ApiModelProperty(example = "0")
    @JsonProperty(access = JsonProperty.Access.AUTO)
    protected Long id;

    @ApiModelProperty(example = "2025-04-03T00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty(access = JsonProperty.Access.AUTO)
    protected Date createdDate;

    @Override
    public User asEntity() {
        return User.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .createdDate(nonNull(this.createdDate) ? this.createdDate : new Date())
                .build();
    }
}
