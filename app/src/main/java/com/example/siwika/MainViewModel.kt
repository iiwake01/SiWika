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

    private val cameraTab = NavigationBarModel(title = getCameraTitle(), selectedIcon = Icons.Filled.AccountBox, unselectedIcon = Icons.Outlined.AccountBox, badgeAmount = 13)
    private val homeTab = NavigationBarModel(title = getHomeTitle(), selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
    private val aboutTab = NavigationBarModel(title = getAboutTitle(), selectedIcon = Icons.Filled.List, unselectedIcon = Icons.Outlined.List)
    public val tabBarItems = listOf(cameraTab, homeTab, aboutTab)
    private var selectedTabIndex : MutableLiveData<Int> = MutableLiveData<Int>()

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

    public fun setSelectedIndex(index : Int) {
        selectedTabIndex.setValue(index)
    }

    public fun observeSelectedIndex() : LiveData<Int> {
        return selectedTabIndex
    }

    override fun onCleared() {
        super.onCleared()
    }
}