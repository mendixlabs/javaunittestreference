package myfirstmodule;

import static org.junit.jupiter.api.Assertions.assertFalse;
import system.proxies.User;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author reinout
 */
public class MembersTest extends MendixUnitTestBase {
    
    @DisplayName("Asserts that the hasMember() method of a mocked IMendixObject works")
    @Test
    public void testUserHasName() {
        var user = new User(CONTEXT);
        var imo  = user.getMendixObject();
        assertTrue(imo.hasMember(User.MemberNames.Name.name()));
    }
    
    @DisplayName("Asserts that the hasMember() method of a mocked IMendixObject works for the unhappy path")
    @Test
    public void testUserHasNoNonExistingMember() {
        var user = new User(CONTEXT);
        var imo  = user.getMendixObject();
        assertFalse(imo.hasMember("DOES_NOT_EXIST"));
    }
}
