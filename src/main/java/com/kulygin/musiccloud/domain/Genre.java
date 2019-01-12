package com.kulygin.musiccloud.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@EqualsAndHashCode(of = { "id"})
@ToString(exclude = {"tracks"})
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "genres")
    private Set<Track> tracks;
}
