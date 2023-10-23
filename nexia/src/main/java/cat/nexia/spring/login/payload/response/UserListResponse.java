package cat.nexia.spring.login.payload.response;

public class UserListResponse {
    
    private Long id;
    private String username;
    private String email;
    private String number;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String gender;
    private String name;
    private String surname;

    public UserListResponse(Long id, String username, String email, String number, String address, String city, String country, String postalCode, String gender, String name, String surname) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.number = number;
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.gender = gender;
        this.name = name;
        this.surname = surname;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
