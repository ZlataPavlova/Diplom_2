package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.MatcherAssert;
import request.*;
import response.CreateCustomerResponse;
import response.GetOrdersResponse;
import response.ResponseMessage;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;

public class StepForGetOrdersTest {
    private Customer customer = CustomerGeneration.getDefault();
    private IngredientsRequest ingredientsRequest = OrderGeneration.getDefault();
    ;
    private CustomerClient customerClient = new CustomerClient();
    ;
    private OrderClient orderClient = new OrderClient();
    private String accessToken = "";
    private ResponseMessage responseMessage = new ResponseMessage();


    @Step("Send POST request to api/auth/register")
    public ValidatableResponse correctCustomer() {
        ValidatableResponse response = customerClient.create(customer);
        return response;
    }

    @Step("Send POST request to api/orders")
    public ValidatableResponse createOrder() {
        ValidatableResponse response = orderClient.create(ingredientsRequest, accessToken);
        return response;
    }

    @Step("Send POST request to api/auth/login and deserialize into CreateCustomerResponse class")
    public CreateCustomerResponse sendResponseInLogInCustomerResponseClass(ValidatableResponse response) {
        CreateCustomerResponse createCustomerResponse = response.extract().body().as(CreateCustomerResponse.class);
        return createCustomerResponse;
    }

    @Step("Send POST request to api/auth/login")
    public ValidatableResponse logInCustomer() {
        ValidatableResponse response = customerClient.logIn(CustomerCredentials.from(customer));
        return response;
    }

    @Step("Send GET request to 'api/orders'")
    public ValidatableResponse sendResponseInJson() {
        ValidatableResponse response = orderClient.getListOrders(accessToken);

        return response;
    }

    @Step("Send POST request to api/orders into GetOrdersResponse class")
    public GetOrdersResponse sendResponseInGetOrdersResponseClass(ValidatableResponse response) {
        GetOrdersResponse getOrdersResponse = response.extract().body().as(GetOrdersResponse.class);
        return getOrdersResponse;
    }

    @Step("Assert successful message")
    public void compareMessageToSuccessfulMessage(GetOrdersResponse getOrdersResponse) {
        boolean actualMessage = getOrdersResponse.isSuccess();
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.isMessageSuccessCreateCustomer()));
    }

    @Step("Assert unsuccessful message")
    public void compareMessageToUnsuccessfulMessage(ValidatableResponse response) {
        boolean actualMessage = response.extract().path("success");
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.isMessageUnsuccessfulCreateCustomer()));
    }

    @Step("Assert 401 error message Unauthorized")
    public void compareMessageToError401Unauthorized(ValidatableResponse response) {
        String actualMessage = response.extract().path("message");
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.getMessageError401Unauthorized()));
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

    @Step("Get created accessToken")
    public String getAccessTokenCustomer(CreateCustomerResponse createLogInCustomerResponse) {
        accessToken = createLogInCustomerResponse.getAccessToken().substring(7);
        return accessToken;
    }

    @Step("Check int total is not null in createOrder")
    public void checkTotalInGetOrder(GetOrdersResponse getOrdersResponse) {
        int actualTotal = getOrdersResponse.getTotal();
        MatcherAssert.assertThat(actualTotal, notNullValue());
    }

    @Step("Check int totalToday is not null in createOrder")
    public void checkTotalTodayInGetOrder(GetOrdersResponse getOrdersResponse) {
        int actualTotal = getOrdersResponse.getTotal();
        MatcherAssert.assertThat(actualTotal, notNullValue());
    }

    @Step("Check String id is not empty in orders")
    public void checkOrdersIsNotEmpty(GetOrdersResponse getOrdersResponse) {
        String actualOrders = getOrdersResponse.getOrders().toString();
        MatcherAssert.assertThat(actualOrders.isEmpty(), equalTo(false));
    }

    @Step("Check Ingredients object is not null")
    public void checkIngredientsIsNotNull(GetOrdersResponse getOrdersResponse) {
        MatcherAssert.assertThat(getOrdersResponse.getOrders().toString(), notNullValue());
    }

    @Step("Delete customer")
    public void deleteCustomer() {
        customerClient.delete(accessToken);
    }
}
