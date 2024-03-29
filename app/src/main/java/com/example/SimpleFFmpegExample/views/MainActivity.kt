package com.example.SimpleFFmpegExample.views

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.SimpleFFmpegExample.R
import com.example.SimpleFFmpegExample.utils.Constants
import com.example.SimpleFFmpegExample.utils.Constants.INTENT_VIDEO_PATH
import com.example.SimpleFFmpegExample.utils.Constants.REQUEST_VIDEO_CROP
import com.example.SimpleFFmpegExample.utils.Constants.REQUEST_VIDEO_TRIM
import com.example.SimpleFFmpegExample.utils.Constants.TAG
import com.video.trimmer.utils.FileUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity - onCreate() called") 

        setupPermissions()

        btn_video_crop.setOnClickListener {
            openVideo(REQUEST_VIDEO_CROP)
        }

        btn_video_trim.setOnClickListener {
            openVideo(REQUEST_VIDEO_TRIM)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            Constants.REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    showPermissionDialog()
                }
                return
            }

        }
    }

    private val activityTrimResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data!!.data
            Log.d(TAG, "MainActivity activityTrimResultLauncher called")
            if (data != null) {
                startTrimActivity(data)
            } else {
                Toast.makeText(this, "찾을수 없어요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val activityCropResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data!!.data
            Log.d(TAG, "MainActivity activityCropResultLauncher called")
            if (data != null) {
                startCropActivity(data)
            } else {
                Toast.makeText(this, "찾을수 없어요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionDialog() {
        val dlgPermission = PermissionDialog()
        dlgPermission.show(supportFragmentManager, "permission")
    }

    private fun startCropActivity(uri: Uri) {
        val intent = Intent(this@MainActivity, CropActivity::class.java)
        intent.putExtra(INTENT_VIDEO_PATH, FileUtils.getPath(this, uri))
        activityCropResultLauncher.launch(intent)
    }

    private fun startTrimActivity(uri: Uri) {
        val intent = Intent(this@MainActivity, TrimActivity::class.java)
        intent.putExtra(INTENT_VIDEO_PATH, FileUtils.getPath(this, uri))
        activityTrimResultLauncher.launch(intent)
    }

    private fun openVideo(code: Int){
        val intent = Intent()
        intent.setTypeAndNormalize("video/*")
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        when(code) {
            REQUEST_VIDEO_TRIM -> {
                activityTrimResultLauncher.launch(Intent.createChooser(intent, "Select Video"))
            }
            REQUEST_VIDEO_CROP -> {
                activityCropResultLauncher.launch(Intent.createChooser(intent, "Select Video"))
            }
        }

    }

    private fun setupPermissions() {
        val writePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if (writePermission!= PackageManager.PERMISSION_GRANTED && readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), Constants.REQUEST_CODE
            )
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "MainActivity - onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity - onResume() called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "MainActivity - onRestart() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MainActivity - onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "MainActivity - onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MainActivity - onDestroy() called")
    }

}