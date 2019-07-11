package com.example.presentation.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.presentation.R
import com.example.presentation.ui.settings.APP_MODE
import com.example.presentation.ui.settings.DARK
import com.example.presentation.ui.settings.LIGHT
import com.example.presentation.ui.settings.PACKAGE_NAME
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*


class BaseActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAppTheme()

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setupNavController()

        if (!isCoarseLocationPermissionGranted())
            requestFineLocationPermission()
    }

    private fun checkAppTheme() {
        val preferences = getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)
        val currentTheme = preferences.getString(APP_MODE, LIGHT)

        if (currentTheme == DARK)
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        else
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
    }

    private fun isCoarseLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestFineLocationPermission() {
        Dexter
            .withActivity(this)
            .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    Snackbar.make(root, getString(R.string.permission_granted), Snackbar.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Snackbar.make(root, getString(R.string.permission_denied), Snackbar.LENGTH_LONG).show()
                }
            }).check()
    }

    private fun setupNavController() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        bottom_nav.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}
