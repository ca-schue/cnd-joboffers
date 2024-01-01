package thi.cnd.userservice.adapters.out.kafka.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.ToStringSerializer;
import org.springframework.stereotype.Component;

@Component
public class CustomJsonStringSerializer<T> extends ToStringSerializer<T> {

    private final ObjectMapper objectMapper;

    public CustomJsonStringSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(String topic, Headers headers, T data) {
        super.serialize(topic, headers, data);
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
