import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

public class LoginCourierTests {
    List<Courier> couriers = new ArrayList<Courier>();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";

    }

    @After
    public void deleteCourier() {

        for(int i = 0; i < couriers.size(); i++) {
            Courier courier = couriers.get(i);

            LoginCourierResponse response = NetworkService
                    .login(courier.getLogin(), courier.getPassword())
                    .body()
                    .as(LoginCourierResponse.class);
            NetworkService.deleteCourier(response.getId());
        }
        couriers.clear();
    }

    @Test
    public void successfulLoginCourierTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.randomCourier;

        couriers.add(courier);

        NetworkService.createCourier(courier);
        Response response = NetworkService.login(courier.getLogin(), courier.getPassword());
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void loginWithoutLoginTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.randomCourier;

        couriers.add(courier);

        NetworkService.createCourier(courier);
        Response response = NetworkService.login(null, courier.getPassword());
        response.then().assertThat().body("message", is("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    public void loginWithoutPasswordTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.randomCourier;

        couriers.add(courier);

        NetworkService.createCourier(courier);
        Response response = NetworkService.login(courier.getLogin(), null);
        response.then().assertThat().body("message", is("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    public void loginWithIncorrectLoginTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.randomCourier;

        couriers.add(courier);

        NetworkService.createCourier(courier);
        String incorrectLogin = courier.getLogin()+"test";
        Response response = NetworkService.login(incorrectLogin, courier.getPassword());
        response.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    public void loginWithIncorrectPasswordTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.randomCourier;

        couriers.add(courier);

        NetworkService.createCourier(courier);
        String incorrectPassword = courier.getPassword()+"test";
        Response response = NetworkService.login(courier.getLogin(), incorrectPassword);
        response.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    public void loginByUnregisteredUserTest() {
        Courier courier = new Courier("mister", "twister", "Harry");

        Response response = NetworkService.login(courier.getLogin(), courier.getPassword());
        response.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    public void successfulRequestReturnsIdTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.randomCourier;

        couriers.add(courier);

        NetworkService.createCourier(courier);
        Response response = NetworkService.login(courier.getLogin(), courier.getPassword());
        response.then().assertThat().body("id", notNullValue());
    }
}
