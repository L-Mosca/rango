package com.example.rango.screen.credential.register

import com.example.rango.R
import com.example.rango.base.BaseViewModel
import com.example.rango.base.SingleLiveData
import com.example.rango.domain.models.error.ErrorMessage
import com.example.rango.domain.models.user.User
import com.example.rango.domain.repositories.user.UserRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validatorHelper: RegisterValidatorHelper,
    private val userRepository: UserRepositoryContract
) :
    BaseViewModel() {

    val nameError = SingleLiveData<String>()
    val emailError = SingleLiveData<String>()
    val passwordError = SingleLiveData<String>()
    val confirmPasswordError = SingleLiveData<String>()
    val differentPasswordsError = SingleLiveData<String>()

    val passwordLength = SingleLiveData<Int>()
    val passwordUpperLetter = SingleLiveData<Int>()
    val passwordLowerLetter = SingleLiveData<Int>()
    val passwordNumber = SingleLiveData<Int>()

    val registerSuccess = SingleLiveData<User>()
    val registerError = SingleLiveData<Unit>()

    private var isValidPassword = false

    fun validatePasswordRequirements(newPassword: String?) {
        val password = newPassword ?: ""
        val length = password.length >= 8
        val upperLetter = Regex("[A-Z]").containsMatchIn(password)
        val lowerCase = Regex("[a-z]").containsMatchIn(password)
        val number = Regex("[0-9]").containsMatchIn(password)

        isValidPassword = length && upperLetter && lowerCase && number

        passwordLength.postValue(if (length) R.color.green else R.color.red)
        passwordUpperLetter.postValue(if (upperLetter) R.color.green else R.color.red)
        passwordLowerLetter.postValue(if (lowerCase) R.color.green else R.color.red)
        passwordNumber.postValue(if (number) R.color.green else R.color.red)
    }

    fun signUp(name: String?, email: String?, password: String?, confirmPassword: String?) {
        val errors = validatorHelper.validate(name, email, password, confirmPassword)
        val invalidPasswordMessage = validatorHelper.passWordIsValid(password)

        if (errors.isNullOrEmpty()) {
            if (invalidPasswordMessage == null) register(name!!, email!!, password!!)
            else passwordError.postValue(invalidPasswordMessage!!)
        } else {
            showErrorMessages(errors)
        }
    }

    private fun showErrorMessages(errorMessages: List<ErrorMessage>) {
        errorMessages.forEach { error ->
            when (error.code) {
                RegisterValidatorHelper.EMPTY_NAME -> nameError.postValue(error.message)
                RegisterValidatorHelper.EMPTY_EMAIL -> emailError.postValue(error.message)
                RegisterValidatorHelper.EMPTY_PASSWORD -> passwordError.postValue(error.message)
                RegisterValidatorHelper.EMPTY_CONFIRM_PASSWORD -> confirmPasswordError.postValue(
                    error.message
                )

                RegisterValidatorHelper.DIFFERENT_PASSWORDS -> differentPasswordsError.postValue(
                    error.message
                )
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        defaultLaunch(exceptionHandler = {
            registerError.postValue(Unit)
        }) {
            val user = userRepository.registerNewUser(name, email, password)
            if (user == null) registerError.postValue(Unit)
            else {
                userRepository.saveUserData(user)
                registerSuccess.postValue(user!!)
            }
        }
    }
}