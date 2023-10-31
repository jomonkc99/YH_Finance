package com.joe.yhfinance.data

data class StockDetails(val exchangeName: String?,
                        val exchangeType: String?,
                        val currency: String?,
                        val regularMarketPrice: String?,
                        val regularMarketChange: String?,
                        val regularMarketChangePercent: String?,
                        val regularMarketPreviousClose: String?,
                        val regularMarketOpen: String?,
                        val regularMarketVolume: String?,
                        val averageDailyVolume3Month: String?,
                        val regularMarketDayHigh: String?,
                        val regularMarketDayLow: String?,)