package com.docwei.rxdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

/**
 * Created by tobo on 17/8/12.
 */

public class SubjectActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
    }
    public void asyncSubject(final View view) {
        //asyncSubject作为Observable
        AsyncSubject<String> asyncSubject=AsyncSubject.create();
        //asyncSubject作为Subscriber
        asyncSubject.onNext("a1");
        asyncSubject.onNext("a2");
        asyncSubject.onNext("a3");
        asyncSubject.onCompleted();
        asyncSubject.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNext(String s) {
                System.out.println("first--->"+s);
                Toast.makeText(SubjectActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void behaviorSubject(final View view) {
        BehaviorSubject<String> behaviorSubject=BehaviorSubject.create();
        behaviorSubject.onNext("a1");
        behaviorSubject.onNext("a2");
        behaviorSubject.onNext("a3");
        behaviorSubject.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNext(String s) {
                System.out.println("first--->"+s);
                Toast.makeText(SubjectActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        behaviorSubject.onNext("a4");
        behaviorSubject.onNext("a5");

    }
    public void publishSubject(final View view) {
        //只能接收到订阅之后的数据,,,订阅前发射的都丢失了
        PublishSubject<String> publishSubject=PublishSubject.create();
        publishSubject.onNext("a1");
        publishSubject.onNext("a2");
        publishSubject.onNext("a3");
        publishSubject.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNext(String s) {
                System.out.println("first--->"+s);
                Toast.makeText(SubjectActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        publishSubject.onNext("a4");
        publishSubject.onNext("a5");

    }
    public void replaySubject(final View view) {
        ReplaySubject<String> replaySubject=ReplaySubject.createWithTimeAndSize(2, TimeUnit.SECONDS,1, Schedulers.io());
        replaySubject.onNext("a1");
        replaySubject.onNext("a2");
        replaySubject.onNext("a3");
        replaySubject.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNext(String s) {
                System.out.println("first--->"+s);
                Toast.makeText(SubjectActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        replaySubject.onNext("a4");
        replaySubject.onNext("a5");

    }
}
