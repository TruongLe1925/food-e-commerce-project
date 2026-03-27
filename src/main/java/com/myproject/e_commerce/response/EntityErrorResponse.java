package com.myproject.e_commerce.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EntityErrorResponse {
    private String message;
    private int status;
    private long timestamp;
}
