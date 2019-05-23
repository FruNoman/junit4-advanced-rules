package com.github.frunoman.rules.after_specific_test;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AfterSpecificTestRule extends TestWatcher {

    private Object testClassInstance;

    public AfterSpecificTestRule(final Object testClassInstance) {
        this.testClassInstance = testClassInstance;
    }

    @Override
    protected void succeeded(Description description) {
        invokeAfterHackMethods();
    }

    protected void failed(Throwable e, Description description) {
        invokeAfterHackMethods();
    }

    public void invokeAfterHackMethods() {
        for (Method afterHackMethod :
                this.getAfterHackMethods(this.testClassInstance.getClass())) {
            try {
                afterHackMethod.invoke(this.testClassInstance);
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new RuntimeException("error while invoking afterHackMethod "
                        + afterHackMethod);
            }
        }
    }

    private List<Method> getAfterHackMethods(Class<?> testClass) {
        List<Method> results = new ArrayList<>();
        for (Method method : testClass.getMethods()) {
            if (method.isAnnotationPresent(AfterSpecificTest.class)) {
                String[] tests = method.getAnnotation(AfterSpecificTest.class).test();
                for (String test : tests) {
                    if (method.getName().equals(test)) {
                        results.add(method);
                    }
                }
            }
        }

        return results;
    }
}