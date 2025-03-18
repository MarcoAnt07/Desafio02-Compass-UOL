package com.bugrunners.microsservicob.controllers;

import com.bugrunners.microsservicob.dto.*;
import com.bugrunners.microsservicob.dto.mapper.CommentMapper;
import com.bugrunners.microsservicob.dto.mapper.PostMapper;
import com.bugrunners.microsservicob.entities.Comment;
import com.bugrunners.microsservicob.entities.Post;
import com.bugrunners.microsservicob.errors.ErrorMessage;
import com.bugrunners.microsservicob.errors.throwable.NotFoundError;
import com.bugrunners.microsservicob.feign.JsonPlaceholderClient;
import com.bugrunners.microsservicob.repositories.CommentsRepository;
import com.bugrunners.microsservicob.repositories.PostRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Posts", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um post.")
@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private JsonPlaceholderClient jsonPlaceholderClient;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    CommentsRepository commentsRepository;

    @Operation(summary = "Sincronizar os dados dos posts e comentários",
            description = "Esta operação realiza a sincronização dos posts e seus comentários com a API externa JSONPlaceholder. Os dados sincronizados são armazenados no banco de dados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dados sincronizados com sucesso"
                    )
            })

    @PostMapping("/sync-data")
    public ResponseEntity<Void> syncData() {
        List<PostDTO> postsDTO = jsonPlaceholderClient.getPosts();

        for (PostDTO dto: postsDTO) {
            Post post = PostMapper.toPost(dto);

            postRepository.save(post);

            List<CommentDTO> commentDTOs = jsonPlaceholderClient.getComments(dto.getId());
            List<Comment> comments = new ArrayList<>();

            for (CommentDTO commentDTO : commentDTOs) {
                Comment comment = CommentMapper.toComment(commentDTO, post);
                comments.add(comment);
            }

            commentsRepository.saveAll(comments);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Atualizar um Post", responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Recurso criado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post com o ID especificado não foi encontrado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Entrada de dados inválidos.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable("id") Long id, @Valid @RequestBody UpdatePostDTO postDTO) {
        Post dbPost = postRepository.findById(id).orElseThrow(NotFoundError::new);

        commentsRepository.deleteAll(dbPost.getComments());

        dbPost.setBody(postDTO.getBody());
        dbPost.setTitle(postDTO.getTitle());

        List<Comment> newComments = new ArrayList<>();

        for(UpdatePostCommentDTO cDTO: postDTO.getComments()) {
            Comment comment = CommentMapper.fromUpdateToComment(cDTO, dbPost);
            newComments.add(comment);
        }

        commentsRepository.saveAll(newComments);

        dbPost.setComments(newComments);

        return ResponseEntity.status(HttpStatus.OK).body(PostMapper.toPostResponseDTO(dbPost));
    }

    @Operation(summary = "Criar um novo Post", responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Recurso criado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreatePostDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Entrada de dados inválidos.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody CreatePostDTO postDTO) {
        Post post = PostMapper.fromCreateToPost(postDTO);

        postRepository.save(post);

        List<Comment> newComments = new ArrayList<>();

        for(CreatePostCommentDTO cDTO: postDTO.getComments()) {
            Comment comment = CommentMapper.fromCreateToComment(cDTO, post);
            newComments.add(comment);
        }

        commentsRepository.saveAll(newComments);

        post.setComments(newComments);

        return ResponseEntity.status(HttpStatus.CREATED).body(PostMapper.toPostResponseDTO(post));
    }

    @Operation(summary = "Recuperar todos os posts",
            description = "Requisição para recuperar todos os posts",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Todos os posts recuperados com sucesso",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PostResponseDTO.class))))
            })
    @GetMapping("/posts")
    public  ResponseEntity<List<PostResponseDTO>> getAll(){
        List<Post> dbPosts = postRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(PostMapper.toPostResponseListDTO(dbPosts));
    }


    @Operation(summary = "Recupera post por ID", description = "Recurso para localizar post por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Post recuperado com sucesso",
                            content = @Content(mediaType = "application/JSON", schema = @Schema(implementation = PostResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Post não encontrado",
                            content = @Content(mediaType = "application/JSON", schema = @Schema(implementation = ErrorMessage.class))),
            }
    )
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable("id") Long id) {
        Post dbPost = postRepository.findById(id).orElseThrow(NotFoundError::new);

        return ResponseEntity.status(HttpStatus.OK).body(PostMapper.toPostResponseDTO(dbPost));
    }

    @Operation(summary = "Deletar um post",
            description = "Deletar um post com ID específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Post deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Post com o ID especificado não encontrado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @DeleteMapping("posts/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable("id") Long id) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundError::new);

        commentsRepository.deleteAll(post.getComments());

        postRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
