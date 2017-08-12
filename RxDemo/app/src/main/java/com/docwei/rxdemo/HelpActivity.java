package com.docwei.rxdemo;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

import static rx.Observable.from;
import static rx.Observable.interval;

/**
 * Created by tobo on 17/8/10.
 */

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void delay(final View view) {
        //使用interval第一次发射也延迟多少秒
        interval(5, TimeUnit.SECONDS).take(3).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return "a--" + aLong + "--a";
            }

        })
                //.delay(5000,TimeUnit.MILLISECONDS)
                /*.delay(new Func0<Observable<String>>() {
                    @Override
                    public Observable<String> call() {
                        //订阅延迟
                        return Observable.timer(4,TimeUnit.SECONDS).flatMap(new Func1<Long, Observable<String>>() {
                            @Override
                            public Observable<String> call(Long aLong) {
                                return Observable.just(aLong+"");
                            }
                        });
                    }
                },new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String s) {
                        System.out.println("s--->"+s);
                        //发射延迟
                        return Observable.timer(5, TimeUnit.SECONDS).flatMap(new Func1<Long, Observable<String>>() {
                            @Override
                            public Observable<String> call(Long aLong) {
                                return Observable.just(s);
                            }
                        });
                    }
                })*/
                /*.delaySubscription(new Func0<Observable<Long>>() {
                    @Override
                    public Observable<Long> call() {
                        return Observable.timer(10, TimeUnit.SECONDS);
                    }
                })*/
                //订阅延迟
                .delaySubscription(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
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
                        Toast.makeText(HelpActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void doOnEach(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        interval(1, TimeUnit.SECONDS).take(5).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return list.get(aLong.intValue());
            }
        })
                //每发射一次数据就会回调
                .doOnEach(new Action1<Notification<? super String>>() {
                    @Override
                    public void call(Notification<? super String> notification) {
                        System.out.println("notification.getKind()--->" + notification.getKind());
                        if (notification.isOnNext()) {
                            System.out.println("notification.getValue()--->" + notification.getValue());
                        }
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
                        Toast.makeText(HelpActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void doOnNext(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        interval(1, TimeUnit.SECONDS).take(5).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return list.get(aLong.intValue());
            }
        })
                //每发射一次数据就会回调
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //接受每一次发射的数据项,  可以对数据进行处理,如果不符合要求直接完成或者走抛异常走错误
                        char schar = s.charAt(0);
                        if (schar > 'd') {
                            throw new RuntimeException("to many items");
                        }
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
                        Toast.makeText(HelpActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void doOnSubscribe(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        interval(1, TimeUnit.SECONDS).take(5).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return list.get(aLong.intValue());
            }
        })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("sssssssssssssssssssssssssss");
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
                        Toast.makeText(HelpActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void doOnUnsubscribe(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        final Subscriber<String> subscriber = new Subscriber<String>() {
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
                Toast.makeText(HelpActivity.this, s, Toast.LENGTH_SHORT).show();

            }
        };
        Observable.interval(1, TimeUnit.SECONDS).take(5).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return list.get(aLong.intValue());
            }
        })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //接受每一次发射的数据项,  可以对数据进行处理,如果不符合要求直接完成或者走抛异常走错误
                        char schar = s.charAt(0);
                        if (schar > 'd') {
                            subscriber.unsubscribe();
                        }
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("取消订阅之后调用");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void doOnCompleted(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        final Subscriber<String> subscriber = new Subscriber<String>() {
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
                Toast.makeText(HelpActivity.this, s, Toast.LENGTH_SHORT).show();

            }
        };
        Observable.interval(1, TimeUnit.SECONDS).take(5).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return list.get(aLong.intValue());
            }
        })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("完成之后会回调啊");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void materialize(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");list.add("b");list.add("c");list.add("d");list.add("e");
        Observable.interval(1, TimeUnit.SECONDS).take(5).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                if(aLong==3){
                    throw new RuntimeException("to many items");
                }
                return list.get(aLong.intValue());
            }
        })
                .materialize()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Notification<String>>() {
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onNext(Notification<String> notification) {
                        System.out.println("kind---"+notification.getKind());
                        System.out.println("value---"+notification.getValue());
                        //完成或者终止的通知因为本身是通知,所以也走这里,但是完成或者错误的值为null
                        Toast.makeText(HelpActivity.this, notification.getValue(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void dematerialize(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");list.add("b");list.add("c");list.add("d");list.add("e");
        Observable.interval(1, TimeUnit.SECONDS).take(5).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                if(aLong==3){
                    throw new RuntimeException("to many items");
                }
                return list.get(aLong.intValue());
            }
        })
                .materialize()
                .dematerialize()
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
                    public void onNext(Object s) {
                        System.out.println(s.toString());
                        //这里如果弹吐司,可能超出上限50个,就显得有点慢
                        Toast.makeText(HelpActivity.this, s.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void timeInterval(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");list.add("b");list.add("c");list.add("d");list.add("e");
        Observable.interval(1,TimeUnit.SECONDS,AndroidSchedulers.mainThread())
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "a"+aLong+"a";
                    }
                })
                .take(5)
                .timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TimeInterval<String>>() {
                    @Override
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onNext(TimeInterval<String> stringTimeInterval) {
                        System.out.println(stringTimeInterval.getValue());
                        System.out.println(stringTimeInterval.getIntervalInMilliseconds());
                        //这里如果弹吐司,可能超出上限50个,就显得有点慢
                        Toast.makeText(HelpActivity.this, stringTimeInterval.getValue(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void timestamp(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");list.add("b");list.add("c");list.add("d");list.add("e");
        Observable.interval(1,TimeUnit.SECONDS,AndroidSchedulers.mainThread())
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return "a"+aLong+"a";
                    }
                })
                .take(5)
                .timestamp()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Timestamped<String>>() {
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Timestamped<String> stringTimestamped) {
                        System.out.println(stringTimestamped.getValue());
                        System.out.println(stringTimestamped.getTimestampMillis());
                        //这里如果弹吐司,可能超出上限50个,就显得有点慢
                        Toast.makeText(HelpActivity.this, stringTimestamped.getValue(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void timeout(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");list.add("b");list.add("c");list.add("d");list.add("e");
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> observer) {
                //没有被订阅就不要发射数据
                //在传递给create方法的函数中检查观察者的isUnsubscribed状态，以便在没有观察者的时候，让你的Observable停止发射数据或者做昂贵的运算
                try {
                    if (!observer.isUnsubscribed()) {
                        for (int i = 0; i < 100; i++) {
                            Thread.sleep(i * 250);
                            observer.onNext(i + "");
                        }
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    observer.onError(e);
                }
            }
        })      .subscribeOn(Schedulers.computation())
                //.timeout(2, TimeUnit.SECONDS)
                //超时之后,不走错误通知,走我指定的Observable
                //.timeout(2, TimeUnit.SECONDS,Observable.from(list))
                //当这个Observable终止时原始Observable还没有发射另一项数据，就会认为是超时
               /* .timeout(new Func1<String, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(String s) {
                        return Observable.interval(2000,TimeUnit.MILLISECONDS).take(1);
                    }
                })*/
                //Func0????  Func1?????
                .timeout(new Func0<Observable<Long>>() {
                    @Override
                    public Observable<Long> call() {
                        return Observable.timer(2,TimeUnit.SECONDS);
                    }
                }, new Func1<String, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(String s) {
                        return Observable.timer(5,TimeUnit.SECONDS);
                    }
                }, from(list))

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
                    public void onNext(String s) {
                        System.out.println(s);
                        //这里如果弹吐司,可能超出上限50个,就显得有点慢
                        Toast.makeText(HelpActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void using(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");list.add("b");list.add("c");list.add("d");list.add("e");
        Observable.using(new Func0<File>() {
            @Override
            public File call() {
                File file = new File(getCacheDir(), "a.txt");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                        System.out.println("创建文件");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return file;
            }
        }, new Func1<File, Observable<Long>>() {
            @Override
            public Observable<Long> call(File file) {

                return Observable.timer(10, TimeUnit.SECONDS);
            }
        }, new Action1<File>() {
            @Override
            public void call(File file) {
                if (file != null && file.exists()) {
                    System.out.println("删除文件");
                    file.delete();
                }

            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Object s) {
                        System.out.println(s.toString());
                        //这里如果弹吐司,可能超出上限50个,就显得有点慢
                        Toast.makeText(HelpActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void getIterator(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");list.add("b");list.add("c");list.add("d");list.add("e");
        Iterator<String> iterator =  from(list)
                .take(4).subscribeOn(Schedulers.io())
                .toBlocking()
                .getIterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
    public void toFuture(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");list.add("b");list.add("c");list.add("d");list.add("e");
        Future<List<String>> future=Observable.from(list)
                .take(4).subscribeOn(Schedulers.io())
                .toList()
                .toBlocking()
                .toFuture();
        try {
            //java.lang.IllegalArgumentException: Sequence contains too many elements
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void toList(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");list.add("b");list.add("c");list.add("d");list.add("e");
        //from逐个发射
         Observable.from(list)
                .take(4).subscribeOn(Schedulers.io())
                 //弄成一个集合一次性发射
                .toList()
                .subscribeOn(AndroidSchedulers.mainThread())
               .subscribe(new Subscriber<List<String>>() {
                   public void onCompleted() {
                       Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                   }
                   @Override
                   public void onError(Throwable e) {
                       Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();

                   }
                   @Override
                   public void onNext(List<String> list) {
                       System.out.println(list.toString());
                       //这里如果弹吐司,可能超出上限50个,就显得有点慢
                       Toast.makeText(HelpActivity.this, list.toString(), Toast.LENGTH_SHORT).show();

                   }
               });

    }
    public void toMap(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");list.add("b");list.add("c");list.add("d");list.add("e");
        //from逐个发射
        Observable.from(list)
                .take(4).subscribeOn(Schedulers.io())
                /*.toMap(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        //返回值就是key啊
                        return "a"+s;
                    }
                })*/
                .toMap(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        //键
                        return "key"+s;
                    }
                }, new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        //值
                        return "value"+s;
                    }
                }, new Func0<Map<String, String>>() {
                    @Override
                    public Map<String, String> call() {
                       //默认是HashMap,,这里创建LinkedHashMap实例使用它来存储数据
                        return new LinkedHashMap<String, String>();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, String>>() {
                    @Override
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Map<String, String> map) {
                          for(Map.Entry<String,String> entry:map.entrySet()){
                              System.out.println(entry.getKey()+"---"+entry.getValue());
                              Toast.makeText(HelpActivity.this, entry.getKey()+"---"+entry.getValue(), Toast.LENGTH_SHORT).show();
                          }
                    }
                });

    }
    public void toMultiMap(final View view) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("a");list.add("b");list.add("c");list.add("d");list.add("e");
        //from逐个发射
        Observable.from(list)
                .take(4).subscribeOn(Schedulers.io())
                .toMultimap(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return "key-->" + s;
                    }
                }, new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        //值
                        return "value" + s;
                    }
                }, new Func0<Map<String, Collection<String>>>() {
                    @Override
                    public Map<String, Collection<String>> call() {
                        //更换默认的HashMap为LinkedHashMap
                        return new LinkedHashMap<String, Collection<String>>();
                    }
                }, new Func1<String, Collection<String>>() {
                    @Override
                    public Collection<String> call(String s) {
                        //这里的s是key,然后Collection<String>是值
                        System.out.println("s--->"+s);
                        return new ArrayList<String>();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Collection<String>>>() {
                    @Override
                    public void onCompleted() {
                        Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Map<String, Collection<String>> map) {
                        for(Map.Entry<String, Collection<String>> entry:map.entrySet()){
                            System.out.println(entry.getKey()+"---"+entry.getValue());
                            Toast.makeText(HelpActivity.this, entry.getKey()+"---"+entry.getValue(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}

