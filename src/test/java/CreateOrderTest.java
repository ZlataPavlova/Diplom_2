
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import response.CreateCustomerResponse;
import response.CreateOrderResponse;
import steps.StepsForCreateOrderTest;

public class CreateOrderTest {
    private StepsForCreateOrderTest stepsForCreateOrderTest = new StepsForCreateOrderTest();
    @Test
    @DisplayName("Successful create order without authorization")
    public void createOrderWithoutAuthorization() {

        ValidatableResponse response = stepsForCreateOrderTest.createOrder();
        CreateOrderResponse createOrderResponse = stepsForCreateOrderTest.sendResponseInCreateOrderResponseClass(response);

        stepsForCreateOrderTest.compareStatusCodeTo200(response);
        stepsForCreateOrderTest.compareMessageToSuccessfulMessage(createOrderResponse);

        stepsForCreateOrderTest.checkNameInCreateOrder(createOrderResponse);
        stepsForCreateOrderTest.checkNumberInCreateOrder(createOrderResponse);
    }

    @Test
    @DisplayName("create order without ingredients")
    public void createOrderWithoutIngredients() {
        ValidatableResponse response = stepsForCreateOrderTest.checkOrderWithoutIngredients();
        stepsForCreateOrderTest.compareStatusCodeTo400(response);
        stepsForCreateOrderTest.compareMessageToUnsuccessfulMessage(response);
        stepsForCreateOrderTest.compareMessageToError400ShouldBeIngredients(response);
    }

    @Test
    @DisplayName("create order with incorrect ingredients")
    public void createOrderWithIncorrectIngredients() {
        ValidatableResponse response = stepsForCreateOrderTest.checkOrderWithIncorrectIngredients();
        stepsForCreateOrderTest.compareStatusCodeTo500(response);
    }

    @Test
    @DisplayName("Successful create order with authorization")
    public void createOrderWithAuthorization() {
        //создание пользователя и авторизация
        stepsForCreateOrderTest.createCorrectCustomer();
        ValidatableResponse loginResponse = stepsForCreateOrderTest.logInCustomer();
        CreateCustomerResponse createLogInCustomerResponse = stepsForCreateOrderTest.sendResponseInLogInCustomerResponseClass(loginResponse);
        stepsForCreateOrderTest.getAccessTokenCustomer(createLogInCustomerResponse);
        //создать заказ
        ValidatableResponse response = stepsForCreateOrderTest.createOrder();
        CreateOrderResponse createOrderResponse = stepsForCreateOrderTest.sendResponseInCreateOrderResponseClass(response);
        stepsForCreateOrderTest.compareStatusCodeTo200(response);
        stepsForCreateOrderTest.compareMessageToSuccessfulMessage(createOrderResponse);
        //проверки полей в create order
        stepsForCreateOrderTest.checkNameInCreateOrder(createOrderResponse);
        stepsForCreateOrderTest.checkNumberInCreateOrder(createOrderResponse);
        //проверки полей в order
        stepsForCreateOrderTest.checkIdInOrder(createOrderResponse);
        stepsForCreateOrderTest.checkStatusInOrder(createOrderResponse);
        stepsForCreateOrderTest.checkNameInOrder(createOrderResponse);
        stepsForCreateOrderTest.checkCreatedAtInOrder(createOrderResponse);
        stepsForCreateOrderTest.checkUpdatedAtInOrder(createOrderResponse);
        stepsForCreateOrderTest.checkNumberInOrder(createOrderResponse);
        stepsForCreateOrderTest.checkPriceInOrder(createOrderResponse);
        //проверки полей в owner
        stepsForCreateOrderTest.checkNameInOwner(createOrderResponse);
        stepsForCreateOrderTest.checkEmailInOwner(createOrderResponse);
        stepsForCreateOrderTest.checkCreatedAtInOwner(createOrderResponse);
        stepsForCreateOrderTest.checkUpdatedAtInOwner(createOrderResponse);
        //проверки полей в ingredients
        stepsForCreateOrderTest.checkIngredientsIsNotEmpty(createOrderResponse);
        //удаление созданного пользователя
        stepsForCreateOrderTest.deleteCustomer();

    }


}
