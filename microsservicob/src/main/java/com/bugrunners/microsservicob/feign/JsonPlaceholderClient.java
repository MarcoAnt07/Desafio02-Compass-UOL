package com.bugrunners.microsservicob.feign;

import com.bugrunners.microsservicob.dto.CommentDTO;
import com.bugrunners.microsservicob.dto.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "jsonplaceholder", url = "https://jsonplaceholder.typicode.com")
public interface JsonPlaceholderClient {

    @GetMapping("/posts?_start=0&_limit=10")
    List<PostDTO> getPosts();

    @GetMapping("/posts/{id}/comments")
    List<CommentDTO> getComments(@PathVariable (value = "id") Long id);
}
