package models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This class represents an user with Fields for id, username, password and avatar
 * The users are mapped to the Entity "users" in PostgreSQL-Database
 * Field "id" is generated automatically by the Database
 */

@Entity(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String salt;
    // save id of avatar
    private Long avatar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() { return salt; }

    public void setSalt(String salt) { this.salt = salt; }

    public Long getAvatar() {
        return avatar;
    }

    public void setAvatar(Long avatar) {
        this.avatar = avatar;
    }

    /**
     * Update the user with new parameters.
     * @param updatedUser
     */
    public void mapTo(User updatedUser) {
        this.username = updatedUser.username;
        this.password = updatedUser.password;
        this.avatar = updatedUser.avatar;
    }
}
