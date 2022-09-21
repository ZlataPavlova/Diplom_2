
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import response.CreateCustomerResponse;
import steps.StepForCreateCustomerTest;

public class CreateCustomerTest {
    private StepForCreateCustomerTest stepForCreateCustomerTest = new StepForCreateCustomerTest();

    @Test
    @DisplayName("Successful create customer")
    public void createCorrectCustomer() {
        ValidatableResponse response = stepForCreateCustomerTest.checkCreateCorrectCustomer();
        CreateCustomerResponse createCustomerResponse = stepForCreateCustomerTest.sendResponseInCreateCustomerResponseClass(response);
        stepForCreateCustomerTest.compareStatusCodeTo200(response);
        stepForCreateCustomerTest.compareMessageToSuccessfulMessage(createCustomerResponse);
        stepForCreateCustomerTest.checkAccessTokenIsNotEmpty(createCustomerResponse);
        stepForCreateCustomerTest.checkRefreshTokenIsNotEmpty(createCustomerResponse);
        stepForCreateCustomerTest.checkNameIsCustomerName(createCustomerResponse);
        stepForCreateCustomerTest.checkEmailIsCustomerName(createCustomerResponse);
        stepForCreateCustomerTest.getAccessTokenCustomer(createCustomerResponse);
        stepForCreateCustomerTest.deleteCustomer();

    }

    @Test
    @DisplayName("Create a courier that already exists")
    public void createDoubleCustomerReturnWrong() {
        ValidatableResponse response = stepForCreateCustomerTest.checkCreateCorrectCustomer();
        CreateCustomerResponse createCustomerResponse = stepForCreateCustomerTest.sendResponseInCreateCustomerResponseClass(response);
        ValidatableResponse responseDoubleCustomer = stepForCreateCustomerTest.checkCreateCorrectCustomer();
        stepForCreateCustomerTest.compareStatusCodeTo403(responseDoubleCustomer);
        stepForCreateCustomerTest.compareMessageToError403DoubleCustomer(responseDoubleCustomer);
        stepForCreateCustomerTest.getAccessTokenCustomer(createCustomerResponse);
        stepForCreateCustomerTest.deleteCustomer();
    }

    @Test
    @DisplayName("Create customer with field 'email' is empty")
    public void createCustomerWithEmptyFieldEmailReturnWrong() {
        ValidatableResponse response = stepForCreateCustomerTest.checkCreateCustomerWithFieldEmailEmpty();
        stepForCreateCustomerTest.compareStatusCodeTo403(response);
        stepForCreateCustomerTest.compareMessageToError403CustomerWithEmptyField(response);
    }

    @Test
    @DisplayName("Create customer with field 'password' is empty")
    public void createCustomerWithEmptyFieldPasswordReturnWrong() {
        ValidatableResponse response = stepForCreateCustomerTest.checkCreateCustomerWithFieldPasswordEmpty();
        stepForCreateCustomerTest.compareStatusCodeTo403(response);
        stepForCreateCustomerTest.compareMessageToError403CustomerWithEmptyField(response);
    }

    @Test
    @DisplayName("Create customer with field 'name' is empty")
    public void createCustomerWithEmptyFieldNameReturnWrong() {
        ValidatableResponse response = stepForCreateCustomerTest.checkCreateCustomerWithFieldNameEmpty();
        stepForCreateCustomerTest.compareStatusCodeTo403(response);
        stepForCreateCustomerTest.compareMessageToError403CustomerWithEmptyField(response);
    }

}
