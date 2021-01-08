package com.xuexiang.temical.utils.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.xuexiang.temical.R;
import com.xuexiang.temical.utils.XToastUtils;

public class receive_alarm extends BroadcastReceiver{
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO 实现功能
        String title=intent.getStringExtra("title");
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationChannel notificationChannel=new NotificationChannel("1", "alarm", NotificationManager.IMPORTANCE_DEFAULT);

        notificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder mb=new NotificationCompat.Builder(context,"1");
        mb.setContentTitle(title)
                .setContentText("日程代办")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("收到一条消息").setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{0,1000,1000,1000})
                .setLights(Color.BLUE,1000,1000);
        notificationManager.notify(1,mb.build());
    }
}