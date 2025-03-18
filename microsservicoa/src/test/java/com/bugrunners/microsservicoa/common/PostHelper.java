package com.bugrunners.microsservicoa.common;

import com.bugrunners.microsservicoa.dto.*;

import java.util.ArrayList;
import java.util.List;

public class PostHelper {
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

    public static List<PostDTO> getAllPosts(){
        List<PostDTO> posts = new ArrayList<>();

        posts.add(new PostDTO(1L, "Aa", "Aaa", new ArrayList<>()));
        posts.add(new PostDTO(2L, "Bb", "Bbb", new ArrayList<>()));
        posts.add(new PostDTO(3L, "Cc", "Ccc", new ArrayList<>()));

        return posts;
    }

    public static PostDTO getPost() {
        CommentDTO comment = new CommentDTO();
        comment.setId(1L);
        comment.setName("igor");
        comment.setEmail("igor@email.com");
        comment.setBody("comment 1");
        List<CommentDTO> comments = new ArrayList<>();
        comments.add(comment);
        return new PostDTO(1L, "Aa", "Aaa", comments);
    }
}
