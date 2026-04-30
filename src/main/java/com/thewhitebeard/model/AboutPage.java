package com.thewhitebeard.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "about_page")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AboutPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;
}
