package kajitsu.examples;

import kajitsu.core.App;

/**
 * TODO Convert this into a junit test.
 */
public class FactoryExample {
    public static void main(String... args) {

        App app = new App();

        app.factory("xy", () -> 42);

        System.out.println((Integer) app.get("xy"));

    }
}
