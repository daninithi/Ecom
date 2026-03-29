package com.example.common.dto;

import com.example.common.enums.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMessage<T> {
    private ResponseStatus status;
    private T data;
    
}
