package com.url_shortner.shortner.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardOverview {
    private ComparableMetric total_links_created;
    private ComparableMetric total_clicks;
    private ComparableMetric total_clicks_today;
    private ComparableMetric total_qr_codes;
    private ComparableMetric total_campaigns;
}
