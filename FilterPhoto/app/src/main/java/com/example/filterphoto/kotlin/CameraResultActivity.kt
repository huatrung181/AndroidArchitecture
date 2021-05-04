package com.example.filterphoto.kotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.example.filterphoto.R
import com.example.filterphoto.kotlin.base.BaseActivity
import com.example.filterphoto.kotlin.utils.Constant
import com.example.filterphoto.kotlin.utils.DeviceUtils
import com.isseiaoki.simplecropview.CropImageView
import java.io.File

class CameraResultActivity : BaseActivity() {

    lateinit var imagePath: String
    lateinit var resource : Bitmap
    lateinit var finalResource: Bitmap
    lateinit var targetScreen: String
     var screenWidth = 0

    @BindView(R.id.rv_filter)
    lateinit var rvFilter: RecyclerView
    @BindView(R.id.iv_crop)
    lateinit var ivCrop: CropImageView

    @BindView(R.id.rl_image)
    lateinit var rlImage: RelativeLayout

    @BindView(R.id.tv_loading_image)
    lateinit var tvLoadingImage: TextView


    private val RC_PICK_IMAGE = 1235
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)

        getData()
        screenWidth = DeviceUtils.getScreenWidth(this)
        rlImage.layoutParams.height = screenWidth
        ivCrop.layoutParams.height= screenWidth

        if (imagePath!=null && !imagePath.isEmpty()){
            Glide.with(this).load(File(imagePath)).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).into(object : SimpleTarget<Bitmap?>(){
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap?>?) {
                    this@CameraResultActivity.resource= resource!!
                    ivCrop.imageBitmap=resource
                    ivCrop.setCustomRatio(screenWidth * 2/3, screenWidth*2/3)
                    tvLoadingImage.visibility= View.GONE

                }

            })
        }else{
            var photoPickerIntent= Intent(Intent.ACTION_PICK)
            photoPickerIntent.setType("image/*")
            startActivityForResult(photoPickerIntent, RC_PICK_IMAGE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==RC_PICK_IMAGE && resultCode== Activity.RESULT_OK){
            var selectedImageUri = data?.data
            Glide.with(this).load(File(getRealPathFromURI(selectedImageUri))).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).into(object : SimpleTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    this@CameraResultActivity.resource= resource!!
                    ivCrop.imageBitmap= resource
                    ivCrop.setCustomRatio(screenWidth*2/3, screenWidth*2/3)
                    tvLoadingImage.visibility= View.GONE

                }

            })

        }else{
            finish()
        }
    }

    private fun getRealPathFromURI(selectedImageUri: Uri?): String {
        var result=""
        var cursor = contentResolver.query(selectedImageUri!!, null, null, null, null)
        if (cursor==null){
            result= selectedImageUri.path!!
        }else{
            cursor.moveToFirst()
           var idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
    fun setUpListFilterEffect(){
        rvFilter.layoutManager= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        var listFilter:  MutableList<FilterData> = ArrayList() //List<FilterData> listFilter = new ArrayList<>();
        var imageFilterId = intArrayOf(R.drawable.original_1, R.drawable.natural_1, R.drawable.natural_2
                , R.drawable.pure_1, R.drawable.pure_2, R.drawable.pinky_1
                , R.drawable.pinky_2, R.drawable.pinky_3, R.drawable.pinky_4
                , R.drawable.warm_1, R.drawable.warm_2, R.drawable.cool_1
                , R.drawable.cool_2, R.drawable.mood, R.drawable.bw)
        for (i in 0 until EFFECT_CONFIGS.size) {
            //listFilter.add(new FilterData(EFFECT_CONFIGS[i], imageFilterId[i ]));
            if (i == 0) {
                listFilter.add(FilterData("Original", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 1) {
                listFilter.add(FilterData("Natural 1", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 2) {
                listFilter.add(FilterData("Natural 2", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 3) {
                listFilter.add(FilterData("Pure 1", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 4) {
                listFilter.add(FilterData("Pure 2", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 5) {
                listFilter.add(FilterData("Pinky 1", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 6) {
                listFilter.add(FilterData("Pinky 2", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 7) {
                listFilter.add(FilterData("Pinky 3", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 8) {
                listFilter.add(FilterData("Pinky 4", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 9) {
                listFilter.add(FilterData("Warm 1", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 10) {
                listFilter.add(FilterData("Warm 2", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 11) {
                listFilter.add(FilterData("Cool 1", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 12) {
                listFilter.add(FilterData("Cool 2", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 13) {
                listFilter.add(FilterData("Mood", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            } else if (i == 14) {
                listFilter.add(FilterData("B&W", EFFECT_CONFIGS.get(i), imageFilterId[i]))
            }
        }
        var filterAdapter= ListFilterAdapter(listFilter)
        rvFilter.adapter=filterAdapter




    }


    override fun createView() {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_camera_result
        TODO("Not yet implemented")
    }

    fun getData(){
        var bundle = intent.getBundleExtra(Constant.KEY_EXTRA)
        if(bundle!=null){
            imagePath = bundle.getString(Constant.KEY_IMAGE_PATH)!!
            targetScreen = bundle.getString(Constant.KEY_TARGET_SCREEN)!!
        }
    }



}