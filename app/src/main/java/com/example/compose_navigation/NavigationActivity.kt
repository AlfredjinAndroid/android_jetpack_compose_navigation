package com.example.compose_navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.popUpTo
import androidx.navigation.compose.rememberNavController
import com.example.compose_navigation.ui.theme.ComposeNavigationTheme

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val controller = rememberNavController()
            val viewModel: MyViewModel = viewModel()
            ComposeNavigationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold {
                        NavHost(
                            navController = controller,
                            startDestination = MyRouter.Main.Default.router,
                            builder = {
                                composable(MyRouter.Main.Default.router) {
                                    MainPage(controller, viewModel)
                                }
                                composable(MyRouter.Details.router) {
                                    DetailsPage(navController = controller, viewModel)
                                }
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun MainPage(mainController: NavController, viewModel: MyViewModel = viewModel()) {
    val indexController = rememberNavController()
    Column {

        Box(modifier = Modifier.weight(2.0f, true)) {
            NavHost(
                navController = indexController,
                startDestination = MyRouter.Main.Home.router,
                builder = {
                    composable(MyRouter.Main.Home.router) {
                        HomePage(mainController, viewModel)
                    }
                    composable(MyRouter.Main.Search.router) {
                        SearchPage()
                    }
                    composable(MyRouter.Main.Profile.router) {
                        ProfilePage()
                    }
                })
        }

        BottomStart { pos ->
            when (pos) {
                1 -> indexController.navigateTo(MyRouter.Main.Home) {
                    launchSingleTop = true
                }
                2 -> indexController.navigateTo(MyRouter.Main.Search) {
                    launchSingleTop = true
                    popUpTo(MyRouter.Main.Home.router) {}
                }
                3 -> indexController.navigateTo(MyRouter.Main.Profile) {
                    launchSingleTop = true
                    popUpTo(MyRouter.Main.Home.router) {}
                }
            }
        }
    }
}

@Composable
fun HomePage(mainController: NavController, viewModel: MyViewModel = viewModel()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "Home页面 ", textAlign = TextAlign.Center)
            MyButton(text = "跳转Details", onClick = {
                viewModel.name = "Alfred"
                mainController.navigateTo(MyRouter.Details)
            })
        }
    }
}

@Composable
fun SearchPage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "Search页面 ", textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun ProfilePage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "Profile页面 ", textAlign = TextAlign.Center)
        }
    }
}


@Composable
fun BottomStart(onClick: (index: Int) -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        Button(onClick = { onClick(1) }, modifier = Modifier.weight(1f)) {
            Text(text = "Home")
        }
        Button(onClick = { onClick(2) }, modifier = Modifier.weight(1f)) {
            Text(text = "Search")
        }
        Button(onClick = { onClick(3) }, modifier = Modifier.weight(1f)) {
            Text(text = "Profile")
        }
    }
}


@Composable
fun DetailsPage(navController: NavController, viewModel: MyViewModel = viewModel()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            MyButton(text = "Navigation 第二页", onClick = {
                navController.popBackStack()
            })
            if (viewModel.name.isNotEmpty()) {
                Text(text = viewModel.name)
            }
        }
    }
}

class MyViewModel : ViewModel() {
    var name by mutableStateOf("")
}

sealed class MyRouter(open val router: String) {
    sealed class Main(router: String) : MyRouter(router) {
        object Default : Main(router = "main")
        object Home : Main(router = "main/home")
        object Search : Main(router = "main/search")
        object Profile : Main(router = "main/profile")
    }

    object Details : MyRouter(router = "details")

}

fun NavController.navigateTo(router: MyRouter, builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(router.router, builder)
}