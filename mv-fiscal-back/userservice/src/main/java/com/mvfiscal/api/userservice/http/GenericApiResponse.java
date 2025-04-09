package com.mvfiscal.api.userservice.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class GenericApiResponse<T> {

    protected String message;
    protected T responseBody;
}
