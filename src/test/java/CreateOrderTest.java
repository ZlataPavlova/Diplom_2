
import request.*;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import response.ResponseMessage;
import response.CreateCustomerResponse;
import request.CustomerClient;
import request.OrderClient;
import response.CreateOrderResponse;

import static org.hamcrest.CoreMatchers.notNullValue;
import org.hamcrest.MatcherAssert;


import static org.apache.http.HttpStatus.*;

public class CreateOrderTest {

    private Customer customer;
    private CustomerClient customerClient;
    private OrderClient orderClient;
    private ResponseMessage responseMessage;
    private IngredientsRequest ingredientsRequest;
    private IngredientsRequest withoutIngredientsRequest;
    private IngredientsRequest incorrectIngredientsRequest;
    private String accessToken;

    @Before
    public void setUp() {
        customer = CustomerGeneration.getDefault();
        customerClient = new CustomerClient();
        responseMessage = new ResponseMessage();
        ingredientsRequest = OrderGeneration.getDefault();
        incorrectIngredientsRequest = OrderGeneration.getOrderWithIncorrectIngredients();
        withoutIngredientsRequest = OrderGeneration.getOrderWithoutIngredients();
        orderClient = new OrderClient();
    }

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
    public CreateOrderResponse sendResponseInCreateOrderResponseClass(ValidatableResponse response){
        CreateOrderResponse createOrderResponse = response.extract().body().as(CreateOrderResponse.class);
        return createOrderResponse;
    }

