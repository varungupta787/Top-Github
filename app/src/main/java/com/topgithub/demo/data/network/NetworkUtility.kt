package com.topgithub.demo.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.topgithub.demo.TopGithubApplication


object NetworkUtility {

    val isNetworkAvailable: Boolean
        get() {
            val manager = sManager
            val wifiManager = wifiManager

            val netInfo = manager.activeNetworkInfo
            if (netInfo != null && netInfo.isConnected ||
                wifiManager != null && wifiManager.isWifiEnabled
            ) {
                return true
            }
            return false
        }

    val wifiManager by lazy {
        TopGithubApplication.getAppContext()
            .getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    val sManager by lazy {
        TopGithubApplication.getAppContext()
            .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}
