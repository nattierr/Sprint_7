import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTests {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final boolean isBlack;
    private final boolean isGrey;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

        public CreateOrderTests(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, boolean isBlack, boolean isGrey) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.metroStation = metroStation;
            this.phone = phone;
            this.rentTime = rentTime;
            this.deliveryDate = deliveryDate;
            this.comment = comment;
            this.isBlack = isBlack;
            this.isGrey = isGrey;
        }

        @Parameterized.Parameters
        public static Object[][] createOrderData() {
            return new Object[][]{
                    {"Питер", "Паркер", "Москва", "Черкизовская", "79098889900", 5, "2025-03-14", "Хочу кататься", true, false},
                    {"M", "J", "Москва", "Кутузовская", "79031122335", 1, "2025-03-18", "Классный самокат", false, true},
                    {"Эдди", "Брок", "Москва", "Сокольники", "79882345678", 2, "2025-04-01", " ", true, true},
                    {"Нэнси", "Дрю", "Санкт-Петербург", "Технопарк", "79878989090", 7, "2025-04-28", "Ура, кататься!", false, false},
            };
        }

        @Test
        public void createOrder() {

        List<String> colors = new ArrayList<String>();

            if(isBlack) {
                colors.add("BLACK");
                }

            if(isGrey) {
                colors.add("GREY");
            }

            String[] color = colors.toArray(new String[]{});

            Order order = new Order(firstName, lastName, address,  metroStation,  phone,  rentTime,  deliveryDate,  comment, color);

            Response response = NetworkService.createOrder(order);
            response.then().assertThat().body("track", notNullValue())
                    .and()
                    .statusCode(201);
        }
    }
