package com.juliana.weatherapp.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juliana.weatherapp.presentation.favourites.MapScreen
import com.juliana.weatherapp.presentation.favourites.MapsViewModel
import com.juliana.weatherapp.presentation.ui.theme.WeatherAppTheme
import com.juliana.weatherapp.presentation.weather.HomeScreen
import com.juliana.weatherapp.presentation.weather.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private val mapsViewModel: MapsViewModel by viewModels()

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setupPermissionLauncher()

        setContent {
            WeatherAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val items = listOf(
                        NavigationItems(
                            title = "Home",
                            selectedIcon = Icons.Filled.Home,
                            unselectedIcon = Icons.Outlined.Home,
                            route = "home"
                        ),
                        NavigationItems(
                            title = "Favourites",
                            selectedIcon = Icons.Filled.Favorite,
                            unselectedIcon = Icons.Outlined.Favorite,
                            route = "favourites"
                        )
                    )

                    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            DrawerContent(items, selectedItemIndex) { index ->
                                selectedItemIndex = index
                                scope.launch { drawerState.close() }
                                navController.navigate(items[index].route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    ) {
                        NavigationGraph(viewModel, mapsViewModel, drawerState, navController)
                    }
                }

            }
        }
    }

    private fun setupPermissionLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                viewModel.loadWeather()
            }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}



@Composable
fun DrawerContent(
    items: List<NavigationItems>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        items.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = { Text(text = item.title) },
                selected = index == selectedItemIndex,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) {
                            item.selectedIcon
                        } else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                badge = {
                    item.badgeCount?.let {
                        Text(text = it.toString())
                    }
                },
                modifier = Modifier
                    .padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

@Composable
fun NavigationGraph(
    viewModel: WeatherViewModel,
    mapsViewModel: MapsViewModel,
    drawerState: DrawerState,
    navController: NavHostController,
) {

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = viewModel,
                mapsViewModel = mapsViewModel,
                drawerState = drawerState
            )
        }
        composable("favourites") {
            MapScreen(navController = navController, viewModel = mapsViewModel, drawerState= drawerState)
        }
    }
}


data class NavigationItems(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val route: String,

    )
