package com.joe.yhfinance.data

data class StockItem(val exchangeName: String,
                     val symbol: String,
                     val prevClose: String,
                     val currentValue: String)
