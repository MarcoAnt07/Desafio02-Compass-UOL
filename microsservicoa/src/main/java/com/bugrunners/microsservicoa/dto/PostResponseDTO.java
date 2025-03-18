package com.bugrunners.microsservicoa.dto;

import com.bugrunners.microsservicoa.dto.CommentResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String title;
    private String body;
    List<CommentResponseDTO> comments;
}
