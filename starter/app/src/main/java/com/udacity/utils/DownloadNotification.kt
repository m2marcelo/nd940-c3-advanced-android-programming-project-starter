package com.udacity.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.udacity.R
import com.udacity.utils.ChannelDetails
import com.udacity.MainActivity
import com.udacity.DetailActivity

object DownloadNotification {

    private const val CHANNEL_ID_DOWNLOADS = "downloads"
    private const val CHANNEL_NAME_DOWNLOADS = "Downloads"
    private const val REQUEST_CODE_DOWNLOADS = 1000

    fun getDownloadsChannel(context: Context): ChannelDetails {
        return ChannelDetails(
            CHANNEL_ID_DOWNLOADS,
            CHANNEL_NAME_DOWNLOADS,
            context.getString(R.string.downloading_files),
            NotificationManager.IMPORTANCE_HIGH,
            NotificationCompat.PRIORITY_HIGH,
            NotificationCompat.VISIBILITY_PUBLIC
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, ChannelDetails: ChannelDetails) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        NotificationChannel(
            ChannelDetails.id,
            ChannelDetails.name,
            ChannelDetails.importance
        ).apply {
            enableVibration(true)
            enableLights(true)
            setShowBadge(true)
            lightColor = context.getColor(R.color.colorAccent)
            description = ChannelDetails.description
            lockscreenVisibility = ChannelDetails.visibility
            notificationManager.createNotificationChannel(this)
        }
    }

    fun sendDownloadNotification(
        context: Context,
        downloadId: Int,
        downloadStatus: MainActivity.DownloadStatus,
        fileName: String
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notifyIntent = Intent(context, DetailActivity::class.java)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        notifyIntent.putExtras(DetailActivity.withExtras(downloadId, downloadStatus, fileName))

        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE_DOWNLOADS,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val downloadsChannel = getDownloadsChannel(context)

        val notification = NotificationCompat.Builder(context, downloadsChannel.id)
            .setContentTitle(fileName)
            .setContentText(
                if (downloadStatus == MainActivity.DownloadStatus.SUCCESS) {
                    context.getString(R.string.download_success)
                } else {
                    context.getString(R.string.download_fail)
                }
            )
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setAutoCancel(true)
            .setColor(context.getColor(R.color.colorAccent))
            .setLights(context.getColor(R.color.colorAccent), 1000, 3000)
            .setVisibility(downloadsChannel.visibility)
            .setPriority(downloadsChannel.priority)
            .addAction(
                NotificationCompat.Action(0,context.getString(R.string.download_details),pendingIntent)
            )
            .build()

        notificationManager.notify(downloadId, notification)
    }

    fun clearNotification(context: Context, notificationId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(notificationId)
    }
}