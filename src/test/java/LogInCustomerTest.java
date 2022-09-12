import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import static org.apache.http.HttpStatus.*;
import request.Customer;

import request.CustomerClient;
import request.CustomerCredentials;
import request.CustomerGeneration;
import response.ResponseMessage;
import response.CreateCustomerResponse;

public class LogInCustomerTest {

    private Customer customer;
    private CustomerClient customerClient;
    private ResponseMessage responseMessage;
    private CreateCustomerResponse createCustomerResponse;
    private String accessToken;

    @Before
    public void setUp() {
        customer = CustomerGeneration.getDefault();
        customerClient = new CustomerClient();
        responseMessage = new ResponseMessage();
    }

    @After
    public void tearDown(){

        customerClient.delete(accessToken);

    }

    @Step("Send POST request to api/auth/register")
    public ValidatableResponse createCorrectCustomer() {
        ValidatableResponse response = customerClient.create(customer);
        return response;
    }

    @Step("Send POST request to api/auth/login with incorrect password")
    public ValidatableResponse logInCourierWithIncorrectPassword() {
        ValidatableResponse response = customerClient.logIn(CustomerCredentials.getWrongInFieldPassword(customer));
        return response;
    }

    @Step("Send POST request to api/auth/login with incorrect email")
    public ValidatableResponse logInCourierWithIncorrectEmail() {
        ValidatableResponse response = customerClient.logIn(CustomerCredentials.getWrongInFieldEmail(customer));
        return response;
    }

    @Step("Send POST request to api/auth/login")
    public ValidatableResponse logInCustomer() {
        ValidatableResponse response = customerClient.logIn(CustomerCredentials.from(customer));
        return response;
    }

    @Step("Send POST request to api/auth/login and deserialize into CreateCustomerResponse class")
    public CreateCustomerResponse sendResponseInLogInCustomerResponseClass(ValidatableResponse response){
        CreateCustomerResponse createCustomerResponse = response.extract().body().as(CreateCustomerResponse.class);
        return createCustomerResponse;
    }

    @Step("Assert status code is 200")
    public void compareStatusCodeTo200(ValidatableResponse response){
        int actualStatusCode = response.extract().statusCode();
        Assert.assertEquals(SC_OK, actualStatusCode);
    }

    @Step("Assert status code is 401")
    public void compareStatusCodeTo401(ValidatableResponse response){
        int actualStatusCode = response.extract().statusCode();
        Assert.assertEquals(SC_UNAUTHORIZED, actualStatusCode);
    }

    @Step("Assert successful message")
    public void compareMessageToSuccessfulMessage(CreateCustomerResponse createLogInCustomerResponse, ResponseMessage responseMessage){
        boolean actualMessage = createLogInCustomerResponse.isSuccess();
        Assert.assertEquals(responseMessage.isMessageSuccessCreateCustomer(), actualMessage);
    }

    @Step("Assert 401 error message Unauthorized")
    public void compareMessageToError401Unauthorized(ValidatableResponse response, ResponseMessage responseMessage){
        String actualMessage = response.extract().path("message");
        Assert.assertEquals(responseMessage.getMessageError401IncorrectEmailOrPassword(), actualMessage);
    }

    @Step("Check String AccessToken is not empty")
    public void checkAccessTokenIsNotEmpty(CreateCustomerResponse createLogInCustomerResponse){
        String actualAccessToken = createLogInCustomerResponse.getAccessToken();
        Assert.assertEquals( false, actualAccessToken.isEmpty());
    }

    @Step("Check String RefreshToken is not empty")
    public void checkRefreshTokenIsNotEmpty(CreateCustomerResponse createLogInCustomerResponse){
        String actualRefreshToken = createLogInCustomerResponse.getRefreshToken();
        Assert.assertEquals( false, actualRefreshToken.isEmpty());
    }

    @Step("Check String name is not empty")
    public void checkNameIsCustomerName(CreateCustomerResponse createLogInCustomerResponse){
        String actualName = createLogInCustomerResponse.getUser().getName();
        Assert.assertEquals(customer.getName(), actualName);
    }

    @Step("Check String email is not empty")
    public void checkEmailIsCustomerName(CreateCustomerResponse createLogInCustomerResponse){
        String actualEmail = createLogInCustomerResponse.getUser().getEmail();
        Assert.assertEquals(customer.getEmail(), actualEmail);
    }

    @Step("Get created accessToken")
    public String getAccessTokenCustomer(CreateCustomerResponse createLogInCustomerResponse) {
        accessToken = createLogInCustomerResponse.getAccessToken().substring(7);
        return accessToken;
    }

    @DisplayName("Customer log in")
    @Test
    public void successfullyLogIn() {
        createCorrectCustomer();
        ValidatableResponse response = logInCustomer();
        CreateCustomerResponse createLogInCustomerResponse = sendResponseInLogInCustomerResponseClass(response);
        compareStatusCodeTo200 (response);
        compareMessageToSuccessfulMessage(createLogInCustomerResponse, responseMessage);

        checkAccessTokenIsNotEmpty(createLogInCustomerResponse);
        checkRefreshTokenIsNotEmpty(createLogInCustomerResponse);
        checkNameIsCustomerName(createLogInCustomerResponse);
        checkEmailIsCustomerName(createLogInCustomerResponse);
        getAccessTokenCustomer(createLogInCustomerResponse);

    }

    @DisplayName("Customer log in with incorrect password")
    @Test
    public void logInWithIncorrectPasswordReturnWrongMessage() {
        ValidatableResponse response = createCorrectCustomer();
        CreateCustomerResponse createCustomerResponse = sendResponseInLogInCustomerResponseClass(response);
        ValidatableResponse loginResponse = logInCourierWithIncorrectPassword();
        compareMessageToError401Unauthorized(loginResponse,  responseMessage);
        compareStatusCodeTo401 (loginResponse);
        getAccessTokenCustomer(createCustomerResponse);

    }

    @DisplayName("Customer log in with incorrect email")
    @Test
    public void logInWithIncorrectEmailReturnWrongMessage() {
        ValidatableResponse response = createCorrectCustomer();
        CreateCustomerResponse createCustomerResponse = sendResponseInLogInCustomerResponseClass(response);
        ValidatableResponse loginResponse = logInCourierWithIncorrectEmail();
        compareMessageToError401Unauthorized(loginResponse,  responseMessage);
        compareStatusCodeTo401 (loginResponse);
        getAccessTokenCustomer(createCustomerResponse);

    }

}
