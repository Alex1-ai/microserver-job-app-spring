package com.chidi.jobms.job;

import com.chidi.jobms.job.dto.JobDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<List<JobDTO>> findAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable("id") Long id) {
        return jobService.getJobById(id);
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateJob(@PathVariable("id") Long id, @RequestBody Job updatedJob) {
        System.out.println(updatedJob);
        if (updatedJob.getTitle() != null ||
                updatedJob.getDescription() != null ||
                updatedJob.getLocation() != null ||
                updatedJob.getMaxSalary() != null ||
                updatedJob.getMinSalary() != null


        ){
            return jobService.updateJob(id, updatedJob);

        }else{
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteJob(@PathVariable("id") Long id) {
        boolean delete = jobService.deleteJobById(id);
        if (delete) {
            return new ResponseEntity<>("Job deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Job not found", HttpStatus.NOT_FOUND);
        }
    }
}
