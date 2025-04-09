package com.mvfiscal.api.taskservice.http;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorApiResponse extends GenericApiResponse<String> {

    @ApiModelProperty
    protected String message;
    @ApiModelProperty(hidden = true)
    protected String responseBody;

    public ValidationErrorApiResponse(String message) {
        this.message = message;
    }
}
