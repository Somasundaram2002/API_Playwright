package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


public class test1 {

    Playwright playwright;
    APIRequestContext apiRequestContext;
    Gson gson;

    @BeforeClass
    public void before() {
        playwright = Playwright.create();
        Map<String, String> headers = new HashMap<>();
        headers.put("x-api-key", "reqres-free-v1");
        headers.put("Content-Type", "application/json");

        apiRequestContext = playwright.request().newContext(
                new APIRequest.NewContextOptions().setExtraHTTPHeaders(headers)
        );
        gson = new Gson();
        System.out.println("API Context Created ✅");
        Reporter.log("API Context Created ✅");
    }

    @Test
    public void getData() {
        System.out.println("======================================================================================");
        System.out.println("<----------- Testing get request ------------->");
        Reporter.log("========== GET Request Test Started ==========");

        APIResponse response = apiRequestContext.get("https://reqres.in/api/users/2");
        Reporter.log("GET Request sent to: https://reqres.in/api/users/2");

        String responseBody = response.text();
        int responseCode = response.status();

        System.out.println("Response code is: " + responseCode);
        Reporter.log("Response Code: " + responseCode);
        Assert.assertEquals(responseCode, 200, "Response code should be 200");

        System.out.println("ResponseBody is: " + responseBody);
        Reporter.log("Response Body Length: " + responseBody.length() + " characters");

        JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
        JsonObject data = jsonResponse.getAsJsonObject("data");

        String firstName = data.get("first_name").getAsString();
        System.out.println("First_name is: " + firstName);
        Reporter.log("First Name Retrieved: " + firstName);

        System.out.println("Test case passed ✅");
        Reporter.log("✅ GET Test Passed");
        Reporter.log("==========================================");
    }

    @Test
    public void PostData() {
        System.out.println("==================================================================================");
        System.out.println("<--------- Testing post request ----------->");
        Reporter.log("========== POST Request Test Started ==========");

        Map<String, Object> data = new HashMap<>();
        data.put("name", "Ravi");
        data.put("job", "software developer");
        System.out.println("Sending data to server... " + data);
        Reporter.log("Request Data: " + data.toString());

        APIResponse response = apiRequestContext.post("https://reqres.in/api/users",
                RequestOptions.create().setData(data));
        Reporter.log("POST Request sent to: https://reqres.in/api/users");

        int statusCode = response.status();
        System.out.println("Response Code: "+ statusCode);
        Reporter.log("Response Code: " + statusCode);
        Assert.assertEquals(statusCode, 201, "Status code should be 201 for creation");

        System.out.println("✅ Post request passed ");
        Reporter.log("✅ POST Test Passed");
        Reporter.log("==========================================");
    }

    @Test
    public void putData() {
        System.out.println("============================================================================");
        System.out.println("<----------- Testing PUT request (update data) ------------>");
        Reporter.log("========== PUT Request Test Started ==========");

        Map<String, Object> data = new HashMap<>();
        data.put("name", "Ravi updated");
        data.put("job", "software developer - updated");
        Reporter.log("Update Data: " + data.toString());

        APIResponse response = apiRequestContext.put("https://reqres.in/api/users/2",
                RequestOptions.create().setData(data));
        Reporter.log("PUT Request sent to: https://reqres.in/api/users/2");

        int statusCode = response.status();
        System.out.println("StatusCode is: " + statusCode);
        Reporter.log("Response Code: " + statusCode);
        Assert.assertEquals(statusCode, 200, "StatusCode should be 200 for update data");

        String responseBody = response.text();
        System.out.println("Response is: " + responseBody);
        Reporter.log("Response Body: " + responseBody);

        JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

        String updatedName = jsonResponse.get("name").getAsString();
        String updatedJob = jsonResponse.get("job").getAsString();
        String updatedTime = jsonResponse.get("updatedAt").getAsString();

        System.out.println("updated name is " + updatedName);
        System.out.println("updated job is " + updatedJob);
        System.out.println("updated time " + updatedTime);
        Reporter.log("Updated Name: " + updatedName);
        Reporter.log("Updated Job: " + updatedJob);
        Reporter.log("Updated Time: " + updatedTime);

        Assert.assertEquals(updatedName, "Ravi updated", "Name not updated");
        Assert.assertEquals(updatedJob, "software developer - updated", "Job not updated");
        Assert.assertNotNull(updatedTime, "Update request fails ❌");

        System.out.println("✅ PUT Test Passed");
        Reporter.log("✅ PUT Test Passed");
        Reporter.log("==========================================");
    }

    @Test
    public void deleteData() {
        System.out.println("================================================================================");
        System.out.println("<---------- Testing delete request ------------->");
        Reporter.log("========== DELETE Request Test Started ==========");

        APIResponse response = apiRequestContext.delete("https://reqres.in/api/users/2");
        Reporter.log("DELETE Request sent to: https://reqres.in/api/users/2");

        int StatusCode = response.status();
        System.out.println("statusCode is: " + StatusCode);
        Reporter.log("Response Code: " + StatusCode);
        Assert.assertEquals(StatusCode, 204, "Status code should be 204 for delete or empty");

        String responseBody = response.text();
        Reporter.log("Response Body is empty: " + responseBody.isEmpty());

        Assert.assertTrue(responseBody.isEmpty(), "Response should be empty after delete");

        System.out.println("✅ Delete Request test passed");
        Reporter.log("✅ DELETE Test Passed");
        Reporter.log("==========================================");
    }

    @AfterClass
    public void tearDown() {
        apiRequestContext.dispose();
        playwright.close();
        System.out.println("API content closed 💨");
        Reporter.log("API Context Closed");
    }
}
