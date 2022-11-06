package com.given.filmmovieapp

import android.hardware.Camera
import android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT
import android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.given.filmmovieapp.databinding.ActivityCameraBinding
import java.io.IOException

class CameraActivity : AppCompatActivity() {
    //binding
    private lateinit var binding: ActivityCameraBinding
    // Camera Preview
    private var cameraProfil: Camera? = null
    private var cameraView: CameraView? = null
    private var camInfo= Camera.CameraInfo.CAMERA_FACING_BACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_camera)
        supportActionBar?.setTitle("Take Picture")

        binding = ActivityCameraBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        try {
            cameraProfil = Camera.open(camInfo)
        }catch (e: IOException){
            Log.d("Error", "Failed to open Camera" + e.message)
        }

        if(cameraProfil != null){
            cameraView = CameraView(this@CameraActivity, cameraProfil!!)
//            val camera_view = binding.FLCamera as FrameLayout
//            camera_view.addView(cameraView)
            binding.FLCamera.addView(cameraView)
        }
        // @SuppressLint("MissingInflatedId", "LocalSuppress")
        binding.btnCloseCamera.setOnClickListener{view: View? ->
            cameraProfil?.stopPreview()
            cameraProfil?.release()
            finish()
        }
        binding.btnSwitchCamera.setOnClickListener{
            if (camInfo == CAMERA_FACING_BACK){
                cameraProfil?.stopPreview()
            }

            cameraProfil?.release()
            if (camInfo == CAMERA_FACING_BACK){
                camInfo = CAMERA_FACING_FRONT
            }else{
                camInfo = CAMERA_FACING_BACK
            }

            try {
                cameraProfil = Camera.open(camInfo)
            }catch (e: IOException){
                Log.d("Error", "Failed to open Front Camera" + e.message)
            }
            if(cameraProfil != null){
                cameraView = CameraView(this@CameraActivity, cameraProfil!!)
//            val camera_view = binding.FLCamera as FrameLayout
//            camera_view.addView(cameraView)
                binding.FLCamera.addView(cameraView)
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}