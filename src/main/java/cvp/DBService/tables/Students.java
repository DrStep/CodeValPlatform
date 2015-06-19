package cvp.DBService.tables;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by stepa on 20.05.15.
 */

@Entity
@Table (name = "STUDENTS" )
public class Students implements Serializable{

    @Id
    @Column(name = "STUDENT_ID")
    @GeneratedValue
    private Long studentId;

    @Column(name = "STUDENT")
    private String studName;

    @Column(name = "GROUP_NUMB")
    private String group;

    @Column(name = "LABS_COMPLETED")
    private int labsCompleted;

    @Column(name = "FINAL_ASSESSMENT")
    private String finalAssessment;

    @OneToMany(fetch=FetchType.LAZY,
            mappedBy = "student",
            cascade = CascadeType.ALL)
    private Set<Labs> labs;

    public Students() { }

    public Students(String studName, String group, int labsCompleted, String finalAssessment) {
        this.studName = studName;
        this.group = group;
        this.labsCompleted = labsCompleted;
        this.finalAssessment = finalAssessment;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setStudName(String name) {
        this.studName = name;
    }

    public String getStudName() {
        return studName;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setLabsCompleted(int labsCompleted) {
        this.labsCompleted = labsCompleted;
    }

    public int getLabsCompleted() {
        return labsCompleted;
    }

    public void setFinalAssessment(String finalAssessment) {
        this.finalAssessment = finalAssessment;
    }

    public String getFinalAssessment() {
        return finalAssessment;
    }

    public void setLabs(Set<Labs> labs) {
        this.labs = labs;
    }

    public Set<Labs> getLabs() {
        return this.labs;
    }
}
