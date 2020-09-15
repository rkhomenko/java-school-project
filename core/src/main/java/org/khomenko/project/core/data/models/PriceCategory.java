package org.khomenko.project.core.data.models;

public enum PriceCategory {
    LOW(1, 50),
    MEDIUM(50, 100),
    HIGH(100, 1000);

    public final int minCost;
    public final int maxCost;

    private PriceCategory(int minCost, int maxCost) {
        this.minCost = minCost;
        this.maxCost = maxCost;
    }
}
