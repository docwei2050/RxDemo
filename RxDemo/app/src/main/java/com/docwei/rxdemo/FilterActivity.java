package com.docwei.rxdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tobo on 17/8/9.
 */

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    public void throttleWithTimeout(final View view) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 23; i++) {
                    try {
                        Thread.sleep(i * 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext("源头--->" + i);
                }
                subscriber.onCompleted();
            }
        })
                .throttleWithTimeout(2000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
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
                Toast.makeText(FilterActivity.this, String.valueOf(s), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void debounce(final View view) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(i * 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext("源头--->" + i);
                    if (i == 20) {
                        subscriber.unsubscribe();
                    }
                }
                subscriber.onCompleted();
            }
        })
                .debounce(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return Observable.just(s);
                    }
                }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
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
                Toast.makeText(FilterActivity.this, String.valueOf(s), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void distinct(final View view) {
        Integer[] items = {1, 3, 4, 5, 12, 12, 3, 3, 7, 4, 5};

        Observable.from(items)
                .distinct()
                /*.distinct(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        if(integer==3){
                            return "a";
                        }else if(integer==5){
                            return "b";
                        }
                        return "c";
                    }
                })*/
                //.distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer s) {
                System.out.println(s);
                //这里如果弹吐司,可能超出上限50个,就显得有点慢
                Toast.makeText(FilterActivity.this, String.valueOf(s), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void elementAt(final View view) {
        Integer[] items = {1, 3, 4, 5, 12, 12, 3, 3, 7, 4, 5};
        Observable.from(items)
                .elementAtOrDefault(-1,90)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer integer) {
                Toast.makeText(FilterActivity.this, String.valueOf(integer), Toast.LENGTH_SHORT).show();
                System.out.println(integer);
            }
        });
    }

    public void filter(final View view) {
        //Integer[] items = {1, 3, 4, 5, 12, 12, 3, 3, 7, 4, 5};
        ArrayList list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add("a");
        list.add("b");
        Observable.from(list)
                //这里的Boolean参数是固定的啊
                .ofType(Integer.class)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer integer) {
                Toast.makeText(FilterActivity.this, String.valueOf(integer), Toast.LENGTH_SHORT).show();
                System.out.println(integer);
            }
        });
    }
    public void first(final View view) {

        //.first()
                /*.takeFirst(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer s) {
                        return s==14;
                    }
                })*/
        //.single()
                /*.single(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer==12;
                    }
                })*/
        Integer[] items = {13, 4, 5, 12, 12, 3, 3, 7, 4, 5};
        Observable.from(items)
                .singleOrDefault(9)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer s ){
                Toast.makeText(FilterActivity.this, String.valueOf(s), Toast.LENGTH_SHORT).show();
                System.out.println(s);
            }
        });
    }
    public void ignoreElements(final View view) {
        Integer[] items = {13, 4, 5, 12, 12, 3, 3, 7, 4, 5};
        Observable.from(items)
                .ignoreElements()
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer s ){
                Toast.makeText(FilterActivity.this, String.valueOf(s), Toast.LENGTH_SHORT).show();
                System.out.println(s);
            }
        });
    }
    public void last(final View view) {
        Integer[] items = {13, 4, 5, 12, 12, 3, 3, 7, 4, 5};
        Observable.from(items)
                .lastOrDefault(3,new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer==9;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer s ){
                Toast.makeText(FilterActivity.this, String.valueOf(s), Toast.LENGTH_SHORT).show();
                System.out.println(s);
            }
        });
    }
    public void sample(final View view) {
        Integer[] items = {13, 4, 5, 12, 12, 3, 3, 7, 4, 5};
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=0;i<100;i++){
                    try {
                        Thread.sleep(200);
                        subscriber.onNext(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        })     //.sample(3,TimeUnit.SECONDS)
                .sample(Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        for(int i=400;i<500;i++){
                            subscriber.onNext(i+"");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.computation()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer s ){
                Toast.makeText(FilterActivity.this, String.valueOf(s), Toast.LENGTH_SHORT).show();
                System.out.println(s);
            }
        });
    }
    public void skip(final View view) {
        Integer[] items = {13, 4, 5, 12, 12, 3, 3, 7, 4, 5};
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=0;i<100;i++){
                    try {
                        Thread.sleep(500);
                        subscriber.onNext(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        })     .skip(3,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer s ){
                Toast.makeText(FilterActivity.this, String.valueOf(s), Toast.LENGTH_SHORT).show();
                System.out.println(s);
            }
        });
    }
    public void take(final View view) {
        Integer[] items = {13, 4, 5, 12, 12, 3, 3, 7, 4, 5};
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=0;i<100;i++){
                    try {
                        Thread.sleep(500);
                        subscriber.onNext(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        })     .takeLast(3,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer s ){
                Toast.makeText(FilterActivity.this, String.valueOf(s), Toast.LENGTH_SHORT).show();
                System.out.println(s);
            }
        });
    }
    public void takeLastBuffer(final View view) {
        final Integer[] items = {13, 4, 5, 12, 12, 3, 3, 7, 4, 5};
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=0;i<10;i++){
                    try {
                        Thread.sleep(500);
                        subscriber.onNext(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        })     .takeLastBuffer(3,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Integer>>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<Integer> integers) {
                for(int i=0;i<integers.size();i++) {
                    Toast.makeText(FilterActivity.this,String.valueOf(integers.get(i)), Toast.LENGTH_SHORT).show();
                    System.out.println(integers.get(i));
                }
            }
        });
    }
}
