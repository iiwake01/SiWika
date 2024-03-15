package com.example.siwika

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.lifecycle.AndroidViewModel

class MainViewModel : AndroidViewModel {

    companion object {
        private val TAG = MainViewModel::class.java.getSimpleName()
    }

    private val cameraTab = NavigationBarModel(title = getCameraTitle(), selectedIcon = Icons.Filled.Face, unselectedIcon = Icons.Outlined.Face, badgeAmount = 13)
    private val homeTab = NavigationBarModel(title = getHomeTitle(), selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
    private val aboutTab = NavigationBarModel(title = getAboutTitle(), selectedIcon = Icons.Filled.List, unselectedIcon = Icons.Outlined.List)
    public val tabBarItems = listOf(cameraTab, homeTab, aboutTab)
    public var selectedTabIndex : Int = 0

    constructor(application : Application) : super(application) {

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

    public fun isSelectedIndex(index : Int) : Boolean {
        return selectedTabIndex == index;
    }

    override fun onCleared() {
        super.onCleared()
    }
}