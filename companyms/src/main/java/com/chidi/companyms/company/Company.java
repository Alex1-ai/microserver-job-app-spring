package com.chidi.companyms.company;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double rating;

//    @JsonIgnore
//    @OneToMany(mappedBy = "company")
//    private List<Job> jobs;
//
//    @ToString.Exclude
//    @OneToMany(mappedBy = "company")
//    private List <Review> reviews;






}
