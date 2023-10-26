package cat.nexia.spring.payload.request;

import javax.validation.constraints.Size;

public class UpdateUserRequest {

    @Size(max = 50)
    private String name;
    @Size(max = 50)
    private String surname;
    @Size(max = 30)
    private String number;
    @Size(max = 60)
    private String address;
    @Size(max = 30)
    private String city;
    @Size(max = 30)
    private String country;
    @Size(max = 30)
    private String postalCode;
    @Size(max = 10)
    private String gender;
    @Size(max = 50)
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
