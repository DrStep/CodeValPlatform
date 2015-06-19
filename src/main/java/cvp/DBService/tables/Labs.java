package cvp.DBService.tables;

import javax.persistence.*;

/**
 * Created by stepa on 21.05.15.
 */

@Entity
@Table(name = "LABS")
public class Labs {

    @Id
    @Column(name = "LAB_ID")
    @GeneratedValue
    private Long labId;

    @Column(name = "LAB")
    private String labName;

    @Column(name = "STUDENT_NAME")
    private String studName;

    @Column(name = "ATTEMPTS")
    private int attempts;

    @Column(name = "TIME")
    private Long time;

    @Column(name = "TEST")
    private String test;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID")
    private Students student;

    public Labs() { }

    public Labs(String labName, int attempts, Long time, String test, Students student) {
        this.labName = labName;
        this.studName = student.getStudName();
        this.attempts = attempts;
        this.time = time;
        this.test = test;
        this.student = student;
    }

    public Long getLabId() {
        return labId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
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

    public void setStudent(Students student) {
        this.student = student;
    }

    public Students getStudent() {
        return this.student;
    }
}
