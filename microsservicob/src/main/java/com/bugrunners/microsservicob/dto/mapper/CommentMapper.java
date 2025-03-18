package com.bugrunners.microsservicob.dto.mapper;

import com.bugrunners.microsservicob.dto.CommentDTO;
import com.bugrunners.microsservicob.dto.CreatePostCommentDTO;
import com.bugrunners.microsservicob.dto.UpdatePostCommentDTO;
import com.bugrunners.microsservicob.entities.Comment;
import com.bugrunners.microsservicob.entities.Post;
import org.modelmapper.ModelMapper;

public class CommentMapper {
    public static Comment toComment(CommentDTO commentDTO, Post post) {
        Comment comment = new Comment();

        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());

        comment.setPost(post);

        return comment;
    }

    public static Comment fromUpdateToComment(UpdatePostCommentDTO commentDTO, Post post) {
        Comment comment = new ModelMapper().map(commentDTO, Comment.class);
        comment.setPost(post);
        return comment;
    }

    public static Comment fromCreateToComment(CreatePostCommentDTO commentDTO, Post post) {
        Comment comment = new ModelMapper().map(commentDTO, Comment.class);
        comment.setPost(post);
        return comment;
    }
}
