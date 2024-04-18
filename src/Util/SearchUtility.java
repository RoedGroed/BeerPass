package Util;

import BE.User;

import java.util.List;
import java.util.stream.Collectors;

public class SearchUtility {

    /**
     * Filters a list of users by a given search query on their name or email.
     * @param allUsers List of all users
     * @param query The search query
     * @return Filtered list of users
     */
    public static List<User> filterUsers(List<User> allUsers, String query) {
        if (query == null || query.isEmpty()) {
            return allUsers;
        }
        String lowerCaseQuery = query.toLowerCase();
        return allUsers.stream()
                .filter(user -> user.getUsername().toLowerCase().contains(lowerCaseQuery) ||
                        user.getEmail().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());
    }
}