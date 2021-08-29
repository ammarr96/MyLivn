package com.amar.mylivn.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.internal.and
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Helper {

    companion object {

        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
            return result
        }

        fun md5Java(message: String): String? {
            var digest: String? = null
            try {
                val md: MessageDigest = MessageDigest.getInstance("MD5")
                val hash: ByteArray = md.digest(message.toByteArray(charset("UTF-8")))
                //converting byte array to Hexadecimal String
                val sb = StringBuilder(2 * hash.size)
                for (b in hash) {
                    sb.append(String.format("%02x", b and 0xff))
                }
                digest = sb.toString()
            } catch (ex: UnsupportedEncodingException) {
            } catch (ex: NoSuchAlgorithmException) {
            }
            return digest
        }
    }
}