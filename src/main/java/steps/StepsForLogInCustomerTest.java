package steps;

import io.restassured.response.ValidatableResponse;
import org.hamcrest.MatcherAssert;
import io.qameta.allure.Step;
import request.*;
import response.ResponseMessage;
import response.CreateCustomerResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class StepsForLogInCustomerTest {
    private Customer customer = CustomerGeneration.getDefault();
    ;
    private CustomerClient customerClient = new CustomerClient();
    ;
    private ResponseMessage responseMessage = new ResponseMessage();
    ;
    private CreateCustomerResponse createCustomerResponse;
    private String accessToken = "";

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
    public CreateCustomerResponse sendResponseInLogInCustomerResponseClass(ValidatableResponse response) {
        CreateCustomerResponse createCustomerResponse = response.extract().body().as(CreateCustomerResponse.class);
        return createCustomerResponse;
    }

    @Step("Assert status code is 200")
    public void compareStatusCodeTo200(ValidatableResponse response) {
        int actualStatusCode = response.extract().statusCode();
        MatcherAssert.assertThat(actualStatusCode, equalTo(SC_OK));
    }

    @Step("Assert status code is 401")
    public void compareStatusCodeTo401(ValidatableResponse response) {
        int actualStatusCode = response.extract().statusCode();
        MatcherAssert.assertThat(actualStatusCode, equalTo(SC_UNAUTHORIZED));
    }

    @Step("Assert successful message")
    public void compareMessageToSuccessfulMessage(CreateCustomerResponse createLogInCustomerResponse) {
        boolean actualMessage = createLogInCustomerResponse.isSuccess();
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.isMessageSuccessCreateCustomer()));
    }

    @Step("Assert 401 error message Unauthorized")
    public void compareMessageToError401Unauthorized(ValidatableResponse response) {
        String actualMessage = response.extract().path("message");
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.getMessageError401IncorrectEmailOrPassword()));
    }

    @Step("Check String AccessToken is not empty")
    public void checkAccessTokenIsNotEmpty(CreateCustomerResponse createLogInCustomerResponse) {
        String actualAccessToken = createLogInCustomerResponse.getAccessToken();
        MatcherAssert.assertThat(actualAccessToken.isEmpty(), equalTo(false));
    }

    @Step("Check String RefreshToken is not empty")
    public void checkRefreshTokenIsNotEmpty(CreateCustomerResponse createLogInCustomerResponse) {
        String actualRefreshToken = createLogInCustomerResponse.getRefreshToken();
        MatcherAssert.assertThat(actualRefreshToken.isEmpty(), equalTo(false));
    }

    @Step("Check String name is not empty")
    public void checkNameIsCustomerName(CreateCustomerResponse createLogInCustomerResponse) {
        String actualName = createLogInCustomerResponse.getUser().getName();
        MatcherAssert.assertThat(actualName, equalTo(customer.getName()));
    }

    @Step("Check String email is not empty")
    public void checkEmailIsCustomerName(CreateCustomerResponse createLogInCustomerResponse) {
        String actualEmail = createLogInCustomerResponse.getUser().getEmail();
        MatcherAssert.assertThat(actualEmail, equalTo(customer.getEmail()));
    }

    @Step("Get created accessToken")
    public String getAccessTokenCustomer(CreateCustomerResponse createLogInCustomerResponse) {
        accessToken = createLogInCustomerResponse.getAccessToken().substring(7);
        return accessToken;
    }

    @Step("Delete customer")
    public void deleteCustomer() {
        customerClient.delete(accessToken);
    }
}
