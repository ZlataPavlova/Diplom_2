import request.*;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import response.ResponseMessage;
import response.CreateCustomerResponse;


import static org.apache.http.HttpStatus.*;

public class CreateCustomerTest {
    private Customer customer;
    private Customer customerFieldEmailEmpty;
    private Customer customerFieldPasswordEmpty;
    private CustomerClient customerClient;
    private Customer customerFieldNameEmpty;
    private  ResponseMessage responseMessage;
    private CreateCustomerResponse createCustomerResponse;
private String accessToken;

    @Before
    public void setUp() {
        customer = CustomerGeneration.getDefault();
        customerFieldEmailEmpty = CustomerGeneration.getFieldEmailEmpty();
        customerFieldPasswordEmpty = CustomerGeneration.getFieldPasswordEmpty();
        customerFieldNameEmpty = CustomerGeneration.getFieldNameEmpty();
        customerClient = new CustomerClient();
        responseMessage = new ResponseMessage();
  }

    @After
    public void tearDown(){

        customerClient.delete(accessToken);

    }
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
    public CreateCustomerResponse sendResponseInCreateCustomerResponseClass(ValidatableResponse response){
        CreateCustomerResponse createCustomerResponse = response.extract().body().as(CreateCustomerResponse.class);
        return createCustomerResponse;
    }

    @Step("Assert status code is 200")
    public void compareStatusCodeTo200(ValidatableResponse response){
        int actualStatusCode = response.extract().statusCode();
        Assert.assertEquals(SC_OK, actualStatusCode);
    }

    @Step("Assert successful message")
    public void compareMessageToSuccessfulMessage(CreateCustomerResponse createCustomerResponse, ResponseMessage responseMessage){
        boolean actualMessage = createCustomerResponse.isSuccess();
        Assert.assertEquals(responseMessage.isMessageSuccessCreateCustomer(), actualMessage);
    }

    @Step("Check String AccessToken is not empty")
    public void checkAccessTokenIsNotEmpty(CreateCustomerResponse createCustomerResponse){
        String actualAccessToken = createCustomerResponse.getAccessToken();
        Assert.assertEquals( false, actualAccessToken.isEmpty());
    }

    @Step("Check String RefreshToken is not empty")
    public void checkRefreshTokenIsNotEmpty(CreateCustomerResponse createCustomerResponse){
        String actualRefreshToken = createCustomerResponse.getRefreshToken();
        Assert.assertEquals( false, actualRefreshToken.isEmpty());
    }

    @Step("Check String name is not empty")
    public void checkNameIsCustomerName(CreateCustomerResponse createCustomerResponse){
        String actualName = createCustomerResponse.getUser().getName();
        Assert.assertEquals(customer.getName(), actualName);
    }

    @Step("Check String email is not empty")
    public void checkEmailIsCustomerName(CreateCustomerResponse createCustomerResponse){
        String actualEmail = createCustomerResponse.getUser().getEmail();
        Assert.assertEquals(customer.getEmail(), actualEmail);
    }

    @Step("Get created accessToken")
    public String getAccessTokenCustomer(CreateCustomerResponse createCustomerResponse) {
        accessToken = createCustomerResponse.getAccessToken().substring(7);
        return accessToken;
    }

    @Step("Assert status code is 403")
    public void compareStatusCodeTo403(ValidatableResponse response){
        int actualStatusCode = response.extract().statusCode();
        Assert.assertEquals(SC_FORBIDDEN, actualStatusCode);
    }

    @Step("Assert 403 error message about double customer")
    public void compareMessageToError403DoubleCustomer(ValidatableResponse response, ResponseMessage responseMessage){
        String actualMessage = response.extract().path("message");
        Assert.assertEquals(responseMessage.getMessageError403DoubleCustomer(), actualMessage);
    }

    @Step("Assert 403 error message about double customer")
    public void compareMessageToError403CustomerWithEmptyField(ValidatableResponse response, ResponseMessage responseMessage){
        String actualMessage = response.extract().path("message");
        Assert.assertEquals(responseMessage.getMessageError403CustomerWithEmptyField(), actualMessage);
    }

    @Test
    @DisplayName("Successful create customer")
    public void createCorrectCustomer() {
        ValidatableResponse response = checkCreateCorrectCustomer();
        CreateCustomerResponse createCustomerResponse = sendResponseInCreateCustomerResponseClass(response);
        compareStatusCodeTo200(response);
        compareMessageToSuccessfulMessage(createCustomerResponse, responseMessage);

        checkAccessTokenIsNotEmpty(createCustomerResponse);
        checkRefreshTokenIsNotEmpty(createCustomerResponse);
        checkNameIsCustomerName(createCustomerResponse);
        checkEmailIsCustomerName(createCustomerResponse);
        getAccessTokenCustomer(createCustomerResponse);
        //customerClient.delete(accessToken);
    }

    @Test
    @DisplayName("Create a courier that already exists")

    public void createDoubleCustomerReturnWrong() {
        ValidatableResponse response = checkCreateCorrectCustomer();
        CreateCustomerResponse createCustomerResponse = sendResponseInCreateCustomerResponseClass(response);
        ValidatableResponse responseDoubleCustomer = checkCreateCorrectCustomer();
        compareStatusCodeTo403(responseDoubleCustomer);
        compareMessageToError403DoubleCustomer(responseDoubleCustomer, responseMessage);
        getAccessTokenCustomer(createCustomerResponse);


    }

    @Test
    @DisplayName("Create customer with field 'email' is empty")
    public void createCustomerWithEmptyFieldEmailReturnWrong() {
        ValidatableResponse response = checkCreateCustomerWithFieldEmailEmpty();
        compareStatusCodeTo403(response);
        compareMessageToError403CustomerWithEmptyField(response, responseMessage);
        accessToken="";
    }

    @Test
    @DisplayName("Create customer with field 'password' is empty")
    public void createCustomerWithEmptyFieldPasswordReturnWrong() {
        ValidatableResponse response = checkCreateCustomerWithFieldPasswordEmpty();
        compareStatusCodeTo403(response);
        compareMessageToError403CustomerWithEmptyField(response, responseMessage);
        accessToken="";
    }

    @Test
    @DisplayName("Create customer with field 'name' is empty")
    public void createCustomerWithEmptyFieldNameReturnWrong() {
        ValidatableResponse response = checkCreateCustomerWithFieldNameEmpty();
        compareStatusCodeTo403(response);
        compareMessageToError403CustomerWithEmptyField(response, responseMessage);
        accessToken="";
    }

}
