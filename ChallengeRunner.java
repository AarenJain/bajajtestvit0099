package com.example.bajajtest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ChallengeRunner implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- Starting Bajaj Finserv Health Challenge ---");

        // Step 1 & 2: Generate Webhook and get access token [cite: 8]
        String generateWebhookUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA"; // [cite: 9]
        UserDetails userDetails = new UserDetails("John Doe", "REG12347", "john@example.com"); // [cite: 12, 13, 14]

        System.out.println("1. Sending request to generate webhook...");
        ResponseEntity<WebhookResponse> webhookResponseEntity = restTemplate.postForEntity(generateWebhookUrl, userDetails, WebhookResponse.class);
        
        if (webhookResponseEntity.getStatusCode() != HttpStatus.OK || webhookResponseEntity.getBody() == null) {
            System.err.println("Failed to generate webhook. Exiting.");
            return;
        }

        WebhookResponse webhookResponse = webhookResponseEntity.getBody();
        String submissionUrl = webhookResponse.getWebhookURL();
        String accessToken = webhookResponse.getAccessToken();

        System.out.println("   > Webhook URL Received: " + submissionUrl);
        System.out.println("   > Access Token Received.");

        // Step 3: Solve the SQL problem based on regNo [cite: 19]
        String regNo = userDetails.getRegNo();
        // Extract the last two digits. If less than 2 digits, take the number itself.
        String numericPart = regNo.replaceAll("[^0-9]", "");
        int lastTwoDigits = Integer.parseInt(numericPart.substring(Math.max(0, numericPart.length() - 2)));

        String finalSqlQuery;
        if (lastTwoDigits % 2 != 0) {
            // Odd Number: Use Question 1 [cite: 20]
            System.out.println("2. Registration number ends in an odd number (" + lastTwoDigits + "). Solving Question 1.");
            finalSqlQuery = """
            SELECT
                P.AMOUNT AS SALARY,
                CONCAT(E.FIRST_NAME, ' ', E.LAST_NAME) AS NAME,
                TIMESTAMPDIFF(YEAR, E.DOB, CURDATE()) AS AGE,
                D.DEPARTMENT_NAME
            FROM
                PAYMENTS AS P
            JOIN
                EMPLOYEE AS E ON P.EMP_ID = E.EMP_ID
            JOIN
                DEPARTMENT AS D ON E.DEPARTMENT = D.DEPARTMENT_ID
            WHERE
                DAY(P.PAYMENT_TIME) <> 1
            ORDER BY
                SALARY DESC
            LIMIT 1;
            """;
        } else {
            // Even Number: Placeholder for Question 2 [cite: 22]
            System.out.println("2. Registration number ends in an even number (" + lastTwoDigits + "). Solving Question 2.");
            // Replace with the query for Question 2 if needed.
            finalSqlQuery = "SELECT 'Query for even number problem goes here';";
        }
        
        System.out.println("   > Final SQL Query prepared.");

        // Step 4: Submit the solution [cite: 24]
        System.out.println("3. Submitting the final SQL query to the webhook URL...");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken); // Set JWT token in header [cite: 27]

        SolutionRequest solutionRequest = new SolutionRequest(finalSqlQuery);
        HttpEntity<SolutionRequest> requestEntity = new HttpEntity<>(solutionRequest, headers);

        // Using the URL from the first response to submit the answer
        ResponseEntity<String> submissionResponse = restTemplate.postForEntity(submissionUrl, requestEntity, String.class);

        System.out.println("   > Submission Status: " + submissionResponse.getStatusCode());
        System.out.println("   > Submission Response Body: " + submissionResponse.getBody());
        System.out.println("--- Challenge Completed ---");
    }
}