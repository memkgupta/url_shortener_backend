package com.url_shortner.shortner.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class MonthTimestamps {

    // Get start of the current month
    public static Timestamp getStartOfCurrentMonth() {
        LocalDateTime startOfMonthLdt = LocalDateTime.now()
                .withDayOfMonth(1)
                .toLocalDate()
                .atStartOfDay();
        return Timestamp.valueOf(startOfMonthLdt);
    }

    // Get end of the current month
    public static Timestamp getEndOfCurrentMonth() {
        LocalDateTime endOfMonthLdt = LocalDateTime.now()
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        return Timestamp.valueOf(endOfMonthLdt);
    }

    // Get start of the last month
    public static Timestamp getStartOfLastMonth() {
        LocalDateTime startOfLastMonthLdt = LocalDateTime.now()
                .minusMonths(1)
                .withDayOfMonth(1)
                .toLocalDate()
                .atStartOfDay();
        return Timestamp.valueOf(startOfLastMonthLdt);
    }

    // Get end of the last month
    public static Timestamp getEndOfLastMonth() {
        LocalDateTime endOfLastMonthLdt = LocalDateTime.now()
                .minusMonths(1)
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        return Timestamp.valueOf(endOfLastMonthLdt);
    }

    public static Timestamp getStartOfToday() {
        LocalDateTime startOfToday = LocalDateTime.now()
                .toLocalDate()
                .atStartOfDay();
        return Timestamp.valueOf(startOfToday);
    }

    // ✅ End of today
    public static Timestamp getEndOfToday() {
        LocalDateTime endOfToday = LocalDateTime.now()
                .withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        return Timestamp.valueOf(endOfToday);
    }

    // ✅ Start of previous day
    public static Timestamp getStartOfPreviousDay() {
        LocalDateTime startOfPrevDay = LocalDateTime.now()
                .minusDays(1)
                .toLocalDate()
                .atStartOfDay();
        return Timestamp.valueOf(startOfPrevDay);
    }

    // ✅ End of previous day
    public static Timestamp getEndOfPreviousDay() {
        LocalDateTime endOfPrevDay = LocalDateTime.now()
                .minusDays(1)
                .withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        return Timestamp.valueOf(endOfPrevDay);
    }

}