import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrdersListTests extends BaseTests {

    @Test
    @Description("check response body contains orders")
    public void responseBodyContainsOrders() {
        Response response = sendGetOrdersRequest();
        checkGetOrdersSuccessfulResponse(response);

    }
    @Step("send GET request to /api/v1/orders")
    public Response sendGetOrdersRequest() {
        return NetworkService.getOrders();
    }

    @Step("check response")
    public void checkGetOrdersSuccessfulResponse(Response response) {
        response.then().assertThat().body("orders", notNullValue())
                .and()
                .statusCode(200);
    }
}
