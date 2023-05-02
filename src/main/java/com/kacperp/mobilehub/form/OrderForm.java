package com.kacperp.mobilehub.form;

import com.kacperp.mobilehub.model.OrderProduct;
import com.kacperp.mobilehub.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrderForm {
    @NotEmpty(message = "First Name field cannot be empty.")
    private String firstName;
    @NotEmpty(message = "Last Name field cannot be empty")
    private String lastName;
    @NotEmpty(message = "Email field cannot be empty")
    @Email
    private String email;
    @NotEmpty(message = "City field cannot be empty")
    private String city;
    @NotEmpty(message = "Street field cannot be empty")
    private String street;
    @NotEmpty(message = "Postal code field cannot be empty")
    private String postalCode;
    @NotEmpty(message = "Property number field cannot be empty")
    private String propertyNumber;
    @NotEmpty(message = "Telephone field cannot be empty")
    private String telephone;
    private String comments;
    private List<OrderProduct> cartProducts;
    private User user;
    private double total;
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    public List<OrderProduct> getCartProducts() {
        return cartProducts;
    }
    public void setCartProducts(List<OrderProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
}