    @Step("Send POST request to api/auth/register")
    public ValidatableResponse createCorrectCustomer() {
        ValidatableResponse response = customerClient.create(customer);
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

    @Step("Send POST request to api/auth/login")
    public ValidatableResponse logInCustomer() {
        ValidatableResponse response = customerClient.logIn(CustomerCredentials.from(customer));
        return response;
    }



    @Step("Assert status code is 400")
    public void compareStatusCodeTo400(ValidatableResponse response){
        int actualStatusCode = response.extract().statusCode();
        Assert.assertEquals(SC_BAD_REQUEST , actualStatusCode);
    }

    @Step("Assert status code is 500")
    public void compareStatusCodeTo500(ValidatableResponse response){
        int actualStatusCode = response.extract().statusCode();
        Assert.assertEquals(SC_INTERNAL_SERVER_ERROR, actualStatusCode);
    }
    @Step("Assert successful message")
    public void compareMessageToSuccessfulMessage(CreateOrderResponse createOrderResponse, ResponseMessage responseMessage){
        boolean actualMessage = createOrderResponse.isSuccess();
        Assert.assertEquals(responseMessage.isMessageSuccessCreateCustomer(), actualMessage);
    }
    @Step("Assert unsuccessful message")
    public void compareMessageToUnsuccessfulMessage(ValidatableResponse response, ResponseMessage responseMessage){
        boolean actualMessage = response.extract().path("success");
        Assert.assertEquals(responseMessage.isMessageUnsuccessfulCreateCustomer(), actualMessage);
    }

    @Step("Assert 400 error message about in order should be ingredients")
    public void compareMessageToError400ShouldBeIngredients(ValidatableResponse response, ResponseMessage responseMessage){
        String actualMessage = response.extract().path("message");
        Assert.assertEquals(responseMessage.getMessageError400ShouldBeIngredients(), actualMessage);
    }

    @Step("Check String name is not empty in createOrder")
    public void checkNameInCreateOrder(CreateOrderResponse createOrderResponse){
        String actualName = createOrderResponse.getName();
        Assert.assertEquals("Метеоритный традиционный-галактический флюоресцентный бургер", actualName);
    }

    @Step("Check int number is not empty in createOrder")
    public void checkNumberInCreateOrder(CreateOrderResponse createOrderResponse){
        int actualNumber = createOrderResponse.getOrderResponse().getNumber();
        MatcherAssert.assertThat(actualNumber, notNullValue());
    }

    @Step("Check String id is not empty")
    public void checkIdInOrder(CreateOrderResponse createOrderResponse){
        String actualId = createOrderResponse.getOrderResponse().get_id();
        Assert.assertEquals(actualId.isEmpty(), false);
    }

    @Step("Check String status is not empty")
    public void checkStatusInOrder(CreateOrderResponse createOrderResponse){
        String actualStatus = createOrderResponse.getOrderResponse().getStatus();
        Assert.assertEquals(actualStatus.isEmpty(), false);
    }

    @Step("Check String name is not empty in Order")
    public void checkNameInOrder(CreateOrderResponse createOrderResponse){
        String actualName = createOrderResponse.getOrderResponse().getName();
        Assert.assertEquals(actualName.isEmpty(), false);
    }

    @Step("Check String createdAt is not empty in Order")
    public void checkCreatedAtInOrder(CreateOrderResponse createOrderResponse){
        String actualCreatedAt = createOrderResponse.getOrderResponse().getCreatedAt();
        Assert.assertEquals(actualCreatedAt.isEmpty(), false);
    }

    @Step("Check String updatedAt is not empty in Order")
    public void checkUpdatedAtInOrder(CreateOrderResponse createOrderResponse){
        String actualUpdatedAt = createOrderResponse.getOrderResponse().getUpdatedAt();
        Assert.assertEquals(actualUpdatedAt.isEmpty(), false);
    }

    @Step("Check int number is not empty in Order")
    public void checkNumberInOrder(CreateOrderResponse createOrderResponse){
        int actualNumber = createOrderResponse.getOrderResponse().getNumber();
        MatcherAssert.assertThat(actualNumber, notNullValue());
    }

    @Step("Check int price is not empty in Order")
    public void checkPriceInOrder(CreateOrderResponse createOrderResponse){
        int actualPrice = createOrderResponse.getOrderResponse().getPrice();
        MatcherAssert.assertThat(actualPrice, notNullValue());
    }


    @Step("Check String name is not empty in owner")
    public void checkNameInOwner(CreateOrderResponse createOrderResponse){
        String actualName = createOrderResponse.getOrderResponse().getOwner().getName();
        Assert.assertEquals(customer.getName(), actualName);
    }

    @Step("Check String email is not empty in owner")
    public void checkEmailInOwner(CreateOrderResponse createOrderResponse){
        String actualEmail = createOrderResponse.getOrderResponse().getOwner().getEmail();
        Assert.assertEquals(customer.getEmail(), actualEmail);
    }

    @Step("Check String createdAt is not empty in owner")
    public void checkCreatedAtInOwner(CreateOrderResponse createOrderResponse){
        String actualCreatedAt = createOrderResponse.getOrderResponse().getOwner().getCreatedAt();
        Assert.assertEquals(actualCreatedAt.isEmpty(), false);
    }

    @Step("Check String updatedAt is not empty in owner")
    public void checkUpdatedAtInOwner(CreateOrderResponse createOrderResponse){
        String actualUpdatedAt = createOrderResponse.getOrderResponse().getOwner().getUpdatedAt();
        Assert.assertEquals(actualUpdatedAt.isEmpty(), false);
    }

    @Step("Check String id is not empty in ingredients")
    public void checkIngredientsIsNotEmpty(CreateOrderResponse createOrderResponse){
        String actualIngredients = createOrderResponse.getOrderResponse().getIngredients().toString();
        Assert.assertEquals(actualIngredients.isEmpty(), false);
    }

    @Step("Get created accessToken")
    public String getAccessTokenCustomer(CreateCustomerResponse createLogInCustomerResponse) {
        accessToken = createLogInCustomerResponse.getAccessToken().substring(7);
        return accessToken;
    }


    @Test
    @DisplayName("Successful create order without authorization")
    public void createOrderWithoutAuthorization() {
        accessToken = "";
        ValidatableResponse response = createOrder();
        CreateOrderResponse createOrderResponse = sendResponseInCreateOrderResponseClass(response);

        compareStatusCodeTo200(response);
        compareMessageToSuccessfulMessage(createOrderResponse, responseMessage);

        checkNameInCreateOrder( createOrderResponse);
        checkNumberInCreateOrder(createOrderResponse);

    }

    @Test
    @DisplayName("create order without ingredients")
    public void createOrderWithoutIngredients() {
        accessToken = "";
        ValidatableResponse response = checkOrderWithoutIngredients();

        compareStatusCodeTo400(response);
        compareMessageToUnsuccessfulMessage(response, responseMessage);
        compareMessageToError400ShouldBeIngredients(response, responseMessage);

    }

    @Test
    @DisplayName("create order with incorrect ingredients")
    public void createOrderWithIncorrectIngredients() {
        accessToken = "";
        ValidatableResponse response = checkOrderWithIncorrectIngredients();
        compareStatusCodeTo500(response);
    }

    @Test
    @DisplayName("Successful create order with authorization")
    public void createOrderWithAuthorization() {

        //создание пользователя и авторизация
        createCorrectCustomer();
        ValidatableResponse loginResponse = logInCustomer();
        CreateCustomerResponse createLogInCustomerResponse = sendResponseInLogInCustomerResponseClass(loginResponse);
        getAccessTokenCustomer(createLogInCustomerResponse);

        //создать заказ
        ValidatableResponse response = createOrder();
        CreateOrderResponse createOrderResponse = sendResponseInCreateOrderResponseClass(response);

        compareStatusCodeTo200(response);
        compareMessageToSuccessfulMessage(createOrderResponse, responseMessage);

        //проверки полей в create order
        checkNameInCreateOrder(createOrderResponse);
        checkNumberInCreateOrder(createOrderResponse);
        //проверки полей в order
        checkIdInOrder(createOrderResponse);
        checkStatusInOrder(createOrderResponse);
        checkNameInOrder(createOrderResponse);
        checkCreatedAtInOrder(createOrderResponse);
        checkUpdatedAtInOrder(createOrderResponse);
        checkNumberInOrder(createOrderResponse);
        checkPriceInOrder(createOrderResponse);

        //проверки полей в owner
        checkNameInOwner(createOrderResponse);
        checkEmailInOwner(createOrderResponse);
        checkCreatedAtInOwner(createOrderResponse);
        checkUpdatedAtInOwner(createOrderResponse);
        //проверки полей в ingredients
        checkIngredientsIsNotEmpty(createOrderResponse);
        //удаление созданного пользователя
        customerClient.delete(accessToken);

    }


}
