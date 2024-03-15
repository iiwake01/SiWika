package com.example.siwika

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.siwika.ui.theme.SiWikaTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    companion object {
        private val TAG: String = MainActivity::class.java.getSimpleName()
        public fun newIntent(context: Context): Intent = Intent(context.applicationContext, MainActivity::class.java)
    }

    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController: NavHostController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()
            SiWikaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        content = { paddingValues: PaddingValues ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                NavHostComposable(navController = navController)
                            }
                        },
                        bottomBar = {
                            NavigationBar {
                                viewModel.tabBarItems.forEachIndexed { index, tabBarItem ->
                                    NavigationBarItem(
                                        selected = viewModel.isSelectedIndex(index),
                                        onClick = {
                                            coroutineScope.launch {
                                                viewModel.selectedTabIndex = index
                                                navController.navigate(tabBarItem.title)
                                            }
                                        },
                                        icon = {
                                            TabBarIconView(
                                                isSelected = viewModel.isSelectedIndex(index),
                                                selectedIcon = tabBarItem.selectedIcon,
                                                unselectedIcon = tabBarItem.unselectedIcon,
                                                title = tabBarItem.title,
                                                badgeAmount = tabBarItem.badgeAmount
                                            )
                                        },
                                        label = { Text(tabBarItem.title) }
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TabBarIconView (
        isSelected : Boolean, selectedIcon : ImageVector, unselectedIcon : ImageVector, title : String, badgeAmount : Int? = null
    ) {
        BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
            Icon(
                imageVector = if (isSelected) {
                    selectedIcon
                } else {
                    unselectedIcon
                },
                contentDescription = title
            )
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun TabBarBadgeView(count: Int? = null) {
        if (count != null) {
            Badge {
                Text(count.toString())
            }
        }
    }

    @Composable
    private fun NavHostComposable(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = stringResource(id = R.string.camera)
        ) {
            composable(route = viewModel.getCameraTitle()) { backStackEntry: NavBackStackEntry ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.camera),
                        textAlign = TextAlign.Center,
                    )
                }
            }
            composable(route = viewModel.getHomeTitle()) { backStackEntry: NavBackStackEntry ->
                val content = backStackEntry.arguments?.getString(Constants.KEY_CONTENT)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${stringResource(id = R.string.home)}",
                        textAlign = TextAlign.Center,
                    )
                }
            }
            composable(route = viewModel.getAboutTitle()) { backStackEntry: NavBackStackEntry ->
                val content = backStackEntry.arguments?.getString(Constants.KEY_CONTENT)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${stringResource(id = R.string.about)}",
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}