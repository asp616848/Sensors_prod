package io.sensor_prod.sensor.ui.navigation

import android.hardware.Sensor
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.sensor_prod.sensor.ui.pages.SplashPage
import io.sensor_prod.sensor.ui.pages.home.HomePage
import io.sensor_prod.sensor.ui.pages.sensor.details.SensorPage

sealed class NavDirectionsApp(val route: String) {
    object Root : NavDirectionsApp("root")
    object HomePage : NavDirectionsApp("home_page")
    object SensorDetailPage : NavDirectionsApp("sensor_detail_page")
    object AboutPage : NavDirectionsApp("about_page")
    object Splash : NavDirectionsApp("splash_page")
}

@Composable
fun NavGraphApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavDirectionsApp.Splash.route) {
//        startDestination "labs1"
//        val viewModelHome: HomeViewModel = HomeViewModel()
//        val viewModelSensor: SensorViewModel = SensorViewModel()
        composable(NavDirectionsApp.Splash.route) { SplashPage(navController) }
        composable(NavDirectionsApp.HomePage.route) {
            HomePage(
                navController = navController
            )
        }

        composable("${NavDirectionsApp.SensorDetailPage.route}/{type}", listOf(navArgument("type") {
            type = NavType.IntType
        })) {
            SensorPage(
                navController = navController,
                type = it.arguments?.getInt("type") ?: Sensor.TYPE_GYROSCOPE,
                // TODO might be creating bug

            )
        }
    }
}