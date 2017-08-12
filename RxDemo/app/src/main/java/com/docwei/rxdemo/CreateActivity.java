package com.docwei.rxdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by tobo on 17/8/8.
 */

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

    /**
     * create
     * @param view
     */
    public void create(final View view){
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> observer) {
                //没有被订阅就不要发射数据
                //在传递给create方法的函数中检查观察者的isUnsubscribed状态，以便在没有观察者的时候，让你的Observable停止发射数据或者做昂贵的运算
                try {
                    if (!observer.isUnsubscribed()) {
                        for (int i = 0; i < 100; i++) {
                            observer.onNext(i);
                        }
                        observer.onCompleted();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    observer.onError(e);
                }
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view,"通知完成", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view,"通知完成",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
                //这里如果弹吐司,可能超出上限50个,就显得有点慢
                Toast.makeText(CreateActivity.this,String.valueOf(integer),Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     *  from 引入Future参数
     * @param view
     */
    public void from_Future(final View view){
        ExecutorService exec= Executors.newFixedThreadPool(6);
        //Callable有返回值,可以抛异常
        Callable<Integer[]> task=new Callable<Integer[]>() {
            @Override
            public Integer[] call() throws Exception {
                Thread.sleep(4000);
                Integer[] items=new Integer[100];
                for(int i=0;i<100;i++){
                    items[i]=i;
                }
                return items;
            }
        };
        Future<Integer[]> future=exec.submit(task);

        Observable.from(future).subscribe(new Subscriber<Integer[]>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view,"通知完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view,"通知完成"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer[] items) {
                for(int i=0;i<items.length;i++){
                    System.out.println(items[i]);
                    //这里如果弹吐司,可能超出上限50个,就显得有点慢
                    Toast.makeText(CreateActivity.this,String.valueOf(items[i]),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     *  from 引入数组参数
     * @param view
     */
    public void from_arrays(final View view){
        Integer[] items={2,5,57,8,9,0,8,9,2,34,56}; //不是int数据哦  T[]array
        //工具很智能的添加参数
        Observable.from(items).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println(integer);
                //这里如果弹吐司,可能超出上限50个,就显得有点慢
                Toast.makeText(CreateActivity.this,String.valueOf(integer),Toast.LENGTH_SHORT).show();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Snackbar.make(view,"通知完成"+throwable.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }, new Action0() {
            @Override
            public void call() {
                Snackbar.make(view,"通知完成",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     *  from 引入Iterable参数
     * @param view
     */
    public void from_Iterable(final View view){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);list.add(3); list.add(4); list.add(5); list.add(7);
        Observable.from(list).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view,"通知完成",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view,"通知完成",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
                //这里如果弹吐司,可能超出上限50个,就显得有点慢
                Toast.makeText(CreateActivity.this,String.valueOf(integer),Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     *  interval 定时发送一个整数序列  。。。可以做定时器啊
     * @param view
     */
    public void interval(final View view){
        Observable.interval(3, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view,"通知完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view,"通知完成"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
                //这里如果弹吐司,可能超出上限50个,就显得有点慢
                Toast.makeText(CreateActivity.this,String.valueOf(aLong),Toast.LENGTH_SHORT).show();

            }
        });
    }
    /**
     *  just  一次性发射所有的,而不是一个一个发射,,当然你的参数多个,那就多个发射
     * @param view
     */
    public void just(final View view){
        int[] items=new int[]{1,2,3,4,45,6,7,8,8,8,45,67,89};
        Observable.just(items).subscribe(new Subscriber<int[]>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view,"通知完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view,"通知完成"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(int[] items) {
                for(int i=0;i<items.length;i++){
                    System.out.println(items[i]);
                    //这里如果弹吐司,可能超出上限50个,就显得有点慢
                    Toast.makeText(CreateActivity.this,String.valueOf(items[i]),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }/**
     *  range  发射一个范围内的有序整数序列，你可以指定范围的起始和长度。
     * @param view
     */
    public void range(final View view){
        Observable.range(1,10).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view,"通知完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view,"通知完成"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
                Toast.makeText(CreateActivity.this,String.valueOf(integer),Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     *  repeat  创建一个发射特定数据重复多次的Observable
     * @param view
     */
    public void repeat(final View view){
        Observable.range(0,10).repeat(5).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view,"通知完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view,"通知完成"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
                Toast.makeText(CreateActivity.this,String.valueOf(integer),Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * timer  它在一个给定的延迟后发射一个特殊的值。这里是0 啊
     * @param view
     */
    public void timer(final View view){
        System.out.println("hahaha");
        Observable.timer(3,TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view,"通知完成",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view,"通知完成"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
                Toast.makeText(CreateActivity.this,String.valueOf(aLong),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

