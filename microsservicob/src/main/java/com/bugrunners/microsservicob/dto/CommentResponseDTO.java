package com.bugrunners.microsservicob.dto;

import lombok.Data;

@Data
public class CommentResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String body;
}
