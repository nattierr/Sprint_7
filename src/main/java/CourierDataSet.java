import java.util.Random;

public class CourierDataSet {
    String[] logins = new String[] { "house", "cuddy", "wilson", "chase", "foreman" };
    String[] passwords = new String[] { "0909", "qwerty", "m90doc", "pass2word", "1984" };
    String[] firstNames = new String[] { "Greg", "Liza", "James", "Robert", "Eric" };

    int randomLoginIndex = new Random().nextInt(logins.length);
    int randomPasswordIndex = new Random().nextInt(passwords.length);
    int randomFirstNameIndex = new Random().nextInt(firstNames.length);

    String randomLogin = logins[randomLoginIndex];
    String randomPassword = passwords[randomPasswordIndex];
    String randomFirstName = firstNames[randomFirstNameIndex];

    Courier randomCourier = new Courier(randomLogin, randomPassword, randomFirstName);

}
