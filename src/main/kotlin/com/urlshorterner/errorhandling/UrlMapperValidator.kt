package com.urlshorterner.errorhandling;

import org.apache.commons.validator.routines.UrlValidator
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class UrlMapperValidator: ConstraintValidator<Url, String> {
  override fun isValid(value: String, context: ConstraintValidatorContext) :Boolean {
    return UrlValidator(arrayOf("http","https")).isValid(value)
  }
}