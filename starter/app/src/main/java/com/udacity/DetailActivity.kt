package com.udacity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.udacity.utils.DownloadNotification
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {
    private var downloadId = -1
    private lateinit var downloadStatus: MainActivity.DownloadStatus
    private lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        loadExtras()

        DownloadNotification.clearNotification(this, downloadId)

        text_file.text = fileName

        text_status.text = if (downloadStatus == MainActivity.DownloadStatus.SUCCESS) {
            getString(R.string.download_success)
        } else {
            getString(R.string.download_fail)
        }

        coordinateMotion()

        btn_back.setOnClickListener {
            finish()
        }
    }

    private fun coordinateMotion() {
        val motionLayout: MotionLayout = findViewById(R.id.motion_layout)
        motionLayout.transitionToEnd()
    }

    private fun loadExtras() {
        val extras = intent.extras
        extras?.let {
            downloadId = it.getInt(EXTRA_DOWNLOAD_ID)
            downloadStatus = MainActivity.DownloadStatus.values()[it.getInt(EXTRA_DOWNLOAD_STATUS)]
            fileName = it.getString(EXTRA_FILE_NAME)!!
        }
    }

    companion object {
        private const val EXTRA_DOWNLOAD_ID = "download_id"
        private const val EXTRA_DOWNLOAD_STATUS = "download_status"
        private const val EXTRA_FILE_NAME = "file_name"

        fun withExtras(
            downloadId: Int,
            downloadStatus: MainActivity.DownloadStatus,
            fileName: String
        ): Bundle {
            return Bundle().apply {
                putInt(EXTRA_DOWNLOAD_ID, downloadId)
                putInt(EXTRA_DOWNLOAD_STATUS, downloadStatus.ordinal)
                putString(EXTRA_FILE_NAME, fileName)
            }
        }
    }
}