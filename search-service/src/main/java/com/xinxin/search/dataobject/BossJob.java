package com.xinxin.search.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Tolerate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BossJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String area;
    private String salary;
    private String agelimit;
    private String education;
    private String recruiter;
    private String recruiterPosition;
    private String companyName;
    private String companyType;
    private String companyLogo;
    private String finance;
    private String companySize;
    private String tags;
    private String welfare;
    private String remark;

    private Integer createId;
    private Date createDate;
    private Integer updateId;
    private Date updateDate;

    @Override
    public String toString() {
        return "BossJob{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", area='" + area + '\'' +
                ", salary='" + salary + '\'' +
                ", agelimit='" + agelimit + '\'' +
                ", education='" + education + '\'' +
                ", recruiter='" + recruiter + '\'' +
                ", recruiterPosition='" + recruiterPosition + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyType='" + companyType + '\'' +
                ", companyLogo='" + companyLogo + '\'' +
                ", finance='" + finance + '\'' +
                ", companySize='" + companySize + '\'' +
                ", tags='" + tags + '\'' +
                ", welfare='" + welfare + '\'' +
                ", remark='" + remark + '\'' +
                ", createId=" + createId +
                ", createDate=" + createDate +
                ", updateId=" + updateId +
                ", updateDate=" + updateDate +
                '}';
    }
}
