package com.sangto.rental_car_server.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class MetaResponse<Meta, Data> {

    private int statusCode;
    private String message;
    boolean success = false;

    private Meta meta;
    private Data data;

    public MetaResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        if (statusCode == HttpStatus.OK.value()) this.success = true;
    }

    public static <Meta, Data> MetaResponse<Meta, Data> successfulResponse(String message, Meta meta, Data data) {
        MetaResponse<Meta, Data> metaResponse = new MetaResponse<>(HttpStatus.OK.value(), message);
        metaResponse.setSuccess(true);
        metaResponse.setMeta(meta);
        metaResponse.setData(data);
        return metaResponse;
    }
}
