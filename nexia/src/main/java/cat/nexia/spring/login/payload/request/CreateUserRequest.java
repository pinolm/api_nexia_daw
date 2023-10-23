package cat.nexia.spring.login.payload.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateUserRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    private Set<String> role;
    
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
  
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
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
 
    public String getName(){
      return name;
    }
  
    public void setName(String name){
      this.name = name;
    }
  
    public String getSurname(){
      return surname;
    }
  
    public void setSurname(String surname){
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
    
      public String getGender(){
        return gender;
      }
    
      public void setGender(String gender){
        this.gender = gender;
      }


    public Set<String> getRole() {
      return this.role;
    }
    
    public void setRole(Set<String> role) {
      this.role = role;
    }

    
}
