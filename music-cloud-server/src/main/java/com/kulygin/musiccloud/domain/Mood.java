package com.kulygin.musiccloud.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "public")
@EqualsAndHashCode(of = { "id"})
@ToString(exclude = {"tracks"})
public class Mood {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "moods")
    private Set<Track> tracks;
}
