package com.bugrunners.microsservicob.common;

import com.bugrunners.microsservicob.dto.*;
import com.bugrunners.microsservicob.entities.Comment;
import com.bugrunners.microsservicob.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class PostHelper {
    public static List<PostDTO> makeJsonplaceholderPosts() {
        List<PostDTO> posts = new ArrayList<>();
        PostDTO post = new PostDTO();
        post.setTitle("Um titulo");
        post.setBody("Um body");
        post.setId(1L);
        posts.add(post);
        return posts;
    }

    public static List<CommentDTO> makeJsonplaceholderComments() {
        List<CommentDTO> comments = new ArrayList<>();
        CommentDTO comment = new CommentDTO();
        comment.setName("Igor");
        comment.setEmail("igor@email.com");
        comment.setBody("Um body");
        comments.add(comment);
        return comments;
    }

    public static CreatePostDTO makeValidCreatePost() {
        CreatePostCommentDTO comment = new CreatePostCommentDTO("Igor", "igor@email.com", "ballvllalvcskoadoaksdkoakodo");
        List<CreatePostCommentDTO> comments = new ArrayList<>();
        comments.add(comment);

        return new CreatePostDTO("Sobre bananas", "uma banannaijdsjiasdiajjiadjiiajida", comments);
    }

    public static UpdatePostDTO makeValidUpdatePost() {
        UpdatePostCommentDTO comment = new UpdatePostCommentDTO("Igor", "igor@email.com", "ballvllalvcskoadoaksdkoakodo");
        List<UpdatePostCommentDTO> comments = new ArrayList<>();
        comments.add(comment);

        return new UpdatePostDTO("Sobre bananas", "uma banannaijdsjiasdiajjiadjiiajida", comments);
    }

    public static List<Post> getAllPosts(){
        List<Post> posts = new ArrayList<>();

        posts.add(new Post(1L, "Aa", "Aaa", new ArrayList<>()));
        posts.add(new Post(2L, "Bb", "Bbb", new ArrayList<>()));
        posts.add(new Post(3L, "Cc", "Ccc", new ArrayList<>()));

        return posts;
    }

    public static Post getPost() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setName("igor");
        comment.setEmail("igor@email.com");
        comment.setBody("comment 1");
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        return new Post(1L, "Aa", "Aaa", comments);
    }
}
