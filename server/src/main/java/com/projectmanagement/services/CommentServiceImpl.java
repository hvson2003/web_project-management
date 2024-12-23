package com.projectmanagement.services;

import com.projectmanagement.models.Comment;
import com.projectmanagement.models.Issue;
import com.projectmanagement.models.User;
import com.projectmanagement.repositories.CommentRepository;
import com.projectmanagement.repositories.IssueRepository;
import com.projectmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment createComment(Long issueId, Long userId, String content) throws Exception {
        Optional<Issue> issueOptional = issueRepository.findById(issueId);
        Optional<User> userOptional = userRepository.findById(userId);

        if(issueOptional.isEmpty()) {
            throw new Exception("Issue not found with id " + issueId);
        }

        if(userOptional.isEmpty()) {
            throw new Exception("User not found with id " + userId);
        }

        Issue issue = issueOptional.get();
        User user = userOptional.get();

        Comment comment = new Comment();
        comment.setIssue(issue);
        comment.setUser(user);
        comment.setCreatedDateTime(LocalDateTime.now());
        comment.setContent(content);

        Comment savedComment = commentRepository.save(comment);

        issue.getComments().add(savedComment);

        return savedComment;
    }

    @Override
    public void deleteComment(Long commentId, Long userId) throws Exception {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Optional<User> userOptional = userRepository.findById(userId);

        if(commentOptional.isEmpty()) {
            throw new Exception("Comment not found with id " + commentId);
        }
        if(userOptional.isEmpty()) {
            throw new Exception("User not found with id " + userId);
        }

        Comment comment = commentOptional.get();
        User user = userOptional.get();

        if(comment.getUser().equals(user)) {
            commentRepository.delete(comment);
        } else {
            throw new Exception("User does not have permission to delete this comment!");
        }
    }

    @Override
    public Page<Comment> findCommentByIssueId(Long issueId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findCommentByIssueId(issueId, pageable);
    }}
