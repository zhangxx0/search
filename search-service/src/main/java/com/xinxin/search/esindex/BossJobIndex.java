package com.xinxin.search.esindex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "boss_job_index")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BossJobIndex {
    @Id
    private Long id;
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String name;
    private String area;
    private String salary;
    private String agelimit;
    private String education;
    private String recruiter;
    private String recruiterPosition;
    @Field(type = FieldType.Text, analyzer = "ik_smart")
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
        return "BossJobIndex++{" +
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
