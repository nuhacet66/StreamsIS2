import java.util.*;
import java.util.stream.Collectors;

public class StreamsExamples {

    public static void main(String[] args) {
        List<Country> countries = List.of(
                new Country("USA", "United States", "North America", 9833517, 331002651, 21433226, 1,
                        List.of(new Country.City(1, "New York", 8419600),
                                new Country.City(2, "Los Angeles", 3980400))),
                new Country("CAN", "Canada", "North America", 9984670, 37742154, 1647117, 3,
                        List.of(new Country.City(3, "Toronto", 2731571),
                                new Country.City(4, "Ottawa", 994837))),
                new Country("CHN", "China", "Asia", 9596961, 1439323776, 14722731, 5,
                        List.of(new Country.City(6, "Shanghai", 24150000),
                                new Country.City(7, "Beijing", 21540000))),
                new Country("IND", "India", "Asia", 3287263, 1380004385, 2875142, 7,
                        List.of(new Country.City(8, "Mumbai", 20411000),
                                new Country.City(9, "Delhi", 16787941))),
                new Country("FRA", "France", "Europe", 551695, 65273511, 2715518, 9,
                        List.of(new Country.City(10, "Paris", 2140526))));

        StreamsExamples examples = new StreamsExamples();
        examples.groupMoviesByYear();
        examples.findRichestCountryByContinent(countries);
        examples.findPopulationStats(countries);
        examples.findMostPopulousCityPerContinent(countries);
        examples.findMostPopulousCapitalPerContinent(countries);
    }

    // Agrupar películas por año
    public void groupMoviesByYear() {
        List<Movie> movies = List.of(
                new Movie(1, "Inception", 2010, "tt1375666",
                        List.of(new Genre(1, "Sci-Fi"), new Genre(2, "Thriller")),
                        List.of(new Director(1, "Christopher Nolan", "nm0634240"))),
                new Movie(2, "Interstellar", 2014, "tt0816692",
                        List.of(new Genre(1, "Sci-Fi"), new Genre(3, "Adventure")),
                        List.of(new Director(1, "Christopher Nolan", "nm0634240"))));

        Map<Integer, List<String>> moviesByYear = movies.stream()
                .collect(Collectors.groupingBy(
                        Movie::year,
                        Collectors.mapping(Movie::title, Collectors.toList())));

        moviesByYear.forEach((year, titles) -> System.out.println(year + ": " + titles));
    }

    // País más rico por continente
    public void findRichestCountryByContinent(List<Country> countries) {
        Map<String, Country> richestByContinent = countries.stream()
                .collect(Collectors.groupingBy(
                        Country::continent,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(Country::gnp)),
                                Optional::get)));

        richestByContinent.forEach((continent, country) -> System.out
                .println(continent + ": " + country.name() + " (GNP: " + country.gnp() + ")"));
    }

    // Estadísticas de población
    public void findPopulationStats(List<Country> countries) {
        IntSummaryStatistics stats = countries.stream()
                .mapToInt(Country::population)
                .summaryStatistics();

        System.out.println("Mínima: " + stats.getMin());
        System.out.println("Máxima: " + stats.getMax());
        System.out.println("Promedio: " + stats.getAverage());
    }

    // Ciudad más poblada por continente
    public void findMostPopulousCityPerContinent(List<Country> countries) {
        Map<String, Country.City> mostPopulousCityByContinent = countries.stream()
                .flatMap(country -> country.cities().stream()
                        .map(city -> Map.entry(country.continent(), city)))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(entry -> entry
                                        .getValue().population())),
                                optional -> optional.map(Map.Entry::getValue)
                                        .orElse(null))));

        mostPopulousCityByContinent.forEach((continent, city) -> System.out
                .println(continent + ": " + city.name() + " (Población: " + city.population() + ")"));
    }

    // 5. Capital más poblada por continente
    public void findMostPopulousCapitalPerContinent(List<Country> countries) {
        Map<String, Country.City> mostPopulousCapitalByContinent = countries.stream()
                .flatMap(country -> country.cities().stream()
                        .filter(city -> city.id() == country.capital())
                        .map(city -> Map.entry(country.continent(), city)))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(entry -> entry
                                        .getValue().population())),
                                optional -> optional.map(Map.Entry::getValue)
                                        .orElse(null))));

        mostPopulousCapitalByContinent.forEach((continent, city) -> System.out
                .println(continent + ": " + city.name() + " (Población: " + city.population() + ")"));
    }

    public record Country(String code, String name, String continent, double surfaceArea, int population,
            double gnp,
            int capital, List<City> cities) {
        public record City(int id, String name, int population) {
        }
    }

    public record Movie(int id, String title, int year, String imdb, List<Genre> genres, List<Director> directors) {
        public record Director(int id, String name, String imdb) {
        }

        public record Genre(int id, String name) {
        }
    }
}
