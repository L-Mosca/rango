package com.example.rango.screen.credential.login

import android.view.inputmethod.EditorInfo
import com.example.rango.base.BaseViewModel
import com.example.rango.base.SingleLiveData
import com.example.rango.domain.models.error.ErrorMessage
import com.example.rango.domain.models.user.User
import com.example.rango.domain.repositories.user.UserRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validatorHelper: LoginValidatorHelper,
    private val userRepository: UserRepositoryContract
) :
    BaseViewModel() {

    val emailError = SingleLiveData<String>()
    val passwordError = SingleLiveData<String>()

    val loginError = SingleLiveData<Unit>()
    val loginSuccess = SingleLiveData<User>()

    fun signIn(email: String, password: String) {
        validateLoginFields(email, password)
    }

    private fun validateLoginFields(email: String?, password: String?) {
        val errors = validatorHelper.validate(email, password)

        if (errors.isNullOrEmpty()) {
            doLogin(email!!, password!!)
        } else {
            showErrorMessages(errors)
        }
    }

    private fun doLogin(email: String, password: String) {
        defaultLaunch {
            val user = userRepository.login(email, password)
            if (user == null) loginError.postValue(Unit)
            else {
                userRepository.saveUserData(user)
                loginSuccess.postValue(user!!)
            }
        }
    }

    private fun showErrorMessages(errorMessages: List<ErrorMessage>) {
        errorMessages.forEach { error ->
            when (error.code) {
                LoginValidatorHelper.EMPTY_LOGIN_EMAIL -> {
                    emailError.postValue(error.message)
                }

                LoginValidatorHelper.EMPTY_LOGIN_PASSWORD -> {
                    passwordError.postValue(error.message)
                }
            }
        }
    }

    fun handleImeAction(email: String, password: String, actionId: Int) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            signIn(email, password)
        }
    }
}