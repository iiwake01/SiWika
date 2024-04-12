package com.example.siwika

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class HomeFragment : Fragment {

    companion object {
        private val TAG = HomeFragment::class.java.getSimpleName()
    }

    constructor() {

    }

    protected val viewModel : MainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        Log.d(TAG, "onCreateView()")
        return ComposeView(requireContext()).apply {
            setContent(content = {
                Column (
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(13.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box (
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f / 0.25f).padding(horizontal = 5.dp).border(2.dp, Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Image (
                            painterResource(R.drawable.alphabet),
                            contentDescription = null,
                        )
                    }
                    Box (
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f / 0.25f).padding(horizontal = 5.dp).border(2.dp, Color.Black),
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
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f / 0.25f).padding(horizontal = 5.dp).border(2.dp, Color.Black),
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
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f / 0.25f).padding(horizontal = 5.dp).border(2.dp, Color.Black),
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
            })
        } ?: super.onCreateView(inflater, container, savedInstanceState)
    }
}