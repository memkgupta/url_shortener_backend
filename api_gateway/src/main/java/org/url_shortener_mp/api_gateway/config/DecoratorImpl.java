package org.url_shortener_mp.api_gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.url_shortener_mp.api_gateway.utils.JsonUtil;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DecoratorImpl extends ServerHttpResponseDecorator {
    MediaType contentType;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public DecoratorImpl(ServerHttpResponse delegate) {

        super(delegate);
        this.contentType = delegate.getHeaders().getContentType();
        if (this.contentType == null) {
            this.contentType = MediaType.APPLICATION_JSON;
        }
    }
    private APIResponse transform(Map<String, Object> object) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setData(object);
        apiResponse.setSuccess(true);
        return apiResponse;
    }
    DataBufferFactory bufferFactory = getDelegate().bufferFactory();
    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {





        // ✅ Ensure Content-Type remains intact
        getHeaders().setContentType(contentType);
        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
        HttpStatusCode statusCode = getStatusCode();
        HttpStatus status = HttpStatus.valueOf(statusCode.value());
        if (status.is3xxRedirection()) {
            return super.writeWith(body);  // Don't transform redirects
        }
        if(!contentType.equals(MediaType.APPLICATION_JSON)) {
            return super.writeWith(body);
        }

        return DataBufferUtils.join(fluxBody)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(dataBuffer -> {
                    byte[] content = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(content);
                    DataBufferUtils.release(dataBuffer);

                    String responseBody = new String(content, StandardCharsets.UTF_8);
                    Map<String, Object> mapBody = JsonUtil.toMap(responseBody);
                    APIResponse apiResponse;

                    if(statusCode!=null && statusCode.is4xxClientError())
                    {
                        apiResponse = new APIResponse();
                        apiResponse.setSuccess(false);
                        apiResponse.setCode(statusCode.value());
                        apiResponse.setError(status.getReasonPhrase() );
                        apiResponse.setMessage(responseBody);
                        apiResponse.setMessage(mapBody.getOrDefault("message","Some error occurred").toString());
                    }
                    else{
                        apiResponse = transform(mapBody);
                    }
                    try {
                        byte[] newContent = objectMapper.writeValueAsBytes(apiResponse);
                        DataBuffer buffer = bufferFactory.wrap(newContent);
                        return super.writeWith(Mono.just(buffer));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return super.writeWith(Flux.error(e));
                    }
                });
    }
}
