
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import request.*;
import response.GetOrdersResponse;
import response.ResponseMessage;
import response.CreateCustomerResponse;
import response.CreateOrderResponse;

import static org.hamcrest.CoreMatchers.notNullValue;
import org.hamcrest.MatcherAssert;


import static org.apache.http.HttpStatus.*;

public class GetOrdersTest {

    private Customer customer;
    private IngredientsRequest ingredientsRequest;
    private CustomerClient customerClient;
    private OrderClient orderClient;
    private String accessToken;
    private ResponseMessage responseMessage;

    @Before
    public void setUp() {
        customer = CustomerGeneration.getDefault();
        customerClient = new CustomerClient();
        responseMessage = new ResponseMessage();
        orderClient = new OrderClient();
        ingredientsRequest = OrderGeneration.getDefault();
    }

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
    public CreateCustomerResponse sendResponseInLogInCustomerResponseClass(ValidatableResponse response){
        CreateCustomerResponse createCustomerResponse = response.extract().body().as(CreateCustomerResponse.class);
        return createCustomerResponse;
    }

    @Step("Send POST request to api/auth/login")
    public ValidatableResponse logInCustomer() {
        ValidatableResponse response = customerClient.logIn(CustomerCredentials.from(customer));
        return response;
    }

    @Step("Send GET request to 'api/orders'")
    public ValidatableResponse sendResponseInJson(){
        ValidatableResponse response = orderClient.getListOrders(accessToken);

        return response;
    }

    @Step("Send POST request to api/orders into GetOrdersResponse class")
    public GetOrdersResponse sendResponseInGetOrdersResponseClass(ValidatableResponse response){
        GetOrdersResponse getOrdersResponse = response.extract().body().as(GetOrdersResponse.class);
        return getOrdersResponse;
    }

    @Step("Assert successful message")
    public void compareMessageToSuccessfulMessage(GetOrdersResponse getOrdersResponse, ResponseMessage responseMessage){
        boolean actualMessage = getOrdersResponse.isSuccess();
        Assert.assertEquals(responseMessage.isMessageSuccessCreateCustomer(), actualMessage);
    }

    @Step("Assert unsuccessful message")
    public void compareMessageToUnsuccessfulMessage(ValidatableResponse response, ResponseMessage responseMessage){
        boolean actualMessage = response.extract().path("success");
        System.out.println(actualMessage);
        Assert.assertEquals(responseMessage.isMessageUnsuccessfulCreateCustomer(), actualMessage);
    }

    @Step("Assert 401 error message Unauthorized")
    public void compareMessageToError401Unauthorized(ValidatableResponse response, ResponseMessage responseMessage){
        String actualMessage = response.extract().path("message");
        System.out.println(actualMessage);
        Assert.assertEquals(responseMessage.getMessageError401Unauthorized(), actualMessage);
    }

    @Step("Assert status code is 200")
    public void compareStatusCodeTo200(ValidatableResponse response){
        int actualStatusCode = response.extract().statusCode();
        Assert.assertEquals(SC_OK, actualStatusCode);
    }
    @Step("Assert status code is 401")
    public void compareStatusCodeTo401(ValidatableResponse response){
        int actualStatusCode = response.extract().statusCode();
        System.out.println(actualStatusCode);
        Assert.assertEquals(SC_UNAUTHORIZED, actualStatusCode);
    }

    @Step("Get created accessToken")
    public String getAccessTokenCustomer(CreateCustomerResponse createLogInCustomerResponse) {
        accessToken = createLogInCustomerResponse.getAccessToken().substring(7);
        return accessToken;
    }

    @Step("Check int total is not null in createOrder")
    public void checkTotalInGetOrder(GetOrdersResponse getOrdersResponse){
        int actualTotal = getOrdersResponse.getTotal();
        MatcherAssert.assertThat(actualTotal, notNullValue());
    }
    @Step("Check int totalToday is not null in createOrder")
    public void checkTotalTodayInGetOrder(GetOrdersResponse getOrdersResponse){
        int actualTotal = getOrdersResponse.getTotal();
        MatcherAssert.assertThat(actualTotal, notNullValue());
    }

    @Step("Check String id is not empty in orders")
    public void checkOrdersIsNotEmpty(GetOrdersResponse getOrdersResponse){
        String actualOrders = getOrdersResponse.getOrders().toString();
        System.out.println(actualOrders);
        Assert.assertEquals( false, actualOrders.isEmpty());
    }

    @Step("Check Ingredients object is not null")
    public void checkIngredientsIsNotNull(GetOrdersResponse getOrdersResponse){
        MatcherAssert.assertThat(getOrdersResponse.getOrders().toString(), notNullValue());
    }


    @Test
    @DisplayName("Get order without authorization")
    public void getOrderWithoutAuthorization() {
        accessToken = "";
        ValidatableResponse response = sendResponseInJson();

        compareMessageToError401Unauthorized(response, responseMessage);
        compareMessageToUnsuccessfulMessage(response, responseMessage);
        compareStatusCodeTo401(response);

    }

    @Test
    @DisplayName("Get order with authorization")
    public void getOrderWithAuthorization() {
        //создать пользователя
        correctCustomer();
        ValidatableResponse loginResponse = logInCustomer();
        CreateCustomerResponse createLogInCustomerResponse = sendResponseInLogInCustomerResponseClass(loginResponse);
        getAccessTokenCustomer(createLogInCustomerResponse);

        //создать заказ для пользователя
       // ValidatableResponse createOrderResponse = createOrder();


        //отправка запроса на заказы для созданного пользователя
        ValidatableResponse response = sendResponseInJson();
        GetOrdersResponse getOrdersResponse = sendResponseInGetOrdersResponseClass(response);

        //проверки
        compareStatusCodeTo200(response);
        compareMessageToSuccessfulMessage(getOrdersResponse, responseMessage);
        checkTotalInGetOrder(getOrdersResponse);
        checkTotalTodayInGetOrder(getOrdersResponse);
        checkOrdersIsNotEmpty(getOrdersResponse);
        checkIngredientsIsNotNull(getOrdersResponse);
        customerClient.delete(accessToken);


    }

}
