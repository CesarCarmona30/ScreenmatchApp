package com.aluracursos.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episode {
    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double rating;
    private LocalDate releaseDate;

    public Episode(Integer number, EpisodeData d) {
        this.season = number;
        this.title = d.title();
        this.episodeNumber = d.episodeNumber();
        try {
            this.rating = Double.valueOf(d.rating());
        } catch (NumberFormatException e) {
            this.rating = 0.0;
        }
        try {
            this.releaseDate = LocalDate.parse(d.releaseDate());
        } catch (DateTimeParseException e) {
            this.releaseDate = null;
        }
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "T" + season +
                " - EP" + episodeNumber +
                " '" + title +
                "' " + rating +
                "/10 " + releaseDate;
    }
}
