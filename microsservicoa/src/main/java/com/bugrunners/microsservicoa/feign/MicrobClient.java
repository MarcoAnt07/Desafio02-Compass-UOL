package com.bugrunners.microsservicoa.feign;

import com.bugrunners.microsservicoa.dto.CommentDTO;
import com.bugrunners.microsservicoa.dto.CreatePostDTO;
import com.bugrunners.microsservicoa.dto.PostDTO;
import com.bugrunners.microsservicoa.dto.UpdatePostDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "microsservicob", url = "localhost:8081/api")
public interface MicrobClient {

    @GetMapping("/posts")
    List<PostDTO> getPosts();

    @DeleteMapping("posts/{id}")
    void deletePostById(@PathVariable("id") Long id);

    @GetMapping("/posts/{id}")
    PostDTO getPostById(@PathVariable Long id);

    @PutMapping("/posts/{id}")
    PostDTO updatePostById(@PathVariable Long id, @Valid @RequestBody UpdatePostDTO postDTO);

    @PostMapping("/posts")
    PostDTO createPost(@Valid @RequestBody CreatePostDTO postDTO);
}
