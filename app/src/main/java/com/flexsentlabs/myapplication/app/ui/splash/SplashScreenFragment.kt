package com.flexsentlabs.myapplication.app.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flexsentlabs.myapplication.app.ui.navigation.Navigator
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import com.flexsentlabs.myapplication.R
import com.flexsentlabs.myapplication.app.ui.theme.MyApplicationTheme

class SplashScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyApplicationTheme {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = getString(R.string.app_name)
                        )

                        // navigate when the box is rendered
                        LaunchedEffect(Unit) {
                            Navigator.from(this@SplashScreenFragment).decideStartDestination()
                        }
                    }

                }
            }
        }
    }
}


