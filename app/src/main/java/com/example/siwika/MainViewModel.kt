package com.example.siwika

import android.app.Application
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class MainViewModel(application: Application) : AndroidViewModel(application) {

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

    init {
        keepSplashAlive = true
        cameraTab = NavigationBarModel(title = getCameraTitle(), icon = R.drawable.camera)
        homeTab = NavigationBarModel(title = getHomeTitle(),icon = R.drawable.home,)
        aboutTab = NavigationBarModel(title = getAboutTitle(),icon = R.drawable.about,)

        tabBarItems = listOf(cameraTab, homeTab, aboutTab)
        liveSelectedBottomBarTabIndex = MutableLiveData<Int>(0)
        liveTopBarTitle = MutableLiveData<String>(getApplication<Application>().getString(R.string.translator))
        keepSplashAlive = false
        liveCameraGranted = MutableLiveData<Boolean>()
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
    public fun checkCameraPermission(permissionResultResultLauncher : ActivityResultLauncher<String>,) {
        ManifestPermission.checkSelfPermission (
            getApplication<Application>(),
            ManifestPermission.cameraPermission,
            isGranted = {
                grantedCameraPermission()
            },
            isDenied = {
                deniedCameraPermission()
                ManifestPermission.requestPermission(permissionResultResultLauncher, ManifestPermission.cameraPermission,)
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

    override fun onCleared() {
        super.onCleared()
    }
}
