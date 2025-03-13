import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrdersListTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void responseBodyContainsOrders () {

        Response response = given().get("/api/v1/orders");

        response.then().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(200);

    }
}
