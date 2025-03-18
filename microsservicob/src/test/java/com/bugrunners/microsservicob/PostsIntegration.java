package com.bugrunners.microsservicob;

import com.bugrunners.microsservicob.dto.PostDTO;
import com.bugrunners.microsservicob.dto.PostResponseDTO;
import com.bugrunners.microsservicob.dto.UpdatePostCommentDTO;
import com.bugrunners.microsservicob.dto.UpdatePostDTO;
import com.bugrunners.microsservicob.errors.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@Sql(scripts = "/sql/posts/posts-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/comments/comments-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

@Sql(scripts = "/sql/comments/comments-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "/sql/posts/posts-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
                .uri("/api/posts/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(post)
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(PostResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(res).isNotNull();
        Assertions.assertThat(res.getId()).isEqualTo(100);

        res = testClient
                .get()
                .uri("/api/posts/100")
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
    public void  updatePost_WithInvalidBody_ReturnStatus422() {
        List<UpdatePostCommentDTO> comments = new ArrayList<>();
        UpdatePostCommentDTO comment = new UpdatePostCommentDTO("Um nome", "user@email.com", "um body");
        comments.add(comment);

        UpdatePostDTO post = new UpdatePostDTO("", "Um body", comments);

        ErrorMessage res = testClient
                .put()
                .uri("/api/posts/100")
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
                .uri("/api/posts/100")
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
    public void getAllPosts_ReturnAllPostsStatus200() {
        List<PostDTO> responseBody =
                testClient
                        .get()
                        .uri("/api/posts")
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBodyList(PostDTO.class)
                        .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        org.assertj.core.api.Assertions.assertThat(responseBody.get(0).getId()).isEqualTo(100);
        org.assertj.core.api.Assertions.assertThat(responseBody.get(1).getId()).isEqualTo(101);
        org.assertj.core.api.Assertions.assertThat(responseBody.get(2).getId()).isEqualTo(102);

        org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(3);
    }

    @Test
    public void getPost_WithExistingId_ReturnPostByIdStatus200(){
        PostResponseDTO responseBody = testClient
                .get()
                .uri("/api/posts/102")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PostResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(102);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTitle()).isEqualTo("POST 102");
        org.assertj.core.api.Assertions.assertThat(responseBody.getBody()).isNotNull();
    }

    @Test
    public void deletePost_WithExistingId_ReturnsStatus200() {
        testClient
                .delete()
                .uri("/api/posts/100")
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
        Assertions.assertThat(res.getId()).isNotNull();

        res = testClient
                .get()
                .uri("/api/posts/" + res.getId())
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

        UpdatePostDTO post2 = new UpdatePostDTO("Titulo Válido", "Um body", comments2);

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
