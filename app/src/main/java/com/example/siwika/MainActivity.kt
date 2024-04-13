package com.example.siwika

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.siwika.databinding.MainBinder
import com.example.siwika.ui.theme.SiWikaTheme
import com.google.android.material.navigation.NavigationBarView
import com.google.mediapipe.tasks.vision.core.RunningMode
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(),  NavigationBarView.OnItemSelectedListener {

    companion object {
        private val TAG: String = MainActivity::class.java.getSimpleName()
        public fun newIntent(context: Context): Intent = Intent(context.applicationContext, MainActivity::class.java)
    }

    private var binder : MainBinder? = null
    private val viewModel : MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { viewModel.keepSplashAlive }
        binder = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binder?.setLifecycleOwner(this@MainActivity)
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setSupportActionBar(binder?.toolbar)
        binder?.toolbar?.getMenu()?.clear()
        binder?.bottomNavigationView?.setOnItemSelectedListener(this@MainActivity)
        binder?.bottomNavigationView?.setSelectedItemId(-1)
    }

    override fun onNavigationItemSelected(menu : MenuItem): Boolean {
        return if (menu.getItemId() == R.id.camera) {
            Log.d(TAG, "onNavigationItemSelected home")
            viewModel.checkCameraPermission(
                permissionResultResultLauncher = requestPermissionLauncher,
                isGranted = {
                    binder?.toolbar?.setTitle(getString(R.string.camera))
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, CameraFragment()).commitNow()
                }, isDenied = {
                    Toast.makeText(this@MainActivity,"Please Approve Camera Permission!", Toast.LENGTH_LONG).show();
                }
            )
            true
        } else if (menu.getItemId() == R.id.home) {
            Log.d(TAG, "onNavigationItemSelected search")
            binder?.toolbar?.setTitle(getString(R.string.home))
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, HomeFragment())
                .commitNow()
            true
        } else if (menu.getItemId() == R.id.about) {
            Log.d(TAG, "onNavigationItemSelected edit")
            binder?.toolbar?.setTitle(getString(R.string.about))
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, AboutFragment())
                .commitNow()
            true
        } else {
            Log.d(TAG, "onNavigationItemSelected default")
            Toast.makeText(this, "Default", Toast.LENGTH_SHORT).show()
            false
        }
    }


    private val requestPermissionLauncher : ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d("PERMISSIONS", "Launcher result: " + isGranted.toString())
        if (isGranted) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, CameraFragment()).commitNow()
        } else {
            Toast.makeText(this@MainActivity,"Please Approve Camera Permission!", Toast.LENGTH_LONG);
        }
    }
}