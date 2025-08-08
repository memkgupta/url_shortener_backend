package org.url_shortener_mp.api_gateway.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.url_shortener_mp.api_gateway.config.APIResponse;
import org.url_shortener_mp.api_gateway.config.DecoratorImpl;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApiResponseTransformFilter extends AbstractGatewayFilterFactory<ApiResponseTransformFilter.Config> {


    public ApiResponseTransformFilter() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpResponse originalResponse = exchange.getResponse();


            return chain.filter(exchange.mutate().response(new DecoratorImpl(originalResponse)).build());


        }, NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1);
    }

    public static class Config {
        // Future config options
    }
}
