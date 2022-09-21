package steps;

import request.*;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.MatcherAssert;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import response.ResponseMessage;
import response.CreateCustomerResponse;


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class StepForCreateCustomerTest {

    private Customer customer = CustomerGeneration.getDefault();
    ;
    private Customer customerFieldEmailEmpty = CustomerGeneration.getFieldEmailEmpty();
    private Customer customerFieldPasswordEmpty = CustomerGeneration.getFieldPasswordEmpty();
    private CustomerClient customerClient = new CustomerClient();
    private Customer customerFieldNameEmpty = CustomerGeneration.getFieldNameEmpty();
    private ResponseMessage responseMessage = new ResponseMessage();
    private CreateCustomerResponse createCustomerResponse;
    private String accessToken = "";


    @Step("Send POST request to api/auth/register")
    public ValidatableResponse checkCreateCorrectCustomer() {
        ValidatableResponse response = customerClient.create(customer);
        return response;
    }

    @Step("Send POST request to api/auth/register with field email is empty ")
    public ValidatableResponse checkCreateCustomerWithFieldEmailEmpty() {
        ValidatableResponse response = customerClient.create(customerFieldEmailEmpty);
        return response;
    }

    @Step("Send POST request to api/auth/register with field password is empty ")
    public ValidatableResponse checkCreateCustomerWithFieldPasswordEmpty() {
        ValidatableResponse response = customerClient.create(customerFieldPasswordEmpty);
        return response;
    }

    @Step("Send POST request to api/auth/register with field name is empty ")
    public ValidatableResponse checkCreateCustomerWithFieldNameEmpty() {
        ValidatableResponse response = customerClient.create(customerFieldNameEmpty);
        return response;
    }

    @Step("Send POST request to api/auth/register and deserialize into CreateCustomerResponse class")
    public CreateCustomerResponse sendResponseInCreateCustomerResponseClass(ValidatableResponse response) {
        CreateCustomerResponse createCustomerResponse = response.extract().body().as(CreateCustomerResponse.class);
        return createCustomerResponse;
    }

    @Step("Assert status code is 200")
    public void compareStatusCodeTo200(ValidatableResponse response) {
        int actualStatusCode = response.extract().statusCode();
        MatcherAssert.assertThat(actualStatusCode, equalTo(SC_OK));
    }

    @Step("Assert successful message")
    public void compareMessageToSuccessfulMessage(CreateCustomerResponse createCustomerResponse) {
        boolean actualMessage = createCustomerResponse.isSuccess();
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.isMessageSuccessCreateCustomer()));
    }

    @Step("Check String AccessToken is not empty")
    public void checkAccessTokenIsNotEmpty(CreateCustomerResponse createCustomerResponse) {
        String actualAccessToken = createCustomerResponse.getAccessToken();
        MatcherAssert.assertThat(actualAccessToken.isEmpty(), equalTo(false));

    }

    @Step("Check String RefreshToken is not empty")
    public void checkRefreshTokenIsNotEmpty(CreateCustomerResponse createCustomerResponse) {
        String actualRefreshToken = createCustomerResponse.getRefreshToken();
        //Assert.assertEquals(false, actualRefreshToken.isEmpty());
        MatcherAssert.assertThat(actualRefreshToken.isEmpty(), equalTo(false));
    }

    @Step("Check String name is not empty")
    public void checkNameIsCustomerName(CreateCustomerResponse createCustomerResponse) {
        String actualName = createCustomerResponse.getUser().getName();
        MatcherAssert.assertThat(actualName, equalTo(customer.getName()));
    }

    @Step("Check String email is not empty")
    public void checkEmailIsCustomerName(CreateCustomerResponse createCustomerResponse) {
        String actualEmail = createCustomerResponse.getUser().getEmail();
        MatcherAssert.assertThat(actualEmail, equalTo(customer.getEmail()));
    }

    @Step("Get created accessToken")
    public String getAccessTokenCustomer(CreateCustomerResponse createCustomerResponse) {
        accessToken = createCustomerResponse.getAccessToken().substring(7);
        return accessToken;
    }

    @Step("Assert status code is 403")
    public void compareStatusCodeTo403(ValidatableResponse response) {
        int actualStatusCode = response.extract().statusCode();
        MatcherAssert.assertThat(actualStatusCode, equalTo(SC_FORBIDDEN));
    }

    @Step("Assert 403 error message about double customer")
    public void compareMessageToError403DoubleCustomer(ValidatableResponse response) {
        String actualMessage = response.extract().path("message");
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.getMessageError403DoubleCustomer()));
    }

    @Step("Assert 403 error message about double customer")
    public void compareMessageToError403CustomerWithEmptyField(ValidatableResponse response) {
        String actualMessage = response.extract().path("message");
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.getMessageError403CustomerWithEmptyField()));
    }

    @Step("Delete customer")
    public void deleteCustomer() {
        customerClient.delete(accessToken);
    }
}