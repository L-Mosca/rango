package com.example.rango.domain.models.error

data class ErrorMessage(
    val message: String,
    val code: Int = -1
)
