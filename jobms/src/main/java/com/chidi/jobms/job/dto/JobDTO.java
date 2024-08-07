package com.chidi.jobms.job.dto;

import com.chidi.jobms.job.external.Company;
import com.chidi.jobms.job.external.Review;
import lombok.Data;

import java.util.List;

@Data
public class JobDTO {
    private Long id;
    private String title;
    private String description;
    private String minSalary;
    private String maxSalary;
    private String location;
    private Company company;
    private List<Review> reviews;
}
