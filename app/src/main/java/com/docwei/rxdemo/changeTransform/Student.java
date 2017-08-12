package com.docwei.rxdemo.changeTransform;

import java.util.List;

/**
 * Created by tobo on 17/8/8.
 */
public class Student {
    public String name;
    public String age;
    public List<Course> mCourses;

    public Student(String name, String age, List<Course> courses) {
        this.name = name;
        this.age = age;
        mCourses = courses;
    }
}
