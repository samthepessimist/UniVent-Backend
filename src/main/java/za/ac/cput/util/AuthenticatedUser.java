package za.ac.cput.util;

import java.security.Principal;

/**

 *
 * @author Amanda Msutu (222428600)
 */
public record AuthenticatedUser(String userId, String email, String role) implements Principal {

    @Override
    public String getName() {
        return email;
    }
}
