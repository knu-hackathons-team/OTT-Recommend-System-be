package com.software.ott.content.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "content",
        indexes = {
                @Index(name = "idx_title", columnList = "title"),
                @Index(name = "idx_listedIn", columnList = "listedIn"),
                @Index(name = "idx_director", columnList = "director"),
                @Index(name = "idx_cast", columnList = "cast"),
                @Index(name = "idx_title_director_listedIn_cast", columnList = "title, director, listedIn, cast")
        }
)
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String showId;
    private String type;

    @Column(length = 255)
    private String title;

    @Column(length = 255)
    private String director;

    @Column(name = "castMember", length = 1000)
    private String cast;

    private String country;
    private String dateAdded;
    private String releaseYear;
    private String rating;
    private String duration;

    @Column(length = 500)
    private String listedIn;

    @Column(length = 1000)
    private String description;

    private String posterPath;
}
