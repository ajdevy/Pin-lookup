package com.example.myapplication.app.ui.util

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R

class Navigator private constructor(private val fragment: Fragment) {

    fun decideStartDestination() {
        val isLoggedIn = false // replace with real session check
        if (isLoggedIn) {
            goToPins(clearBackStack = true)
        } else {
            goToLogin(clearBackStack = true)
        }
    }

    fun goToLogin(clearBackStack: Boolean = false) {
        val navController = fragment.findNavController()
        val actionId = R.id.action_splash_to_login
        if (navController.currentDestination?.id == R.id.splashScreenFragment) {
            navController.navigate(actionId)
        }
    }

    fun goToPins(clearBackStack: Boolean = false) {
        val navController = fragment.findNavController()
        val current = navController.currentDestination?.id
        when (current) {
            R.id.splashScreenFragment -> navController.navigate(R.id.action_splash_to_pins)
            R.id.loginFragment -> navController.navigate(R.id.action_login_to_pins)
        }
    }

    companion object {
        fun from(fragment: Fragment): Navigator = Navigator(fragment)
    }
}


