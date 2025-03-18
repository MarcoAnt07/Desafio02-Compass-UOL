package com.bugrunners.microsservicob.repositories;

import com.bugrunners.microsservicob.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
