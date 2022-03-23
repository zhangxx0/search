package com.xinxin.search.esindex;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "boss_job_index")
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
}
