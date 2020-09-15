package org.khomenko.project.core.data.models;

public enum PriceCategory {
    LOW(1, 50),
    MEDIUM(50, 100),
    HIGH(100, 1000);

    public final int min;
    public final int max;

    private PriceCategory(int min, int max) {
        this.min = min;
        this.max = max;
    }
}
