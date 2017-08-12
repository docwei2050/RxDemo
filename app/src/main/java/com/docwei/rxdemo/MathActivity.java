package com.docwei.rxdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by tobo on 17/8/12.
 */

public class MathActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
    }
    public void count(final View view) {
        final String[] array1={"a1", "b1", "c1", "e1", "f1", "g1", "h1", "a1", "n1"};
        Observable.interval(500, TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return array1[aLong.intValue()];
            }
        }).take(9)
                .count()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Integer value) {
                        System.out.println(value);
                        Toast.makeText(MathActivity.this, String.valueOf(value), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void reduce(final View view) {
        final String[] array1={"a1", "b1", "c1", "e1", "f1", "g1", "h1", "a1", "n1"};
        Observable.interval(500, TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return array1[aLong.intValue()];
            }
        }).take(9)
                .reduce("seed argus",new Func2<String, String, String>() {
                    @Override
                    public String call(String s, String s2) {
                        return s+s2;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onNext(String value) {
                        System.out.println(value);
                        Toast.makeText(MathActivity.this, value, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void collect(final View view) {
        final String[] array1 = {"a1", "b1", "c1", "e1", "f1", "g1", "h1", "a1", "n1"};
        Observable.interval(500, TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return array1[aLong.intValue()];
            }
        }).take(9)
                .collect(new Func0<ArrayList<String>>() {
                    @Override
                    public ArrayList<String> call() {
                        return new ArrayList<String>();
                    }
                }, new Action2<ArrayList<String>, String>() {
                    @Override
                    public void call(ArrayList<String> list, String s) {
                        list.add(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<String>>() {
                    @Override
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onNext(ArrayList<String> list) {
                        for(int i=0;i<list.size();i++){
                            System.out.println(list.get(i));
                            Toast.makeText(MathActivity.this, list.get(i), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}