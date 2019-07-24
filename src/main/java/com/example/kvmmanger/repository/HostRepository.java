package com.example.kvmmanger.repository;

import com.example.kvmmanger.entity.Host;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HostRepository extends JpaRepositoryImplementation<Host,Integer> {
}
