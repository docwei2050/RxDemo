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
 * Created by tobo on 17/8/9.
 */

public class CombineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine);
    }

    public void combineLatest(final View view) {
        /*ArrayList<String> items1= new ArrayList<>();
        items1.add("1");items1.add("2");items1.add("3");items1.add("4");
        Observable<String> observable1=Observable.from(items1);
        ArrayList<String> items2= new ArrayList<>();
        items2.add("a");items2.add("b");items2.add("c");items2.add("d");
        Observable<String> observable2=Observable.from(items2);*/
        Observable<String> observable1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 4; i++) {
                    try {
                        Thread.sleep(1000);
                        subscriber.onNext("a" + i + "---");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation());
        Observable<String> observable2 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 6; i++) {
                    try {
                        Thread.sleep(400);
                        subscriber.onNext("---" + "b" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        });
        Observable.combineLatest(observable1, observable2, new Func2<String, String, String>() {
            @Override
            public String call(String s1, String s2) {
                return s1 + s2;
            }
        }).subscribe(new Subscriber<String>() {
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
                Toast.makeText(CombineActivity.this, String.valueOf(s), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void join(final View view) {
        Observable<String> observable1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "a" + aLong + "---";
                    }
                }).take(6);
        Observable<String> observable2 = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "--" + "b" + aLong;
                    }
                }).take(5);
        observable1.join(observable2, new Func1<String, Observable<Long>>() {
            @Override
            public Observable<Long> call(String s) {
                return Observable.timer(1, TimeUnit.SECONDS);
            }
        }, new Func1<String, Observable<Long>>() {
            @Override
            public Observable<Long> call(String s) {
                return Observable.timer(0, TimeUnit.SECONDS);
            }
        }, new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s + s2;
            }
        })
                .subscribeOn(Schedulers.io())
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
                        Toast.makeText(CombineActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void merge(final View view) {
        Observable<String> observable1 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "a" + aLong + "---";
                    }
                }).take(6);
        Observable<String> observable2 = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "--" + "b" + aLong;
                    }
                }).take(5);
        Observable.merge(observable1,observable2)
                .subscribeOn(Schedulers.io())
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
                        Toast.makeText(CombineActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void startwith(final View view) {
        Observable<String> observable1 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "a" + aLong + "---";
                    }
                }).take(6);
        Observable<String> observable2 = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "--" + "b" + aLong;
                    }
                }).take(5);
        observable1.startWith(observable2)
                .subscribeOn(Schedulers.io())
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
                        Toast.makeText(CombineActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void concat(final View view) {
        Observable<String> observable1 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "a" + aLong + "---";
                    }
                }).take(6);
        Observable<String> observable2 = Observable.interval(1, TimeUnit.SECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "--" + "b" + aLong;
                    }
                }).take(5);
        Observable.concat(observable1, observable2)
                .subscribeOn(Schedulers.io())
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
                        Toast.makeText(CombineActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void switchOnNext(final View view) {
        Observable<Observable<String>> observable1 = Observable.interval(600, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Observable<String>>() {
                         @Override
                         public Observable<String> call(Long aLong) {
                             return Observable.timer(0, 200, TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
                                 @Override
                                 public String call(Long aLong) {
                                     return aLong * 10 + "";
                                 }
                             }).take(4);
                         }
                     }
                ).take(6);

        Observable.switchOnNext(observable1)
                .subscribeOn(Schedulers.io())
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
                        Toast.makeText(CombineActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void zip(final View view) {
        Observable<String> observable1 = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "a" + aLong + "---";
                    }
                }).take(7);
        Observable<String> observable2 = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "--" + "b" + aLong;
                    }
                }).take(5);
        Observable.zip(observable1, observable2, new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s+s2;
            }
        })
                .subscribeOn(Schedulers.io())
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
                        Toast.makeText(CombineActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
