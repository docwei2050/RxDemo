package com.docwei.rxdemo.changeTransform;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.docwei.rxdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * Created by tobo on 17/8/8.
 */

public class ChangeTransformActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changetransform);
    }

    /**
     * 定期和定量发送数据给,什么close open完全不知道是啥意思
     *
     * @param view
     */
    public void buffer(final View view) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    for (int i = 100; i < 200; i++) {
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        subscriber.onNext("源头---->" + i);
                    }
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .buffer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<String> items) {
                for (int i = 0; i < items.size(); i++) {
                    System.out.println(items.get(i));
                    //这里如果弹吐司,可能超出上限50个,就显得有点慢
                    Toast.makeText(ChangeTransformActivity.this, items.get(i), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * flatMap
     *
     * @param view
     */
    public void flatMap(final View view) {

        ArrayList<Student> list = new ArrayList<>();
        final ArrayList<Course> courselist = new ArrayList<>();
        Course course1 = new Course("神雕侠侣", "16");
        Course course2 = new Course("仙剑", "189");
        courselist.add(course1);
        courselist.add(course2);
        Student sd1 = new Student("厘米", "19", courselist);

        ArrayList<Course> courselist2 = new ArrayList<>();
        Course course3 = new Course("琅琊榜", "13");
        Course course4 = new Course("蝴蝶剑", "189");
        courselist2.add(course3);
        courselist2.add(course4);
        Student sd2 = new Student("分米", "20", courselist2);

        list.add(sd1);
        list.add(sd2);
        //FlatMap 将一个发射数据的Observable变换为多个Observables，然后将它们发射的数据合并
        //后放进一个单独的Observable,
        //我的需求是拿到所有学生的课程名
        //那一一条数据就是一个Stduent
        Observable.from(list)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        //刚好from可以对数据进行逐个发射,那就是可以拿到每一个课程啦
                        return Observable.from(student.mCourses);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Course>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Course course) {
                System.out.println(course.name);
                Toast.makeText(ChangeTransformActivity.this, course.name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * flatMap
     *
     * @param view
     */
    public void switchMap(final View view) {

        ArrayList<Student> list = new ArrayList<>();
        final ArrayList<Course> courselist = new ArrayList<>();
        Course course1 = new Course("神雕侠侣", "16");
        Course course2 = new Course("仙剑", "189");
        courselist.add(course1);
        courselist.add(course2);
        Student sd1 = new Student("厘米", "19", courselist);

        ArrayList<Course> courselist2 = new ArrayList<>();
        Course course3 = new Course("琅琊榜", "13");
        Course course4 = new Course("蝴蝶剑", "189");
        courselist2.add(course3);
        courselist2.add(course4);
        Student sd2 = new Student("分米", "20", courselist2);

        list.add(sd1);
        list.add(sd2);
        //FlatMap 将一个发射数据的Observable变换为多个Observables，然后将它们发射的数据合并
        //后放进一个单独的Observable,
        //我的需求是拿到所有学生的课程名
        //那一一条数据就是一个Stduent
        Observable.from(list)
                .switchMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.mCourses).subscribeOn(Schedulers.newThread());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Course>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Course course) {
                System.out.println(course.name);
                Toast.makeText(ChangeTransformActivity.this, course.name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * concatMap
     *
     * @param view
     */
    public void concatMap(final View view) {
        Integer[] list = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        //FlatMap 将一个发射数据的Observable变换为多个Observables，然后将它们发射的数据合并
        //后放进一个单独的Observable,
        //我的需求是获取到原数据元素值的平方值,,,注意顺序
        Observable.from(list)
                .concatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(final Integer integer) {
                        return Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(Subscriber<? super Integer> subscriber) {
                                subscriber.onNext(integer * integer);
                                subscriber.onCompleted();
                            }
                        }).subscribeOn(Schedulers.io());
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
            public void onNext(Integer integer) {
                Toast.makeText(ChangeTransformActivity.this, String.valueOf(integer), Toast.LENGTH_SHORT).show();
                System.out.println(integer);
            }
        });
    }

    /**
     * groupBy将原始Observable分拆为一些Observables集合，它们中的每一个发射原始Observable数据序列的一个子序列。哪个数据项由哪一个Observable发射是由一个函数判定的，这个函数给每一项指定一个Key，Key相同的数据会被同一个Observable发射。RxJava实现了 groupBy 操作符。它返回Observable的一个特殊子类 GroupedObservable ，实现了 GroupedObservable 接口的对象有一个额外的方法 getKey ，这个Key用于将数据分组到指定的Observable。
     *
     * @param view
     */
    public void groupBy(final View view) {
        String[] list = {"a", "b", "a", "b", "b", "a", "b", "c", "a", "c", "a", "c", "a", "c"};
        Observable.from(list)
                //第一个参数是list的元素类型,这个是已知的
                .groupBy(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        if ("a".equals(s)) {
                            return 1;
                        } else if ("b".equals(s)) {
                            return 2;
                        } else {
                            return 3;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GroupedObservable<Integer, String>>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
                System.out.println("通知完成啊");  //内层的完成这个才能完成
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("通知错误终止");
            }

            @Override
            public void onNext(GroupedObservable<Integer, String> integerStringGroupedObservable) {
                //根据之前的区分的键的不同来获取结果
                switch (integerStringGroupedObservable.getKey()) {
                    case 1:                                   //只需要next事件
                        integerStringGroupedObservable.subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                System.out.println(s);
                            }
                        });
                        break;
                    case 2://只关心next事件
                        integerStringGroupedObservable.subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                System.out.println(s);
                            }
                        });
                        break;
                    case 3://只关心next事件
                        integerStringGroupedObservable.subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                System.out.println(s);
                            }
                        });
                        break;
                }

            }
        });
    }
    /**
     *map
     *
     * @param view
     */
    public void map(final View view) {
        Integer[] list = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        //map
        //我的需求是获取到原数据元素值的平方值,,,注意顺序
        Observable.from(list)
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        //使用一个函数进行变换
                        return integer*integer;
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
            public void onNext(Integer integer) {
                Toast.makeText(ChangeTransformActivity.this, String.valueOf(integer), Toast.LENGTH_SHORT).show();
                System.out.println(integer);
            }
        });

    }
    /**
     * scan
     *
     * @param view
     */
    public void scan(final View view) {
        Integer[] list = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        //map
        //我的需求是获取到原数据元素值的平方值,,,注意顺序
        Observable.from(list)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer sum, Integer item) {
                        return sum+item;
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
            public void onNext(Integer integer) {
                Toast.makeText(ChangeTransformActivity.this, String.valueOf(integer), Toast.LENGTH_SHORT).show();
                System.out.println(integer);
            }
        });

    }
    /**
     * 定期和定量发送数据????跟buffer还是不一样的
     *
     * @param view
     */
    public void window(final View view) {
       /* Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    for (int i = 100; i < 200; i++) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        subscriber.onNext("源头---->" + i);
                    }
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .window(3)
                .subscribe(new Subscriber<Observable<String>>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Observable<String> stringObservable) {
                    stringObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            Toast.makeText(ChangeTransformActivity.this,  s, Toast.LENGTH_SHORT).show();
                            System.out.println( s);
                        }
                    });
            }
        });*/
        Observable.interval(1,TimeUnit.SECONDS).take(10).window(3,TimeUnit.SECONDS).subscribe(new Observer<Observable<Long>>() {
            @Override
            public void onCompleted() {
                Snackbar.make(view, "通知完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(view, "通知错误终止" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Observable<Long> integerObservable) {
                integerObservable.subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long integer) {
                        System.out.println("------>call():" + integer);
                    }
                });
            }
        });
    }

}
