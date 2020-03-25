package me.ford.periodicholographicdisplays.users;

/**
 * UserManager
 */
public interface UserStorage {

    public UserCache getCache();

    public void save(boolean inSync);
    
}