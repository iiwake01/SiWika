package com.example.siwika

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mediapipe.tasks.vision.core.RunningMode

class MainViewModel : AndroidViewModel, GestureRecognizerHelper.GestureRecognizerListener {

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
        gestureRecognizerHelper = GestureRecognizerHelper(
            context = application,
            runningMode = RunningMode.LIVE_STREAM,
            minHandDetectionConfidence = currentMinHandDetectionConfidence,
            minHandTrackingConfidence = currentMinHandTrackingConfidence,
            minHandPresenceConfidence = currentMinHandPresenceConfidence,
            currentDelegate = currentDelegate,
            gestureRecognizerListener = this
        )
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
    //region Camera Permission Methods
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
    //endregion
    //region MP Gesture Recognizer
    public val gestureRecognizerHelper : GestureRecognizerHelper?
    private var _delegate: Int = GestureRecognizerHelper.DELEGATE_CPU
    private var _minHandDetectionConfidence: Float = GestureRecognizerHelper.DEFAULT_HAND_DETECTION_CONFIDENCE
    private var _minHandTrackingConfidence: Float = GestureRecognizerHelper.DEFAULT_HAND_TRACKING_CONFIDENCE
    private var _minHandPresenceConfidence: Float = GestureRecognizerHelper.DEFAULT_HAND_PRESENCE_CONFIDENCE
    val currentDelegate: Int get() = _delegate
    val currentMinHandDetectionConfidence: Float get() = _minHandDetectionConfidence
    val currentMinHandTrackingConfidence: Float get() = _minHandTrackingConfidence
    val currentMinHandPresenceConfidence: Float get() = _minHandPresenceConfidence
    fun setDelegate(delegate: Int?) {
        if (delegate != null) {
            _delegate = delegate
        }
    }
    fun setMinHandDetectionConfidence(confidence: Float?) {
        confidence?.let { _minHandDetectionConfidence = it }
    }
    fun setMinHandTrackingConfidence(confidence: Float?) {
        if (confidence != null) {
            _minHandTrackingConfidence = confidence
        }
    }
    fun setMinHandPresenceConfidence(confidence: Float?) {
        if (confidence != null) {
            _minHandPresenceConfidence = confidence
        }
    }

    override fun onResults(resultBundle : GestureRecognizerHelper.ResultBundle) {
        Log.d(TAG, "onResults ${resultBundle.results}")
    }

    override fun onError(error : String, errorCode : Int) {
        Log.d(TAG, "onError $error $errorCode")
    }
    //endreiogn
    override fun onCleared() {
        super.onCleared()
    }
}