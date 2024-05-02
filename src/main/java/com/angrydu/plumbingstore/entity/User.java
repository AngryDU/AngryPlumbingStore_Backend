package com.angrydu.plumbingstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;
import java.util.UUID;

import static com.angrydu.plumbingstore.service.impl.UserServiceImpl.EMAIL_NOT_CORRECT;
import static com.angrydu.plumbingstore.service.impl.UserServiceImpl.NAME_NOT_CORRECT;
import static com.angrydu.plumbingstore.service.impl.UserServiceImpl.PASSWORD_NOT_CORRECT;
import static com.angrydu.plumbingstore.service.impl.UserServiceImpl.THE_LENGTH_OF_THE_NAME;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    private UUID id;

    @NotBlank(message = EMAIL_NOT_CORRECT)
    @Length(min = 2, max = 255, message = THE_LENGTH_OF_THE_NAME)
    @Pattern(message = "Bad formed person name: ${validatedValue}",
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+\\.[A-Za-z0-9-.]+$")
    @Column(name = "email")
    private String email;

    @NotBlank(message = PASSWORD_NOT_CORRECT)
    @Size(min = 8)
    @Column(name = "password")
    private String password;

    @NotBlank(message = NAME_NOT_CORRECT)
    @Length(min = 2, max = 255, message = THE_LENGTH_OF_THE_NAME)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = NAME_NOT_CORRECT)
    @Length(min = 2, max = 255, message = THE_LENGTH_OF_THE_NAME)
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "about_yourself")
    private String aboutYourself;

    @Column(name = "is_active")
    private boolean isActive;

        public UUID getId() {
                return id;
        }

        public void setId(UUID id) {
                this.id = id;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

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

        public Status getStatus() {
                return status;
        }

        public void setStatus(Status status) {
                this.status = status;
        }

        public String getPhone() {
                return phone;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }

        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public String getAboutYourself() {
                return aboutYourself;
        }

        public void setAboutYourself(String aboutYourself) {
                this.aboutYourself = aboutYourself;
        }

        public boolean isActive() {
                return isActive;
        }

        public void setActive(boolean active) {
                isActive = active;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                User user = (User) o;
                return isActive == user.isActive && Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && status == user.status && Objects.equals(phone, user.phone) && Objects.equals(address, user.address) && Objects.equals(aboutYourself, user.aboutYourself);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id, email, password, firstName, lastName, status, phone, address, aboutYourself, isActive);
        }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status=" + status +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", aboutYourself='" + aboutYourself + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    public enum Status {
        USER, ADMINISTRATOR
    }
}
