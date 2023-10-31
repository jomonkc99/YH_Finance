package com.joe.yhfinance.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joe.yhfinance.api.ApiService
import com.joe.yhfinance.api.RetrofitClient
import com.joe.yhfinance.data.StockDetails
import com.joe.yhfinance.data.StockItem
import com.joe.yhfinance.repository.StockRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StockViewModel : ViewModel() {

    val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
    val stockRepository = StockRepository(apiService)

    private val _items = mutableStateOf<List<StockItem>>(emptyList())

    val selList = MutableStateFlow<List<StockItem>>(emptyList())

    val stockDetails = MutableLiveData<StockDetails>()

    fun fetchItems(context: Context) {

        viewModelScope.launch {
            try {

                selList.emit(stockRepository.getStockList(context))

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getStockDetails(symbol: String) {

        CoroutineScope(Dispatchers.IO).launch {

            val details = stockRepository.getDetails(symbol)

            withContext(Dispatchers.Main) {
                stockDetails.postValue(details)
            }
        }
    }
}
