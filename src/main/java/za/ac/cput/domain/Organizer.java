package za.ac.cput.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**Student name: Amanda Msutu
 * Student number: 222428600
 * Group: 3H
 * Organizer.java
 * Date: 05 July 2026
 **/

@Entity
@Table(name = "organizer")
public class Organizer extends User {
    private String organizationName;
    private String organizationType;
    private String organizationEmail;

    public Organizer() {
    }

    private Organizer(Builder builder) {
        this.userId = builder.userId;
        this.name = builder.name;
        this.email = builder.email;
        this.passwordHash = builder.passwordHash;
        this.phoneNumber = builder.phoneNumber;
        this.role = builder.role;
        this.organizationName = builder.organizationName;
        this.organizationType = builder.organizationType;
        this.organizationEmail = builder.organizationEmail;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public String getOrganizationEmail() {
        return organizationEmail;
    }

    @Override
    public String toString() {
        return "Organizer{" +
                "organizationName='" + organizationName + '\'' +
                ", organizationType='" + organizationType + '\'' +
                ", organizationEmail='" + organizationEmail + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", disabled=" + disabled +
                '}';
    }

    public static class Builder {
        private String userId;
        private String name;
        private String email;
        private String passwordHash;
        private String phoneNumber;
        private RoleEnum role;
        private String organizationName;
        private String organizationType;
        private String organizationEmail;

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

        public Builder setOrganizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public Builder setOrganizationType(String organizationType) {
            this.organizationType = organizationType;
            return this;
        }

        public Builder setOrganizationEmail(String organizationEmail) {
            this.organizationEmail = organizationEmail;
            return this;
        }

        public Organizer build() {
            return new Organizer(this);
        }
    }
}