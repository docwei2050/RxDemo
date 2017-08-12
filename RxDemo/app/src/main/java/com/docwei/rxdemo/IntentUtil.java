package com.docwei.rxdemo;

import android.content.Context;
import android.content.Intent;

/**
 * Created by tobo on 17/8/8.
 */

public class IntentUtil {
    private IntentUtil(){}
    public static IntentUtil getInstance(){
        return IntentUtilHolder.intentUtil;
    }
    private static class IntentUtilHolder{
        private static final IntentUtil intentUtil=new IntentUtil();
    }
    public void next2UI(Context packageContext, Class<?> cls){
        Intent intent=new Intent(packageContext,cls);
        packageContext.startActivity(intent);
    }
}
