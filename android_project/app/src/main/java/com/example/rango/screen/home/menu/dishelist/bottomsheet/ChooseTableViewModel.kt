package com.example.rango.screen.home.menu.dishelist.bottomsheet

import com.example.rango.base.BaseViewModel
import com.example.rango.base.SingleLiveData
import com.example.rango.domain.models.menu.Table
import com.example.rango.domain.repositories.menu.MenuRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseTableViewModel @Inject constructor(
    private val menuRepository: MenuRepositoryContract
) :
    BaseViewModel() {

    val tableList = SingleLiveData<List<Table>>()
    val tableSelected = SingleLiveData<Table>()

    fun fetchTables() {
        defaultLaunch {
            val tables = menuRepository.fetchTables()
            if (tables.isNotEmpty()) tableList.postValue(tables)
        }
    }

    fun selectTable(table: Table) {
        defaultLaunch {
            menuRepository.setTable(table)
            tableSelected.postValue(table)
        }
    }
}