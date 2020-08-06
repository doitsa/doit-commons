package br.com.doit.commons.moshi.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.squareup.moshi.JsonQualifier;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
@JsonQualifier
public @interface SerializeNull {

}
