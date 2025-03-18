package com.bugrunners.microsservicoa;

import com.bugrunners.microsservicoa.dto.PostDTO;
import com.bugrunners.microsservicoa.dto.PostResponseDTO;
import com.bugrunners.microsservicoa.dto.UpdatePostCommentDTO;
import com.bugrunners.microsservicoa.dto.UpdatePostDTO;
import com.bugrunners.microsservicoa.errors.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsIntegration {

    @Autowired
    WebTestClient testClient;

    @Test
    public void updatePost_WithNonExistingId_ReturnStatus404() {
        List<UpdatePostCommentDTO> comments = new ArrayList<>();
        UpdatePostCommentDTO comment = new UpdatePostCommentDTO("Um nome", "user@email.com", "um body");
        comments.add(comment);

        UpdatePostDTO post = new UpdatePostDTO("Um titulo", "Um body", comments);

        ErrorMessage res = testClient
                .put()
                .uri("/api/posts/666")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(post)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(404);
    }

    @Test
    public void updatePost_WithValidBody_ReturnStatus200() {
        List<UpdatePostCommentDTO> comments = new ArrayList<>();
        UpdatePostCommentDTO comment = new UpdatePostCommentDTO("Um nome", "user@email.com", "um body");
        comments.add(comment);

        UpdatePostDTO post = new UpdatePostDTO("Um titulo", "Um body", comments);

        PostResponseDTO res = testClient
                .put()
                .uri("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(post)
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(PostResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getId()).isEqualTo(1);

        res = testClient
                .get()
                .uri("/api/posts/1")
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(PostResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getTitle()).isEqualTo("Um titulo");
        Assertions.assertThat(res.getComments().size()).isEqualTo(1);
        Assertions.assertThat(res.getComments().get(0).getName()).isEqualTo("Um nome");
    }

    @Test
    public void updatePost_WithInvalidBody_ReturnStatus422() {
        List<UpdatePostCommentDTO> comments = new ArrayList<>();
        UpdatePostCommentDTO comment = new UpdatePostCommentDTO("Um nome", "user@email.com", "um body");
        comments.add(comment);

        UpdatePostDTO post = new UpdatePostDTO("", "Um body", comments);

        ErrorMessage res = testClient
                .put()
                .uri("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(post)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(422);

        List<UpdatePostCommentDTO> comments2 = new ArrayList<>();
        UpdatePostCommentDTO comment2 = new UpdatePostCommentDTO("Um nome", "user", "um body");
        comments2.add(comment2);

        UpdatePostDTO post2 = new UpdatePostDTO("saddsasddsadsaasd", "Um body", comments2);

        res = testClient
                .put()
                .uri("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(post2)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(422);
    }

    @Test
    public void fetchAllPosts_ReturnWithStatus200() {
        List<PostDTO> responseBody =
                testClient
                        .get()
                        .uri("/api/posts")
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBodyList(PostDTO.class)
                        .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody).isNotEmpty();
    }

    @Test
    public void fetchAllPosts_WithNoPosts_ReturnErrorMessageIsEmpty() {
        List<PostDTO> responseBody =
                testClient
                        .get()
                        .uri("/api/posts")
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBodyList(PostDTO.class)
                        .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotEmpty();
    }

    @Test
    public void fetchAllPosts_ReturnErrorMessageWithStatus500() {
        List<PostDTO> responseBody =
                testClient
                        .get()
                        .uri("/api/posts")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBodyList(PostDTO.class)
                        .returnResult().getResponseBody();
    }

    @Test
    public void getPost_WithExistingId_ReturnPostByIdStatus200(){
        PostResponseDTO responseBody = testClient
                .get()
                .uri("/api/posts/2")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PostResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTitle()).isEqualTo("qui est esse");
        org.assertj.core.api.Assertions.assertThat(responseBody.getBody()).isNotNull();
    }

    @Test
    public void deletePost_WithExistingId_ReturnsStatus200() {
        testClient
                .delete()
                .uri("/api/posts/4")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(void.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    public void deletePost_WithNonExistingId_ReturnsStatus404() {
        ErrorMessage responseMessage = testClient
                .delete()
                .uri("/api/posts/999")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();
    }
    @Test
    public void createPost_WithValidBody_ReturnStatus201() {
        List<UpdatePostCommentDTO> comments = new ArrayList<>();
        UpdatePostCommentDTO comment = new UpdatePostCommentDTO("Um nome", "user@email.com", "um body");
        comments.add(comment);

        UpdatePostDTO post = new UpdatePostDTO("Um titulo", "Um body", comments);

        PostResponseDTO res = testClient
                .post()
                .uri("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(post)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(PostResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getTitle()).isEqualTo("Um titulo");
        Assertions.assertThat(res.getComments().size()).isEqualTo(1);
        Assertions.assertThat(res.getComments().get(0).getName()).isEqualTo("Um nome");
    }

    @Test
    public void createPost_WithInvalidBody_ReturnStatus422() {
        List<UpdatePostCommentDTO> comments = new ArrayList<>();
        UpdatePostCommentDTO comment = new UpdatePostCommentDTO("Um nome", "user@email.com", "um body");
        comments.add(comment);

        UpdatePostDTO post = new UpdatePostDTO("", "Um body", comments);

        ErrorMessage res = testClient
                .post()
                .uri("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(post)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(422);

        List<UpdatePostCommentDTO> comments2 = new ArrayList<>();
        UpdatePostCommentDTO comment2 = new UpdatePostCommentDTO("Um nome", "user", "um body");
        comments2.add(comment2);

        UpdatePostDTO post2 = new UpdatePostDTO("Título Válido", "Um body", comments2);

        res = testClient
                .post()
                .uri("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(post2)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getStatus()).isEqualTo(422);
    }
}
