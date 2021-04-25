package com.example.filterphoto.kotlin.base

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import butterknife.ButterKnife
import com.example.filterphoto.kotlin.utils.PermissionUtils
import java.util.*
import kotlin.collections.ArrayList


abstract class  BaseActivity : AppCompatActivity() {

    val KEY_PERMISSION = 200
    lateinit var permissionResult: PermissionUtils.PermissionResult
    lateinit var permissionAsk : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        ButterKnife.bind(this)
        createView()
    }

    open fun showToast(msg: String) {
        if (msg!=null){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
        }
    }
    open fun showActivity(t: Class<*>?) {
        val intent =  Intent(this,t)
        startActivity(intent)
    }

    open fun showActivityWithBundle(t: Class<*>? , bundle: Bundle){
        val intent = Intent(this, t)
        intent.putExtra("", bundle)
        startActivity(intent)
    }

    abstract fun createView()

    abstract fun getLayoutId(): Int

    open fun isPermissionGranted(permission: String?): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ActivityCompat.checkSelfPermission(
            this,
            permission!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun askCompactPermissions(permissions: Array<String>, permissionResult: com.example.filterphoto.kotlin.utils.PermissionUtils.PermissionResult){
        permissionAsk = permissions
        this.permissionResult = permissionResult
        internalRequestPermission(permissionAsk)
    }

    private fun internalRequestPermission(permissionAsk: Array<String>) {
        var arrayPermissionNotGranted : Array<String?>
        var permissionsNotGranted = ArrayList<String>()

        for (i in permissionAsk.indices){
            if (!isPermissionGranted(permissionAsk[i])){
                permissionsNotGranted.add(permissionAsk[i])
            }
        }

        if (permissionsNotGranted.isEmpty()){
            if (permissionResult !=null)
                permissionResult.permissionGranted()
        }else{
            arrayPermissionNotGranted = arrayOfNulls(permissionsNotGranted.size)
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted)
            ActivityCompat.requestPermissions(this, arrayPermissionNotGranted, KEY_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode!=KEY_PERMISSION){
            return
        }
      var  permissionDenied = LinkedList<String>()
        var granted = true
        for (i in grantResults.indices){
            if (grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                granted=false
                permissionDenied.add(permissions[i])
            }
        }

        if (permissionResult!=null){
            if (granted){
                permissionResult.permissionGranted()
            }else{
                for (s in permissionDenied){
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this,s)){
                        permissionResult.permissionDenied()
                        return
                    }
                }
                permissionResult.permissionDenied()
            }
        }

    }


}