package com.example.filterphoto.java

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.*

class base : AppCompatActivity() {
    private val KEY_PERMISSION = 200
    private var permissionResult: PermissionUtils.PermissionResult? =
        null
    private lateinit var permissionsAsk: Array<String>
    fun askCompactPermissions(
        permissions: Array<String>,
        permissionResult: PermissionUtils.PermissionResult?
    ) {
        permissionsAsk = permissions
        this.permissionResult = permissionResult
        internalRequestPermission(permissionsAsk)
    }

    private fun internalRequestPermission(permissionAsk: Array<String>) {
        var arrayPermissionNotGranted: Array<String?>
        val permissionsNotGranted =
            ArrayList<String>()
        for (i in permissionAsk.indices) {
            /*if (!isPermissionGranted(permissionAsk[i])) {
                permissionsNotGranted.add(permissionAsk[i]);
            }*/
        }
        if (permissionsNotGranted.isEmpty()) {
            if (permissionResult != null) permissionResult!!.permissionGranted()
        } else {
            arrayPermissionNotGranted = arrayOfNulls(permissionsNotGranted.size)
            arrayPermissionNotGranted =
                permissionsNotGranted.toArray(arrayPermissionNotGranted)
            ActivityCompat.requestPermissions(this, arrayPermissionNotGranted, KEY_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != KEY_PERMISSION) {
            return
        }
        val permissionDienid: MutableList<String> =
            LinkedList()
        var granted = true
        for (i in grantResults.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                granted = false
                permissionDienid.add(permissions[i])
            }
        }
        if (permissionResult != null) {
            if (granted) {
                permissionResult!!.permissionGranted()
            } else {
                for (s in permissionDienid) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, s)) {
                        permissionResult!!.permissionForeverDienid()
                        return
                    }
                }
                permissionResult!!.permissionDenied()
            }
        }
    }
}