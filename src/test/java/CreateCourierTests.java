import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;


import static org.hamcrest.Matchers.*;

public class CreateCourierTests extends BaseTests {

    Courier courierForDelete;;

    @After
    public void deleteCourier() {
        if(courierForDelete != null) {
            LoginCourierResponse response = NetworkService
                    .login(courierForDelete.getLogin(), courierForDelete.getPassword())
                    .body()
                    .as(LoginCourierResponse.class);
            NetworkService.deleteCourier(response.getId());
        }
    }

    @Test
    @Description("check new courier successful creation: status code and response body")
    public void createNewCourierTest() {
        Courier courier = generateNewCourier();

        Response response = sendCreateCourierRequest(courier);
        checkCreateCourierSuccessfulResponse(response);

        courierForDelete = courier;
    }

    @Step("generate new courier")
    public Courier generateNewCourier() {
        CourierDataSet dataSet = new CourierDataSet();
        return dataSet.courier;
    }
    @Step("send POST request to /api/v1/courier")
    public Response sendCreateCourierRequest(Courier courier) {
        return NetworkService.createCourier(courier);
    }
    @Step("check create courier response")
    public void checkCreateCourierSuccessfulResponse(Response response) {
        response.then().assertThat()
                .body("ok", is(true))
                .and()
                .statusCode(201);
    }


    @Test
    @Description("check impossible two identical couriers creation: status code and response body")
    public void cannotCreateTwoIdenticalCouriersTest() {
        Courier courier = generateNewCourier();

        Response firstResponse = sendCreateCourierRequest(courier);

        checkCreateCourierSuccessfulResponse(firstResponse);

        courierForDelete = courier;

        Response secondResponse = sendCreateCourierRequest(courier);
        checkCreateCourierDuplicateErrorResponse(secondResponse);
    }
    @Step("check duplicate courier response error")
    public void checkCreateCourierDuplicateErrorResponse(Response response) {
        response.then().assertThat().body("message", is("Этот логин уже используется"))
                .and()
                .statusCode(409);
    }



    @Test
    @Description("check creation courier without login")
    public void createCourierWithoutLoginTest() {
        Courier courier = generateCourierWithoutLogin();

        Response response = sendCreateCourierRequest(courier);
        checkAbsentRequiredFieldResponseError(response);
    }
    @Step("generate courier without login")
    public Courier generateCourierWithoutLogin() {
        CourierDataSet dataSet = new CourierDataSet();

        return new Courier(null, dataSet.password, dataSet.firstName);
    }
    @Step("check absent required field response error")
    public void checkAbsentRequiredFieldResponseError(Response response) {
        response.then().assertThat().body("message", is("Недостаточно данных для создания учетной записи"));
    }



    @Test
    @Description("check creation courier without password")
    public void createCourierWithoutPasswordTest() {
        Courier courier = generateCourierWithoutPassword();

        Response response = sendCreateCourierRequest(courier);
        checkAbsentRequiredFieldResponseError(response);
    }
    @Step("generate courier without password")
    public Courier generateCourierWithoutPassword() {
        CourierDataSet dataSet = new CourierDataSet();

        return new Courier(dataSet.login, null, dataSet.firstName);
    }


    @Test
    @Description("check creation courier without firstname")
    public void createCourierWithoutFirstNameTest() {
        Courier courier = generateCourierWithoutFirstName();

        courierForDelete = courier;

        Response response = sendCreateCourierRequest(courier);
        checkCreateCourierSuccessfulResponse(response);
    }
    @Step("generate courier without firstname")
    public Courier generateCourierWithoutFirstName() {
        CourierDataSet dataSet = new CourierDataSet();

        return new Courier(dataSet.login, dataSet.password, null);
    }

    @Test
    @Description("check login with taken login")
    public void loginAlreadyTakenTest() {

        Courier firstCourier = generateNewCourier();
        courierForDelete = firstCourier;

        Response firstResponse = sendCreateCourierRequest(firstCourier);
        checkCreateCourierSuccessfulResponse(firstResponse);

        Courier secondCourier = generateCourierWithFixedLogin(firstCourier.getLogin());

        Response secondResponse = sendCreateCourierRequest(secondCourier);
        checkCreateCourierDuplicateErrorResponse(secondResponse);
    }
    @Step("generate courier with fixed login")
    public Courier generateCourierWithFixedLogin(String login) {
        CourierDataSet dataSet2 = new CourierDataSet();

        return new Courier(login, dataSet2.password, dataSet2.firstName);
    }


}

