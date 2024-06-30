package com.example.rango.screen.credential

import com.example.rango.base.BaseViewModel
import com.example.rango.base.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CredentialViewModel @Inject constructor() : BaseViewModel() {

    val switchViewPager = SingleLiveData<Int>()

    fun handleRegisterClick(pagePosition: Int) {
        when (pagePosition) {
            CredentialAdapter.SIGN_IN_PAGE -> switchViewPager.postValue(CredentialAdapter.SIGN_UP_PAGE)
            CredentialAdapter.SIGN_UP_PAGE -> switchViewPager.post(CredentialAdapter.SIGN_IN_PAGE)
            else -> switchViewPager.postValue(CredentialAdapter.SIGN_UP_PAGE)
        }
    }
}