package com.bugrunners.microsservicob.controllers;

import com.bugrunners.microsservicob.common.PostHelper;
import com.bugrunners.microsservicob.dto.CreatePostDTO;
import com.bugrunners.microsservicob.dto.PostResponseDTO;
import com.bugrunners.microsservicob.dto.UpdatePostDTO;
import com.bugrunners.microsservicob.dto.mapper.PostMapper;
import com.bugrunners.microsservicob.entities.Comment;
import com.bugrunners.microsservicob.entities.Post;
import com.bugrunners.microsservicob.feign.JsonPlaceholderClient;
import com.bugrunners.microsservicob.repositories.CommentsRepository;
import com.bugrunners.microsservicob.repositories.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    private PostRepository postRepository;

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private JsonPlaceholderClient jsonPlaceholderClient;

    @Test
    void syncData_WithNoInput_returnStatus200() {
        Mockito.when(jsonPlaceholderClient.getPosts()).thenReturn(PostHelper.makeJsonplaceholderPosts());
        Mockito.when(jsonPlaceholderClient.getComments(Mockito.any(Long.class))).thenReturn(PostHelper.makeJsonplaceholderComments());

        ResponseEntity<Void> res = postController.syncData();
        Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        Mockito.verify(postRepository).save(postCaptor.capture());
        Post postIn = postCaptor.getValue();
        Assertions.assertThat(postIn).isNotNull();
        Assertions.assertThat(postIn.getTitle()).isEqualTo("Um titulo");
        Assertions.assertThat(postIn.getBody()).isEqualTo("Um body");

        ArgumentCaptor<List<Comment>> commentsCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(commentsRepository).saveAll(commentsCaptor.capture());
        List<Comment> commentsIn = commentsCaptor.getValue();
        Assertions.assertThat(commentsIn.get(0)).isNotNull();
        Assertions.assertThat(commentsIn.get(0).getPost()).isEqualTo(postIn);
        Assertions.assertThat(commentsIn.get(0).getName()).isEqualTo("Igor");
        Assertions.assertThat(commentsIn.get(0).getEmail()).isEqualTo("igor@email.com");
        Assertions.assertThat(commentsIn.get(0).getBody()).isEqualTo("Um body");

    }

    @Test
    void createPost_WithValidData_ReturnPost() {
        CreatePostDTO postDTO = PostHelper.makeValidCreatePost();

        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenReturn(PostMapper.fromCreateToPost(postDTO));

        ResponseEntity<PostResponseDTO> res = postController.createPost(postDTO);

        Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        Assertions.assertThat(res.getBody()).isNotNull();
        Assertions.assertThat(res.getBody().toString()).isEqualTo("PostResponseDTO(id=null, title=Sobre bananas, body=uma banannaijdsjiasdiajjiadjiiajida, comments=[CommentResponseDTO(id=null, name=Igor, email=igor@email.com, body=ballvllalvcskoadoaksdkoakodo)])");
    }

    @Test
    void updatePost_WithValidData_ReturnPost() {
        Long postId = 1L;
        UpdatePostDTO postDTO = PostHelper.makeValidUpdatePost();
        Post post = PostMapper.fromUpdateToPost(postDTO);

        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        ResponseEntity<PostResponseDTO> res = postController.updatePost(postId, postDTO);

        Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(res.getBody()).isNotNull();
        Assertions.assertThat(res.getBody().toString()).isEqualTo("PostResponseDTO(id=null, title=Sobre bananas, body=uma banannaijdsjiasdiajjiadjiiajida, comments=[CommentResponseDTO(id=null, name=Igor, email=igor@email.com, body=ballvllalvcskoadoaksdkoakodo)])");
    }

    @Test
    void getAllPosts_ReturnListOfAllPosts() {
        List<Post> mockPosts = PostHelper.getAllPosts();

        Mockito.when(postRepository.findAll()).thenReturn(mockPosts);

        ResponseEntity<List<PostResponseDTO>> response = postController.getAll();

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).hasSize(mockPosts.size());
        Assertions.assertThat(response.getBody())
                .extracting(PostResponseDTO::getTitle)
                .containsExactlyElementsOf(
                        mockPosts.stream().map(Post::getTitle).toList()
                );
    }

    @Test
    void deletePostById_WithValidId_Returns200() {
        Long postId = 1L;

        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        ResponseEntity<Void> res = postController.deletePostById(postId);

        Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }

    @Test
    void deletePostById_WithInvalidId_ThrowsNotFoundError() {
        Long postId = 999L;

        Assertions.assertThatThrownBy(() -> postController.deletePostById(postId)).isInstanceOf(RuntimeException.class).hasMessageContaining("Entity not found");
    }

    @Test
    void getPostById_WithExisting_ReturnPost() {
        Post mockPost = PostHelper.getPost();

        Mockito.when(postRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(mockPost));

        ResponseEntity<PostResponseDTO> response = postController.getPostById(1L);

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getTitle()).isEqualTo(mockPost.getTitle());
        Assertions.assertThat(response.getBody().getComments().get(0)).isNotNull();
        Assertions.assertThat(response.getBody().getComments().get(0).getEmail()).isEqualTo("igor@email.com");
    }

    @Test
    void getPostById_WithNonExisting_ReturnPost() {
        Mockito.when(postRepository.findById(1L)).thenThrow(new RuntimeException("Entity not found"));

        Assertions.assertThatThrownBy(() -> postController.getPostById(1L)).isInstanceOf(RuntimeException.class).hasMessageContaining("Entity not found");
    }
}
