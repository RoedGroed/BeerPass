import BE.User;
import Util.SearchUtility;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SearchUtilityTest {

    @Test
    public void testFilterUsersByName(){
        User testUser1 = new User(1, "Sonja Svendsen", "Sonsve@easv365.dk");
        User testUser2 = new User(2, "Preben Poulsen", "Prepou@easv365.dk");
        User testUser3 = new User(3, "Ursula Gregers", "Ursgre@easv365.dk");

        List<User> users = Arrays.asList(testUser1, testUser2, testUser3);

        List<User> filteredByName = SearchUtility.filterUsers(users, "Preben");
        assertEquals(1, filteredByName.size());
        assertTrue(filteredByName.contains(testUser2));
    }

    @Test
    public void testFilterUsersByEmail(){
        User testUser1 = new User(1, "Sonja Svendsen", "Sonsve@easv365.dk");
        User testUser2 = new User(2, "Preben Poulsen", "Prepou@easv365.dk");
        User testUser3 = new User(3, "Ursula Gregers", "Ursgre@easv365.dk");

        List<User> users = Arrays.asList(testUser1, testUser2, testUser3);

        List<User> filteredByEmail = SearchUtility.filterUsers(users, "Ursgre@");
        assertEquals(1, filteredByEmail.size());
        assertTrue(filteredByEmail.contains(testUser3));
    }

    @Test
    public void testNoMatch() {
        User testUser1 = new User(1, "Sonja Svendsen", "Sonsve@easv365.dk");
        User testUser2 = new User(2, "Preben Poulsen", "Prepou@easv365.dk");
        User testUser3 = new User(3, "Ursula Gregers", "Ursgre@easv365.dk");

        List<User> users = Arrays.asList(testUser1, testUser2, testUser3);

        List<User> noMatches = SearchUtility.filterUsers(users, "!xyz!");
        assertTrue(noMatches.isEmpty());
    }
}