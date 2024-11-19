package com.projectmanagement.services;

import com.projectmanagement.models.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
    Comment createComment(Long issueId, Long userId, String content) throws Exception;

    void deleteComment(Long commentId, Long userId) throws Exception;

    Page<Comment> findCommentByIssueId(Long issueId, int page, int size);
}
