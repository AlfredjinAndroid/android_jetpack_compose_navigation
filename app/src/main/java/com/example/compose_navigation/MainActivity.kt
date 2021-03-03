package com.example.compose_navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.compose_navigation.ui.theme.ComposeNavigationTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        setContent {
            val controller = rememberNavController()
            ComposeNavigationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold {
                        NavHost(
                            navController = controller,
                            startDestination = "start",
                            builder = {
                                composable("start") {
                                    NavigationCompose(context, controller)
                                }
                                composable("second") {
                                    SecondPage(controller)
                                }
                                composable("third/{name}", arguments = listOf(navArgument("name") { type = NavType.StringType })) { entry ->
                                    val name = entry.arguments?.getString("name")
                                    ThirdPage(navController = controller, name = name)
                                }
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationCompose(context: Context, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MyButton(text = "startActivity 空参数", onClick = {
            context.startActivity(Intent(context, SecondActivity::class.java))
        })
        MyButton(text = "startActivity 带参数", onClick = {
            val intent = Intent(context, SecondActivity::class.java)
            intent.putExtra("name", "Alfred")
            context.startActivity(intent)
        })

        MyButton(text = "Navigation 空参数", onClick = {
            navController.navigate("second")
        })

        MyButton(text = "Navigation 带参数", onClick = {
            navController.navigate("third/Alfred")
        })

        MyButton(text = "使用ViewModel共享参数", onClick = {
            context.startActivity(Intent(context, NavigationActivity::class.java))
        })
    }
}

/**
 * 使用Navigation，不传递参数
 */
@Composable
fun SecondPage(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            MyButton(text = "Navigation 第二页", onClick = {
                navController.popBackStack()
            })
        }
    }
}

/**
 * 使用Navigation 传递参数
 */
@Composable
fun ThirdPage(navController: NavController, name: String?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            MyButton(text = "Navigation 第二页", onClick = {
                navController.popBackStack()
            })
            if (name != null) {
                Text(text = "传递参数 : $name")
            }
        }
    }
}

@Composable
fun MyButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.padding(10.dp)) {
        Text(text = text)
    }
}