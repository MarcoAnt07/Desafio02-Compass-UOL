package com.bugrunners.microsservicoa.dto.mapper;

import com.bugrunners.microsservicoa.dto.CreatePostDTO;
import com.bugrunners.microsservicoa.dto.PostDTO;
import com.bugrunners.microsservicoa.dto.PostResponseDTO;
import com.bugrunners.microsservicoa.dto.UpdatePostDTO;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {
    public static PostResponseDTO toPostResponseDTO(PostDTO post) {
        return new ModelMapper().map(post, PostResponseDTO.class);
    }

    public static PostDTO fromUpdatepostDTO(UpdatePostDTO post) {
        return new ModelMapper().map(post, PostDTO.class);
    }

    public static PostDTO fromCreatepostDTO(CreatePostDTO post) {
        return new ModelMapper().map(post, PostDTO.class);
    }

    public static List<PostResponseDTO> toPostResponseListDTO(List<PostDTO> posts) {
        return posts.stream().map(PostMapper::toPostResponseDTO).collect(Collectors.toList());
    }
}
