package za.ac.cput.factory;

import za.ac.cput.domain.Organizer;
import za.ac.cput.domain.RoleEnum;
import za.ac.cput.util.Helper;

/**Student name: Amanda Msutu
 * Student number: 222428600
 * Group: 3H
 * OrganizerFactory.java
 * Date: 05 July 2026
 * Updated: 12 September 2026 — removed events list; contactEmail -> organizationEmail
 **/

public class OrganizerFactory {
    public static Organizer createOrganizer(String name,
                                            String email,
                                            String password,
                                            String phoneNumber,
                                            String organizationName,
                                            String organizationType,
                                            String organizationEmail) {
        if (Helper.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Name is required");
        }

        if (!Helper.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email");
        }

        if (Helper.isNullOrEmpty(password)) {
            throw new IllegalArgumentException("Password is required");
        }

        if (!Helper.isValidPhone(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        if (Helper.isNullOrEmpty(organizationName)) {
            throw new IllegalArgumentException("Organization name is required");
        }

        if (Helper.isNullOrEmpty(organizationType)) {
            throw new IllegalArgumentException("Organization type is required");
        }

        if (!Helper.isValidEmail(organizationEmail)) {
            throw new IllegalArgumentException("Invalid organization email");
        }

        return new Organizer.Builder()
                .setUserId(Helper.generateId())
                .setName(name)
                .setEmail(email)
                .setPasswordHash(password)
                .setPhoneNumber(phoneNumber)
                .setRole(RoleEnum.ORGANIZER)        // role is fixed for this subclass
                .setOrganizationName(organizationName)
                .setOrganizationType(organizationType)
                .setOrganizationEmail(organizationEmail)
                .build();
    }
}