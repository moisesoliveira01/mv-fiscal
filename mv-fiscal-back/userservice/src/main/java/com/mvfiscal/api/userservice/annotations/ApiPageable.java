package com.mvfiscal.api.userservice.annotations;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "Página de resultados a ser buscada (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "Número de registros por página"
        )}
)
public @interface ApiPageable {
}
