package com.example.filterphoto.java

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife

abstract class Mybase : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        ButterKnife.bind(this)
        createView()
    }

    //Hàm abstract trả về layout của activity
    protected abstract val layoutId: Int

    //Hàm set sự kiện cho các view trong activity
    protected abstract fun createView()

    //Hàm show thông báo toast
    fun showToast(msg: String?) {
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
        }
    }

    //Hàm chuyển activity
    fun showActivity(t: Class<*>?) {
        val intent = Intent(this, t)
        startActivity(intent)
    }

    ////Hàm chuyển activity kèm theo bundle
    fun showActivity(t: Class<*>?, bundle: Bundle?) {
        val intent = Intent(this, t)
        intent.putExtra("", bundle)
        startActivity(intent)
    }
}