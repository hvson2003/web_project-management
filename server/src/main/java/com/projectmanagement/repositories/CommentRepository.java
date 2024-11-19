package com.projectmanagement.repositories;

import com.projectmanagement.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findCommentByIssueId(Long issueId, Pageable pageable);
}
