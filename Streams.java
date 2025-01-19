import java.util.stream.Stream;

public class StreamExample {

    public static void main(String[] args) {
        Stream<String> stream = Stream.of("one", "two", "three", "four");
        stream.forEach(System.out::println);
    }
}