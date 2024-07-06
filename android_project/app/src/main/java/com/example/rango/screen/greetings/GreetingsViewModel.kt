package com.example.rango.screen.greetings

import com.example.rango.base.BaseViewModel
import com.example.rango.domain.local.datastore.PreferencesDataStoreContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GreetingsViewModel @Inject constructor(private val preferencesDataStore: PreferencesDataStoreContract) :
    BaseViewModel() {

    fun disableGreetings() {
        defaultLaunch {
            preferencesDataStore.showGreetings(false)
        }
    }
}