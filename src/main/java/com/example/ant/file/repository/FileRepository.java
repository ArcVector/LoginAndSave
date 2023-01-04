package com.example.ant.file.repository;

import com.example.ant.file.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository

public interface FileRepository extends JpaRepository<File, String> {

}
