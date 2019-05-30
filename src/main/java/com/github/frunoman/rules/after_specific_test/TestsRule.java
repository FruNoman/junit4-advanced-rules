package com.github.frunoman.rules.after_specific_test;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestsRule extends TestWatcher {

    private Object testClassInstance;

    public TestsRule(final Object testClassInstance) {
        this.testClassInstance = testClassInstance;
    }

    @Override
    protected void starting(Description description) {
        invokeBeforeHackMethods(description);
    }

    @Override
    protected void succeeded(Description description) {
        invokeAfterHackMethods(description);
    }

    protected void failed(Throwable e, Description description) {
        invokeAfterHackMethods(description);
    }

    public void invokeAfterHackMethods(Description description) {
        for (Method specificMethod : this.getAfterSpecificMethods(this.testClassInstance.getClass())) {
            String[] methods = specificMethod.getAnnotation(AfterTests.class).test();
            for (String name : methods) {
                if (description.getMethodName().contains(name)) {
                    try {
                        specificMethod.invoke(this.testClassInstance);
                    } catch (IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException e) {
                        throw new RuntimeException("error while invoking afterHackMethod "
                                + specificMethod);
                    }
                }
            }
        }
    }

    public void invokeBeforeHackMethods(Description description) {
        for (Method specificMethod : this.getBeforeSpecificMethods(this.testClassInstance.getClass())) {
            String[] methods = specificMethod.getAnnotation(BeforeTests.class).test();
            for (String name : methods) {
                if (description.getMethodName().contains(name)) {
                    try {
                        specificMethod.invoke(this.testClassInstance);
                    } catch (IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException e) {
                        throw new RuntimeException("error while invoking beforeHackMethod "
                                + specificMethod);
                    }
                }
            }
        }
    }

    public List<Method> getBeforeSpecificMethods(Class<?> testClass) {
        List<Method> results = new ArrayList<>();
        for (Method method : testClass.getMethods()) {
            if (method.isAnnotationPresent(AfterTests.class)) {
                results.add(method);
            }
        }
        return results;
    }

    public List<Method> getAfterSpecificMethods(Class<?> testClass) {
        List<Method> results = new ArrayList<>();
        for (Method method : testClass.getMethods()) {
            if (method.isAnnotationPresent(AfterTests.class)) {
                results.add(method);
            }
        }
        return results;
    }
}