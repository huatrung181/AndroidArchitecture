package com.example.filterphoto.kotlin.utils

import android.Manifest


object  PermissionUtils{
    const val Manifest_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    const val Manifest_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val Manifest_CAMERA = Manifest.permission.CAMERA


    interface PermissionResult{
       fun permissionGranted()
        fun permissionDenied()
        fun permissionForeverDenied()
    }
}