package org.khomenko.project.core.util.listeners;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.khomenko.project.core.util.annotations.EnableMultiThreadScheduled;
import org.khomenko.project.core.util.annotations.MultiThreadScheduled;
import org.khomenko.project.core.util.internal.ScheduledTaskContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MultiThreadScheduledAnnotationApplicationListener {
    @Autowired
    ScheduledTaskContainer scheduledTaskContainer;

    @Autowired
    ConfigurableListableBeanFactory beanFactory;

    @SneakyThrows
    private void addScheduledTaskForBean(String beanName, Object bean) {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
        Class<?> clazz = beanFactory.getBeanClassLoader().loadClass(beanDefinition.getBeanClassName());

        Map<String, MultiThreadScheduled> annotatedMethods = new HashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MultiThreadScheduled.class)) {
                annotatedMethods.put(method.getName(), method.getAnnotation(MultiThreadScheduled.class));
            }
        }

        for (Map.Entry<String, MultiThreadScheduled> entry : annotatedMethods.entrySet()) {
            Method method = bean.getClass().getMethod(entry.getKey());

            MultiThreadScheduled annotation = entry.getValue();
            for (int i = 0; i < annotation.threads(); i++) {
                scheduledTaskContainer.add(bean, () -> {
                    try {
                        method.invoke(bean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }, annotation.initDelay(), annotation.period(), annotation.unit());
            }
        }
    }

    @EventListener({ContextRefreshedEvent.class})
    public void processRefreshedEvent() {
        for (String beanName : beanFactory.getBeanNamesForAnnotation(EnableMultiThreadScheduled.class)) {
            addScheduledTaskForBean(beanName, beanFactory.getBean(beanName));
        }
    }

    @EventListener({ContextClosedEvent.class})
    public void processClosedEvent() {
        for (String beanName : beanFactory.getBeanNamesForAnnotation(EnableMultiThreadScheduled.class)) {
            scheduledTaskContainer.cancelBeanTasks(beanName);
        }
    }
}
