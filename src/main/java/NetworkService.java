import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class NetworkService {

    public static Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    public static Response deleteCourier(String id) {
        return given()
                .delete("/api/v1/courier/{id}", id);
    }

    public static Response login(String login, String password) {
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(login, password);
        return given()
                .header("Content-type", "application/json")
                .body(loginCourierRequest)
                .when()
                .post("/api/v1/courier/login");
    }

    public static Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }
}
