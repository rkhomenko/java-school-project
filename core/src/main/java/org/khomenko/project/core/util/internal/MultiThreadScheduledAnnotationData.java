package org.khomenko.project.core.util.internal;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.khomenko.project.core.util.annotations.MultiThreadScheduled;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
@Builder
public class MultiThreadScheduledAnnotationData {
    private final int threads;
    private final int initDelay;
    private final int period;
    private final TimeUnit unit;

    public static MultiThreadScheduledAnnotationData of(MultiThreadScheduled annotation) {
        var builder =  MultiThreadScheduledAnnotationData.builder()
                .threads(annotation.threads())
                .unit(annotation.unit());

        if (annotation.initDelayVar() != 0) {
            int initDelayVar = annotation.initDelayVar();
            int delay = annotation.initDelay();
            builder.initDelay(ThreadLocalRandom.current().nextInt(delay - initDelayVar, delay + initDelayVar));
        } else {
            builder.initDelay(annotation.initDelay());
        }

        if (annotation.periodVar() != 0) {
            int periodVar = annotation.periodVar();
            int period = annotation.period();
            builder.period(ThreadLocalRandom.current().nextInt(period - periodVar, period + periodVar));
        } else {
            builder.period(annotation.period());
        }

        return builder.build();
    }
}
