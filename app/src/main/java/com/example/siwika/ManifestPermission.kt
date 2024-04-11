package com.example.siwika

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

public object ManifestPermission {

    private val TAG = ManifestPermission::class.java.getSimpleName()

    val cameraPermission = Manifest.permission.CAMERA

    fun checkSelfPermission(context : Context, permission : String, isGranted : () -> Unit = {}, isDenied : () -> Unit = {}) : Boolean {
        Log.d(TAG,"checkSelfPermission($context,$permission, isGranted(), isDenied())")
        return if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"isGranted()")
            isGranted()
            true
        } else {
            Log.d(TAG,"denied()")
            isDenied()
            false
        }
    }

    fun requestPermission(permissionResultResultLauncher : ActivityResultLauncher<String>, permission : String) {
        Log.d(TAG,"requestPermission($permissionResultResultLauncher,$permission")
        permissionResultResultLauncher.launch(permission)
    }
}