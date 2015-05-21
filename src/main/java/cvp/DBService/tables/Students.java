package cvp.DBService.tables;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by stepa on 20.05.15.
 */

@Entity
@Table (name = "STUDENTS" )
public class Students implements Serializable{

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "STUDENT", unique=true)
    private String studName;

    @Column(name = "GROUP_NUMB")
    private String group;

    @Column(name = "LABS_COMPLETED")
    private int labsCompleted;

    @Column(name = "FINAL_ASSESSMENT")
    private String finalAssessment;

    public Students() { }

    public Students(String studName, String group, int labsCompleted, String finalAssessment) {
        this.studName = studName;
        this.group = group;
        this.labsCompleted = labsCompleted;
        this.finalAssessment = finalAssessment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
