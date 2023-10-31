package com.joe.yhfinance.views.composables

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.joe.yhfinance.data.StockItem
import com.joe.yhfinance.views.DetailsActivity

@Composable
fun ExchangeListItem(context: Context, item: StockItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
            .background(Color.White),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
                .clickable {
                           itemClick(context, item)
                },
        ) {

            var difference = item.currentValue.toFloat().minus(item.prevClose.toFloat())

            Row {

                Text(
                    text = item.exchangeName,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = item.currentValue,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {

                Text(
                    text = item.symbol,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "%.2f".format(difference),
                    Modifier.background(color = if (difference > 0) Color.Green else Color.Red)
                        .padding(2.dp)
                        .width(60.dp),
                    textAlign = TextAlign.End,
                    color = if (difference > 0) Color.Black else Color.White

                )
            }
        }
    }
}

fun itemClick(context: Context, item: StockItem) {

    val intent = Intent(context, DetailsActivity::class.java)
    intent.putExtra("symbol", item.symbol)
    context.startActivity(intent)
}