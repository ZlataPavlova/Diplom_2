package request;


import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


public class OrderClient extends RestClient {
    private static final String ORDER_POST  = "api/orders";
    private static final String ORDER_GET = "api/orders";



    public ValidatableResponse create (IngredientsRequest ingredientsRequest, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(ingredientsRequest)
                .when()
                .post(ORDER_POST)
                .then();
    }

    public ValidatableResponse getListOrders (String accessToken) {
        return  given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .get(ORDER_GET)
                .then();
    }

}
