package com.example.filterphoto.kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.filterphoto.R
import com.example.filterphoto.kotlin.base.BaseActivity
import com.example.filterphoto.kotlin.utils.Constant
import com.example.filterphoto.kotlin.utils.DeviceUtils
import java.io.File

class FinalImageActivity : BaseActivity() {

    @BindView(R.id.iv_picture)
    lateinit var ivPicture: ImageView

    @BindView(R.id.et_caption)
    lateinit var etCaption: EditText
    lateinit var imagePath: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)
        getData()
        if (imagePath!=null&& !imagePath.isEmpty()){
            Glide.with(this).load(File(imagePath)).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivPicture)
        }
    }

    @OnClick(R.id.bt_post)
    fun post(){
        val intent = Intent(android.content.Intent.ACTION_SEND)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(File(imagePath)))
        intent.putExtra(Intent.EXTRA_TEXT, etCaption.text.toString())
        intent.setType("image/png")
        startActivity(Intent.createChooser(intent, "Share image via"))

    }

    fun getData(){
        var bundle = intent.getBundleExtra(Constant.KEY_EXTRA)
        if(bundle!=null){
            imagePath = bundle.getString(Constant.KEY_IMAGE_PATH)!!
        }
    }


    override fun createView() {
        var screenWidth= DeviceUtils.getScreenWidth(this)
        ivPicture.layoutParams.height= screenWidth
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_final_image
    }
}