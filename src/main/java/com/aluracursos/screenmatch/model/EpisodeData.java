package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeData(
        @JsonAlias("Title") String title,
        @JsonAlias("Episode") Integer episodeNumber,
        @JsonAlias("imdbRating") String rating,
        @JsonAlias("Released") String releaseDate
    ) {

    @Override
    public String toString() {
        return "\n\t " + episodeNumber +
                ". '" + title +
                "'  " + rating +
                "/10★  " + releaseDate;
    }
}
