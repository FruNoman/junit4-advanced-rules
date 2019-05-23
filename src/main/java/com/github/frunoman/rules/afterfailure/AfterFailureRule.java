package com.github.frunoman.rules.afterfailure;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AfterFailureRule extends TestWatcher {

    private Object testClassInstance;

    public AfterFailureRule(final Object testClassInstance) {
        this.testClassInstance = testClassInstance;
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
            if (method.isAnnotationPresent(AfterFailure.class)) {
                results.add(method);
            }
        }
        return results;
    }
}