# Automation Anywhere Test Framework

A robust, containerized testing framework designed to validate both UI and API interactions within the Automation Anywhere Community Edition platform.

## Table of Contents
1. [Architecture & Data Flow](#architecture--data-flow)
2. [Design & Tool Choices](#design--tool-choices)
3. [Prerequisites](#prerequisites)
4. [Setup Instructions](#setup-instructions)
5. [Execution Guide](#execution-guide)
6. [Test Reporting](#test-reporting)

---

## Architecture & Data Flow

The framework is designed to run seamlessly inside a Docker container, executing tests against the live Automation Anywhere web application.

```mermaid
flowchart TD
    subgraph Local Machine
        env[.env Credentials]
        docker[Docker Compose]
    end

    subgraph Container Environment
        playwright_img[Playwright Java Image]
        
        subgraph Test Suite
            ui[UI Tests POM]
            api[API Tests RestAssured]
        end
        
        allure[Allure Reporter]
    end

    subgraph Automation Anywhere Platform
        aa_ui[Web UI Dashboard/Form Builder]
        aa_api[REST API Endpoints]
    end

    env --> docker
    docker --> playwright_img
    playwright_img --> ui
    playwright_img --> api
    
    ui -- "Browser Automation" --> aa_ui
    api -- "HTTP Requests" --> aa_api
    
    ui --> allure
    api --> allure
```

---

## Design & Tool Choices

### Tool Stack
- **Java 17 & Maven**: The industry standard for robust enterprise test automation. Maven handles our dependency management.
- **Playwright (Java Port)**: Chosen over Selenium for UI automation due to its superior speed, auto-waiting mechanism, and native support for modern web elements (like shadow DOM and complex drag-and-drop operations).
- **RestAssured**: Provides a highly readable, behavior-driven (Given/When/Then) syntax for API testing and assertions.
- **Allure**: Used for generating comprehensive, visual HTML test reports that include execution time and step breakdowns.
- **Docker Compose**: Eliminates the "it works on my machine" problem by standardizing the test execution environment.

### Design Patterns
- **Page Object Model (POM)**: The UI tests are separated into "Page Classes" (e.g., `FormBuilderPage.java`, `RulesBuilderPage.java`). This ensures that if the UI changes, we only update the locators in one place, keeping the test scripts clean.
- **ThreadLocal Browser Factory**: The `PlaywrightFactory.java` uses `ThreadLocal` storage for the browser context. This allows tests to be safely run in parallel in the future without browser sessions overlapping or leaking data.
- **Configuration Management**: Credentials are never hardcoded. We use `java-dotenv` to inject sensitive information at runtime.
- **POJO Data Modeling**: API request payloads are structured as Java Objects (`LearningInstanceRequest.java`) and automatically serialized into JSON using Jackson, ensuring strict schema enforcement.

---

## Prerequisites

- **Docker** and **Docker Compose** installed on your system.
- Registered credentials for the Automation Anywhere Community Edition.

---

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone <repo_url>
   cd automation-assignment
   ```

2. **Configure Credentials:**
   To securely pass your login details to the test suite, duplicate the example environment file and fill it in.
   ```bash
   cp .env.example .env
   ```
   Open the new `.env` file in your editor and provide your username and password.

3. **Locator Adjustments:**
   Web applications frequently update their DOM elements. If a test fails to find an element, inspect the page and update the respective selector inside `src/main/java/com/automationanywhere/pages/`.

---

## Execution Guide

### Using Docker (Recommended)
This is the most reliable way to run the suite. It uses the official Microsoft Playwright image and runs entirely headlessly (no browser pops up). It also caches Maven dependencies to drastically speed up consecutive runs.

```bash
docker-compose up
```
*Once the container exits, test results are automatically mapped back to your local `target/` directory.*

### Running Locally (Headed Mode)
If you want to debug the tests and watch the browser magically click buttons on your screen:
1. Ensure Java 17 and Maven are installed locally.
2. Open `src/test/java/com/automationanywhere/ui/FormRulesBuilderTest.java`.
3. Change `PlaywrightFactory.initBrowser("chromium", true)` to `false` (turns off headless mode).
4. Run the suite:
   ```bash
   mvn clean test
   ```

---

## Test Reporting

The framework generates rich Allure test reports. After execution (either via Docker or locally), you can serve the report to view the results in your browser:

```bash
mvn allure:serve
```
*Note: This command spins up a temporary local web server and opens the HTML report dashboard.*
