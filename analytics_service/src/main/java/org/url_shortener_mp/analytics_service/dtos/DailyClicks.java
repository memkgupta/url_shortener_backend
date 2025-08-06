package org.url_shortener_mp.analytics_service.dtos;


import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


public class DailyClicks {
    @Field("_id")
    private Date interval;
    private Long totalClicks;

    public Date getInterval() {
        return interval;
    }

    public void setInterval(Date interval) {
        this.interval = interval;
    }

    public Long getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(Long totalClicks) {
        this.totalClicks = totalClicks;
    }
}