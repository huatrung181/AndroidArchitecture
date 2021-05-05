package com.example.filterphoto

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.example.filterphoto.kotlin.CameraResultActivity
import com.example.filterphoto.kotlin.FilterData
import com.example.filterphoto.kotlin.ListFilterAdapter
import com.example.filterphoto.kotlin.base.BaseActivity
import com.example.filterphoto.kotlin.utils.DeviceUtils
import com.makeramen.roundedimageview.RoundedImageView
import org.wysaid.camera.CameraInstance
import org.wysaid.myUtils.ImageUtil
import org.wysaid.nativePort.CGENativeLibrary
import org.wysaid.view.CameraRecordGLSurfaceView
import java.io.File
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

     var EFFECT_CONFIGS = arrayOf("@adjust lut original.png",
            "@adjust lut natural01.png",
            "@adjust lut natural02.png",
            "@adjust lut pure01.png",
            "@adjust lut pure02.png",
            "@adjust lut lovely01.png",
            "@adjust lut lovely02.png",
            "@adjust lut lovely03.png",
            "@adjust lut lovely04.png",
            "@adjust lut warm01.png",
            "@adjust lut warm02.png",
            "@adjust lut cool01.png",
            "@adjust lut cool02.png",
            "@adjust lut vintage.png",
            "@adjust lut gray.png")


    override fun createView() {
        setupCameraView()
        setUpListFilterEffect()
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

    var selectedFilterData = FilterData("None", EFFECT_CONFIGS[0],0)
    fun setUpListFilterEffect(){
        rvFilter.layoutManager= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        var listFilter= ArrayList<FilterData>()

        var imageFilterId = arrayOf(R.drawable.original_1, R.drawable.natural_1, R.drawable.natural_2
                , R.drawable.pure_1, R.drawable.pure_2, R.drawable.pinky_1
                , R.drawable.pinky_2, R.drawable.pinky_3, R.drawable.pinky_4
                , R.drawable.warm_1, R.drawable.warm_2, R.drawable.cool_1
                , R.drawable.cool_2, R.drawable.mood, R.drawable.bw)
        for (i in EFFECT_CONFIGS.indices) {
            when(i){
                0-> listFilter.add(FilterData("Original", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                1-> listFilter.add(FilterData("Natural 1", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                2-> listFilter.add(FilterData("Natural 2", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                3-> listFilter.add(FilterData("Pure 1", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                4-> listFilter.add(FilterData("Pure 2", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                5-> listFilter.add(FilterData("Pinky 1", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                6-> listFilter.add(FilterData("Pinky 2", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                7-> listFilter.add(FilterData("Pinky 3", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                8-> listFilter.add(FilterData("Pinky 4", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                9-> listFilter.add(FilterData("Warm 1", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                10-> listFilter.add(FilterData("Warm 2", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                11-> listFilter.add(FilterData("Cool 1", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                12-> listFilter.add(FilterData("Cool 2", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                13-> listFilter.add(FilterData("Mood", EFFECT_CONFIGS.get(i), imageFilterId[i]))
                14-> listFilter.add(FilterData("B&W", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            }
        }
        var filterAdapter = ListFilterAdapter(listFilter)


        filterAdapter.onFilterSelect= object :ListFilterAdapter.OnFilterSelect{
            override fun onSelect(filterData: FilterData?) {
                selectedFilterData = filterData!!
                cameraView.setFilterWithConfig(filterData.rule)
            }
        }

        rvFilter.adapter= filterAdapter
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
    @OnClick(R.id.bt_close)
    fun closeCamera(){
        finish()
    }

    @OnClick(R.id.iv_pick_image)
    fun pickImage(){
        var bundle = Bundle()
        showActivityWithBundle(CameraResultActivity::class.java, bundle)

    }

    @OnClick(R.id.bt_take_picture)
    open fun onTakePictureClick(): Unit {
        showToast("Đang chụp ảnh...")
        cameraView.takeShot { bmp ->
            val file = File(Environment.getExternalStorageDirectory().toString() + "/FilterImageDemo")
            if (!file.exists()) {
                file.mkdirs()
            }
            if (bmp != null) {
                val imagePath: String = ImageUtil.saveBitmap(bmp, file.getAbsolutePath().toString() + "/" + System.currentTimeMillis() + ".jpg")
                bmp.recycle()
                //showToast("Đã xong!");
                val bundle = Bundle()
             //   bundle.putString(Constant.KEY_IMAGE_PATH, imagePath)
             //   bundle.putSerializable(Constant.KEY_FILTER, seletedFilterData)
               // showActivity(CameraResultActivity::class.java, bundle)
            } else {
                showToast("Ôi, có lỗi rồi!")
            }
        }
    }


}