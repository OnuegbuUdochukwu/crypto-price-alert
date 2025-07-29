# Crypto Price Alert Service ⏰

A backend Spring Boot application that runs a scheduled task to monitor the price of a specific cryptocurrency (btcngn). It triggers a one-time alert in the console when the price crosses a predefined target and then programmatically shuts down.

This is a non-web application and does not expose any API endpoints. Its sole purpose is to run as a background service.

## Features
- Uses Spring's `@Scheduled` for periodic background tasks.
- Fetches live price data from the Quidax API.
- Uses `BigDecimal` for accurate financial comparisons.
- Triggers a one-time console alert.
- Programmatically shuts down after the alert condition is met using `System.exit(0)`.

## Technologies Used
- Java 17
- Spring Boot 3
- Spring Scheduler (`@EnableScheduling`, `@Scheduled`)
- Maven
- Lombok

## How to Run

### Prerequisites
- JDK (Java Development Kit) 17 or later
- Maven

### Installation & Execution
Clone the repository:

```bash
git clone https://github.com/your-username/crypto-price-alert.git
```

Navigate to the project directory:

```bash
cd crypto-price-alert
```

Run the application using the Maven wrapper:

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

## What to Expect
This is a non-web application. There are no endpoints to test in your browser.

Watch the console output directly in your terminal or IDE. You will see a message appear every 10 seconds:

> Checking price for btcngn at 10:49:05.123456789

If the price of btcngn crosses the target price set in the code, a final alert will be printed, and the application will shut down.