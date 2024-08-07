package com.chidi.companyms.company;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private  CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }


    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Long id, @RequestBody Company updatedCompany) {
        Boolean isUpdated =companyService.updateCompany(id, updatedCompany);
        if(isUpdated){
            return new ResponseEntity<>("Company updated successfully", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Company not found", HttpStatus.OK);

        }
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company){
        return companyService.createCompany(company);
    }

    @GetMapping("{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id){

        Company company = companyService.getCompanyById(id);
        if (company != null){
            return new ResponseEntity<>(company, HttpStatus.OK);


        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCompanyById(@PathVariable Long id){
        return companyService.deleteCompanyById(id);

    }

}


