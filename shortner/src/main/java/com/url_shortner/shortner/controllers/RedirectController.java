package com.url_shortner.shortner.controllers;

import com.url_shortner.shortner.config.kafka.KafkaProducer;
import com.url_shortner.shortner.dtos.URLClickEventDTO;
import com.url_shortner.shortner.enitities.URL;
import com.url_shortner.shortner.services.URLService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.Timestamp;

@Controller
@RequestMapping("/s")
@RequiredArgsConstructor
public class RedirectController {
    private final URLService urlService;
    private final KafkaProducer kafkaProducer;
    private final UserAgentAnalyzer userAgentAnalyzer;
    @GetMapping("/{short_code}")
    public RedirectView redirectURL(@PathVariable String short_code, HttpServletRequest request) {
        RedirectView redirectView = new RedirectView();
        redirectView.setContextRelative(true);
      UserAgent u = userAgentAnalyzer.parse(request.getHeader("User-Agent"));

        try {

            URL url = urlService.fetchURL(short_code);
            kafkaProducer.produce("url-click-event", URLClickEventDTO.builder()
                            .short_url(url.getShortCode())
                            .id(url.getId())
                            .timestamp(new Timestamp(System.currentTimeMillis()))
                            .long_url(url.getUrl())
                            .agent(u.get("AgentName").getValue())
                            .device(u.get("DeviceName").getValue())
                            .browser(u.get("AgentName").getValue())
                            .os(u.get("OperatingSystemName").getValue())
                            .ip(request.getRemoteAddr())
                            .referrer(request.getHeader("Referer"))
                    .build());
            redirectView.setUrl(url.getUrl());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            redirectView.setUrl("http://localhost:3000/not-found");
        }

        return redirectView;
    }

}
