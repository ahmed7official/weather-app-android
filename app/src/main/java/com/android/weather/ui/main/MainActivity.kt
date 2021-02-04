package com.android.weather.ui.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.weather.R
import com.android.weather.ui.home.HomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

private const val TAG = "tag_s"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: ")

    }//onCreate

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume: ")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermissions()

        } else {

            resolveNavigation()

        }

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun checkLocationPermissions() {

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(multiplePermissionsListener)
            .check()

    }//checkLocationPermissions

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private val multiplePermissionsListener = object : MultiplePermissionsListener {

        override fun onPermissionsChecked(report: MultiplePermissionsReport) {

            if (report.areAllPermissionsGranted()) {

                resolveNavigation()

            } else {

                requestLocationPermissionsManually()

            }

        }//onPermissionsChecked

        override fun onPermissionRationaleShouldBeShown(
            requests: MutableList<PermissionRequest>,
            token: PermissionToken
        ) {

            requestLocationPermissionsManually()

        }//onPermissionRationaleShouldBeShown

    }//multiplePermissionsListener

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun resolveNavigation() {

        startActivity(Intent(this, HomeActivity::class.java))

        finishAffinity()

    }//resolveNavigation

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Show dialog to request the user to open app settings and grant location permissions manually
     */
    private fun requestLocationPermissionsManually() {

        val builder = MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle(R.string.location_per_req_title)
            .setMessage(R.string.req_loc_per_sett)
            .setPositiveButton(R.string.app_settings) { _, _ ->
                openAppSettings()
            }

        val dialog = builder.create()

        dialog.show()

    }//requestLocationPermissionsManually

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private fun openAppSettings() {

        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)

    }//openAppSettings()

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.w(TAG, "onActivityResult: ")
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

}//MainActivity