
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import steps.StepForChangeUserCredentialsTest;
import response.RefreshCustomer;
import response.CreateCustomerResponse;

public class ChangeUserCredentialsTest {
    private StepForChangeUserCredentialsTest stepForChangeUserCredentialsTest = new StepForChangeUserCredentialsTest();

    @DisplayName("change credentials registered customer")
    @Test
    public void checkChangeCredentialsRegisteredCustomer() {
        stepForChangeUserCredentialsTest.createCorrectCustomer();
        ValidatableResponse loginResponse = stepForChangeUserCredentialsTest.logInCustomer();
        CreateCustomerResponse createLogInCustomerResponse = stepForChangeUserCredentialsTest.sendResponseInLogInCustomerResponseClass(loginResponse);
        stepForChangeUserCredentialsTest.getAccessTokenCustomer(createLogInCustomerResponse);

        ValidatableResponse response = stepForChangeUserCredentialsTest.changeCredentialsRegisteredCustomer();
        RefreshCustomer refreshCustomerResponse = stepForChangeUserCredentialsTest.sendResponseChangeCustomerResponseClass(response);

        stepForChangeUserCredentialsTest.compareStatusCodeTo200(response);
        stepForChangeUserCredentialsTest.compareMessageToSuccessfulMessage(refreshCustomerResponse);
        stepForChangeUserCredentialsTest.checkNameIsCustomerName(refreshCustomerResponse);
        stepForChangeUserCredentialsTest.checkEmailIsCustomerName(refreshCustomerResponse);
        stepForChangeUserCredentialsTest.deleteCustomer();
    }

    @DisplayName("change credentials unregistered customer")
    @Test
    public void checkChangeCredentialsUnregisteredCustomer() {
        ValidatableResponse customerResponse = stepForChangeUserCredentialsTest.createCorrectCustomer();
        CreateCustomerResponse createCustomerResponse = stepForChangeUserCredentialsTest.sendResponseInLogInCustomerResponseClass(customerResponse);

        ValidatableResponse response = stepForChangeUserCredentialsTest.changeCredentialsRegisteredCustomer();

        stepForChangeUserCredentialsTest.compareStatusCodeTo401(response);
        stepForChangeUserCredentialsTest.compareMessageToError401Unauthorized(response);
        stepForChangeUserCredentialsTest.compareMessageToUnsuccessfulMessage(response);

        stepForChangeUserCredentialsTest.getAccessTokenCustomer(createCustomerResponse);
        stepForChangeUserCredentialsTest.deleteCustomer();
    }

    @DisplayName("change credentials registered customer with existed email")
    @Test
    public void checkChangeCredentialsRegisteredCustomerWithExistedEmail() {
        //создали пользователя данные которого будем менять
        ValidatableResponse customerResponse = stepForChangeUserCredentialsTest.createCorrectCustomer();
        CreateCustomerResponse createCustomerResponse = stepForChangeUserCredentialsTest.sendResponseInLogInCustomerResponseClass(customerResponse);
        //создали пользователя с которым совпадет email
        ValidatableResponse customerResponseWithRepeatEmail = stepForChangeUserCredentialsTest.createCustomerWithRepeatEmail();
        CreateCustomerResponse createCustomerWithRepeatEmail = stepForChangeUserCredentialsTest.sendResponseInLogInCustomerResponseClass(customerResponseWithRepeatEmail);
        //авторизовываемся под пользователем, которому будем менять данные
        ValidatableResponse loginResponse = stepForChangeUserCredentialsTest.logInCustomer();
        CreateCustomerResponse createLogInCustomerResponse = stepForChangeUserCredentialsTest.sendResponseInLogInCustomerResponseClass(loginResponse);
        stepForChangeUserCredentialsTest.getAccessTokenCustomer(createLogInCustomerResponse);
        //изменяем данные, где почта будет совпадать с уже созданным чуваком
        ValidatableResponse response = stepForChangeUserCredentialsTest.changeCredentialsRegisteredCustomer();
        //проверки ответа
        stepForChangeUserCredentialsTest.compareStatusCodeTo403(response);
        stepForChangeUserCredentialsTest.compareMessageToUnsuccessfulMessage(response);
        stepForChangeUserCredentialsTest.compareMessageToError403Forbidden(response);
        //удалить двух созданных тестовых пользователей
        stepForChangeUserCredentialsTest.deleteCustomer();
        stepForChangeUserCredentialsTest.getAccessTokenCustomer(createCustomerResponse);
        stepForChangeUserCredentialsTest.deleteCustomer();
        stepForChangeUserCredentialsTest.getAccessTokenCustomer(createCustomerWithRepeatEmail);
        stepForChangeUserCredentialsTest.deleteCustomer();

    }

}
