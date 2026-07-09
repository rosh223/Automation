# Automation Anywhere Test Framework

This repository contains an automated testing framework for the Automation Anywhere Community Edition. It demonstrates UI Automation using **Playwright (Java)** and API Automation using **RestAssured**.

## Prerequisites
- Docker and Docker Compose installed on your machine.
- Automation Anywhere Community Edition credentials.

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone <repo_url>
   cd automation-assignment
   ```

2. **Configure Credentials:**
   Copy the `.env.example` file and create a `.env` file in the root directory. Fill in your credentials:
   ```bash
   cp .env.example .env
   ```
   *Edit `.env` and add your username and password.*

3. **Locators & Endpoints Note:**
   Depending on the exact DOM structure and network traffic of your AA CE instance, you may need to update:
   - UI Locators in `src/main/java/com/automationanywhere/pages/`
   - API Endpoints in `src/test/java/com/automationanywhere/api/LearningInstanceApiTest.java`

## Running Tests

### Method 1: Using Docker (Recommended)
This runs the tests in a headless, containerized Playwright environment.
```bash
docker-compose up
```
*Wait for the container to finish. The test reports will be generated in the `target/` directory.*

### Method 2: Running Locally (Headed Mode)
To see the browser physically opening up:
1. Ensure Java 17+ and Maven are installed.
2. Open `src/test/java/com/automationanywhere/ui/FormRulesBuilderTest.java`.
3. Change `PlaywrightFactory.initBrowser("chromium", true);` to `false` for headed mode.
4. Run:
```bash
mvn clean test
```

## Viewing Test Reports (Allure)
After running the tests, an Allure report is generated. If you ran via Docker or Maven, execute:
```bash
mvn allure:serve
```
This will start a local web server and open a beautiful HTML report in your browser.
