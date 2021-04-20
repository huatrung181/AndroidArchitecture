package com.example.lifecycleaware

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_LOCATION_PERMISSION_CODE = 1
    }

    lateinit var myLocationManager: MyLocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_CODE
            )
        } else {
            // TODO: Bắt đầu sử dụng GPS sau khi xin xong quyền
            setupLocationListener();
        }
    }

    private fun setupLocationListener() {
        myLocationManager = MyLocationManager(this) { location ->
            tvContent.text = location.latitude.toString() + ", " + location.longitude.toString()
        }

        lifecycle.addObserver(myLocationManager)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupLocationListener()
        } else {
            Toast.makeText(
                this,
                "Bạn chưa có quyền truy cập vào GPS của thiết bị",
                Toast.LENGTH_LONG
            ).show()
        }
    }
/*Dùng LifeCycle Aware : @annotation lắng nghe được đặt ở MyLocationManager, chủ động lắng nghe thực thi theo trạng thái
* ONSTART , ONRESUME, ONPAUSE nên ko cần cài đặt ở Activity hay Fragment:  lifecycle.addObserver(myLocationManager) */
/*    override fun onStart() {
        super.onStart()
        myLocationManager.start()
    }

    override fun onStop() {
        super.onStop()
        myLocationManager.stop()
    }*/
}