package com.chidi.companyms.company;

import com.chidi.companyms.company.dto.ReviewMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CompanyService {
    ResponseEntity<List<Company>> getAllCompanies();

    boolean updateCompany(Long id, Company updatedCompany);

    ResponseEntity<Company> createCompany(Company company);

    ResponseEntity<String> deleteCompanyById(Long id);

    Company getCompanyById(Long id);
    public void updateCompanyRating(ReviewMessage reviewMessage);
}
