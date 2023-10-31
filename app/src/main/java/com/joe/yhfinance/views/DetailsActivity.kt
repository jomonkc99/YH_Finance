package com.joe.yhfinance.views

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.joe.yhfinance.R
import com.joe.yhfinance.utilities.Utilities
import com.joe.yhfinance.viewmodel.StockViewModel

class DetailsActivity : ComponentActivity() {

    private lateinit var viewModel: StockViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val symbol = intent.extras?.getString("symbol")

        findViewById<TextView>(R.id.txtHeading).text = symbol

        findViewById<RelativeLayout>(R.id.layoutLoading).visibility = View.VISIBLE

        viewModel = ViewModelProvider(this)[StockViewModel::class.java]

        viewModel.stockDetails.observe(this) {

            findViewById<TextView>(R.id.txtExchangeName).text = it.exchangeName ?: ""
            findViewById<TextView>(R.id.txtExchangeType).text = it.exchangeType ?: ""
            findViewById<TextView>(R.id.txtMarketPrice).text = it.regularMarketPrice ?: ""
            findViewById<TextView>(R.id.txtCurrency).text = it.currency ?: ""
            findViewById<TextView>(R.id.txtMarketChange).text = it.regularMarketChange ?: ""
            findViewById<TextView>(R.id.txtChangePercentage).text =
                "(${it.regularMarketChangePercent ?: ""})"
            findViewById<TextView>(R.id.txtPrevClose).text = it.regularMarketPreviousClose ?: ""
            findViewById<TextView>(R.id.txtOpenPrice).text = it.regularMarketOpen ?: ""
            findViewById<TextView>(R.id.txtVolumn).text = it.regularMarketVolume ?: ""
            findViewById<TextView>(R.id.txtAvgVolumn).text = it.averageDailyVolume3Month ?: ""
            findViewById<TextView>(R.id.txtDayHigh).text = it.regularMarketDayHigh ?: ""
            findViewById<TextView>(R.id.txtDayLow).text = it.regularMarketDayLow ?: ""

            val textColor: Int =
                if (it.regularMarketChange?.toFloat()!! > 0.0) resources.getColor(R.color.green) else resources.getColor(
                    R.color.red
                )
            findViewById<TextView>(R.id.txtMarketChange).setTextColor(textColor)
            findViewById<TextView>(R.id.txtChangePercentage).setTextColor(textColor)

            findViewById<RelativeLayout>(R.id.layoutLoading).visibility = View.GONE
        }

        if (Utilities.isOnline(this@DetailsActivity)) {

            viewModel.getStockDetails(symbol!!)
        }
        else {

            Toast.makeText(this@DetailsActivity, "No internet connection available..!!", Toast.LENGTH_LONG).show()

        }

        findViewById<ImageView>(R.id.imgBack).setOnClickListener {
            finish()
        }
    }
}