package com.juliana.weatherapp.presentation.favourites

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import androidx.navigation.NavController
import com.juliana.weatherapp.presentation.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapsViewModel,
    navController: NavController,
) {
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }

    val context = LocalContext.current


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favourite Locations", fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(MapEvent.ToggleFalloutMap) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = if (viewModel.state.isFalloutMap) {
                        Icons.Default.ToggleOff
                    } else Icons.Default.ToggleOn,
                    contentDescription = "Toggle Fallout map",
                    tint = Color.White
                )
            }
        },
        content = { paddingValues ->
            BackHandler {
                navController.popBackStack()
            }
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                properties = viewModel.state.properties,
                uiSettings = uiSettings,
                onMapLongClick = { viewModel.onEvent(MapEvent.OnMapLongClick(it)) }
            ) {
                if (viewModel.state.favouriteLocations.isNotEmpty()) {
                    viewModel.state.favouriteLocations.forEach { spot ->
                        val markerState = rememberMarkerState(position = LatLng(spot.lat, spot.lng))

                        Marker(
                            state = markerState,
                            title = "Favourite Location (${spot.lat}, ${spot.lng})",
                            snippet = "Long click to delete",
                            onInfoWindowLongClick = {
                                viewModel.onEvent(MapEvent.OnInfoWindowLongClick(spot))
                            },
                            onClick = {
                                it.showInfoWindow()
                                true
                            },
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                        )
                    }

                } else {
                    Toast.makeText(context, "You have no favorite spots yet", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    )
}
