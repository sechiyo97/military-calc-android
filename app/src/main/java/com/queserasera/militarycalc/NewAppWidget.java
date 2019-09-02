package com.queserasera.militarycalc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Date;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    private static final String TAG = "Widget";
    private static final int WIDGET_UPDATE_INTERVAL = 5000;
    private static PendingIntent mSender;
    private static AlarmManager mManager;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);

        String action = intent.getAction();
        // 위젯 업데이트 인텐트를 수신했을 때
        if(action.equals("android.appwidget.action.APPWIDGET_UPDATE"))
        {
            Log.w(TAG, "android.appwidget.action.APPWIDGET_UPDATE");
            removePreviousAlarm();

            long firstTime = System.currentTimeMillis() + WIDGET_UPDATE_INTERVAL;
            mSender = PendingIntent.getBroadcast(context, 0, intent, 0);
            mManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mManager.set(AlarmManager.RTC, firstTime, mSender);
        }
        // 위젯 제거 인텐트를 수신했을 때
        else if(action.equals("android.appwidget.action.APPWIDGET_DISABLED"))
        {
            Log.w(TAG, "android.appwidget.action.APPWIDGET_DISABLED");
            removePreviousAlarm();
        }
    }

    /**
     * 예약되어있는 알람을 취소합니다.
     */
    public void removePreviousAlarm()
    {
        if(mManager != null && mSender != null)
        {
            mSender.cancel();
            mManager.cancel(mSender);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SharedPreferences appData;
        int mStartYear, mStartMonth, mStartDay;
        int mEndYear, mEndMonth, mEndDay;
        int totalDay, pastDay, leftDay;
        long now = System.currentTimeMillis();
        int progress;

        String mUntilMessage;
        String mDDay;
        String mPercentage;
        String mEndDate;
        ProgressBar mProgress;

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        // views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent intent=new Intent(context, SplashActivity.class);
        PendingIntent pe= PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widgetLL, pe);

        // 설정값 불러오기
        appData = context.getSharedPreferences("appData", MODE_PRIVATE);

        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        mStartYear = appData.getInt("STARTYEAR", 0);
        mStartMonth = appData.getInt("STARTMONTH", 0);
        mStartDay = appData.getInt("STARTDAY", 0);
        mEndYear = appData.getInt("ENDYEAR", 0);
        mEndMonth = appData.getInt("ENDMONTH", 0);
        mEndDay = appData.getInt("ENDDAY", 0);


        Date startDate = new Date(mStartYear-1900, mStartMonth, mStartDay);
        Date endDate = new Date(mEndYear-1900, mEndMonth, mEndDay);
        Date curDate = new Date(now);

        totalDay = (int)((endDate.getTime()-startDate.getTime())/(double)(24*60*60*1000));
        pastDay = (int)((curDate.getTime()-startDate.getTime())/(double)(24*60*60*1000));
        leftDay = (int)Math.ceil((endDate.getTime()-curDate.getTime())/(double)(24*60*60*1000));

        // calculation
        progress = (int)(((double)pastDay/(double)totalDay)*10000); //소수2째자리까지

        mDDay = "D-"+String.valueOf(leftDay);
        mPercentage = String.format("%.02f", progress/(double)100) + "%";
        mEndDate = String.format("%02d", mEndYear) + ". "
                + String.format("%02d", mEndMonth+1) + ". "
                + String.format("%02d", mEndDay) + ". 까지";

        views.setTextViewText(R.id.dDay, mDDay);
        views.setTextViewText(R.id.percentage, mPercentage);
        views.setTextViewText(R.id.untilMessage, mEndDate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // 현재 클래스로 등록된 모든 위젯의 리스트를 가져옴
        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int N = appWidgetIds.length;
        for(int i = 0 ; i < N ; i++){
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);
      }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

