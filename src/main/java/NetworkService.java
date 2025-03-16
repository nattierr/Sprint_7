import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class NetworkService {
    private static final String CREATE_COURIER = "/api/v1/courier";
    private static final String LOGIN_COURIER = "/api/v1/courier/login";
    private static final String DELETE_COURIER = "/api/v1/courier/{id}";
    private static final String CREATE_ORDERS = "/api/v1/orders";
    private static final String GET_ORDERS = "/api/v1/orders";

    public static Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(CREATE_COURIER);
    }

    public static Response deleteCourier(String id) {
        return given()
                .delete(DELETE_COURIER, id);
    }

    public static Response login(String login, String password) {
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest(login, password);
        return given()
                .header("Content-type", "application/json")
                .body(loginCourierRequest)
                .when()
                .post(LOGIN_COURIER);
    }

    public static Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(CREATE_ORDERS);
    }

    public static Response getOrders() {
        return given()
                .get(GET_ORDERS);
    }
}
