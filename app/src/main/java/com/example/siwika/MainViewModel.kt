package com.example.siwika

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

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

    constructor(application : Application) : super(application) {
        keepSplashAlive = true
        cameraTab = NavigationBarModel(title = getCameraTitle(), selectedIcon = Icons.Filled.AccountBox, unselectedIcon = Icons.Outlined.AccountBox, badgeAmount = 13)
        homeTab = NavigationBarModel(title = getHomeTitle(), selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
        aboutTab = NavigationBarModel(title = getAboutTitle(), selectedIcon = Icons.Filled.List, unselectedIcon = Icons.Outlined.List)
        tabBarItems = listOf(cameraTab, homeTab, aboutTab)
        liveSelectedBottomBarTabIndex = MutableLiveData<Int>(0)
        liveTopBarTitle = MutableLiveData<String>(getApplication<Application>().getString(R.string.translator))
        keepSplashAlive = false
    }

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

    override fun onCleared() {
        super.onCleared()
    }
}