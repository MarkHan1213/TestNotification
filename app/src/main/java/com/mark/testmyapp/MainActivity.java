package com.mark.testmyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ll_mute;
    private RelativeLayout rl_root;
    private Button button,btn_top;
    private Looper looper;
    private H h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_main);
        ll_mute = findViewById(R.id.ll_mute);
        button = findViewById(R.id.btn_ainm);
        btn_top = findViewById(R.id.btn_top);
        rl_root = findViewById(R.id.rl_root);

        HandlerThread ht = new HandlerThread(MainActivity.class.getSimpleName(),
                Process.THREAD_PRIORITY_BACKGROUND);
        ht.start();
        looper = ht.getLooper();
        h = new H(looper);
    }

    public void sendGroup(View view) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("testNotification", "测试", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setSound(Uri.parse("content://settings/system/notification_sound"), new AudioAttributes.Builder().build());
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "testNotification");
        NotificationCompat.BigPictureStyle pictureStyle = new NotificationCompat.BigPictureStyle();
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
        Bitmap bmp2 = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);

        pictureStyle.bigPicture(bmp).bigLargeIcon(bmp2);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是标题")
                .setContentText("我是内容 " + i)
                .setSound(Uri.parse("content://settings/system/notification_sound"))
                .setStyle(pictureStyle)
                .setDefaults(Notification.DEFAULT_ALL);


        builder.setGroup("custom").setGroupSummary(isSummary);
        if (isSummary) {
            builder.setContentTitle("我是分组通知的Summary的标题");
        } else {
            builder.setContentTitle("我是分组通知的标题");
        }
        isSummary = false;
        notificationManager.notify(i, builder.build());
        i++;
    }


    private class H extends Handler {

        public H(@NonNull Looper looper) {
            super(looper);
        }


        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.e("HZB", "Thread = " + Thread.currentThread());
            btn_top.setText("你点到我了");
        }
    }

    int i = 1;
    private boolean isSummary = true;

    public void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("testNotification", "测试", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setSound(Uri.parse("content://settings/system/notification_sound"), new AudioAttributes.Builder().build());
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "testNotification");
        NotificationCompat.BigPictureStyle pictureStyle = new NotificationCompat.BigPictureStyle();
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
        Bitmap bmp2 = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);

        pictureStyle.bigPicture(bmp).bigLargeIcon(bmp2);

        boolean isGroup = i %2 == 0;
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是标题")
                .setContentText("我是内容 " + i)
                .setSound(Uri.parse("content://settings/system/notification_sound"))
                .setStyle(pictureStyle)
                .setDefaults(Notification.DEFAULT_ALL);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setCategory(NotificationCompat.CATEGORY_ALARM);

        Intent intent = new Intent(this, Main2Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setFullScreenIntent(pendingIntent, false);


        /*if (i % 2 == 0) {
            builder.setGroup("custom").setGroupSummary(isSummary);
            if (isSummary) {
                builder.setContentTitle("我是分组通知的Summary的标题");
            } else {
                builder.setContentTitle("我是分组通知的标题");
            }
            isSummary = false;
        }*/
        notificationManager.notify(i, builder.build());
        i++;

        /*h.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendNotification();
            }
        }, 1000L);*/
    }

    public void send(View view) {
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("HZB","sendNotification");
                sendNotification();
            }
        }, 1000);
    }

    public void sendThree(View view) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("testNotification3", "测试3", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setSound(Uri.parse("content://settings/system/notification_sound"), new AudioAttributes.Builder().build());
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "testNotification");
        NotificationCompat.BigPictureStyle pictureStyle = new NotificationCompat.BigPictureStyle();
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
        Bitmap bmp2 = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);

        pictureStyle.bigPicture(bmp).bigLargeIcon(bmp2);

        boolean isGroup = i %2 == 0;
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是标题")
                .setContentText("我是内容 " + i)
                .setSubText("第三行内容")
                .setSound(Uri.parse("content://settings/system/notification_sound"))
                .setStyle(pictureStyle)
                .setDefaults(Notification.DEFAULT_ALL);
       /* if (i % 2 == 0) {
            builder.setGroup("custom").setGroupSummary(isSummary);
            if (isSummary) {
                builder.setContentTitle("我是分组通知的Summary的标题");
            } else {
                builder.setContentTitle("我是分组通知的标题");
            }
            isSummary = false;
        }*/
        notificationManager.notify(i, builder.build());
        i++;


    }


    public void sendCustom(View view) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("testNotification2", "测试2", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(false);
            channel.enableLights(true);
            channel.setSound(Uri.parse("content://settings/system/notification_sound"), new AudioAttributes.Builder().build());
            if (notificationManager != null) notificationManager.createNotificationChannel(channel);
        }

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.test_custom);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "testNotification2");
        //创建大视图样式
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                .setBigContentTitle("Big picture style notification ~ Expand title")
                .setSummaryText("Demo for big picture style notification ! ~ Expand summery")
                .bigPicture(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是自定义标题")
                .setContentText("我是自定义内容 + " + i)
                .setContent(remoteViews)
                .setSound(Uri.parse("content://settings/system/notification_sound"))
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup("groupKey")
                .setGroupSummary(true)
                .setStyle(bigPictureStyle);
        if (i % 2 == 0) {
            builder.setGroup("custom").setGroupSummary(isSummary);
            isSummary = false;
        }
        notificationManager.notify(i, builder.build());
        i++;
//        h.sendEmptyMessage(1);
    }

    Scene mScene1, mScene2, mScene3;

    Transition transition;

    public void doAnim(View view) {


    }
}
