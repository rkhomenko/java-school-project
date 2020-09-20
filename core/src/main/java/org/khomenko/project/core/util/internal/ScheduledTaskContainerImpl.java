package org.khomenko.project.core.util.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledTaskContainerImpl implements ScheduledTaskContainer {
    private final ScheduledExecutorService executorService;
    private final Map<Object, List<ScheduledFuture<?>>> beanTasks = new HashMap<>();

    public ScheduledTaskContainerImpl(@Value("${my.multiThreadScheduled.threads}") int threads) {
        executorService = Executors.newScheduledThreadPool(threads);
    }

    @Override
    public void add(Object bean, Runnable runnable, int initDelay, int period, TimeUnit unit) {
        List<ScheduledFuture<?>> scheduledFutures;
        if (beanTasks.containsKey(bean)) {
            scheduledFutures = beanTasks.get(bean);
        } else {
            scheduledFutures = new ArrayList<>();
        }

        scheduledFutures.add(executorService.scheduleAtFixedRate(runnable, initDelay, period, unit));
        beanTasks.put(bean, scheduledFutures);
    }

    @Override
    public boolean containsTasksForBean(Object bean) {
        return beanTasks.containsKey(bean);
    }

    @Override
    public void cancelBeanTasks(Object bean) {
        if (!containsTasksForBean(bean)) {
            return;
        }

        List<ScheduledFuture<?>> scheduledFutures = beanTasks.get(bean);
        for (ScheduledFuture<?> scheduledFuture : scheduledFutures) {
            scheduledFuture.cancel(true);
        }

        beanTasks.remove(bean);
    }
}
