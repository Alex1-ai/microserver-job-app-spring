package com.chidi.jobms.job.impl;


import com.chidi.jobms.job.Job;
import com.chidi.jobms.job.JobRepository;
import com.chidi.jobms.job.JobService;
import com.chidi.jobms.job.clients.CompanyClient;
import com.chidi.jobms.job.clients.ReviewClient;
import com.chidi.jobms.job.dto.JobDTO;
import com.chidi.jobms.job.external.Company;
import com.chidi.jobms.job.external.Review;
import com.chidi.jobms.job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepo;

    private CompanyClient companyClient;

    private  ReviewClient reviewClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    public JobServiceImpl(
            JobRepository jobRepo,
            CompanyClient companyClient,
            ReviewClient reviewClient
    )
    {
        this.jobRepo = jobRepo;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    @Override
    @CircuitBreaker(
            name=  "companyBreaker",
            fallbackMethod = "companyBreakerFallback"
    )
    public ResponseEntity<List<JobDTO>> getAllJobs() {
        List<Job> jobs = jobRepo.findAll();
//        System.out.print("Size " + jobs.size());
        List<JobDTO> jobDTOS = new ArrayList<>();



        jobDTOS = jobs.stream().map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(jobDTOS, HttpStatus.OK);
    }

    public List<String> companyBreakerFallback(Exception e){
        List<String> list = new ArrayList<>();
        list.add("Dummy");
        return list;
    }
    private JobDTO convertToDTO(Job job){
//        RestTemplate restTemplate = new RestTemplate();
//            JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
//            jobWithCompanyDTO.setJob(job);
           JobDTO jobDTO;


            try {
//                Company company = restTemplate.getForObject("http://localhost:8081/companies/" + job.getCompanyId(), Company.class);
//                Company company = restTemplate.getForObject("http://COMPANY-SERVICE:8081/companies/" + job.getCompanyId(), Company.class);
                Company company = companyClient.getCompany(job.getCompanyId());
//                ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
//                        "http://REVIEW-SERVICE:8081/reviews?companyId=" + job.getCompanyId(),
//                        HttpMethod.GET,
//                        // null is for request boddy
//                        null,
//                        new ParameterizedTypeReference<List<Review>>(){
//
//                        });
//                 List<Review> reviews = reviewResponse.getBody();
                List<Review> reviews = reviewClient.getReviews(job.getCompanyId());
//                jobWithCompanyDTO.setCompany(company);
                jobDTO = JobMapper.mapToJobWithCompanyDTO(job, company, reviews);
            } catch (Exception e) {

                // Handle the exception appropriately (log it, set a default value, etc.)
                System.err.println("Failed to fetch company details for job ID " + job.getId() + ": " + e.getMessage());
                 jobDTO = JobMapper.mapToJobWithCompanyDTO(job, null, null);
//                throw new Exception("An error occured");
            }

            return jobDTO;
    }




    @Override
    public ResponseEntity<Job> createJob(Job job) {
        Job savedJob = jobRepo.save(job);
        return new ResponseEntity<>(savedJob, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<JobDTO> getJobById(Long id) {
        Job job = jobRepo.findById(id).orElse(null);
        if ( job != null) {
            return new ResponseEntity<>(convertToDTO(job), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public boolean deleteJobById(Long id) {
        if (jobRepo.existsById(id)) {
            try {
                jobRepo.deleteById(id);
                return true;
            } catch (Exception e) {
                // Optionally log the exception
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public ResponseEntity<String> updateJob(Long id, Job updatedJob) {
        Optional<Job> existingJobOpt = jobRepo.findById(id);
        if (existingJobOpt.isPresent()) {
            Job existingJob = existingJobOpt.get();
            existingJob.setTitle(updatedJob.getTitle());
            System.out.println(existingJob);
            existingJob.setDescription(updatedJob.getDescription());
            existingJob.setMinSalary(updatedJob.getMinSalary());
            existingJob.setMaxSalary(updatedJob.getMaxSalary());
            existingJob.setLocation(updatedJob.getLocation());
            jobRepo.save(existingJob);
            return new ResponseEntity<>("Job updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Job not found", HttpStatus.NOT_FOUND);
        }
    }
}
