package com.example.rango.domain.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.rango.BuildConfig
import com.example.rango.domain.models.menu.DisheItem
import com.example.rango.domain.models.menu.Table
import com.example.rango.domain.models.order.Order
import com.example.rango.domain.models.user.User
import com.example.rangodomain.local.datastore.PreferencesDataStoreContract
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PreferencesDataStore @Inject constructor(@ApplicationContext val context: Context) :
    PreferencesDataStoreContract {

    companion object {
        private const val PREFERENCES_NAME =
            "${BuildConfig.APP_NAME}.${BuildConfig.FLAVOR}.DataStore.WriteAway"
        private val userData = stringPreferencesKey(name = "$PREFERENCES_NAME.userData")
        private val order = stringPreferencesKey(name = "$PREFERENCES_NAME.order")
    }

    private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)
    private val dataStore = context.dataStore

    override suspend fun saveUserData(user: User?) {
        dataStore.edit { pref ->
            pref[userData] = Gson().toJson(user)
        }
    }

    override suspend fun getUserData(): User? {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            val data = pref[userData]
            if (data.isNullOrEmpty()) return@map null
            else Gson().fromJson(data, User::class.java)
        }.first()
    }

    override suspend fun setTable(tableData: Table?) {
        var orderData = dataStore.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { pref ->
            val data = pref[order]
            if (data.isNullOrEmpty()) return@map null
            else Gson().fromJson(data, Order::class.java)
        }.first()
        if (orderData == null) orderData = Order.buildDefaultOrder()
        orderData.table = tableData ?: Table.buildADefaultTable()
        dataStore.edit { pref ->
            pref[order] = Gson().toJson(orderData)
        }
    }

    override suspend fun getTable(): Table? {
        return dataStore.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { pref ->
            val data = pref[order]
            if (data.isNullOrEmpty()) return@map null
            else Gson().fromJson(data, Order::class.java).table
        }.first()
    }

    override suspend fun getOrder(): Order? {
        return dataStore.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { pref ->
            val data = pref[order]
            if (data.isNullOrEmpty()) return@map null
            else Gson().fromJson(data, Order::class.java)
        }.first()
    }

    override suspend fun setOrder(orderData: Order?) {
        dataStore.edit { pref ->
            pref[order] = Gson().toJson(orderData)
        }
    }

    override suspend fun addOrderItem(dishe: DisheItem) {
        var orderData = dataStore.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { pref ->
            val data = pref[order]
            if (data.isNullOrEmpty()) return@map null
            else Gson().fromJson(data, Order::class.java)
        }.first()
        if (orderData == null) orderData = Order.buildDefaultOrder()

        orderData.items.add(dishe)

        dataStore.edit { pref ->
            pref[order] = Gson().toJson(orderData)
        }
    }

    override suspend fun removeOrderItem(dishe: DisheItem) {
        var orderData = dataStore.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { pref ->
            val data = pref[order]
            if (data.isNullOrEmpty()) return@map null
            else Gson().fromJson(data, Order::class.java)
        }.first()
        if (orderData == null) orderData = Order.buildDefaultOrder()

        if (orderData.items.contains(dishe)) orderData.items.remove(dishe)

        dataStore.edit { pref ->
            pref[order] = Gson().toJson(orderData)
        }
    }

    override suspend fun clearOrder() {
        dataStore.edit { pref ->
            pref[order] = Gson().toJson(Order.buildDefaultOrder())
        }
    }

    override suspend fun observeOrder(): Flow<Order?> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { pref ->
            val data = pref[order]
            if (data.isNullOrEmpty()) return@map null
            else Gson().fromJson(data, Order::class.java)
        }
    }

    override suspend fun setMessage(message: String) {
        var orderData = dataStore.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { pref ->
            val data = pref[order]
            if (data.isNullOrEmpty()) return@map null
            else Gson().fromJson(data, Order::class.java)
        }.first()
        if (orderData == null) orderData = Order.buildDefaultOrder()

        orderData.message = message

        dataStore.edit { pref ->
            pref[order] = Gson().toJson(orderData)
        }
    }
}