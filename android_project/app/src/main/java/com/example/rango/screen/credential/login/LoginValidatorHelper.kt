package com.example.rango.screen.credential.login

import android.content.Context
import com.example.rango.R
import com.example.rango.base.BaseValidatorHelper
import com.example.rango.domain.models.error.ErrorMessage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LoginValidatorHelper @Inject constructor(@ApplicationContext private val context: Context) :
    BaseValidatorHelper {

    companion object {
        const val EMPTY_LOGIN_EMAIL = 0
        const val EMPTY_LOGIN_PASSWORD = 1
    }

    override fun validate(vararg any: Any?): List<ErrorMessage>? {
        val email = any[0] as String?
        val password = any[1] as String?

        val errors = mutableListOf<ErrorMessage>()


        if (email.isNullOrEmpty()) errors.add(
            ErrorMessage(
                code = EMPTY_LOGIN_EMAIL, message = context.getString(
                    R.string.add_email
                )
            )
        )

        if (password.isNullOrEmpty()) errors.add(
            ErrorMessage(
                code = EMPTY_LOGIN_PASSWORD,
                message = context.getString(R.string.add_password)
            )
        )

        return errors.ifEmpty { null }
    }
}