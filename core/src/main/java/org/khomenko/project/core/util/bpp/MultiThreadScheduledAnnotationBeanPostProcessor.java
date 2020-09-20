package org.khomenko.project.core.util.bpp;

import lombok.extern.slf4j.Slf4j;
import org.khomenko.project.core.util.internal.ScheduledTaskContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MultiThreadScheduledAnnotationBeanPostProcessor implements DestructionAwareBeanPostProcessor {
    @Autowired
    ScheduledTaskContainer scheduledTaskContainer;

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) {
        log.info("Cancel execution for bean {}", beanName);

        scheduledTaskContainer.cancelBeanTasks(bean);
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        return scheduledTaskContainer.containsTasksForBean(bean);
    }
}
