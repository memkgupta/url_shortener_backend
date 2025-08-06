package org.url_shortener_mp.analytics_service.dtos;

public enum WindowUnit {
    MINUTE("minute"),
    HOUR("hour"),
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    QUARTER("quarter"),
    YEAR("year");

    private final String mongoUnit;

    WindowUnit(String mongoUnit) {
        this.mongoUnit = mongoUnit;
    }

    public String getMongoUnit() {
        return mongoUnit;
    }

    /**
     * Converts a string to a valid WindowUnit.
     * Throws IllegalArgumentException if invalid.
     */
    public static WindowUnit fromString(String unit) {
        for (WindowUnit wu : WindowUnit.values()) {
            if (wu.mongoUnit.equalsIgnoreCase(unit)) {
                return wu;
            }
        }
        throw new IllegalArgumentException("Invalid window unit: " + unit);
    }
}