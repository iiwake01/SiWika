package com.example.siwika

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.siwika.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.util.Arrays

class MainViewModel : AndroidViewModel {

    companion object {
        private val TAG = MainViewModel::class.java.getSimpleName()
    }

    public var keepSplashAlive  : Boolean
    private val cameraTab : NavigationBarModel
    private val homeTab : NavigationBarModel
    private val aboutTab : NavigationBarModel
    public val tabBarItems : List<NavigationBarModel>
    private var liveSelectedBottomBarTabIndex : MutableLiveData<Int>
    private var liveTopBarTitle : MutableLiveData<String>
    private var liveCameraGranted : MutableLiveData<Boolean>
    private var bitmap : Bitmap? = null
    private val result : MutableLiveData<String>

    constructor(application : Application) : super(application) {
        keepSplashAlive = true
        cameraTab = NavigationBarModel(title = getCameraTitle(), icon = R.drawable.camera)
        homeTab = NavigationBarModel(title = getHomeTitle(), icon = R.drawable.home)
        aboutTab = NavigationBarModel(title = getAboutTitle(), icon = R.drawable.about)
        tabBarItems = listOf(cameraTab, homeTab, aboutTab)
        liveSelectedBottomBarTabIndex = MutableLiveData<Int>(0)
        liveTopBarTitle = MutableLiveData<String>(getApplication<Application>().getString(R.string.translator))
        keepSplashAlive = false
        liveCameraGranted = MutableLiveData<Boolean>()
        result = MutableLiveData<String>()
    }
    //region TopAppBar Title
    public fun getCameraTitle() : String {
        return getApplication<Application>().getString(R.string.camera)
    }

    public fun getHomeTitle() : String {
        return getApplication<Application>().getString(R.string.home)
    }

    public fun getAboutTitle() : String {
        return getApplication<Application>().getString(R.string.about)
    }
    
    public fun observeTitle() : LiveData<String> {
        return liveTopBarTitle
    }
    //endregion
    //region NavigationBar Index
    public fun setSelectedIndex(index : Int) {
        liveSelectedBottomBarTabIndex.setValue(index)
        if (index == 0) {
            liveTopBarTitle.setValue(getApplication<Application>().getString(R.string.translator))
        } else if (index == 1) {
            liveTopBarTitle.setValue(getApplication<Application>().getString(R.string.your_lesson))
        } else if (index == 2) {
            liveTopBarTitle.setValue(getApplication<Application>().getString(R.string.setting))
        }
    }

    public fun observeSelectedBottomBarIndex() : LiveData<Int> {
        return liveSelectedBottomBarTabIndex
    }
    //endregion
    public fun checkCameraPermission(permissionResultResultLauncher: ActivityResultLauncher<String>) {
        ManifestPermission.checkSelfPermission (
            getApplication<Application>(),
            ManifestPermission.cameraPermission,
            isGranted = {
                grantedCameraPermission()
            },
            isDenied = {
                deniedCameraPermission()
                ManifestPermission.requestPermission(
                    permissionResultResultLauncher,
                    ManifestPermission.cameraPermission
                )
            }
        )
    }

    public fun grantedCameraPermission() {
        liveCameraGranted.setValue(true)
    }

    public fun deniedCameraPermission() {
        liveCameraGranted.setValue(false)
    }

    public fun observeCameraPermission() : LiveData<Boolean> {
        return liveCameraGranted
    }

    private fun predict() {
        Log.d(TAG,"predict()")
        try { Log.d(TAG,"try")
            bitmap = Bitmap.createScaledBitmap(bitmap!!, 224, 224, true)
            val model : Model = Model.newInstance(getApplication<Application>())
            // Creates inputs for reference.
            val inputFeature0 : TensorBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            val tensorImage : TensorImage = TensorImage(DataType.UINT8)
            tensorImage.load(bitmap)
            val byteBuffer : ByteBuffer = tensorImage.getBuffer()

            inputFeature0.loadBuffer(byteBuffer);
            // Runs model inference and gets result.
            val outputs : Model.Outputs = model.process(inputFeature0)
            val outputFeature0 : TensorBuffer = outputs.getOutputFeature0AsTensorBuffer()
            // Releases model ml if no longer used.
            model.close()
            getMax(outputFeature0.getFloatArray());
            Log.d("Result", Arrays.toString(outputFeature0.getFloatArray()))
        } catch (exception : IOException) {
            Log.e(TAG,"IOException " + exception.message);
        }
    }

    private fun getMax(outputs : FloatArray) {
        Log.d(TAG, "getMax( " + Arrays.toString(outputs) + ")")
        if ((outputs.size != 0) and (outputs[0] > outputs[1]) and (outputs[0] > outputs[2]) and (outputs[0] > outputs[3])) {
            result.setValue("Mahal kita")
        } else if ((outputs.size != 0) and (outputs[1] > outputs[0]) and (outputs[1] > outputs[2]) and (outputs[1] > outputs[3])) {
            result.setValue("Thank you")
        } else if ((outputs.size != 0) and (outputs[2] > outputs[0]) and (outputs[2] > outputs[1]) and (outputs[2] > outputs[3])) {
            result.setValue("Peace")
        } else {
            result.setValue("")
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun getOutputFileOptions(suffix : String?) : ImageCapture.OutputFileOptions {
        return ImageCapture.OutputFileOptions.Builder (
            getCacheFile(suffix ?: Constants.IMAGE_EXTENSION)
        ).build()
    }

    private fun isExternalStorageWritable() : Boolean {
        val state : String = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    private fun getCacheFile(suffix : String) : File {
        val cacheDir : File =
            if (isExternalStorageWritable().not()) getApplication<Application>().getCacheDir()
            else getApplication<Application>().getExternalCacheDir()!!

        val filePathFolder : File = File(cacheDir,getApplication<Application>().getString(R.string.camerax))
        if (!filePathFolder.exists()) filePathFolder.mkdirs()

        val fileName : String = "${System.currentTimeMillis()}${Constants.IMAGE_SUFFIX}"

        val fileValue : File = File.createTempFile(fileName, suffix, filePathFolder)

        return fileValue
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun convertToBitmap(output : ImageCapture.OutputFileResults) {
        Log.d(TAG,"logImageSaved ${output.getSavedUri()}")
        bitmap = BitmapFactory.decodeFile(output.getSavedUri()?.path)
    }

    override fun onCleared() {
        super.onCleared()
    }
}