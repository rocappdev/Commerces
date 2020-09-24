package com.rocappdev.commercelist.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.rocappdev.commercelist.CommerceApplication

class NetworkUtil(
    private val mConnectivityManager: ConnectivityManager =
        CommerceApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
) {

    fun checkNetwork(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            versionCodeMax28()
        } else {
            versionCodeMin23()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun versionCodeMin23(): Boolean {
        val activeNetwork =
            mConnectivityManager.getNetworkCapabilities(mConnectivityManager.activeNetwork)
        return when {
            activeNetwork == null -> false
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    @Suppress("DEPRECATION")
    private fun versionCodeMax28(): Boolean {
        val activeNetwork = mConnectivityManager.activeNetworkInfo
        return when {
            activeNetwork == null -> false
            activeNetwork.type == ConnectivityManager.TYPE_WIFI -> true
            activeNetwork.type == ConnectivityManager.TYPE_MOBILE -> true
            else -> false
        }
    }
}
