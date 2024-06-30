package com.example.rango.domain.repositories.menu

import com.example.rango.domain.models.menu.DisheItem
import com.example.rango.domain.models.menu.MenuCategory
import com.example.rango.domain.models.menu.Table

interface MenuRepositoryContract {

    suspend fun fetchCategories(): List<MenuCategory>

    suspend fun fetchTables(): List<Table>

    suspend fun setTable(table: Table?)

    suspend fun fetchTable(): Table?

    suspend fun fetchDisheList(): List<DisheItem>
}