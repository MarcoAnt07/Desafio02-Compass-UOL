package com.bugrunners.microsservicob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class PostResponseDTO {
    private Long id;
    private String title;
    private String body;
    List<CommentResponseDTO> comments;
}
