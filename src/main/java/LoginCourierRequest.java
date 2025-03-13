public class LoginCourierRequest {
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String login;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;


    public LoginCourierRequest (String login, String password) {
        this.login = login;
        this.password = password;
    }

    public LoginCourierRequest () {
    }
}
