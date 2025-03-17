import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

public class LoginCourierTests extends BaseTests {
   Courier courierForDelete;

    @After
    public void deleteCourier() {
        if(courierForDelete != null) {
            LoginCourierResponse response = NetworkService
                    .login(courierForDelete.getLogin(), courierForDelete.getPassword())
                    .body()
                    .as(LoginCourierResponse.class);
            NetworkService.deleteCourier(response.getId());
        }
        courierForDelete = null;
    }

    @Test
    @Description("check successful courier login")
    public void successfulLoginCourierTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.courier;

        courierForDelete = courier;

        NetworkService.createCourier(courier);
        Response response = sendLoginCourierRequest(courier);
        checkLoginCourierSuccessfulResponse(response);
    }
    @Step("send GET request to /api/v1/courier/login")
    public Response sendLoginCourierRequest(Courier courier) {
        Response response = NetworkService.login(courier.getLogin(), courier.getPassword());
        return response;
    }
    @Step("check login courier response")
    public void checkLoginCourierSuccessfulResponse(Response response) {
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
    }


    @Test
    @Description("check login without login")
    public void loginWithoutLoginTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.courier;

        courierForDelete = courier;

        NetworkService.createCourier(courier);
        Response response = NetworkService.login(null, courier.getPassword());

        checkLoginWithoutRequiredFieldResponse(response);
    }
    @Step("check login without required field response")
    public void checkLoginWithoutRequiredFieldResponse(Response response) {
        response.then().assertThat().body("message", is("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }


    @Test
    @Description("check login without password")
    public void loginWithoutPasswordTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.courier;

        courierForDelete = courier;

        NetworkService.createCourier(courier);
        Response response = NetworkService.login(courier.getLogin(), null);

        checkLoginWithoutRequiredFieldResponse(response);
    }


    @Test
    @Description("check login with incorrect login")
    public void loginWithIncorrectLoginTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.courier;

        courierForDelete = courier;

        NetworkService.createCourier(courier);
        String incorrectLogin = courier.getLogin() + "test";
        Response response = NetworkService.login(incorrectLogin, courier.getPassword());

        checkLoginWithIncorrectDataResponse(response);
    }
    @Step("check login with incorrect data")
    public void checkLoginWithIncorrectDataResponse(Response response) {
        response.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }


    @Test
    @Description("check login with incorrect password")
    public void loginWithIncorrectPasswordTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.courier;

        courierForDelete = courier;

        NetworkService.createCourier(courier);
        String incorrectPassword = courier.getPassword() + "test";
        Response response = NetworkService.login(courier.getLogin(), incorrectPassword);

        checkLoginWithIncorrectDataResponse(response);
    }

    @Test
    @Description("check login by unregistered user")
    public void loginByUnregisteredUserTest() {
        Courier courier = new Courier("mister", "twister", "Harry");

        Response response = sendLoginCourierRequest(courier);

        checkLoginWithIncorrectDataResponse(response);
    }


    @Test
    @Description("check successful request returns id")
    public void successfulRequestReturnsIdTest() {
        CourierDataSet dataSet = new CourierDataSet();

        Courier courier = dataSet.courier;

        courierForDelete = courier;

        NetworkService.createCourier(courier);
        Response response = sendLoginCourierRequest(courier);
        checkResponseBodyContainsIdIfSuccess(response);
    }
    @Step("check response body contains id if success")
    public void checkResponseBodyContainsIdIfSuccess(Response response) {
        response.then().assertThat().body("id", notNullValue());
    }
}
