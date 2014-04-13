# kajitsu

**Status:** Prototype / Java 8 Lambda test project.

Tiny Java 8 web application framework.

100% dependency free and inspired by the great javascript frameworks
[express](http://expressjs.com/) and [koa](http://koajs.com/).

```java
public class Example1 {
    public static void main(String... args) {

        App app = new App();

        app.use(new RequestLogger());
        app.use(new ExpiresIn(10, ChronoUnit.MINUTES));
        app.use(new CalculateContentLength());
        app.use(new Compress());

        app.use((request, response, next) -> {
            response.setHeader("Server", "Example1");
            response.setHeader("Content-Type", "text/plain");

            try {
                response.getOutputStream().write("Hallo\n".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            next.run();
        });

        app.listen(5000);
    }
}
```
