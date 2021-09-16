package request;

public class SignUpRequest {
    public String alias;
    public String password;
    public String firstname;
    public String lastname;
    public String imageURL;

    public SignUpRequest(){}

    public SignUpRequest(String alias, String password, String firstname, String lastname, String imageURL) {
        this.alias = alias;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.imageURL = imageURL;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}