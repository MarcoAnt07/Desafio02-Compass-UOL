package com.bugrunners.microsservicob.dto.mapper;

import com.bugrunners.microsservicob.dto.CreatePostDTO;
import com.bugrunners.microsservicob.dto.PostDTO;
import com.bugrunners.microsservicob.dto.PostResponseDTO;
import com.bugrunners.microsservicob.dto.UpdatePostDTO;
import com.bugrunners.microsservicob.entities.Post;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {
    public static Post toPost(PostDTO postDTO) {
        Post post = new Post();
        post.setBody(postDTO.getBody());
        post.setTitle(postDTO.getTitle());
        return post;
    }

    public static Post fromUpdateToPost(UpdatePostDTO postDTO) {
        return new ModelMapper().map(postDTO, Post.class);
    }

    public static Post fromCreateToPost(CreatePostDTO postDTO) {
        return new ModelMapper().map(postDTO, Post.class);
    }

    public static PostResponseDTO toPostResponseDTO(Post post) {
        return new ModelMapper().map(post, PostResponseDTO.class);
    }

    public static List<PostResponseDTO> toPostResponseListDTO(List<Post> posts) {
        return posts.stream().map(PostMapper::toPostResponseDTO).collect(Collectors.toList());
    }
}
