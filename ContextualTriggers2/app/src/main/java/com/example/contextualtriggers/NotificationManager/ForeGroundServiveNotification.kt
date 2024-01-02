import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

object ForeGroundServiveNotification {
    private const val CHANNEL_ID = "channel_id"
    private const val CHANNEL_NAME = "channel_name"
    private const val CHANNEL_DESC = "channel_description"
    private const val NOTIFICATION_CHANNEL_ID = "notification_channel_id"

    fun startForegroundService(
        context: Context,
        title: String,
        message: String,
        notificationId: Int,
        service: Service,
        iconResId: Int = android.R.drawable.ic_dialog_info,
        channelName: String = "Default Channel",
        intent: Intent? = null,
        requestCode: Int = 0
    ) {
        // Create the notification channel for devices running Android Oreo or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create a pending intent for the notification's click action
        val pendingIntent = if (intent != null) {
            PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            null
        }

        // Build the notification
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(iconResId)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // Start the foreground service
        service.startForeground(notificationId, notification)
    }
}
