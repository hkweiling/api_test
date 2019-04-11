package com.rest.autotest.listener;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @description:实现IAnnotationTransformer接口，修改@Test的retryAnalyzer属性
 * @author:
 * @time: 2019/4/2 9:37
 */
public class FailedRetryListener implements IAnnotationTransformer {
    public void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor, Method method) {
        {
            IRetryAnalyzer retry = iTestAnnotation.getRetryAnalyzer();
            if (retry == null) {
                iTestAnnotation.setRetryAnalyzer(FailedRetry.class);
            }
        }
    }
}
