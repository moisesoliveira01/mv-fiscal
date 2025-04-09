package com.mvfiscal.api.taskservice.http;

import io.swagger.annotations.ApiModelProperty;

public class VoidApiResponse extends GenericApiResponse<Void> {

    @ApiModelProperty(hidden = true)
    private String message;
    @ApiModelProperty(hidden = true)
    private Void responseBody;
}
