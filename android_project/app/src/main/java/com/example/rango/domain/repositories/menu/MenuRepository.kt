package com.example.rango.domain.repositories.menu

import com.example.rango.domain.models.menu.DisheItem
import com.example.rango.domain.models.menu.MenuCategory
import com.example.rango.domain.models.menu.Table
import com.example.rango.domain.models.menu.toDisheList
import com.example.rango.domain.models.menu.toMenuCategories
import com.example.rango.domain.models.menu.toTables
import com.example.rango.utils.ApiConstants
import com.example.rango.domain.local.datastore.PreferencesDataStoreContract
import com.google.firebase.database.ktx.database
import com.google.firebase.database.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MenuRepository @Inject constructor(private val preferencesDataStore: PreferencesDataStoreContract) :
    MenuRepositoryContract {

    private val database = Firebase.database

    override suspend fun fetchCategories(): List<MenuCategory> {
        val firebaseData = database.getReference("/${ApiConstants.MENU_CATEGORIES}")
        val raw = firebaseData.snapshots.first().value as HashMap<*, *>
        val categoryList = raw.toMenuCategories()
        return categoryList
    }

    override suspend fun fetchTables(): List<Table> {
        val firebaseData = database.getReference("/${ApiConstants.TABLES}")
        val raw = firebaseData.snapshots.first().value as HashMap<*, *>
        val tableList = raw.toTables()
        return tableList
    }

    override suspend fun setTable(table: Table?) {
        preferencesDataStore.setTable(table)
    }

    override suspend fun fetchTable(): Table? = preferencesDataStore.getTable()

    override suspend fun fetchDisheList(): List<DisheItem> {
        val firebaseData = database.getReference("/${ApiConstants.ALL_DISHES}")
        val raw = firebaseData.snapshots.first().value as HashMap<*, *>
        val disheList = raw.toDisheList()
        return disheList
    }
}