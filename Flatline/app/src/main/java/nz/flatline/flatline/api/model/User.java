package nz.flatline.flatline.api.model;


public class User {

    private final int pk;
    private final String name;
    private final String email;
    private final String password;

    public User(int pk, String name, String email, String password) {
        this.pk = pk;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
}
