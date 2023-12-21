package db.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SecurityManager {

    private static final Map<String, String> passwords = new HashMap<>();
    private static final Map<String, acesRights> acesRights = new HashMap<>();
    private final MessageDigest md;


    public SecurityManager() throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("MD5");
        passwords.put("admin", getHash("admin"));
        passwords.put("user", getHash("user"));
        acesRights.put("admin", db.security.acesRights.ADMIN);
        acesRights.put("user", db.security.acesRights.USER);
    }

    public boolean logIn(String username, String password) {
        String passwordHash = passwords.get(username);
        if (passwordHash == null) return false;
        return passwordHash.equals(getHash(password));
    }

    public String getHash(String toHash){
        md.update(toHash.getBytes());
        return new String(md.digest());
    }

    public boolean isAdmin(String userName) {
        return acesRights.get(userName) == db.security.acesRights.ADMIN;
    }
}
