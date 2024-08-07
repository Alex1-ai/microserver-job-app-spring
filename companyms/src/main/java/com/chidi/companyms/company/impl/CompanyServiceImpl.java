package com.chidi.companyms.company.impl;


import com.chidi.companyms.company.Company;
import com.chidi.companyms.company.CompanyRepository;
import com.chidi.companyms.company.CompanyService;
import com.chidi.companyms.company.clients.ReviewClient;
import com.chidi.companyms.company.dto.ReviewMessage;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepo;
    private ReviewClient reviewClient;

    @Autowired
    public CompanyServiceImpl(
            CompanyRepository companyRepo,
            ReviewClient reviewClient) {
        this.companyRepo = companyRepo;
        this.reviewClient = reviewClient;
    }

    @Override
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyRepo.findAll();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Company> createCompany(Company company) {
        Company savedCompany = companyRepo.save(company);
        return new ResponseEntity<>(savedCompany, HttpStatus.CREATED);
    }

    @Override
    public Company getCompanyById(Long id) {
        Optional<Company> companyOpt = companyRepo.findById(id);
        return companyOpt.orElse(null);
    }

    @Override
    public void updateCompanyRating(ReviewMessage reviewMessage) {
        System.out.println(reviewMessage.getDescription());
        Company company = companyRepo.findById(reviewMessage.getCompanyId())
                .orElseThrow(() -> new NotFoundException("company not found "+ reviewMessage.getCompanyId()));

        double averageRating = reviewClient.getAverageRatingForCompany(reviewMessage.getCompanyId());
        company.setRating(averageRating);
        companyRepo.save(company);

    }

    @Override
    public ResponseEntity<String> deleteCompanyById(Long id) {
        if (companyRepo.existsById(id)) {
            try {
                companyRepo.deleteById(id);
                return new ResponseEntity<>("Company deleted successfully", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Error deleting company", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public boolean updateCompany(Long id, Company updatedCompany) {
        Optional<Company> existingCompanyOpt = companyRepo.findById(id);
        if (existingCompanyOpt.isPresent()) {
            Company existingCompany = existingCompanyOpt.get();
            existingCompany.setName(updatedCompany.getName());
            existingCompany.setDescription(updatedCompany.getDescription());

            Company savedCompany = companyRepo.save(existingCompany);
            return true;
        } else {
            return false;
        }
    }
}
