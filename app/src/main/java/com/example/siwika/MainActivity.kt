package com.example.siwika

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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

    private val viewModel : MainViewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { viewModel.keepSplashAlive }
        super.onCreate(savedInstanceState)
        setContent {
            val navController : NavHostController = rememberNavController()
            SiWikaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold (
                        topBar = {
                            TopBarComposable()
                        },
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
                            BottomBarComposable(navController)
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TopBarComposable() {
        val selectedTitle : String by viewModel.observeTitle().observeAsState("")
        TopAppBar (
            title = { Text(text = selectedTitle) }
        )
    }

    @Composable
    private fun NavHostComposable(navController : NavHostController) {
        NavHost (
            navController = navController,
            startDestination = stringResource(id = R.string.camera)
        ) {
            composable(route = viewModel.getCameraTitle()) { backStackEntry : NavBackStackEntry ->
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
            composable(route = viewModel.getHomeTitle()) { backStackEntry : NavBackStackEntry ->
                Column (
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(13.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box (
                        modifier = Modifier.fillMaxWidth()
                            .aspectRatio(1f/0.25f)
                            .padding(horizontal = 5.dp)
                            .border(2.dp, Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Text (
                            text = stringResource(id = R.string.abc),
                        )
                    }
                    Box (
                        modifier = Modifier.fillMaxWidth()
                            .aspectRatio(1f/0.25f)
                            .padding(horizontal = 5.dp)
                            .border(2.dp, Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(13.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_waving_hand),
                                contentDescription = null,
                            )
                            Text (
                                text = stringResource(id = R.string.greetings),
                            )
                        }
                    }
                    Box (
                        modifier = Modifier.fillMaxWidth()
                            .aspectRatio(1f/0.25f)
                            .padding(horizontal = 5.dp)
                            .border(2.dp, Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(13.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_emoji_emotions),
                                contentDescription = null,
                            )
                            Text (
                                text = stringResource(id = R.string.emoticons),
                            )
                        }
                    }
                    Box (
                        modifier = Modifier.fillMaxWidth()
                            .aspectRatio(1f/0.25f)
                            .padding(horizontal = 5.dp)
                            .border(2.dp, Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Row() {
                            Image(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = null,
                            )
                            Text (
                                text = stringResource(id = R.string.about_the_deaf_community),
                            )
                        }
                    }
                }
            }
            composable(route = viewModel.getAboutTitle()) { backStackEntry : NavBackStackEntry ->
                Box (
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box (
                        modifier = Modifier.fillMaxWidth()
                            .aspectRatio(1f/0.25f)
                            .padding(horizontal = 5.dp)
                            .border(2.dp, Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Text (
                            text = stringResource(id = R.string.about_the_app),
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun BottomBarComposable(navController : NavHostController) {
        val coroutineScope = rememberCoroutineScope()
        val selectedTabIndex : Int by viewModel.observeSelectedBottomBarIndex().observeAsState(0)
        NavigationBar {
            viewModel.tabBarItems.forEachIndexed { index, tabBarItem ->
                NavigationBarItem (
                    selected = selectedTabIndex == index,
                    onClick = {
                        coroutineScope.launch {
                            viewModel.setSelectedIndex(index)
                            navController.navigate(tabBarItem.title)
                        }
                    },
                    icon = {
                        TabBarIconView(
                            isSelected = selectedTabIndex == index,
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TabBarIconView (
        isSelected : Boolean, selectedIcon : ImageVector, unselectedIcon : ImageVector, title : String, badgeAmount : Int? = null
    ) {
        BadgedBox(badge = { TabBarBadgeView(badgeAmount) }) {
            Icon (
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
    private fun TabBarBadgeView(count : Int? = null) {
        if (count != null) {
            Badge {
                Text(count.toString())
            }
        }
    }
}