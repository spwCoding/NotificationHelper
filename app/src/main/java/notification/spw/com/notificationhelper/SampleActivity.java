package notification.spw.com.notificationhelper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class SampleActivity extends AppCompatActivity {

    public static final String TAG = "SampleActivity";

    private int updateValue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        findViewById(R.id.default_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotificaion();
            }
        });

        findViewById(R.id.backstack_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBackStackNotificaion();
            }
        });

        findViewById(R.id.full_screen_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreenNotificaion();
            }
        });

        findViewById(R.id.progress_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressNotification();
            }
        });

        findViewById(R.id.remoteviews_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoteViewNotificaion();
            }
        });
    }

    /**
     * 默认通知显示
     */
    private void showNotificaion() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "test notification")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        Intent resultIntent = new Intent(this, SampleActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,99,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, mBuilder.build());
    }

    /**
     * 返回堆栈的通知显示
     */
    private void showBackStackNotificaion() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "test notification")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My notification")
                .setContentText("Hello World!");
        Intent resultIntent = new Intent(this, SampleActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SampleActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, mBuilder.build());
    }

    /**
     * 一些定制的ROM上没有显示浮动通知的权限，无法显示浮动通知
     */
    private void showFullScreenNotificaion() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "test notification")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My notification")
                .setContentText("Hello World!");
        Intent resultIntent = new Intent(this, SampleActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SampleActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setFullScreenIntent(resultPendingIntent,true);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, mBuilder.build());
    }

    /**
     * 带有进度的条的通知显示
     */
    private void showProgressNotification(){
       final  NotificationManager  mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       final  NotificationCompat.Builder mBuilder  = new NotificationCompat.Builder(this,"push channel");
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher_round);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        for (incr = 0; incr <= 100; incr+=5) {
                            mBuilder.setProgress(100, incr, false);
                            //mBuilder.setProgress(0, 0, true);
                            mNotifyManager.notify(0, mBuilder.build());
                            try {
                                Thread.sleep(2*1000);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "sleep failure");
                            }
                        }
                        mBuilder.setContentText("Download complete")
                                .setProgress(0,0,false);
                        mNotifyManager.notify(0, mBuilder.build());
                    }
                }
        ).start();
    }

    /**
     * 默认自定义通知样式remoteview
     */
    private void showRemoteViewNotificaion() {

        RemoteViews mRemoteViews=new RemoteViews(this.getPackageName(), R.layout.notification_remote_view);
        mRemoteViews.setTextViewText(R.id.remote_text_view, "通知内容");
        mRemoteViews.setImageViewResource(R.id.remote_image_view,R.mipmap.ic_launcher_round);

        //2.构建一个打开Activity的PendingIntent
        Intent intent=new Intent(SampleActivity.this,SampleActivity.class);
        PendingIntent mPendingIntent=PendingIntent.getActivity(SampleActivity.this, 102, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "test notification")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My notification")
                .setContentText("Hello World!");
        mBuilder.setContentIntent(mPendingIntent);
        mBuilder.setContent(mRemoteViews);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(102, mBuilder.build());
    }

}
