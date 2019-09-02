package com.queserasera.militarycalc;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class InputActivity extends Activity {
    /** Called when the activity is first created. */

    private TextView mStartDateDisplay;
    private TextView mEndDateDisplay;
    private Button mInputDone;

    private int startYear;
    private int startMonth;
    private int startDay;
    private int endYear;
    private int endMonth;
    private int endDay;

    static final int START_DATE_DIALOG_ID = 0;
    static final int END_DATE_DIALOG_ID = 1;
    private SharedPreferences appData;
    final Context context = this;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        // 선택 완료 시 main으로
        mInputDone = (Button) findViewById(R.id.inputDone);
        mInputDone.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                save();

                showMain();
            }
        });

        //// Date Picker: 시작  ////////////
        // (1) main.xml의 레이아웃에 배치된 날짜 입력을 위한 TextView 인식
        mStartDateDisplay = (TextView) findViewById(R.id.startDateButton);
        mEndDateDisplay = (TextView) findViewById(R.id.endDateButton);
        // (2) 인식된 TextView에 click listener 추가
        mStartDateDisplay.setOnClickListener(new View.OnClickListener() {
            // (5) 클릭되면 실행
            public void onClick(View v) {
                // (6) 날짜 설정을 위한 다이어로그 출력
                showDialog(START_DATE_DIALOG_ID);
            }
        });
        mEndDateDisplay.setOnClickListener(new View.OnClickListener() {
            // (5) 클릭되면 실행
            public void onClick(View v) {
                // (6) 날짜 설정을 위한 다이어로그 출력
                showDialog(END_DATE_DIALOG_ID);
            }
        });
        // (3) 저장된 날짜 인식
        final Calendar c = Calendar.getInstance();
        startYear = appData.getInt("STARTYEAR", c.get(Calendar.YEAR));
        startMonth = appData.getInt("STARTMONTH", c.get(Calendar.MONTH));
        startDay = appData.getInt("STARTDAY", c.get(Calendar.DAY_OF_MONTH));
        endYear = appData.getInt("ENDYEAR",  c.get(Calendar.YEAR));
        endMonth = appData.getInt("ENDMONTH", c.get(Calendar.MONTH));
        endDay = appData.getInt("ENDDAY", c.get(Calendar.DAY_OF_MONTH));

        // (4) 인식된 날짜를  출력
        updateStartDateDisplay();
        updateEndDateDisplay();
        //// Date Picker: 끝  ////////////
    }

    public void showMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    // 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putInt("STARTYEAR", startYear);
        editor.putInt("STARTMONTH", startMonth);
        editor.putInt("STARTDAY", startDay);

        editor.putInt("ENDYEAR", endYear);
        editor.putInt("ENDMONTH", endMonth);
        editor.putInt("ENDDAY", endDay);

        editor.putBoolean("INPUT",true);

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // (7) 다이어로그 출력시 DatePicker 다이어로그 출력
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                return new DatePickerDialog(this, startDateSetListener, startYear, startMonth, startDay);
            case END_DATE_DIALOG_ID:
                return new DatePickerDialog(this, endDateSetListener, endYear, endMonth, endDay);
        }
        return null;
    }

    // (8) 다이어로그에 있는 날짜를 설정(set)하면 실행됨
    private DatePickerDialog.OnDateSetListener startDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    startYear = year;
                    startMonth = monthOfYear;
                    startDay = dayOfMonth;
                    // 사용자가 지정한 날짜를 출력
                    updateStartDateDisplay();
                }
            };

    // 설정된 날짜를 TextView에 출력
    private void updateStartDateDisplay() {
        //main.xml의 레이아웃에 배치된 날짜 입력 TextView에 인식된 날짜 출력
        mStartDateDisplay.setText(
                new StringBuilder()
                        // 월은 시스템에서 0~11로 인식하기 때문에 1을 더해줌
                        .append(startYear).append("-")
                        .append(startMonth+1).append("-")
                        .append(startDay).append(" ")
        );
    }
    // (8) 다이어로그에 있는 날짜를 설정(set)하면 실행됨
    private DatePickerDialog.OnDateSetListener endDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    endYear = year;
                    endMonth = monthOfYear;
                    endDay = dayOfMonth;
                    // 사용자가 지정한 날짜를 출력
                    updateEndDateDisplay();
                }
            };

    // 설정된 날짜를 TextView에 출력
    private void updateEndDateDisplay() {
        //main.xml의 레이아웃에 배치된 날짜 입력 TextView에 인식된 날짜 출력
        mEndDateDisplay.setText(
                new StringBuilder()
                        // 월은 시스템에서 0~11로 인식하기 때문에 1을 더해줌
                        .append(endYear).append("-")
                        .append(endMonth+1).append("-")
                        .append(endDay).append(" ")
        );
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(0,R.anim.fadeout);
    }
}