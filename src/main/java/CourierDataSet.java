import com.github.javafaker.Faker;

import java.util.Random;


public class CourierDataSet {

    Faker faker = new Faker();

    String login = faker.name().username();
    String password = faker.internet().password();
    String firstName = faker.name().firstName();

    Courier courier = new Courier(login, password, firstName);

}
