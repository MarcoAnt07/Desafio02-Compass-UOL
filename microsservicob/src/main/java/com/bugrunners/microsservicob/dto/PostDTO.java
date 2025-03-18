package com.bugrunners.microsservicob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String body;
}
