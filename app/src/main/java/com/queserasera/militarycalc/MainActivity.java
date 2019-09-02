package com.queserasera.militarycalc;

 import android.app.Activity;
 import android.app.AlertDialog;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.os.Bundle;
 import android.view.GestureDetector;
 import android.view.View;
 import android.widget.ImageView;
 import android.widget.ProgressBar;
 import android.widget.TextView;

 import java.util.Date;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    private GestureDetector mGestureDetector;

    private ImageView mSettingIcon;
    private TextView mUntilMessage;
    private TextView mDDay;
    private TextView mPercentage;
    private TextView mStartDate;
    private TextView mEndDate;
    private ProgressBar mProgress;
    private int mStartYear, mStartMonth, mStartDay;
    private int mEndYear, mEndMonth, mEndDay;
    private SharedPreferences appData;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        mSettingIcon = (ImageView)findViewById(R.id.settingIcon);
        mSettingIcon.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                // 팝업 다이어로그 출력
                confirmPopup(v);
            }
        });
        load();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        mStartYear = appData.getInt("STARTYEAR", 0);
        mStartMonth = appData.getInt("STARTMONTH", 0);
        mStartDay = appData.getInt("STARTDAY", 0);
        mEndYear = appData.getInt("ENDYEAR", 0);
        mEndMonth = appData.getInt("ENDMONTH", 0);
        mEndDay = appData.getInt("ENDDAY", 0);

        mUntilMessage = (TextView)findViewById(R.id.untilMessage);
        String untilMessage =
                Integer.toString(mEndYear) + ". " + String.format("%02d", mEndMonth+1) +
                ". " + String.format("%02d", mEndDay) + ". 까지";

        mUntilMessage.setText(untilMessage);
        calculate();
    }

    public void confirmPopup(View view){
        new AlertDialog.Builder(this)
                .setTitle("내 정보 변경")
                .setMessage("변경하시겠습니까?")
                //.setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        showSettings();
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }})
                .show();
    }

    public void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(0,0);
    }

    private void calculate(){
        // initialization
        int totalDay, pastDay, leftDay;
        long now = System.currentTimeMillis();

        Date startDate = new Date(mStartYear-1900, mStartMonth, mStartDay);
        Date endDate = new Date(mEndYear-1900, mEndMonth, mEndDay);
        Date curDate = new Date(now);

        totalDay = (int)((endDate.getTime()-startDate.getTime())/(double)(24*60*60*1000));
        pastDay = (int)((curDate.getTime()-startDate.getTime())/(double)(24*60*60*1000));
        leftDay = (int)Math.ceil((endDate.getTime()-curDate.getTime())/(double)(24*60*60*1000));

        // update
        updateProgress(totalDay, pastDay, leftDay);
    }

    private void updateProgress(int totalDay, int pastDay, int leftDay){
        // initialization
        int progress;

        // calculation
        progress = (int)(((double)pastDay/(double)totalDay)*10000); //소수2째자리까지

        mStartDate=(TextView)findViewById(R.id.startDate);
        mStartDate.setText(
                new StringBuilder()
                        .append(mStartYear%100)
                        .append(". ")
                        .append(String.format("%02d", mStartMonth+1))
                        .append(". ")
                        .append(String.format("%02d", mStartDay))
                        .append(".")
        );
        mEndDate=(TextView)findViewById(R.id.endDate);
        mEndDate.setText(
                new StringBuilder()
                        .append(mEndYear%100)
                        .append(". ")
                        .append(String.format("%02d", mEndMonth+1))
                        .append(". ")
                        .append(String.format("%02d", mEndDay))
                        .append(".")
        );

        mProgress = (ProgressBar)findViewById(R.id.progress);
        mProgress.setProgress(progress);

        mDDay= (TextView)findViewById(R.id.dDay);
        mDDay.setText(
                new StringBuilder()
                        .append("D-").append(leftDay)
        );
        mPercentage= (TextView)findViewById(R.id.percentage);
        mPercentage.setText(
                new StringBuilder()
                        .append(String.format("%.02f", progress/(double)100)).append("%")
        );
    }
}