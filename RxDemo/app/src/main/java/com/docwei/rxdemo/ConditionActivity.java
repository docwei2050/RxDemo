package com.docwei.rxdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by tobo on 17/8/11.
 */

public class ConditionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);
    }
    public void all(final View view) {
            Observable.interval(1,TimeUnit.SECONDS).take(5)
                .subscribeOn(Schedulers.io())
                .all(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long item) {
                        return item>4;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        System.out.println(aBoolean);
                        Toast.makeText(ConditionActivity.this,String.valueOf(aBoolean),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void amb(final View view) {
        final String[] array1={"a1", "b1", "c1"};
        final String[] array2={"a2", "b2", "c2"};
        //Integer[] num={1,2,3};
        Observable.interval(500,TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return array1[aLong.intValue()];
            }
        }).take(3)
                //必须跟前一个Observable类型一致
                //相互竞争,谁先发射,只发射谁
                .ambWith(Observable.interval(550,TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return array2[aLong.intValue()];
                    }
                }).take(3))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
               public void onCompleted() {
                   Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
               }
               @Override
               public void onError(Throwable e) {
                   Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
               }
               @Override
               public void onNext(String aLong) {
                   System.out.println(aLong);
                   Toast.makeText(ConditionActivity.this, aLong, Toast.LENGTH_SHORT).show();

               }
           });
    }
    public void contains(final View view) {
        final String[] array1={"a1", "b1", "c1"};
        //Integer[] num={1,2,3};
        Observable.interval(500,TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return array1[aLong.intValue()];
            }
        }).take(3)
                //用来判断发射的数据是否包含了这个元素
                .contains("a1")
               //.isEmpty()//源Observable发射数据是空就会发射true
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        System.out.println(aBoolean);
                        Toast.makeText(ConditionActivity.this, String.valueOf(aBoolean), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void exists(final View view) {
        final String[] array1={"a1", "b1", "c1"};
        Observable.interval(500,TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return array1[aLong.intValue()];
            }
        }).take(3)
                .exists(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s=="b1";
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        System.out.println(aBoolean);
                        Toast.makeText(ConditionActivity.this, String.valueOf(aBoolean), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void defaultIfEmpty(final View view) {
        final String[] array1={"a1", "b1", "c1"};
        //empty将创建一个空的Observale
        Observable.empty()
                //源Observable没有发射任何数据正常终止,就以你提供的默认值"sssssss"作为数据发射
                .defaultIfEmpty("sssssss")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Object o) {
                        System.out.println(o);
                        Toast.makeText(ConditionActivity.this, o.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void sequenceEqual(final View view) {
        final String[] array1={"a1", "b1", "c1"};
        //empty将创建一个空的Observale
        Observable.sequenceEqual(Observable.from(array1), Observable.from(array1), new Func2<Serializable, Serializable, Boolean>() {
            @Override
            public Boolean call(Serializable serializable, Serializable serializable2) {
                return serializable.equals(serializable2);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        System.out.println(aBoolean);
                        Toast.makeText(ConditionActivity.this, String.valueOf(aBoolean), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void skipUntil(final View view) {
        final String[] array1={"a1", "b1", "c1", "e1", "f1", "g1", "h1", "a1", "n1"};
        Observable.interval(500,TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return array1[aLong.intValue()];
            }
        }).take(9)
                .skipUntil(Observable.timer(2000,TimeUnit.MILLISECONDS))
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
                        Toast.makeText(ConditionActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void skipWhile(final View view) {
        final String[] array1={"a1", "b1", "c1", "e1", "f1", "g1", "h1", "a1", "n1"};
        Observable.interval(500,TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return array1[aLong.intValue()];
            }
        }).take(9)
                .skipWhile(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !"c1".equals(s);
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
                        Toast.makeText(ConditionActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void takeUntil(final View view) {
        final String[] array1={"a1", "b1", "c1", "e1", "f1", "g1", "h1", "a1", "n1"};
        Observable.interval(500,TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return array1[aLong.intValue()];
            }
        }).take(9)
                .takeUntil(Observable.timer(2000,TimeUnit.MILLISECONDS))
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
                        Toast.makeText(ConditionActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void takeWhile(final View view) {
        final String[] array1={"a1", "b1", "c1", "e1", "f1", "g1", "h1", "a1", "n1"};
        Observable.interval(500,TimeUnit.MILLISECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return array1[aLong.intValue()];
            }
        }).take(9)
              .takeWhile(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !"c1".equals(s);
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
                        Toast.makeText(ConditionActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
