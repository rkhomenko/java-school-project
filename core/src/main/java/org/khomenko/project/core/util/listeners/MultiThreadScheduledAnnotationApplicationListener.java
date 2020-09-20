package org.khomenko.project.core.util.listeners;

import lombok.SneakyThrows;

import org.khomenko.project.core.util.annotations.EnableMultiThreadScheduled;
import org.khomenko.project.core.util.annotations.MultiThreadScheduled;
import org.khomenko.project.core.util.internal.MultiThreadScheduledAnnotationData;
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
public class MultiThreadScheduledAnnotationApplicationListener {
    @Autowired
    ScheduledTaskContainer scheduledTaskContainer;

    @Autowired
    ConfigurableListableBeanFactory beanFactory;

    @SneakyThrows
    private void addScheduledTaskForBean(String beanName, Object bean) {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
        Class<?> clazz = beanFactory.getBeanClassLoader().loadClass(beanDefinition.getBeanClassName());

        Map<String, MultiThreadScheduledAnnotationData> annotatedMethods = new HashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MultiThreadScheduled.class)) {
                MultiThreadScheduled annotation = method.getAnnotation(MultiThreadScheduled.class);
                annotatedMethods.put(method.getName(), MultiThreadScheduledAnnotationData.of(annotation));
            }
        }

        for (Map.Entry<String, MultiThreadScheduledAnnotationData> entry : annotatedMethods.entrySet()) {
            Method method = bean.getClass().getMethod(entry.getKey());

            MultiThreadScheduledAnnotationData annotationData = entry.getValue();
            for (int i = 0; i < annotationData.getThreads(); i++) {
                scheduledTaskContainer.add(bean, () -> {
                    try {
                        method.invoke(bean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }, annotationData.getInitDelay(), annotationData.getPeriod(), annotationData.getUnit());
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
