import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class CreateCourierTests {

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
    public void createNewCourierTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.randomCourier;

        couriers.add(courier);

        Response response = NetworkService.createCourier(courier);
        response.then().assertThat().body("ok", is(true))
                .and()
                .statusCode(201);
    }


    @Test
    public void cannotCreateTwoIdenticalCouriersTest() {
        CourierDataSet dataSet = new CourierDataSet();
        Courier courier = dataSet.randomCourier;

        couriers.add(courier);


        Response firstResponse = NetworkService.createCourier(courier);
        firstResponse.then().assertThat().body("ok", is(true))
                .and()
                .statusCode(201);

        Response secondResponse = NetworkService.createCourier(courier);
        secondResponse.then().assertThat().body("message", is("Этот логин уже используется"))
                .and()
                .statusCode(409);
    }

    @Test
    public void correctResponseCodeTest() {
        CourierDataSet dataSet = new CourierDataSet();
        Courier courier = dataSet.randomCourier;

        couriers.add(courier);

        Response response = NetworkService.createCourier(courier);
        response.then().statusCode(201);
    }

    @Test
    public void successfulRequestResponseBodyTest() {
        CourierDataSet dataSet = new CourierDataSet();
        Courier courier = dataSet.randomCourier;

        couriers.add(courier);

        Response response = NetworkService.createCourier(courier);
        response.then().assertThat().body("ok", is(true));
    }

    @Test
    public void createCourierWithoutLoginTest() {
        CourierDataSet dataSet = new CourierDataSet();
        Courier courier = new Courier(null, dataSet.randomPassword, dataSet.randomFirstName);


        Response response = NetworkService.createCourier(courier);
        response.then().assertThat().body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void createCourierWithoutPasswordTest() {
        CourierDataSet dataSet = new CourierDataSet();
        Courier courier = new Courier(dataSet.randomLogin, null, dataSet.randomFirstName);


        Response response = NetworkService.createCourier(courier);
        response.then().assertThat().body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void createCourierWithoutFirstNameTest() {
        CourierDataSet dataSet = new CourierDataSet();
        Courier courier = new Courier(dataSet.randomLogin, dataSet.randomPassword, null);

        couriers.add(courier);


        Response response = NetworkService.createCourier(courier);
        response.then().assertThat().body("ok", is(true))
                .and()
                .statusCode(201);
    }

    @Test
    public void loginAlreadyTakenTest() {
        CourierDataSet dataSet1 = new CourierDataSet();
        Courier firstCourier = dataSet1.randomCourier;
        couriers.add(firstCourier);


        Response firstResponse = NetworkService.createCourier(firstCourier);
        firstResponse.then().assertThat().body("ok", is(true))
                .and()
                .statusCode(201);

        CourierDataSet dataSet2 = new CourierDataSet();

        Courier secondCourier = new Courier(firstCourier.getLogin(), dataSet2.randomPassword, dataSet2.randomFirstName);

        couriers.add(secondCourier);


        Response secondResponse = NetworkService.createCourier(secondCourier);
        secondResponse.then().assertThat().body("message", is("Этот логин уже используется"))
                .and()
                .statusCode(409);
    }


}

