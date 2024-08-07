package com.chidi.jobms.job.mapper;

import com.chidi.jobms.job.Job;
import com.chidi.jobms.job.dto.JobDTO;
import com.chidi.jobms.job.external.Company;
import com.chidi.jobms.job.external.Review;

import java.util.List;

public class JobMapper {
    public static JobDTO mapToJobWithCompanyDTO(
            Job job,
            Company company,
            List<Review> reviews
    ){
        JobDTO jobDTO = new JobDTO();
        jobDTO.setTitle(job.getTitle());
        jobDTO.setId(job.getId());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setMinSalary(job.getMinSalary());
        jobDTO.setCompany(company);
        jobDTO.setReviews(reviews);


        return jobDTO;

    }
}
