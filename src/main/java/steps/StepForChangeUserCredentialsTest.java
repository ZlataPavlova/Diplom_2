package steps;

import io.qameta.allure.Step;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.CoreMatchers.equalTo;
import io.restassured.response.ValidatableResponse;
import request.Customer;
import response.RefreshCustomer;
import request.CustomerClient;
import request.CustomerCredentials;
import request.CustomerGeneration;
import response.ResponseMessage;
import response.CreateCustomerResponse;
import static org.apache.http.HttpStatus.*;

public class StepForChangeUserCredentialsTest {
    private Customer customer = CustomerGeneration.getDefault();
    private Customer customerChange = CustomerGeneration.getNewCredentials();
    ;
    private Customer customerWithRepeatEmail = CustomerGeneration.getRepeatCredentials();
    private CustomerClient customerClient = new CustomerClient();
    private ResponseMessage responseMessage = new ResponseMessage();
    private CreateCustomerResponse createCustomerResponse;
    private RefreshCustomer refreshCustomer;
    private String accessToken = "";

    @Step("Send POST request to api/auth/register")
    public ValidatableResponse createCorrectCustomer() {
        System.out.println(customer);
        ValidatableResponse response = customerClient.create(customer);
        return response;
    }

    @Step("Send POST request to api/auth/register for create customer with repeat email")
    public ValidatableResponse createCustomerWithRepeatEmail() {
        ValidatableResponse response = customerClient.create(customerWithRepeatEmail);
        return response;
    }

    @Step("Send PATH request to api/auth/user")
    public ValidatableResponse changeCredentialsRegisteredCustomer() {
        ValidatableResponse response = customerClient.change(customerChange, accessToken);
        return response;
    }

    @Step("Send PATH request to api/auth/user and deserialize into RefreshCustomer class")
    public RefreshCustomer sendResponseChangeCustomerResponseClass(ValidatableResponse response) {
        RefreshCustomer refreshCustomerResponse = response.extract().body().as(RefreshCustomer.class);
        return refreshCustomerResponse;
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

    @Step("Get created accessToken")
    public String getAccessTokenCustomer(CreateCustomerResponse createCustomerResponse) {
        accessToken = createCustomerResponse.getAccessToken().substring(7);
        return accessToken;
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

    @Step("Assert status code is 403")
    public void compareStatusCodeTo403(ValidatableResponse response) {
        int actualStatusCode = response.extract().statusCode();
        MatcherAssert.assertThat(actualStatusCode, equalTo(SC_FORBIDDEN));
    }

    @Step("Assert successful message")
    public void compareMessageToSuccessfulMessage(RefreshCustomer refreshCustomerResponse) {
        boolean actualMessage = refreshCustomerResponse.isSuccess();
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.isMessageSuccessCreateCustomer()));
    }

    @Step("Assert 401 error message Unauthorized")
    public void compareMessageToError401Unauthorized(ValidatableResponse response) {
        String actualMessage = response.extract().path("message");
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.getMessageError401Unauthorized()));
    }

    @Step("Assert 403 error message Forbidden")
    public void compareMessageToError403Forbidden(ValidatableResponse response) {
        String actualMessage = response.extract().path("message");
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.getMessageError403CustomerWithRepeatPassword()));
    }

    @Step("Assert unsuccessful message")
    public void compareMessageToUnsuccessfulMessage(ValidatableResponse response) {
        boolean actualMessage = response.extract().path("success");
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.isMessageUnsuccessfulCreateCustomer()));
    }

    @Step("Check String name is not empty")
    public void checkNameIsCustomerName(RefreshCustomer refreshCustomerResponse) {
        String actualName = refreshCustomerResponse.getUser().getName();
        MatcherAssert.assertThat(actualName, equalTo(customerChange.getName()));
    }

    @Step("Check String email is not empty")
    public void checkEmailIsCustomerName(RefreshCustomer refreshCustomerResponse) {
        String actualEmail = refreshCustomerResponse.getUser().getEmail();
        MatcherAssert.assertThat(actualEmail, equalTo(customerChange.getEmail()));
    }

    @Step("Delete customer")
    public void deleteCustomer() {
        customerClient.delete(accessToken);
    }

}
