package com.example.blogdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorsDto {
    private String errCode;
    private String errDetail;
}
