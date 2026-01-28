package com.example.off.common.annotation;

import com.example.off.common.swagger.SwaggerResponseDescription;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomExceptionDescription {

    SwaggerResponseDescription value();
}
