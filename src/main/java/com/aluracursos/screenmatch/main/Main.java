package com.aluracursos.screenmatch.main;

import com.aluracursos.screenmatch.model.EpisodeData;
import com.aluracursos.screenmatch.model.SeriesData;
import com.aluracursos.screenmatch.model.SeasonData;
import com.aluracursos.screenmatch.model.Episode;
import com.aluracursos.screenmatch.service.APIConsumption;
import com.aluracursos.screenmatch.service.ConvertData;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final String BASE_URL= "https://www.omdbapi.com/?t=";
    private final String API_KEY = "mi_api_key";
    private APIConsumption apiConsumption = new APIConsumption();
    private Scanner scanner = new Scanner(System.in);
    private  ConvertData conversor = new ConvertData();

    public void menu() {
        System.out.print("\nPor favor, escribe el nombre de la serie que deseas buscar: ");
        var seriesName = scanner.nextLine();
        var json = apiConsumption.getData(BASE_URL + seriesName.replace(" ", "+") + API_KEY);
        var data = conversor.getData(json, SeriesData.class);
        System.out.println("\nDatos de la serie: " + data + "\n");

        // Busca los datos de todas las temporadas de la serie
        List<SeasonData> seasons = new ArrayList<>();
        for (int i = 1; i <= data.totalSeasons(); i++) {
            var seasonjson = apiConsumption.getData(BASE_URL + seriesName.replace(" ", "+") + "&Season=" + i + API_KEY);
            var seasonsData = conversor.getData(seasonjson, SeasonData.class);
            seasons.add(seasonsData);
        }
        seasons.forEach(System.out::println);

        // Mostrar solo el titulo de los episodes para las temporadas

        /* Opción 1 - For
        *    for (int i = 0; i < data.totalSeasons(); i++) {
        *        List<EpisodeData> seasonEpisodes = seasons.get(i).episodes();
        *        for (int j = 0; j < seasonEpisodes.size(); j++) {
        *        System.out.println("Episode: " + seasonEpisodes.get(j).title());
        *       }
        *    }
        **/

        /* Opción 2 - For Each
        *    for (SeasonData season: seasons) {
        *        for (EpisodeData episode: season.episodes()) {
        *            System.out.println("Episode: " + episode.title());
        *        }
        *    }
        **/

        // Opción 3 - Lambda function y .forEach
        // seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));

        List<EpisodeData> episodesData = seasons.stream()
                .flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());

        // Mostrar el top 5 episodios de la serie
        System.out.println("\nTop 5 episodios de la serie: ");


        episodesData.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
        //        .peek(e -> System.out.println("Primer filtro (N/A)" + e))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
        //        .peek(e -> System.out.println("Segundo filtro ordenacion (M>m)" + e))
                .limit(5)
                .forEach(e -> System.out.println(
                        e.title() + ", " +
                        e.rating() + "/10"
                ));

        // Coniviertiendo los datos a una lista de tipo Episode
        List<Episode> episodes = seasons.stream()
                .flatMap(t -> t.episodes().stream()
                        .map(d -> new Episode(t.seasonNumber(), d)))
                .collect(Collectors.toList());

        /*
        *    System.out.println("Por favor indica el año a partir del cual deseas ver los episodios: ");
        *    var year = scanner.nextInt();
        *    scanner.nextLine();

        *    LocalDate searchDate = LocalDate.of(year, 1, 1);
        *    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        *    episodes.stream()
        *            .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
        *            .forEach(e -> System.out.println(
        *                    "Temporada " + e.getSeason() +
        *                            " Episode " + e.getTitle() +
        *                            " Fecha de Lanzamiento " + e.getReleaseDate().format(dtf)
        *            ));
        **/

        //Busca episodio por fragmento de titulo
        System.out.print("\nPor favor escribe el titulo del episodio que desea buscar: ");
        var title = scanner.nextLine();

        Optional<Episode> wantedEpisode = episodes.stream()
                .filter(e -> e.getTitle().toLowerCase().contains(title.toLowerCase()))
                .findFirst();

        if (wantedEpisode.isPresent()) {
            System.out.println("Episodio encontrado...");
            System.out.println("Datos del episodio: " + wantedEpisode.get());
        } else {
            System.out.println("Episodio no encontrado T-T");
        }

        System.out.println("\nMetricas de la serie");

        Map<Integer, Double> seasonsRating = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)));

        System.out.println("- Evaluaciones por temporada: " + seasonsRating);

        DoubleSummaryStatistics est = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));
        System.out.println("- Media: " + est.getAverage());
        System.out.println("- Episodio mejor evaluado: " + est.getMax());
        System.out.println("- Episodio peor evaluado: " + est.getMin());
    }
}
