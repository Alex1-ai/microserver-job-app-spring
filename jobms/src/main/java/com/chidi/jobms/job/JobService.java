package com.chidi.jobms.job;

import com.chidi.jobms.job.dto.JobDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JobService {
    ResponseEntity<List<JobDTO>> getAllJobs();


    ResponseEntity<Job> createJob(Job job);

    ResponseEntity<JobDTO> getJobById(Long id);

    boolean deleteJobById(Long id);

    ResponseEntity<String> updateJob(Long id, Job updatedJob);
}
