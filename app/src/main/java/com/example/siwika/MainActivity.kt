package com.example.siwika

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.siwika.ui.theme.SiWikaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

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
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                        .border(2.dp, Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    viewModel.checkCameraPermission(requestPermissionLauncher )
                    val isGranted : Boolean by viewModel.observeCameraPermission().observeAsState(false)
                    if (isGranted) {
                        CameraComposable()
                    } else {
                        Text(
                            text = stringResource(id = R.string.camera_not_granted),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            composable(route = viewModel.getHomeTitle()) { backStackEntry : NavBackStackEntry ->
                Column (
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(13.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f / 0.25f)
                            .padding(horizontal = 5.dp)
                            .border(2.dp, Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Image (
                            painterResource(R.drawable.alphabet),
                            contentDescription = null,
                        )
                    }
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f / 0.25f)
                            .padding(horizontal = 5.dp)
                            .border(2.dp, Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image (
                                painterResource(R.drawable.greetings),
                                contentDescription = null,
                            )
                            Text (
                                text = stringResource(id = R.string.greetings),
                            )
                        }
                    }
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f / 0.25f)
                            .padding(horizontal = 5.dp)
                            .border(2.dp, Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image (
                                painter = painterResource(id = R.drawable.emotions),
                                contentDescription = null,
                            )
                            Text (
                                text = stringResource(id = R.string.emotions),
                            )
                        }
                    }
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f / 0.25f)
                            .padding(horizontal = 5.dp)
                            .border(2.dp, Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image (
                                painter = painterResource(id = R.drawable.about),
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f / 0.25f)
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
                        Image(painter = painterResource(id = tabBarItem.icon), contentDescription = viewModel.getCameraTitle())
                    },
                )
            }
        }
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    @Composable
    private fun CameraComposable() {
        /*
        AndroidViewBinding(ComposeFragmentCameraBinding::inflate) {
            val fragment : CameraFragment = fragmentContainerView.getFragment<CameraFragment>()
        }
        */
        val context : Context = LocalContext.current
        val previewView : PreviewView = remember { PreviewView(context) }
        val overlayView : OverlayView = remember { OverlayView(context, null) }
        val cameraController : LifecycleCameraController = remember { LifecycleCameraController(context) }
        val lifecycleOwner : LifecycleOwner = LocalLifecycleOwner.current
        /*
        val imageAnalyzer : ImageAnalysis = ImageAnalysis.Builder()
          .setTargetAspectRatio(AspectRatio.RATIO_4_3)
          .setTargetRotation(previewView.display.rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
        */
        val executor : ExecutorService = remember { Executors.newSingleThreadExecutor() }
        //val result : String by viewModel.observeResult().observeAsState("")
        cameraController.bindToLifecycle(lifecycleOwner)
        cameraController.setCameraSelector(CameraSelector.DEFAULT_FRONT_CAMERA)
        cameraController.setImageAnalysisAnalyzer(executor) { imageProxy ->
            viewModel.gestureRecognizerHelper?.recognizeLiveStream(
                imageProxy = imageProxy,
            )
        }
        cameraController.setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
        previewView.setController(cameraController)
        ConstraintLayout {
            val (preview, overlay, text, button,) = createRefs()
            Box(
                modifier = Modifier.constrainAs(preview) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                content = {
                    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
                }
            )
            Box(
                modifier = Modifier.constrainAs(overlay) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                content = {
                    AndroidView(factory = { overlayView }, modifier = Modifier.fillMaxSize())
                }
            )
            /*
            Text(modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },text = result)
            */
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.gestureRecognizerHelper?.isClosed() == true) {
            viewModel.gestureRecognizerHelper?.setupGestureRecognizer()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.setMinHandDetectionConfidence(viewModel.gestureRecognizerHelper?.minHandDetectionConfidence)
        viewModel.setMinHandTrackingConfidence(viewModel.gestureRecognizerHelper?.minHandTrackingConfidence)
        viewModel.setMinHandPresenceConfidence(viewModel.gestureRecognizerHelper?.minHandPresenceConfidence)
        viewModel.setDelegate(viewModel.gestureRecognizerHelper?.currentDelegate)
        // Close the Gesture Recognizer helper and release resources
        //backgroundExecutor.execute { viewModel.gestureRecognizerHelper.clearGestureRecognizer() }
        viewModel.gestureRecognizerHelper?.clearGestureRecognizer()
    }

    private val requestPermissionLauncher : ActivityResultLauncher<String> = registerForActivityResult( ActivityResultContracts.RequestPermission(),) { isGranted ->
        Log.d("PERMISSIONS", "Launcher result: " + isGranted.toString())
        if (isGranted) {
            viewModel.grantedCameraPermission()
        } else {
            viewModel.deniedCameraPermission()
        }
    }
}