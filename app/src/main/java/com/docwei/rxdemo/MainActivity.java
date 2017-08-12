package com.docwei.rxdemo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.docwei.rxdemo.changeTransform.ChangeTransformActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void go2Create(View view){
        IntentUtil.getInstance().next2UI(this,CreateActivity.class);
    }
    public void go2ChangeTransform(View view){
        IntentUtil.getInstance().next2UI(this,ChangeTransformActivity.class);
    }
    public void go2Filter(View view){
        IntentUtil.getInstance().next2UI(this,FilterActivity.class);
    }
    public void go2Combine(View view){
        IntentUtil.getInstance().next2UI(this,CombineActivity.class);
    }
    public void go2ErrorHandle(View view){
        IntentUtil.getInstance().next2UI(this,ErrorHandleActivity.class);
    }
    public void go2help(View view){
        IntentUtil.getInstance().next2UI(this,HelpActivity.class);
    }
    public void go2condition(View view){
        IntentUtil.getInstance().next2UI(this,ConditionActivity.class);
    }
    public void go2math(View view){
        IntentUtil.getInstance().next2UI(this,MathActivity.class);
    }
    public void go2connect(View view){
        IntentUtil.getInstance().next2UI(this,ConnectActivity.class);
    }
    public void go2subject(View view){
        IntentUtil.getInstance().next2UI(this,SubjectActivity.class);
    }
}
