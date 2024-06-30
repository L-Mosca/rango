package com.example.rango.screen.credential.register

import android.content.Context
import com.example.rango.R
import com.example.rango.base.BaseValidatorHelper
import com.example.rango.domain.models.error.ErrorMessage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RegisterValidatorHelper @Inject constructor(@ApplicationContext private val context: Context) :
    BaseValidatorHelper {

    companion object {
        const val EMPTY_NAME = 0
        const val EMPTY_EMAIL = 1
        const val EMPTY_PASSWORD = 2
        const val EMPTY_CONFIRM_PASSWORD = 3
        const val DIFFERENT_PASSWORDS = 4
    }

    override fun validate(vararg any: Any?): List<ErrorMessage>? {
        val name = any[0] as String?
        val email = any[1] as String?
        val password = any[2] as String?
        val confirmPassword = any[3] as String?

        val errors = mutableListOf<ErrorMessage>()

        if (name.isNullOrEmpty()) errors.add(
            ErrorMessage(
                code = EMPTY_NAME, message = context.getString(
                    R.string.insert_name
                )
            )
        )

        if (email.isNullOrEmpty()) errors.add(
            ErrorMessage(
                code = EMPTY_EMAIL, message = context.getString(
                    R.string.add_email
                )
            )
        )

        if (password.isNullOrEmpty()) errors.add(
            ErrorMessage(
                code = EMPTY_PASSWORD, message = context.getString(
                    R.string.add_password
                )
            )
        )

        if (confirmPassword.isNullOrEmpty()) errors.add(
            ErrorMessage(
                code = EMPTY_CONFIRM_PASSWORD, message = context.getString(
                    R.string.confirm_password_empty
                )
            )
        )

        if (confirmPassword != password) errors.add(
            ErrorMessage(
                code = DIFFERENT_PASSWORDS, message = context.getString(
                    R.string.different_passwords
                )
            )
        )

        return errors.ifEmpty { null }
    }

    fun passWordIsValid(data: String?): String? {
        val password = data ?: ""
        val length = password.length >= 8
        val upperLetter = Regex("[A-Z]").containsMatchIn(password)
        val lowerCase = Regex("[a-z]").containsMatchIn(password)
        val number = Regex("[0-9]").containsMatchIn(password)

        return if (length && upperLetter && lowerCase && number) null else context.getString(R.string.invalid_password_format)
    }
}