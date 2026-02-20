package com.blackint.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "blog_categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    private Boolean isActive = true;
}