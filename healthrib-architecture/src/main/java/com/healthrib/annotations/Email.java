package com.healthrib.annotations;

import static com.healthrib.enums.ValidationMessagesType.EMAIL;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.healthrib.annotations.Email.List;
import com.healthrib.enums.ValidationMessagesType;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


/**
 * The annotated element must not be {@code null} and must contain at least one
 * only allow strings with email regex pattern: ^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$
 *
 * <br>
 * <br>
 * <strong>Examples:</strong>
 * <ul>
 * <ol>null ---> false</ol>
 * <ol>"" ---> false</ol>
 * <ol>test ---> false</ol>
 * <ol>test@ ---> false</ol>
 * <ol>test@company.com ---> true</ol>
 * <ol>test@company.com.br ---> true</ol>
 *  <ol>company@company.com.br ---> true</ol>
 * </ul>
 *
 * @author Lucas Ribeiro
 * @since 2.0
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface Email {
	
	ValidationMessagesType message() default EMAIL;

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
	/**
	 * Defines several {@link Email} annotations on the same element.
	 *
	 * @see com.healthrib.annotations.Email
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		Email[] value();
	}

}
