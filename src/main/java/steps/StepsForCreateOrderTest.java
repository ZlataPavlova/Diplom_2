package steps;

import io.restassured.response.ValidatableResponse;
import org.hamcrest.MatcherAssert;
import io.qameta.allure.Step;
import request.*;
import response.ResponseMessage;
import response.CreateCustomerResponse;
import response.CreateOrderResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.*;

public class StepsForCreateOrderTest {

    private Customer customer = CustomerGeneration.getDefault();
    private CustomerClient customerClient = new CustomerClient();
    private OrderClient orderClient = new OrderClient();
    private ResponseMessage responseMessage = new ResponseMessage();
    private IngredientsRequest ingredientsRequest = OrderGeneration.getDefault();
    ;
    private IngredientsRequest withoutIngredientsRequest = OrderGeneration.getOrderWithoutIngredients();
    ;
    private IngredientsRequest incorrectIngredientsRequest = OrderGeneration.getOrderWithIncorrectIngredients();
    ;
    private String accessToken = "";

    @Step("Send POST request to api/orders")
    public ValidatableResponse createOrder() {
        ValidatableResponse response = orderClient.create(ingredientsRequest, accessToken);
        return response;
    }

    @Step("Send POST request to api/orders without ingredients")
    public ValidatableResponse checkOrderWithoutIngredients() {
        ValidatableResponse response = orderClient.create(withoutIngredientsRequest, accessToken);
        return response;
    }

    @Step("Send POST request to api/orders with incorrect ingredients")
    public ValidatableResponse checkOrderWithIncorrectIngredients() {
        ValidatableResponse response = orderClient.create(incorrectIngredientsRequest, accessToken);
        return response;
    }

    @Step("Send POST request to api/orders into CreateOrderResponse class")
    public CreateOrderResponse sendResponseInCreateOrderResponseClass(ValidatableResponse response) {
        CreateOrderResponse createOrderResponse = response.extract().body().as(CreateOrderResponse.class);
        return createOrderResponse;
    }

