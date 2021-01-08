package com.xuexiang.temical.utils.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class alarm extends Service {
    int count = 0;
    long start_time;
    long repeat_time;
    long end_time;
    String title;
    public alarm(){
        start_time=0;
        repeat_time=0;
        end_time=0;
        title="无收到";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start_time=intent.getLongExtra("start",start_time);
        repeat_time=intent.getLongExtra("repeat",repeat_time);
        end_time=intent.getLongExtra("end",end_time);
        title=intent.getStringExtra("title");
//        String title=intent.getStringExtra("title");
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, receive_alarm.class);
        i.putExtra("title",title);
        PendingIntent pi = PendingIntent.getBroadcast(this, count++, i, PendingIntent.FLAG_UPDATE_CURRENT);
        if(start_time<=end_time)
        {
            manager.set(AlarmManager.RTC_WAKEUP, start_time, pi);
            start_time+=repeat_time;
        }
        else
        {
            onDestroy();
            stopService(i);

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, receive_alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

