package com.example.rango.screen.splash

import android.os.Handler
import android.os.Looper
import com.example.rango.base.BaseViewModel
import com.example.rango.base.SingleLiveData
import com.example.rango.domain.models.order.Order
import com.example.rango.domain.repositories.order.OrderRepositoryContract
import com.example.rango.domain.repositories.user.UserRepositoryContract
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepositoryContract,
    private val orderRepository: OrderRepositoryContract
) :
    BaseViewModel() {

    val showHomeScreen = SingleLiveData<Unit>()
    val showCredentialScreen = SingleLiveData<Unit>()
    val showGreetingsScreen = SingleLiveData<Unit>()

    fun loadSplashData() {
        Handler(Looper.getMainLooper()).postDelayed({
            fetchData()
        }, 2000)
    }

    private fun fetchData() {
        defaultLaunch {
            checkOrder()
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val user = userRepository.getUserData()

            if (user != null && firebaseUser != null) showHomeScreen.postValue(Unit)
            else showCredentialScreen.postValue(Unit)
        }
    }

    private fun checkOrder() {
        defaultLaunch {
            val order = orderRepository.fetchOrders()
            if (order == null) {
                val newData = Order.buildDefaultOrder()
                orderRepository.createNewOrder(newData)
            }
        }
    }
}