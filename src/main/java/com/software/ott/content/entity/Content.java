package com.software.ott.content.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String showId;
    private String type;
    private String title;
    private String director;
    @Column(name = "castMember", length = 1000)
    private String cast;
    private String country;
    private String dateAdded;
    private String releaseYear;
    private String rating;
    private String duration;
    private String listedIn;
    @Column(length = 1000)
    private String description;
}
