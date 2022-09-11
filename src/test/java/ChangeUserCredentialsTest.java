
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;
import static org.apache.http.HttpStatus.*;
import request.Customer;
import response.RefreshCustomer;
import request.CustomerClient;
import request.CustomerCredentials;
import request.CustomerGeneration;
import response.ResponseMessage;
import response.CreateCustomerResponse;

public class ChangeUserCredentialsTest {

    private Customer customer;
    private Customer customerChange;
    private Customer customerWithRepeatEmail;
    private CustomerClient customerClient;
    private ResponseMessage responseMessage;
    private CreateCustomerResponse createCustomerResponse;
    private RefreshCustomer refreshCustomer;
    private String accessToken;

    @Before
    public void setUp() {
        customer = CustomerGeneration.getDefault();
        customerChange = CustomerGeneration.getNewCredentials();
        customerWithRepeatEmail = CustomerGeneration.getRepeatCredentials();
        customerClient = new CustomerClient();
        responseMessage = new ResponseMessage();
    }

    @Step("Send POST request to api/auth/register")
    public ValidatableResponse createCorrectCustomer() {
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
        System.out.println(accessToken);
        ValidatableResponse response = customerClient.change(customerChange, accessToken);
        return response;
    }

    @Step("Send PATH request to api/auth/user and deserialize into RefreshCustomer class")
    public RefreshCustomer sendResponseChangeCustomerResponseClass(ValidatableResponse response){
        RefreshCustomer refreshCustomerResponse = response.extract().body().as(RefreshCustomer.class);
        return refreshCustomerResponse;
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

    @Step("Get created accessToken")
    public String getAccessTokenCustomer(CreateCustomerResponse createCustomerResponse) {
        accessToken = createCustomerResponse.getAccessToken().substring(7);
        return accessToken;
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
    @Step("Assert status code is 403")
    public void compareStatusCodeTo403(ValidatableResponse response){
        int actualStatusCode = response.extract().statusCode();
        System.out.println(actualStatusCode);
        Assert.assertEquals(SC_FORBIDDEN, actualStatusCode);
    }

    @Step("Assert successful message")
    public void compareMessageToSuccessfulMessage(RefreshCustomer refreshCustomerResponse, ResponseMessage responseMessage){
        boolean actualMessage = refreshCustomerResponse.isSuccess();
        System.out.println(actualMessage);
        Assert.assertEquals(responseMessage.isMessageSuccessCreateCustomer(), actualMessage);
    }

    @Step("Assert 401 error message Unauthorized")
    public void compareMessageToError401Unauthorized(ValidatableResponse response, ResponseMessage responseMessage){
        String actualMessage = response.extract().path("message");
        System.out.println(actualMessage);
        Assert.assertEquals(responseMessage.getMessageError401Unauthorized(), actualMessage);
    }

    @Step("Assert 403 error message Forbidden")
    public void compareMessageToError403Forbidden(ValidatableResponse response, ResponseMessage responseMessage){
        String actualMessage = response.extract().path("message");
        System.out.println(actualMessage);
        Assert.assertEquals(responseMessage.getMessageError403CustomerWithRepeatPassword(), actualMessage);
    }

    @Step("Assert unsuccessful message")
    public void compareMessageToUnsuccessfulMessage(ValidatableResponse response, ResponseMessage responseMessage){
        boolean actualMessage = response.extract().path("success");
        System.out.println(actualMessage);
        Assert.assertEquals(responseMessage.isMessageUnsuccessfulCreateCustomer(), actualMessage);
    }

    @Step("Check String name is not empty")
    public void checkNameIsCustomerName(RefreshCustomer refreshCustomerResponse){
        String actualName = refreshCustomerResponse.getUser().getName();
        System.out.println(actualName);
        Assert.assertEquals(customerChange.getName(), actualName);
    }

    @Step("Check String email is not empty")
    public void checkEmailIsCustomerName(RefreshCustomer refreshCustomerResponse){
        String actualEmail = refreshCustomerResponse.getUser().getEmail();
        System.out.println(actualEmail);
        Assert.assertEquals(customerChange.getEmail(), actualEmail);
    }

    @DisplayName("change credentials registered customer")
    @Test
    public void checkChangeCredentialsRegisteredCustomer() {
        createCorrectCustomer();
        ValidatableResponse loginResponse = logInCustomer();
        CreateCustomerResponse createLogInCustomerResponse = sendResponseInLogInCustomerResponseClass(loginResponse);
        getAccessTokenCustomer(createLogInCustomerResponse);
        System.out.println(accessToken);

        ValidatableResponse response = changeCredentialsRegisteredCustomer();
        RefreshCustomer refreshCustomerResponse = sendResponseChangeCustomerResponseClass(response);

        compareStatusCodeTo200(response);
        compareMessageToSuccessfulMessage(refreshCustomerResponse, responseMessage);
        checkNameIsCustomerName(refreshCustomerResponse);
        checkEmailIsCustomerName(refreshCustomerResponse);
        customerClient.delete(accessToken);

    }

    @DisplayName("change credentials unregistered customer")
    @Test
    public void checkChangeCredentialsUnregisteredCustomer() {
        ValidatableResponse customerResponse = createCorrectCustomer();
        CreateCustomerResponse createCustomerResponse = sendResponseInLogInCustomerResponseClass(customerResponse);

        accessToken = "";
        ValidatableResponse response = changeCredentialsRegisteredCustomer();

        compareStatusCodeTo401(response);
        compareMessageToError401Unauthorized( response,  responseMessage);
        compareMessageToUnsuccessfulMessage(response, responseMessage);

        getAccessTokenCustomer(createCustomerResponse);
        customerClient.delete(accessToken);

    }

    @DisplayName("change credentials registered customer with existed email")
    @Test
    public void checkChangeCredentialsRegisteredCustomerWithExistedEmail() {
        //создали пользователя данные которого будем менять
        ValidatableResponse customerResponse = createCorrectCustomer();
        CreateCustomerResponse createCustomerResponse = sendResponseInLogInCustomerResponseClass(customerResponse);

        //создали пользователя с которым совпадет email
        ValidatableResponse customerResponseWithRepeatEmail = createCustomerWithRepeatEmail();
        CreateCustomerResponse createCustomerWithRepeatEmail = sendResponseInLogInCustomerResponseClass(customerResponseWithRepeatEmail);

        //авторизовываемся под пользователем, которому будем менять данные
        ValidatableResponse loginResponse = logInCustomer();
        CreateCustomerResponse createLogInCustomerResponse = sendResponseInLogInCustomerResponseClass(loginResponse);
        getAccessTokenCustomer(createLogInCustomerResponse);
        System.out.println(accessToken);

        //изменяем данные, где почта будет совпадать с уже созданным чуваком
        ValidatableResponse response = changeCredentialsRegisteredCustomer();


        //проверки ответа
        compareStatusCodeTo403(response);
        compareMessageToUnsuccessfulMessage(response, responseMessage);
        compareMessageToError403Forbidden(response,responseMessage);

        //удалить двух созданных тестовых пользователей

        customerClient.delete(accessToken);
        getAccessTokenCustomer(createCustomerResponse);
        customerClient.delete(accessToken);
        getAccessTokenCustomer(createCustomerWithRepeatEmail);
        customerClient.delete(accessToken);

    }

}
