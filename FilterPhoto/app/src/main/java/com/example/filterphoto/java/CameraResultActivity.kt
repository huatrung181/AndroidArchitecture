package com.example.filterphoto.java

import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.example.filterphoto.java.DeviceUtils.getScreenWidth
import com.example.filterphoto.kotlin.FilterData
import com.example.filterphoto.kotlin.base.BaseActivity
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback
import org.wysaid.nativePort.CGENativeLibrary


class CameraResultActivity : BaseActivity{
    private val RC_PICK_IMAGE = 1235

    @Bind(R.id.rv_filter)
    var rvFilter: RecyclerView? = null

    @Bind(R.id.iv_crop)
    var ivCrop: CropImageView? = null

    @Bind(R.id.rl_image)
    var rlImage: RelativeLayout? = null

    @Bind(R.id.tv_loading_image)
    var tvLoadingImage: TextView? = null
    var imagePath: String? = null
    var targetScreen: String? = null
    var resource: Bitmap? = null
    var finalResource: Bitmap? = null

    var screenWidth = 0

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
        screenWidth = getScreenWidth(this)
        rlImage!!.layoutParams.height = screenWidth
        ivCrop!!.layoutParams.height = screenWidth
        if (imagePath != null && !imagePath!!.isEmpty()) {
            Glide.with(this).load<Any>(File(imagePath)).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).into(object : SimpleTarget<Bitmap?>() {
                fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                    this@CameraResultActivity.resource = resource
                    ivCrop!!.imageBitmap = resource
                    ivCrop!!.setCustomRatio(screenWidth * 2 / 3, screenWidth * 2 / 3)
                    tvLoadingImage!!.visibility = View.GONE
                }
            })
        } else {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, RC_PICK_IMAGE)
        }
    }

    protected override fun getLayoutId(): Int {
        return R.layout.activity_camera_result
    }

    protected override fun createView() {
        setUpListFilterEffect()
    }

    private fun getData() {
        val bundle = intent.getBundleExtra(Constant.KEY_EXTRA)
        if (bundle != null) {
            imagePath = bundle.getString(Constant.KEY_IMAGE_PATH)
            targetScreen = bundle.getString(Constant.KEY_TARGET_SCREEN, "")
        }
    }

    private fun setUpListFilterEffect() {
        rvFilter!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //create list filter
        val listFilter: MutableList<FilterData> = ArrayList()
        val imageFilterId = intArrayOf(R.drawable.original_1, R.drawable.natural_1, R.drawable.natural_2
                , R.drawable.pure_1, R.drawable.pure_2, R.drawable.pinky_1
                , R.drawable.pinky_2, R.drawable.pinky_3, R.drawable.pinky_4
                , R.drawable.warm_1, R.drawable.warm_2, R.drawable.cool_1
                , R.drawable.cool_2, R.drawable.mood, R.drawable.bw)
        for (i in 0 until EFFECT_CONFIGS.length) {
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
        val filterAdapter = ListFilterAdapter(listFilter)
        filterAdapter.onFilterSelect = object : ListFilterAdapter.OnFilterSelect {
            override fun onSelect(filterData: FilterData?) {
                FilterBitmapTask().execute(filterData!!.rule)
            }
        }
        rvFilter!!.adapter = filterAdapter
    }

    private class FilterBitmapTask : AsyncTask<String?, Void?, Bitmap?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showProgressDialog("filtering")
        }

        override fun onPostExecute(resultBitmap: Bitmap?) {
            super.onPostExecute(resultBitmap)
            ivCrop.setImageBitmap(resultBitmap)
            var logData = "FilterBitmapTask " + " bimap null:" + (resultBitmap == null)
            if (resultBitmap != null) {
                logData = logData + "bitmap width = " + resultBitmap.width + " - bitmap height = " + resultBitmap.height
            }
            hideProgressDialog()
        }

        protected override fun doInBackground(vararg params: String): Bitmap? {
            finalResource = CGENativeLibrary.cgeFilterImage_MultipleEffects(resource, params[0], 1f)
            return finalResource
        }
    }

    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data.data
            Glide.with(this).load<Any>(File(getRealPathFromURI(selectedImageUri))).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).into(object : SimpleTarget<Bitmap?>() {
                fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                    this@CameraResultActivity.resource = resource
                    ivCrop!!.imageBitmap = resource
                    ivCrop!!.setCustomRatio(screenWidth * 2 / 3, screenWidth * 2 / 3)
                    tvLoadingImage!!.visibility = View.GONE
                }
            })
        } else {
            finish()
        }
    }

    private fun getRealPathFromURI(contentURI: Uri?): String? {
        val result: String
        val cursor: Cursor? = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath()
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    @OnClick(R.id.bt_continue, R.id.bt_done)
    fun cropImage() {
        ivCrop!!.startCrop(null, object : CropCallback {
            override fun onSuccess(cropped: Bitmap) {
                val file = File(Environment.getExternalStorageDirectory().toString() + "/FeedyPhoto/Filtered")
                if (!file.exists()) {
                    file.mkdirs()
                }
                val imagePath: String = ImageUtil.saveBitmap(cropped, file.getAbsolutePath().toString() + "/" + System.currentTimeMillis() + ".jpg")
                val bundle = Bundle()
                if (imagePath != null) {
                    bundle.putString(Constant.KEY_IMAGE_PATH, imagePath)
                    showActivity(FinalImageActivity::class.java, bundle)
                }
            }

            override fun onError(e: Throwable) {}
        }, object : SaveCallback() {
            fun onSuccess(uri: Uri?) {}
            fun onError(e: Throwable?) {}
        })
    }


    @OnClick(R.id.bt_close, R.id.bt_back)
    fun closeCamera() {
        finish()
    }
}