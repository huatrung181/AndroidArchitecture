package com.example.filterphoto

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.BindViews
import com.example.filterphoto.kotlin.base.BaseActivity
import com.example.filterphoto.kotlin.utils.DeviceUtils
import com.makeramen.roundedimageview.RoundedImageView
import org.wysaid.camera.CameraInstance
import org.wysaid.nativePort.CGENativeLibrary
import org.wysaid.view.CameraRecordGLSurfaceView
import java.io.IOException
import java.io.InputStream


class MainActivity : BaseActivity() {

    @BindView(R.id.rl_camera_area)
    lateinit var rlCameraView: RelativeLayout

    @BindView(R.id.c_view)
    lateinit var cameraView: CameraRecordGLSurfaceView

    @BindView(R.id.bt_flash_mode)
    lateinit var btFlashMode: ImageView

    @BindView(R.id.rv_filter)
    lateinit var rvFilter: RecyclerView

    @BindView(R.id.iv_pick_image)
    lateinit var ivPickImage: RoundedImageView


    override fun createView() {
        setupCameraView()
    }

    override fun onResume() {
        super.onResume()
        cameraView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        CameraInstance.getInstance().stopCamera()
        cameraView?.release(null)
        cameraView?.onPause()
    }
    private fun setupCameraView() {
        var screenWidth= DeviceUtils.getScreenWidth(this)
        var screenHeight= DeviceUtils.getScreenHeight(this)

        rlCameraView?.layoutParams?.height= screenHeight
        cameraView?.layoutParams?.height=screenHeight
        cameraView?.presetCameraForward(true)
        cameraView?.presetRecordingSize(screenWidth, screenHeight)
        cameraView?.setPictureSize(screenHeight,screenHeight,true)

        cameraView?.setZOrderOnTop(false)
        cameraView?.setZOrderMediaOverlay(true)
        btFlashMode?.setOnClickListener{
            var flashIndex = 0
            var  flashModes: Array<String> = arrayOf(Camera.Parameters.FLASH_MODE_TORCH, Camera.Parameters.FLASH_MODE_AUTO)

            cameraView?.setFlashLightMode(flashModes[flashIndex])
            ++flashIndex
            flashIndex%=flashModes.size
            if (flashIndex==0){
                btFlashMode!!.setImageResource(R.drawable.ic_turn_on_flash)
            }else{
                btFlashMode!!.setImageResource(R.drawable.ic_turn_off_flash)
            }
        }

        CGENativeLibrary.setLoadImageCallback(loader,null)

    }
    var loader: CGENativeLibrary.LoadImageCallback = object: CGENativeLibrary.LoadImageCallback{
        override fun loadImage(name: String?, arg: Any?): Bitmap? {
            var asetMager : AssetManager= assets
            var inputStream : InputStream
            try {
                inputStream = name?.let { asetMager.open(it) }!!
            }catch (e: IOException){
                return null
            }
            return BitmapFactory.decodeStream(inputStream)
        }

        override fun loadImageOK(bmp: Bitmap?, arg: Any?) {
            bmp?.recycle()
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


}