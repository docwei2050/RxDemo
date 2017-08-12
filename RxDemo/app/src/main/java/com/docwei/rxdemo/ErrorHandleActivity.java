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
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by tobo on 17/8/10.
 */

public class ErrorHandleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errorhandle);
    }
    public void onErrorReturn(final View view){
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                  for(int i=0;i<10;i++){
                      try {
                          Thread.sleep(1000);
                          subscriber.onNext("--"+i+"--");
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                      if(i==6){
                          subscriber.onError(new Exception("出错了哦"));

                      }
                  }
                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, String>() {
                    @Override
                    public String call(Throwable throwable) {
                        return "写这方法就是不让他走onError错误通知啊";
                    }
                })
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
                    public void onNext(String s) {
                        System.out.println(s);
                        //这里如果弹吐司,可能超出上限50个,就显得有点慢
                        Toast.makeText(ErrorHandleActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void onErrorResumeNext(final View view){
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                for(int i=0;i<10;i++){
                    try {
                        Thread.sleep(1000);
                        subscriber.onNext("--"+i+"--");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(i==3){
                        /*try {
                            throw new Exception("抛异常");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        subscriber.onError(new Exception("aaaa"));
                    }
                }
                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.computation())
               .onErrorResumeNext(new Func1<Throwable, Observable<String>>() {
                   @Override
                   public Observable<String> call(Throwable throwable) {
                       //让Observable在遇到错误时开始发射第二个Observable的数据序列。
                       return Observable.interval(1, TimeUnit.SECONDS).map(new Func1<Long, String>() {
                           @Override
                           public String call(Long aLong) {
                               return "a"+aLong+"a";
                           }
                       }).take(5);
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
                    public void onNext(String s) {
                        System.out.println(s);
                        //这里如果弹吐司,可能超出上限50个,就显得有点慢
                        Toast.makeText(ErrorHandleActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void onExceptionResumeNext(final View view){
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for(int i=0;i<10;i++){
                    try {
                        Thread.sleep(1000);
                        subscriber.onNext("--"+i+"--");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(i==3){
                        //抛出自定义异常是不行的
                        /*try {
                            throw new MyException("主动抛异常");
                        } catch (MyException e) {
                            e.printStackTrace();
                        }*/
                        //subscriber.onError(new Error("aaaaa"));
                        //subscriber.onError(new Exception("aaaaa"));
                        throw new NumberFormatException();
                    }
                }
                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.computation())
               .onExceptionResumeNext(Observable.just("xxxxxxx","cccc","sfs"))
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
                    public void onNext(String s) {
                        System.out.println(s);
                        //这里如果弹吐司,可能超出上限50个,就显得有点慢
                        Toast.makeText(ErrorHandleActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void retry(final View view){
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for(int i=0;i<10;i++){
                    try {
                        Thread.sleep(1000);
                        subscriber.onNext("--"+i+"--");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(i==3){
                        subscriber.onError(new Error("aaaaa"));
                    }
                }
                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.computation())
                .retry(new Func2<Integer, Throwable, Boolean>() {
                    @Override
                    public Boolean call(Integer integer, Throwable throwable) {
                        //订阅次数超过3次之后就丢掉/不再重新订阅,就走错误通知了
                        if(integer>3){
                            return false;
                        }
                        return true;
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
                    public void onNext(String s) {
                        System.out.println(s);
                        //这里如果弹吐司,可能超出上限50个,就显得有点慢
                        Toast.makeText(ErrorHandleActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public int retryCount;
    public void retryWhen(final View view){
        //间隔3秒重试,如果重试3次还没有好就走错误通知
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for(int i=0;i<10;i++){
                    try {
                        Thread.sleep(1000);
                        subscriber.onNext("--"+i+"--");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(i==3){
                        if(retryCount<3) {
                            subscriber.onError(new Error("aaaaa"));
                        }
                    }
                }
                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.computation())
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable
                                .flatMap(new Func1<Throwable, Observable<?>>() {
                                    @Override
                                    public Observable<?> call(Throwable throwable) {
                                        //重试如果出错不超过3次就继续走这里重试
                                        if (++retryCount <= 3) {
                                            // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                                            return Observable.timer(3000,
                                                    TimeUnit.MILLISECONDS);
                                        }
                                        // Max retries hit. Just pass the error along.
                                        //3次重试还有错误,就把错误传下去,走错误通知
                                        return Observable.error(throwable);
                                    }
                                });
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
                    public void onNext(String s) {
                        System.out.println(s);
                        //这里如果弹吐司,可能超出上限50个,就显得有点慢
                        Toast.makeText(ErrorHandleActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }











    public static class MyException extends Exception{
        public MyException() {
        }

        public MyException(String message) {
            super(message);
        }
    }
}
