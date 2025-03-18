package com.bugrunners.microsservicoa.controllers;

import com.bugrunners.microsservicoa.dto.CreatePostDTO;
import com.bugrunners.microsservicoa.dto.PostDTO;
import com.bugrunners.microsservicoa.dto.PostResponseDTO;
import com.bugrunners.microsservicoa.dto.UpdatePostDTO;
import com.bugrunners.microsservicoa.dto.mapper.PostMapper;
import com.bugrunners.microsservicoa.errors.ErrorMessage;
import com.bugrunners.microsservicoa.feign.MicrobClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private MicrobClient microbClient;


    @Operation(summary = "Recuperar todos os posts",
            description = "Requisição para buscar todos os posts",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Todos posts recuperados com sucesso",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PostResponseDTO.class)))),
                    @ApiResponse(responseCode = "500", description = "Falha na conexão com o Microsserviço B",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDTO>> getPosts(){

        List<PostDTO> posts = microbClient.getPosts();

        return ResponseEntity.status(HttpStatus.OK).body(PostMapper.toPostResponseListDTO(posts));
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
        microbClient.deletePostById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
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
        PostDTO post = microbClient.getPostById(id);
        return ResponseEntity.status(HttpStatus.OK).body(PostMapper.toPostResponseDTO(post));
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
        PostDTO post = microbClient.updatePostById(id, postDTO);

        return ResponseEntity.status(HttpStatus.OK).body(PostMapper.toPostResponseDTO(post));
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
            ),
            @ApiResponse(responseCode = "500", description = "Falha na conexão com o Microsserviço B",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody CreatePostDTO postDTO) {
        PostDTO post = microbClient.createPost(postDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(PostMapper.toPostResponseDTO(post));
    }
}
