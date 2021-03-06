package com.example.submission2bfaa.alarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.submission2bfaa.MainActivity
import com.example.submission2bfaa.R
import java.util.*
import kotlin.math.log

class MyReceiver : BroadcastReceiver() {
    companion object{
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"
        const val TYPE_REPEATING ="Reminder"
        private const val ID_REPEATING = 101


        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val title = TYPE_REPEATING
        val notifId = ID_REPEATING
        Log.d(message, "onReceive: $message ")

        if (message!=null){
            showAlarmNotificaiton(context,title, message,notifId)
        }
    }

    private fun showAlarmNotificaiton(context: Context, title:String,message: String, notifId:Int){
        val CHANNEL_ID = "channel_1"
        val CHANNEL_NAME = "AlarmManager channel"
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT)
        val builder = NotificationCompat.Builder(context,CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.github_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context,android.R.color.transparent))
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    fun setRepeatingAlarm(context:Context?,type:String,time:String,message:String){
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE)as AlarmManager
        val intent = Intent(context,MyReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE,message)
        val putExtra = intent.putExtra(EXTRA_TYPE,type)
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE,Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND,0)

        val pendingIntent = PendingIntent.getBroadcast(context,ID_REPEATING,intent,0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
        Toast.makeText(context,"Alarm set up",Toast.LENGTH_SHORT).show()
    }

    fun batalkanAlarm(context: Context?){
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING,intent,0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context,"Pengingat harian nonaktif", Toast.LENGTH_SHORT).show()
    }

}