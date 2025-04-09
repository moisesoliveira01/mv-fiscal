package com.mvfiscal.api.userservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mvfiscal.api.userservice.model.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

import static java.util.Objects.nonNull;

@ApiModel("Usuário")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @ApiModelProperty(example = "0", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Long id;

    @ApiModelProperty(example = "João da Silva")
    @NotBlank(message = "Nome do usuário é obrigatório")
    protected String name;

    @ApiModelProperty(example = "joao.silva@gmail.com")
    @NotBlank(message = "Email do usuário é obrigatório")
    @Email(message = "Email inválido")
    protected String email;

    @ApiModelProperty(example = "2025-04-03T00:00", readOnly = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Date createdDate;

    public User asEntity() {
        return User.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .createdDate(nonNull(this.createdDate) ? this.createdDate : new Date())
                .build();
    }

    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedDate());
    }
}
