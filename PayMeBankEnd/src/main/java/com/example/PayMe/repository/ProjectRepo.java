package com.example.PayMe.repository;

import com.example.PayMe.Entity.PostProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends JpaRepository<PostProject,Long> {
}
