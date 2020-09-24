package com.rocappdev.commercelist.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.rocappdev.commercelist.R

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

fun loadImage(image: ImageView, url: String) {
    Glide
        .with(image.context)
        .load(url)
        .error(ContextCompat.getDrawable(image.context, R.drawable.ic_commerce))
        .into(image)
}

fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

////////LOCATION////////////

const val PERMISSION_ID = 42

fun Activity.checkPermission(vararg perm: String): Boolean {
    val havePermissions = perm.toList().all {
        ContextCompat.checkSelfPermission(this, it) ==
                PackageManager.PERMISSION_GRANTED
    }
    if (!havePermissions) {
        if (perm.toList().any {
                ActivityCompat.shouldShowRequestPermissionRationale(this, it)
            }
        ) {
            val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.location_permission))
                .setMessage(getString(R.string.location_necessary))
                .setPositiveButton(getString(R.string.give_permission)) { _, _ ->
                    ActivityCompat.requestPermissions(
                        this, perm, PERMISSION_ID
                    )
                }
                .setNegativeButton(getString(android.R.string.cancel)) { _, _ -> }
                .create()
            dialog.show()
        } else {
            ActivityCompat.requestPermissions(this, perm, PERMISSION_ID)
        }
        return false
    }
    return true
}

fun Activity.isLocationEnabled(): Boolean {
    val locationManager: LocationManager =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}

