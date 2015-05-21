package cvp.DBService.tables;

import org.yecht.Data;

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
    private String time;

    @Column(name = "ASSESMENT")
    private String assessment;

    public Labs() { }

    public Labs(String labName, String studName, int attempts, String time, String assessment) {
        this.labName = labName;
        this.studName = studName;
        this.attempts = attempts;
        this.time = time;
        this.assessment = assessment;
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

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getAssessment() {
        return assessment;
    }
}
