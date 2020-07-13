package com.zm.platform.quartz.util;

import com.zm.platform.common.util.EmptyUtil;

import java.lang.reflect.Method;

public class MethodCheck {

    /**
     * 用于定时任务新增修改验证执行方法名
     * @param invokeName 包名.类名.方法名
     * @return
     */
    public static boolean checkHasMethod (String invokeName) {
        if (EmptyUtil.isNotEmpty(invokeName)) {
            int lastPointIndex = invokeName.lastIndexOf(".");
            if(lastPointIndex == -1){
                return false;
            }
            String beanName = invokeName.substring(0, lastPointIndex);
            String methodName = invokeName.substring(lastPointIndex+1);
            try {
                Class<?> clz = Class.forName(beanName);
                Method method = clz.getDeclaredMethod(methodName);
                if (method != null) {
                    return true;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
