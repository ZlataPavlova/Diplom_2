
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import response.GetOrdersResponse;
import response.CreateCustomerResponse;
import steps.StepForGetOrdersTest;

public class GetOrdersTest {
    StepForGetOrdersTest stepForGetOrdersTest = new StepForGetOrdersTest();

    @Test
    @DisplayName("Get order without authorization")
    public void getOrderWithoutAuthorization() {

        ValidatableResponse response = stepForGetOrdersTest.sendResponseInJson();
        stepForGetOrdersTest.compareMessageToError401Unauthorized(response);
        stepForGetOrdersTest.compareMessageToUnsuccessfulMessage(response);
        stepForGetOrdersTest.compareStatusCodeTo401(response);
    }

    @Test
    @DisplayName("Get order with authorization")
    public void getOrderWithAuthorization() {
        //создать пользователя
        stepForGetOrdersTest.correctCustomer();
        ValidatableResponse loginResponse = stepForGetOrdersTest.logInCustomer();
        CreateCustomerResponse createLogInCustomerResponse = stepForGetOrdersTest.sendResponseInLogInCustomerResponseClass(loginResponse);
        stepForGetOrdersTest.getAccessTokenCustomer(createLogInCustomerResponse);
        //создать заказ для пользователя
        ValidatableResponse createOrderResponse = stepForGetOrdersTest.createOrder();
        //отправка запроса на заказы для созданного пользователя
        ValidatableResponse response = stepForGetOrdersTest.sendResponseInJson();
        GetOrdersResponse getOrdersResponse = stepForGetOrdersTest.sendResponseInGetOrdersResponseClass(response);
        //проверки
        stepForGetOrdersTest.compareStatusCodeTo200(response);
        stepForGetOrdersTest.compareMessageToSuccessfulMessage(getOrdersResponse);
        stepForGetOrdersTest.checkTotalInGetOrder(getOrdersResponse);
        stepForGetOrdersTest.checkTotalTodayInGetOrder(getOrdersResponse);
        stepForGetOrdersTest.checkOrdersIsNotEmpty(getOrdersResponse);
        stepForGetOrdersTest.checkIngredientsIsNotNull(getOrdersResponse);
        stepForGetOrdersTest.deleteCustomer();

    }

}
