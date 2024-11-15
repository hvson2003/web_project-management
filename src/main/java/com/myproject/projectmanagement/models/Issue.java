package com.myproject.projectmanagement.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User assignee;
}
