package com.bugrunners.microsservicob.repositories;

import com.bugrunners.microsservicob.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
}
