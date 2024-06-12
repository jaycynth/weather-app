package com.juliana.weatherapp.presentation.weather

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.juliana.weatherapp.presentation.favourites.MapsViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: WeatherViewModel,
    mapsViewModel: MapsViewModel,
) {
    val state = viewModel.state
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false)}
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(text = "Add Favourites", fontSize = 12.sp, modifier = Modifier.padding(10.dp))
            }
        },
        content = { paddingValues ->
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                   Column(modifier = Modifier
                       .padding(vertical = 60.dp,)
                       .align(Alignment.CenterHorizontally),
                       verticalArrangement = Arrangement.Center,
                       horizontalAlignment = Alignment.CenterHorizontally
                   ){
                       Text("Add your current location as favourite")
                       Spacer(modifier = Modifier.height(10.dp))
                       Button(onClick = {
                           scope.launch { sheetState.hide() }.invokeOnCompletion {
                               state.lat?.let { lat ->
                                   state.lon?.let { lon ->
                                       mapsViewModel.addFavouriteSpot(lat, lon)
                                   }
                               }
                               if (!sheetState.isVisible) {
                                   showBottomSheet = false
                               }
                               Toast.makeText(context,"Added Location", Toast.LENGTH_SHORT).show()
                           }
                       }) {
                           Text("Add")
                       }
                   }
                }
            }

            Box(Modifier.padding(paddingValues), contentAlignment = Alignment.Center) {
                WeatherCurrent(state = state, navController = navController)

                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }
        })

}







