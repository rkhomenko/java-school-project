package org.khomenko.project.core.util.internal;

import java.util.concurrent.TimeUnit;

public interface ScheduledTaskContainer {
    void add(Object bean, Runnable runnable, int initDelay, int period, TimeUnit unit);
    boolean containsTasksForBean(Object bean);
    void cancelBeanTasks(Object bean);
}
