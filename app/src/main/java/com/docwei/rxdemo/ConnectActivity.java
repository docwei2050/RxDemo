package com.docwei.rxdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by tobo on 17/8/12.
 */

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
    }
    public void connect(final View view) {
        //冷的变成热的
        ConnectableObservable<String> observable=Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=0;i<50;i++){
                    try {
                        Thread.sleep(200);
                        subscriber.onNext(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation())
                .sample(500,TimeUnit.MILLISECONDS).map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer+"";
                    }
                }).publish();
                //将一个Observable转换为一个可连接的Observable
               //.publish();
        final Subscriber subscriber1=new Subscriber<String>() {
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
                Toast.makeText(ConnectActivity.this, s, Toast.LENGTH_SHORT).show();

            }
        };
        final Subscriber subscriber2=new Subscriber<String>() {
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
                System.out.println("second--->"+s);
                Toast.makeText(ConnectActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber1);
        observable.observeOn(AndroidSchedulers.mainThread()).delaySubscription(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return Observable.timer(1000, TimeUnit.MILLISECONDS);
            }
        }).subscribe(subscriber2);

        observable.connect();//直到走到这里才开始发射数据呢
        /*observable.connect(new Action1<Subscription>() {
            @Override
            public void call(Subscription subscription) {
                System.out.println(subscriber1);
                System.out.println(subscriber2);
                System.out.println(subscription);
            }
        });*/
    }
    public void publish(final View view) {
        //冷的变成热的
        ConnectableObservable<String> observable=Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=0;i<50;i++){
                    try {
                        Thread.sleep(200);
                        subscriber.onNext(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation())
                .sample(500,TimeUnit.MILLISECONDS).map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer+"";
                    }
                }).publish();
        //将一个Observable转换为一个可连接的Observable
        //.publish();
        final Subscriber subscriber1=new Subscriber<String>() {
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
                Toast.makeText(ConnectActivity.this, s, Toast.LENGTH_SHORT).show();

            }
        };
        final Subscriber subscriber2=new Subscriber<String>() {
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
                System.out.println("second--->"+s);
                Toast.makeText(ConnectActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber1);
        observable.observeOn(AndroidSchedulers.mainThread()).delaySubscription(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return Observable.timer(1000, TimeUnit.MILLISECONDS);
            }
        }).subscribe(subscriber2);

        observable.connect();//直到走到这里才开始发射数据呢
        /*observable.connect(new Action1<Subscription>() {
            @Override
            public void call(Subscription subscription) {
                System.out.println(subscriber1);
                System.out.println(subscriber2);
                System.out.println(subscription);
            }
        });*/
    }
    public void refcount(final View view) {
        //冷的变成热的
        ConnectableObservable<String> connectableObservable=Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=0;i<50;i++){
                    try {
                        Thread.sleep(200);
                        subscriber.onNext(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation())
                .sample(500,TimeUnit.MILLISECONDS).map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer+"";
                    }
                }).publish();
        //将一个Observable转换为一个可连接的Observable
        //.publish();
        //将可连接的Observable转换为一个普通的Observable
        Observable<String> observable=connectableObservable.refCount();

        final Subscriber subscriber1=new Subscriber<String>() {
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
                Toast.makeText(ConnectActivity.this, s, Toast.LENGTH_SHORT).show();

            }
        };
        final Subscriber subscriber2=new Subscriber<String>() {
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
                System.out.println("second--->"+s);
                Toast.makeText(ConnectActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber1);
        observable.observeOn(AndroidSchedulers.mainThread()).delaySubscription(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return Observable.timer(1000, TimeUnit.MILLISECONDS);
            }
        }).subscribe(subscriber2);

       //直到走到这里才开始发射数据呢
        /*observable.connect(new Action1<Subscription>() {
            @Override
            public void call(Subscription subscription) {
                System.out.println(subscriber1);
                System.out.println(subscriber2);
                System.out.println(subscription);
            }
        });*/
    }
    public void replay(final View view) {
        //冷的变成热的
        ConnectableObservable<String> connectableObservable=Observable.interval(500,TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return aLong+"";
            }
        }).take(4).replay(2);

        final Subscriber subscriber1=new Subscriber<String>() {
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
                Toast.makeText(ConnectActivity.this, s, Toast.LENGTH_SHORT).show();

            }
        };
        final Subscriber subscriber2=new Subscriber<String>() {
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
                System.out.println("second--->"+s);
                Toast.makeText(ConnectActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };
        connectableObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber1);
        connectableObservable.observeOn(AndroidSchedulers.mainThread()).delaySubscription(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return Observable.timer(2000, TimeUnit.MILLISECONDS);
            }
        }).subscribe(subscriber2);
        connectableObservable.connect();
    }
}
