package com.example.rango.di

import com.example.rango.domain.local.datastore.PreferencesDataStore
import com.example.rango.domain.repositories.menu.MenuRepository
import com.example.rango.domain.repositories.menu.MenuRepositoryContract
import com.example.rango.domain.repositories.order.OrderRepository
import com.example.rango.domain.repositories.order.OrderRepositoryContract
import com.example.rango.domain.repositories.user.UserRepository
import com.example.rango.domain.repositories.user.UserRepositoryContract
import com.example.rangodomain.local.datastore.PreferencesDataStoreContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Singleton
    @Binds
    fun bindDataStore(dataStore: PreferencesDataStore): PreferencesDataStoreContract

    @Singleton
    @Binds
    fun bindUserRepository(userRepository: UserRepository): UserRepositoryContract

    @Singleton
    @Binds
    fun bindMenuRepository(menuRepository: MenuRepository): MenuRepositoryContract

    @Singleton
    @Binds
    fun bindOrderRepository(orderRepository: OrderRepository): OrderRepositoryContract
}