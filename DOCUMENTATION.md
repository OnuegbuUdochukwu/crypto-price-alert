# Project Documentation: Crypto Price Alert (v2)

## 1. Core Purpose & Scope

The Crypto Price Alert application is a non-interactive, backend service built with Spring Boot. Unlike the previous projects, it does not expose any web-facing API endpoints. Its sole purpose is to run in the background, periodically monitor an external data source, and trigger a one-time event when a specific condition is met.

### Business Logic:

- **Scheduled Polling:** The core functionality is a scheduled task that runs at a fixed interval (every 10 seconds).

- **Data Fetching:** On each run, it calls the public Quidax API to get the latest price for a specific cryptocurrency market (`btcngn`).

- **Conditional Logic:** It compares the fetched price against a hardcoded target price.

- **Stateful Event Trigger:** If the price target is met, it performs two actions:
    - Prints a detailed alert message to the console.
    - Programmatically terminates the application, as its primary goal has been achieved.

It uses an internal flag (`alertTriggered`) to ensure this event only happens once.

## 2. Core Dependencies

- **spring-boot-starter**: This is the core starter package for any Spring Boot application. Unlike the spring-boot-starter-web package used previously, this one provides the essential auto-configuration and dependency management for a standalone application without including an embedded web server like Tomcat.

- **lombok**: A utility library that reduces boilerplate code (getters, setters, etc.) in our data classes (DTOs) through annotations.

## 3. Project Structure and Components

The project's structure is different from a typical web service, notably lacking a controller package because it serves no incoming HTTP requests.

```
/dto/
 ├── Ticker.java
 ├── MarketData.java
 └── SingleTickerResponse.java
/service/
 └── QuidaxService.java
/scheduler/
 └── PriceAlertScheduler.java
```

# 4. Detailed Class Explanations

## The DTO Layer (The Data Models)

This layer's purpose is to create a set of Java classes that exactly mirror the nested structure of the JSON response from the Quidax API. This allows the Jackson library (used by Spring) to automatically and reliably convert the JSON text into Java objects.

- **Ticker.java:** The innermost object, representing the actual price and volume data. It uses `@JsonProperty` to map API field names (like `last` and `vol`) to more intuitive Java field names (price and volume).

- **MarketData.java:** The middle-layer object. It acts as a container for the Ticker object and its associated metadata (like the at timestamp).

- **SingleTickerResponse.java:** The top-level, outermost object. It represents the entire JSON "box" sent by the API, containing the status, message, and the nested data (our MarketData object).

## service/QuidaxService.java - The Data Fetching Logic

This class has a single responsibility: to handle all communication with the external Quidax API. This isolates the rest of our application from the complexities of network calls and data parsing.

### getTicker(String market) Method:

This method constructs the precise URL for fetching a single market's ticker data.

- It uses Spring's `RestTemplate` to make the HTTP GET request.

- Crucially, it tells `RestTemplate` to expect a `SingleTickerResponse.class` object. This initiates the DTO mapping process.

Upon receiving the response, it performs the "unwrapping" logic: it checks if the call was successful (`response != null && "success".equals(response.getStatus())`) and then drills down through the layers (`response.getData().getTicker()`) to extract and return only the innermost Ticker object, which is the only piece of data the rest of our application cares about.

## scheduler/PriceAlertScheduler.java - The Core Engine

This is the heart of the application. It contains the logic that is executed repeatedly in the background.

- `@Component`: This is a generic annotation that tells Spring to create an instance of this class (a "bean") and manage it. This is necessary for Spring to detect the `@Scheduled` annotation within it.

- `@Scheduled(fixedRate = 10000)`: This annotation turns the `checkPriceAndAlert()` method into a scheduled task.

    - `fixedRate`: This specific parameter tells the scheduler to execute the method every 10,000 milliseconds (10 seconds). The timer starts from the beginning of the last execution. This is ideal for tasks that need to run at a consistent interval, regardless of how long the task itself takes to complete.

    - Alternative: Another common option is `fixedDelay`, which would wait 10 seconds after the previous execution finishes.

- `alertTriggered` Flag: This boolean field acts as a stateful "latch." Its purpose is to ensure the main alert logic runs only once. In the `checkPriceAndAlert` method, the very first line is `if (alertTriggered) { return; }`. This makes the scheduler highly efficient after its goal is met; it still wakes up every 10 seconds but immediately returns without making any network calls or performing checks.

# Price Checking Logic

## BigDecimal
The code uses `java.math.BigDecimal` to handle the prices. This is the industry standard for any financial or monetary calculations in Java because it provides exact precision and avoids the rounding errors common with float and double types.

## compareTo()
This is the correct method for comparing `BigDecimal` values. It returns:
1. **0** if they are equal,
2. **1** if the first number is greater, and
3. **-1** if it is less.

Our logic `currentPrice.compareTo(TARGET_PRICE) >= 0` checks if the current price is greater than or equal to our target.

## System.exit(0)
This is the command that programmatically shuts down the application. It terminates the entire Java Virtual Machine (JVM) that the application is running in.

The **0** is an exit code that signifies a "normal" or "successful" shutdown, as opposed to a non-zero code which typically indicates an error.

This is a simpler, more direct approach than using `SpringApplication.exit()`, which avoids potential dependency injection issues for a project of this scope.

## CryptoPriceAlertApplication.java - The Application Starter
This is the main entry point that boots up the Spring application.

### @EnableScheduling
This is the master switch that activates Spring's entire scheduling subsystem. When Spring Boot starts, it sees this annotation and begins searching for any `@Component` beans that contain `@Scheduled` methods, and then it executes them according to their configuration. Without `@EnableScheduling`, all `@Scheduled` annotations are ignored.cheduling: This is the master switch that activates Spring's entire scheduling subsystem. When Spring Boot starts, it sees this annotation and begins searching for any @Component beans that contain @Scheduled methods, and then it executes them according to their configuration. Without @EnableScheduling, all @Scheduled annotations are ignored.