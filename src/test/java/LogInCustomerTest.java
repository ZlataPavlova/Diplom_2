import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import response.CreateCustomerResponse;

import steps.StepsForLogInCustomerTest;

public class LogInCustomerTest {
    StepsForLogInCustomerTest stepsForLogInCustomerTest = new StepsForLogInCustomerTest();

    @After
    public void tearDown() {
        stepsForLogInCustomerTest.deleteCustomer();
    }

    @DisplayName("Customer log in")
    @Test
    public void successfullyLogIn() {
        stepsForLogInCustomerTest.createCorrectCustomer();
        ValidatableResponse response = stepsForLogInCustomerTest.logInCustomer();
        CreateCustomerResponse createLogInCustomerResponse = stepsForLogInCustomerTest.sendResponseInLogInCustomerResponseClass(response);
        stepsForLogInCustomerTest.compareStatusCodeTo200(response);
        stepsForLogInCustomerTest.compareMessageToSuccessfulMessage(createLogInCustomerResponse);

        stepsForLogInCustomerTest.checkAccessTokenIsNotEmpty(createLogInCustomerResponse);
        stepsForLogInCustomerTest.checkRefreshTokenIsNotEmpty(createLogInCustomerResponse);
        stepsForLogInCustomerTest.checkNameIsCustomerName(createLogInCustomerResponse);
        stepsForLogInCustomerTest.checkEmailIsCustomerName(createLogInCustomerResponse);
        stepsForLogInCustomerTest.getAccessTokenCustomer(createLogInCustomerResponse);
    }

    @DisplayName("Customer log in with incorrect password")
    @Test
    public void logInWithIncorrectPasswordReturnWrongMessage() {
        ValidatableResponse response = stepsForLogInCustomerTest.createCorrectCustomer();
        CreateCustomerResponse createCustomerResponse = stepsForLogInCustomerTest.sendResponseInLogInCustomerResponseClass(response);
        ValidatableResponse loginResponse = stepsForLogInCustomerTest.logInCourierWithIncorrectPassword();
        stepsForLogInCustomerTest.compareMessageToError401Unauthorized(loginResponse);
        stepsForLogInCustomerTest.compareStatusCodeTo401(loginResponse);
        stepsForLogInCustomerTest.getAccessTokenCustomer(createCustomerResponse);
    }

    @DisplayName("Customer log in with incorrect email")
    @Test
    public void logInWithIncorrectEmailReturnWrongMessage() {
        ValidatableResponse response = stepsForLogInCustomerTest.createCorrectCustomer();
        CreateCustomerResponse createCustomerResponse = stepsForLogInCustomerTest.sendResponseInLogInCustomerResponseClass(response);
        ValidatableResponse loginResponse = stepsForLogInCustomerTest.logInCourierWithIncorrectEmail();
        stepsForLogInCustomerTest.compareMessageToError401Unauthorized(loginResponse);
        stepsForLogInCustomerTest.compareStatusCodeTo401(loginResponse);
        stepsForLogInCustomerTest.getAccessTokenCustomer(createCustomerResponse);
    }
}
