package com.bugrunners.microsservicoa.controllers;

import com.bugrunners.microsservicoa.common.PostHelper;
import com.bugrunners.microsservicoa.dto.CreatePostDTO;
import com.bugrunners.microsservicoa.dto.PostDTO;
import com.bugrunners.microsservicoa.dto.PostResponseDTO;
import com.bugrunners.microsservicoa.dto.UpdatePostDTO;
import com.bugrunners.microsservicoa.dto.mapper.PostMapper;
import com.bugrunners.microsservicoa.feign.MicrobClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PostControllerUnit {
    @InjectMocks
    private PostController postController;

    @Mock
    private MicrobClient microbClient;

    @Test
    void createPost_WithValidData_ReturnPost() {
        CreatePostDTO postDTO = PostHelper.makeValidCreatePost();

        Mockito.when(microbClient.createPost(Mockito.any(CreatePostDTO.class))).thenReturn(PostMapper.fromCreatepostDTO(postDTO));

        ResponseEntity<PostResponseDTO> res = postController.createPost(postDTO);

        Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        Assertions.assertThat(res.getBody()).isNotNull();
        Assertions.assertThat(res.getBody().toString()).isEqualTo("PostResponseDTO(id=null, title=Sobre bananas, body=uma banannaijdsjiasdiajjiadjiiajida, comments=[CommentResponseDTO(id=null, name=Igor, email=igor@email.com, body=ballvllalvcskoadoaksdkoakodo)])");
    }

    @Test
    void updatePost_WithValidData_ReturnPost() {
        Long postId = 1L;
        UpdatePostDTO updatePostDTO = PostHelper.makeValidUpdatePost();

        Mockito.when(microbClient.updatePostById(Mockito.any(Long.class), Mockito.any(UpdatePostDTO.class))).thenReturn(PostMapper.fromUpdatepostDTO(updatePostDTO));

        ResponseEntity<PostResponseDTO> res = postController.updatePost(postId, updatePostDTO);

        Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(res.getBody()).isNotNull();
        Assertions.assertThat(res.getBody().toString()).isEqualTo("PostResponseDTO(id=null, title=Sobre bananas, body=uma banannaijdsjiasdiajjiadjiiajida, comments=[CommentResponseDTO(id=null, name=Igor, email=igor@email.com, body=ballvllalvcskoadoaksdkoakodo)])");
    }

    @Test
    void getAllPosts_ReturnListOfAllPosts(){
        List<PostDTO> mockPosts = PostHelper.getAllPosts();

        Mockito.when(microbClient.getPosts()).thenReturn(mockPosts);

        ResponseEntity<List<PostResponseDTO>> response = postController.getPosts();

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).hasSize(mockPosts.size());
        Assertions.assertThat(response.getBody())
                .extracting(PostResponseDTO::getTitle)
                .containsExactlyElementsOf(
                        mockPosts.stream().map(PostDTO::getTitle).toList()
                );
    }

    @Test
    void deletePostById_WithValidId_Returns200() {
        Long postId = 1L;

        Mockito.doNothing().when(microbClient).deletePostById(postId);
        ResponseEntity<Void> res = postController.deletePostById(postId);

        Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }

    @Test
    void deletePostById_WithInvalidId_ThrowsNotFoundError() {
        Long postId = 999L;

        Mockito.doThrow(new RuntimeException("Entity not found")).when(microbClient).deletePostById(postId);

        Assertions.assertThatThrownBy(() -> postController.deletePostById(postId)).isInstanceOf(RuntimeException.class).hasMessageContaining("Entity not found");
    }

    @Test
    void getPostById_WithExisting_ReturnPost() {
        PostDTO mockPost = PostHelper.getPost();

        Mockito.when(microbClient.getPostById(Mockito.any(Long.class))).thenReturn(mockPost);

        ResponseEntity<PostResponseDTO> response = postController.getPostById(1L);

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getTitle()).isEqualTo(mockPost.getTitle());
        Assertions.assertThat(response.getBody().getComments().get(0)).isNotNull();
        Assertions.assertThat(response.getBody().getComments().get(0).getEmail()).isEqualTo("igor@email.com");
    }

    @Test
    void getPostById_WithNonExisting_ReturnPost() {
        Mockito.when(microbClient.getPostById(1L)).thenThrow(new RuntimeException("Entity not found"));

        Assertions.assertThatThrownBy(() -> postController.getPostById(1L)).isInstanceOf(RuntimeException.class).hasMessageContaining("Entity not found");
    }
}
