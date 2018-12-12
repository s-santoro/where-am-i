package models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This class represents a session with Fields for id, score, timestamp, user_fk (reference to user)
 * and location_fk (reference to location)
 * The sessions are mapped to the Entity "sessions" in PostgreSQL-Database
 * Field "id" is generated automatically by the Database
 */

@Entity(name="sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long user_fk;
    private long location_fk;
    private int score;
    private String timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_fk() {
        return user_fk;
    }

    public void setUser_fk(long user_fk) {
        this.user_fk = user_fk;
    }

    public long getLocation_fk() {
        return location_fk;
    }

    public void setLocation_fk(long location_fk) {
        this.location_fk = location_fk;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
