package com.fullDemo2.repo;

import com.fullDemo2.Entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BranchRepo extends JpaRepository<Branch,Long> {

    Branch findBranchBybName(String bName);

}
