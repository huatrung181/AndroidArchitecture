package com.example.filterphoto.kotlin.ui

import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import butterknife.BindView
import com.example.filterphoto.MainActivity
import com.example.filterphoto.R
import com.example.filterphoto.kotlin.base.BaseActivity
import com.example.filterphoto.kotlin.utils.DeviceUtils
import com.example.filterphoto.kotlin.utils.PermissionUtils

class SplashActivity : BaseActivity() {

   // @BindView(R.id.tv_splash)
    lateinit var tvSplash : TextView

    override fun getLayoutId(): Int {
        return    R.layout.activity_splash
    }

    override fun createView() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvSplash = findViewById(R.id.tv_splash)
    }

    override fun onResume() {
        super.onResume()
        animate()
    }

    private fun animate() {
       val animate = AlphaAnimation(0f, 1f)
        animate.duration=2000
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                askForPermission()
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })

        tvSplash.startAnimation(animate)
    }

    private fun askForPermission() {
        askCompactPermissions(
            arrayOf(
                PermissionUtils.Manifest_CAMERA,
                PermissionUtils.Manifest_READ_EXTERNAL_STORAGE,
                PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE
        ), object : PermissionUtils.PermissionResult{
                override fun permissionGranted() {
                    showActivity(MainActivity::class.java)
                    finish()
                }

                override fun permissionDenied() {
                    showToast("Ban can cap quyen su dung ung dung")
                    finish()
                }

                override fun permissionForeverDenied() {
                    AlertDialog.Builder(this@SplashActivity).setTitle("Cung cap quyen")
                        .setMessage("Ban co muon cap quyen camera, doc file, ghi file?")
                        .setPositiveButton("Co"){dialog, which-> DeviceUtils.openSettingApp(this@SplashActivity)}
                        .setNegativeButton("Khong"){dialog, which ->  finish()}.show()
                }
            })
    }


}