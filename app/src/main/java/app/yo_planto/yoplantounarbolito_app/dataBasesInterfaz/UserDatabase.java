package app.yo_planto.yoplantounarbolito_app.dataBasesInterfaz;

public class UserDatabase {
    private String name;
    private String last_name;
    private String nick;
    private String email;
    private String phone;
    private String password;
    private String password_confirmation;
    private String points;

    public UserDatabase(){
        name = "name";
        last_name = "last_name";
        nick = "nick";
        email = "email";
        phone = "phone";
        password = "password";
        points = "points";
        password_confirmation = "password_confirmation";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}

