package request;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CustomerClient extends RestClient {

    private static final String CUSTOMER_POST  = "api/auth/register";
    private static final String CUSTOMER_DELETE  = "api/auth/user";
    private static final String LOGIN_POST  = "api/auth/login";
    private static final String CUSTOMER_PATH  = "api/auth/user";


    public ValidatableResponse create (Customer customer) {
        return given()
                .spec(getBaseSpec())
                .body(customer)
                .when()
                .post(CUSTOMER_POST)
                .then();
    }

    public ValidatableResponse delete (String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete(CUSTOMER_DELETE)
                .then();

    }

    public ValidatableResponse logIn (CustomerCredentials customer) {
        return given()
                .spec(getBaseSpec())
                .body(customer)
                .when()
                .post(LOGIN_POST)
                .then();
    }


    public ValidatableResponse change (Customer customer, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(customer)
                .when()
                .patch(CUSTOMER_PATH)
                .then();
    }
}
