package cvp.DBService.tables;

import javax.persistence.*;

/**
 * Created by stepa on 21.05.15.
 */

@Entity
@Table(name = "LABS")
public class Labs {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "LAB")
    private String labName;

    @Column(name = "STUDENT")
    private String studName;

    @Column(name = "ATTEMPTS")
    private int attempts;

    @Column(name = "TIME")
    private Long time;

    @Column(name = "TEST")
    private String test;

    public Labs() { }

    public Labs(String labName, String studName, int attempts, Long time, String test) {
        this.labName = labName;
        this.studName = studName;
        this.attempts = attempts;
        this.time = time;
        this.test = test;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLabName(String name) {
        this.labName = name;
    }

    public String getLabName() {
        return labName;
    }

    public void setStudName(String name) {
        this.studName = name;
    }

    public String getStudName() {
        return studName;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getTest() {
        return test;
    }
}
