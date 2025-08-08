package org.url_shortener_mp.api_gateway.config.filters;


import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.url_shortener_mp.api_gateway.config.RouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Autowired
    private RouteValidator routeValidator;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {


            ServerHttpRequest request = null;
            if (routeValidator.isSecured.test(exchange.getRequest())) {

                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);


                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                } else {
                    throw new RuntimeException("missing authorization header");
                }
                try {
//                    REST call to AUTH service

                    return webClientBuilder.build()
                            .get()
                            .uri("http://user-service/v1/auth/me")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + authHeader)
                            .retrieve()
                            .bodyToMono(Map.class)
                            .flatMap(user -> {
                                // Extract user id
                                String userId = (String) user.get("id");

                                // Mutate the request with new header
                                ServerHttpRequest mutatedRequest = exchange.getRequest()
                                        .mutate()
                                        .header("X-USER-ID", userId)
                                        .build();

                                ServerWebExchange mutatedExchange = exchange.mutate()
                                        .request(mutatedRequest)
                                        .build();

                                // Continue filter chain
                                return chain.filter(mutatedExchange);
                            })
                            .onErrorResume(error -> {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                return exchange.getResponse().setComplete();
                            });


                } catch (Exception e) {
                    e.printStackTrace();

                    throw new RuntimeException("un-authorized access to application");
                }
            }
            return chain.filter(exchange);


        }));
    }

    public static class Config {
    }
}
