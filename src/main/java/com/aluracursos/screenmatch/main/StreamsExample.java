package com.aluracursos.screenmatch.main;
import java.util.Arrays;
import java.util.List;

public class StreamsExample {
    public void showExample() {
        List<String> names = Arrays.asList("César", "Leroy", "Carmona", "González");

        names.stream()
                .sorted()
                .limit(3)
                .filter(name -> name.startsWith("C"))
                .map(name -> name.toUpperCase())
                .forEach(System.out::println);
    }
}
