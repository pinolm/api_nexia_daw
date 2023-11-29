package cat.nexia.spring.dto.request;

import javax.validation.constraints.Size;

/**
 * POJO del DTO de la clase UpdateUserRequestDto
 */

public class UpdateUserRequestDto {

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
    @Size(max = 50)
    private String email;

    private String image;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
