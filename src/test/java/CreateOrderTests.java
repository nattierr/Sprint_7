import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTests extends BaseTests {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List<String> colors;

    public CreateOrderTests(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> colors) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.colors = colors;
    }
    @Parameterized.Parameters
    public static Object[][] createOrderData() {
        return new Object[][]{
                {"Питер", "Паркер", "Москва", "Черкизовская", "79098889900", 5, "2025-03-14", "Хочу кататься", List.of("BLACK")},
                {"M", "J", "Москва", "Кутузовская", "79031122335", 1, "2025-03-18", "Классный самокат", List.of("GREY")},
                {"Эдди", "Брок", "Москва", "Сокольники", "79882345678", 2, "2025-04-01", " ", Arrays.asList("BLACK", "GREY")},
                {"Нэнси", "Дрю", "Санкт-Петербург", "Технопарк", "79878989090", 7, "2025-04-28", "Ура, кататься!", new ArrayList<String>()},
        };
    }

    @Test
    @Description("check orders creation")
    public void createOrder() {
        Order order = makeOrder();

        Response response = sendCreateOrderRequest(order);
        checkCreateOrderSuccessfulResponse(response);
    }

    @Step("check make order")
    public Order makeOrder() {
       return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colors.toArray(new String[]{}));
    }
    @Step("send POST request to /api/v1/orders")
    public Response sendCreateOrderRequest(Order order) {
        return NetworkService.createOrder(order);
    }

    @Step("check response")
    public void checkCreateOrderSuccessfulResponse(Response response) {
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
    }


}
