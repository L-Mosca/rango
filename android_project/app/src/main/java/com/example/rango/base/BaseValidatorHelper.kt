package com.example.rango.base

import com.example.rango.domain.models.error.ErrorMessage

interface BaseValidatorHelper {
    fun validate(vararg any: Any?): List<ErrorMessage>?
}