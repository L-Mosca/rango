package com.example.rango.screen.home

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.rango.R
import com.example.rango.base.BaseViewModel
import com.example.rango.base.SingleLiveData
import com.example.rango.domain.repositories.order.OrderRepositoryContract
import com.example.rango.domain.repositories.user.UserRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepositoryContract,
    private val orderRepository: OrderRepositoryContract
) :
    BaseViewModel() {

    val logoutSuccess = SingleLiveData<Unit>()
    val slideUpAnimation = SingleLiveData<Animation>()
    val slideDownAnimation = SingleLiveData<Animation>()
    val hideMenu = SingleLiveData<Unit>()

    fun logout() {
        defaultLaunch {
            userRepository.logout()
            logoutSuccess.postValue(Unit)
        }
    }

    fun setListAnimation(isVisible: Boolean, context: Context, forceToClose: Boolean) {
        defaultLaunch {

        }

        if (forceToClose) {
            if (isVisible) {
                val listAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_down)
                slideDownAnimation.postValue(listAnimation)
            } else {
                hideMenu.postValue(Unit)
                return
            }

        }

        if (isVisible) {
            val listAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
            slideUpAnimation.postValue(listAnimation)
        } else {
            val listAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_down)
            slideDownAnimation.postValue(listAnimation)
        }
    }

}