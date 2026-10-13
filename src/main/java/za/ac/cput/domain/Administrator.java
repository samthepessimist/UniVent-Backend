package za.ac.cput.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.List;

/**Student name: Amanda Msutu
 * Student number: 222428600
 * Group: 3H
 * AdministratorFactory.java
 * Date: 05 July 2026
 * **/

@Entity
@Table(name ="administrator")
public class Administrator extends User {
    private String adminLevel;


    public Administrator() {
    }

    private Administrator(Builder builder) {
        this.userId = builder.userId;
        this.name = builder.name;
        this.email = builder.email;
        this.passwordHash = builder.passwordHash;
        this.phoneNumber = builder.phoneNumber;
        this.role = builder.role;
        this.adminLevel = builder.adminLevel;

    }

    public String getAdminLevel() {
        return adminLevel;
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "adminLevel='" + adminLevel + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", disabled=" + disabled +
                '}';
    }

    public static class Builder{
        private String userId;
        private String name;
        private String email;
        private String passwordHash;
        private String phoneNumber;
        private RoleEnum role;
        private String adminLevel;

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setRole(RoleEnum role) {
            this.role = role;
            return this;
        }

        public Builder setAdminLevel(String adminLevel) {
            this.adminLevel = adminLevel;
            return this;
        }
        public Administrator build() {
            return new Administrator(this);
        }
    }
}

