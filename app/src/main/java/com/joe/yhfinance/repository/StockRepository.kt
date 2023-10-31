package com.joe.yhfinance.repository

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.joe.yhfinance.api.ApiService
import com.joe.yhfinance.data.StockDetails
import com.joe.yhfinance.data.StockItem

class StockRepository(private val apiService: ApiService) {

    suspend fun getStockList(context: Context): List<StockItem> {

        val listItems = mutableStateListOf<StockItem>()

        val response = apiService.getItems()

        if (response.isSuccessful) {

            val responseJson: JsonObject = response.body() ?: JsonObject()

            val responseSummary: JsonObject =
                responseJson.getAsJsonObject("marketSummaryAndSparkResponse")
            val responseArray: JsonArray = responseSummary.getAsJsonArray("result")

            for (json in responseArray) {

                try {

                    val fullExchangeName = json.asJsonObject.get("fullExchangeName").asString
                    val symbol = json.asJsonObject.get("symbol").asString
                    val prevClose =
                        json.asJsonObject.get("spark").asJsonObject.get("previousClose").asString

                    val sparkObject = json.asJsonObject.get("spark").asJsonObject

                    var currentValue = ""

                    if (sparkObject.get("close").isJsonNull) {

                        currentValue = sparkObject.get("previousClose").asString
                    } else {

                        val closeArray =
                            json.asJsonObject.get("spark").asJsonObject.getAsJsonArray("close")
                        currentValue = closeArray.get(closeArray.size() - 1).asString
                    }

                    listItems.add(
                        StockItem(
                            exchangeName = fullExchangeName,
                            symbol = symbol,
                            prevClose = prevClose,
                            currentValue = currentValue
                        )
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            return listItems

        } else {

            Toast.makeText(context, "Failed to fetch the list; You may have exceeded the limit", Toast.LENGTH_LONG).show()

            throw Exception("Failed to fetch items")
        }

    }

    suspend fun getDetails(symbol: String): StockDetails {

        val response = apiService.getDetails(symbol)

        if (response.isSuccessful) {

            val responseJson: JsonObject = response.body() ?: JsonObject()

            val responsePrice: JsonObject = responseJson.getAsJsonObject("price")

            val fullExchangeName = responsePrice.asJsonObject.get("exchangeName").asString
            val exchangeType = responsePrice.asJsonObject.get("quoteType").asString.lowercase()
            val currency = responsePrice.asJsonObject.get("currency").asString
            val regularMarketPrice: JsonElement? =
                responsePrice.asJsonObject.get("regularMarketPrice")?.asJsonObject?.get("fmt")
            val regularMarketChange: JsonElement? =
                responsePrice.asJsonObject.get("regularMarketChange")?.asJsonObject?.get("fmt")
            val regularMarketChangePercent: JsonElement? =
                responsePrice.asJsonObject.get("regularMarketChangePercent")?.asJsonObject?.get("fmt")

            val regularMarketPreviousClose: JsonElement? =
                responsePrice.asJsonObject.get("regularMarketPreviousClose")?.asJsonObject?.get("fmt")
            val regularMarketOpen: JsonElement? =
                responsePrice.asJsonObject.get("regularMarketOpen")?.asJsonObject?.get("fmt")
            val regularMarketVolume: JsonElement? =
                responsePrice.asJsonObject.get("regularMarketVolume")?.asJsonObject?.get("fmt")
            val averageDailyVolume3Month: JsonElement? =
                responsePrice.asJsonObject.get("averageDailyVolume3Month")?.asJsonObject?.get("fmt")
            val regularMarketDayHigh: JsonElement? =
                responsePrice.asJsonObject.get("regularMarketDayHigh")?.asJsonObject?.get("fmt")
            val regularMarketDayLow: JsonElement? =
                responsePrice.asJsonObject.get("regularMarketDayLow")?.asJsonObject?.get("fmt")

            return StockDetails(
                exchangeName = fullExchangeName,
                exchangeType = exchangeType,
                currency = currency,
                regularMarketPrice = if (regularMarketPrice?.isJsonNull == true) "---" else regularMarketPrice?.asString,
                regularMarketChange = if (regularMarketChange?.isJsonNull == true) "---" else regularMarketChange?.asString,
                regularMarketChangePercent = if (regularMarketChangePercent?.isJsonNull == true) "---" else regularMarketChangePercent?.asString,
                regularMarketPreviousClose = if (regularMarketPreviousClose?.isJsonNull == true) "---" else regularMarketPreviousClose?.asString,
                regularMarketOpen = if (regularMarketOpen?.isJsonNull == true) "---" else regularMarketOpen?.asString,
                regularMarketVolume = if (regularMarketVolume?.isJsonNull == true) "---" else regularMarketVolume?.asString,
                averageDailyVolume3Month = if (averageDailyVolume3Month?.isJsonNull == true) "---" else averageDailyVolume3Month?.asString,
                regularMarketDayHigh = if (regularMarketDayHigh?.isJsonNull == true) "---" else regularMarketDayHigh?.asString,
                regularMarketDayLow = if (regularMarketDayLow?.isJsonNull == true) "---" else regularMarketDayLow?.asString,
            )

        } else {
            throw Exception("Failed to fetch items")
        }

    }
}
