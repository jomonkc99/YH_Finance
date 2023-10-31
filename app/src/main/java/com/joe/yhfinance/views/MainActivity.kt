package com.joe.yhfinance.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Settings
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.snackbar.Snackbar
import com.joe.yhfinance.constants.Constants
import com.joe.yhfinance.data.StockItem
import com.joe.yhfinance.ui.theme.YHFinance_JomonTheme
import com.joe.yhfinance.utilities.Utilities
import com.joe.yhfinance.viewmodel.StockViewModel
import com.joe.yhfinance.views.composables.ExchangeListItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val stockViewModel = StockViewModel()
        val handler = Handler()

        val context = this@MainActivity

        setContent {
            YHFinance_JomonTheme {

                MainUI(context = context, viewModel = stockViewModel)
            }
        }

        handler.postDelayed(object : Runnable {
            override fun run() {

                if (Utilities.isOnline(this@MainActivity)) {

                    stockViewModel.fetchItems(this@MainActivity)
                }
                else {

                    Toast.makeText(this@MainActivity, "No internet connection available..!!", Toast.LENGTH_LONG).show()

                }

                handler.postDelayed(this, Constants.fetch_interval * 1000)
            }
        }, 0)

    }
}

@Composable
fun ItemList(context: Context, items: List<StockItem>, searchQuery: String, viewModel: StockViewModel) {
    val filteredItems = remember { mutableStateListOf<StockItem>() }

    filteredItems.clear()

    for (item in items) {
        if (item.exchangeName.contains(searchQuery, ignoreCase = true)
            || item.symbol.contains(searchQuery, ignoreCase = true)) {
            filteredItems.add(item)
        }
    }

    LazyColumn {
        items(filteredItems) { item ->
            ExchangeListItem(context = context, item = item)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(modifier: Modifier = Modifier,
              searchQuery: String,
              onSearchQueryChange: (String) -> Unit) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery,
            onValueChange = { newQuery ->
                onSearchQueryChange(newQuery)
            },
            label = { Text(modifier = Modifier.padding(0.dp), text = "Search") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUI(context: Context, viewModel: StockViewModel) {

    var searchQuery by remember { mutableStateOf("") }

    val listItems by viewModel.selList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "YH Finance")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                ),
            )
        }, content = {

            Column(modifier = Modifier
                .padding(it)
                .fillMaxSize()) {

                SearchBar(modifier = Modifier.fillMaxWidth(), searchQuery) { newQuery ->
                    searchQuery = newQuery
                }
                ItemList(context, listItems, searchQuery, viewModel = viewModel)
            }

        })
}

