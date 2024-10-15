package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeriesData(
        @JsonAlias("Title") String title,
        @JsonAlias("totalSeasons") Integer totalSeasons,
        @JsonAlias("imdbRating") String rating ) {

    @Override
    public String toString() {
        return title + " | " + totalSeasons + " Temporadas | " + rating + "/10★";
    }
}