    @Step("Send POST request to api/auth/register")
    public ValidatableResponse createCorrectCustomer() {
        ValidatableResponse response = customerClient.create(customer);
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

    @Step("Send POST request to api/auth/login")
    public ValidatableResponse logInCustomer() {
        ValidatableResponse response = customerClient.logIn(CustomerCredentials.from(customer));
        return response;
    }

    @Step("Assert status code is 400")
    public void compareStatusCodeTo400(ValidatableResponse response) {
        int actualStatusCode = response.extract().statusCode();
        MatcherAssert.assertThat(actualStatusCode, equalTo(SC_BAD_REQUEST));
    }

    @Step("Assert status code is 500")
    public void compareStatusCodeTo500(ValidatableResponse response) {
        int actualStatusCode = response.extract().statusCode();
        MatcherAssert.assertThat(actualStatusCode, equalTo(SC_INTERNAL_SERVER_ERROR));
    }

    @Step("Assert successful message")
    public void compareMessageToSuccessfulMessage(CreateOrderResponse createOrderResponse) {
        boolean actualMessage = createOrderResponse.isSuccess();
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.isMessageSuccessCreateCustomer()));
    }

    @Step("Assert unsuccessful message")
    public void compareMessageToUnsuccessfulMessage(ValidatableResponse response) {
        boolean actualMessage = response.extract().path("success");
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.isMessageUnsuccessfulCreateCustomer()));
    }

    @Step("Assert 400 error message about in order should be ingredients")
    public void compareMessageToError400ShouldBeIngredients(ValidatableResponse response) {
        String actualMessage = response.extract().path("message");
        MatcherAssert.assertThat(actualMessage, equalTo(responseMessage.getMessageError400ShouldBeIngredients()));
    }

    @Step("Check String name is not empty in createOrder")
    public void checkNameInCreateOrder(CreateOrderResponse createOrderResponse) {
        String actualName = createOrderResponse.getName();
        MatcherAssert.assertThat(actualName, equalTo("Метеоритный флюоресцентный традиционный-галактический бургер"));
    }

    @Step("Check int number is not empty in createOrder")
    public void checkNumberInCreateOrder(CreateOrderResponse createOrderResponse) {
        int actualNumber = createOrderResponse.getOrderResponse().getNumber();
        MatcherAssert.assertThat(actualNumber, notNullValue());
    }

    @Step("Check String id is not empty")
    public void checkIdInOrder(CreateOrderResponse createOrderResponse) {
        String actualId = createOrderResponse.getOrderResponse().get_id();
        MatcherAssert.assertThat(actualId.isEmpty(), equalTo(false));
    }

    @Step("Check String status is not empty")
    public void checkStatusInOrder(CreateOrderResponse createOrderResponse) {
        String actualStatus = createOrderResponse.getOrderResponse().getStatus();
        MatcherAssert.assertThat(actualStatus.isEmpty(), equalTo(false));
    }

    @Step("Check String name is not empty in Order")
    public void checkNameInOrder(CreateOrderResponse createOrderResponse) {
        String actualName = createOrderResponse.getOrderResponse().getName();
        MatcherAssert.assertThat(actualName.isEmpty(), equalTo(false));
    }

    @Step("Check String createdAt is not empty in Order")
    public void checkCreatedAtInOrder(CreateOrderResponse createOrderResponse) {
        String actualCreatedAt = createOrderResponse.getOrderResponse().getCreatedAt();
        MatcherAssert.assertThat(actualCreatedAt.isEmpty(), equalTo(false));
    }

    @Step("Check String updatedAt is not empty in Order")
    public void checkUpdatedAtInOrder(CreateOrderResponse createOrderResponse) {
        String actualUpdatedAt = createOrderResponse.getOrderResponse().getUpdatedAt();
        MatcherAssert.assertThat(actualUpdatedAt.isEmpty(), equalTo(false));
    }

    @Step("Check int number is not empty in Order")
    public void checkNumberInOrder(CreateOrderResponse createOrderResponse) {
        int actualNumber = createOrderResponse.getOrderResponse().getNumber();
        MatcherAssert.assertThat(actualNumber, notNullValue());
    }

    @Step("Check int price is not empty in Order")
    public void checkPriceInOrder(CreateOrderResponse createOrderResponse) {
        int actualPrice = createOrderResponse.getOrderResponse().getPrice();
        MatcherAssert.assertThat(actualPrice, notNullValue());
    }

    @Step("Check String name is not empty in owner")
    public void checkNameInOwner(CreateOrderResponse createOrderResponse) {
        String actualName = createOrderResponse.getOrderResponse().getOwner().getName();
        MatcherAssert.assertThat(actualName, equalTo(customer.getName()));
    }

    @Step("Check String email is not empty in owner")
    public void checkEmailInOwner(CreateOrderResponse createOrderResponse) {
        String actualEmail = createOrderResponse.getOrderResponse().getOwner().getEmail();
        MatcherAssert.assertThat(actualEmail, equalTo(customer.getEmail()));
    }

    @Step("Check String createdAt is not empty in owner")
    public void checkCreatedAtInOwner(CreateOrderResponse createOrderResponse) {
        String actualCreatedAt = createOrderResponse.getOrderResponse().getOwner().getCreatedAt();
        MatcherAssert.assertThat(actualCreatedAt.isEmpty(), equalTo(false));
    }

    @Step("Check String updatedAt is not empty in owner")
    public void checkUpdatedAtInOwner(CreateOrderResponse createOrderResponse) {
        String actualUpdatedAt = createOrderResponse.getOrderResponse().getOwner().getUpdatedAt();
        MatcherAssert.assertThat(actualUpdatedAt.isEmpty(), equalTo(false));
    }

    @Step("Check String id is not empty in ingredients")
    public void checkIngredientsIsNotEmpty(CreateOrderResponse createOrderResponse) {
        String actualIngredients = createOrderResponse.getOrderResponse().getIngredients().toString();
        MatcherAssert.assertThat(actualIngredients.isEmpty(), equalTo(false));
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
