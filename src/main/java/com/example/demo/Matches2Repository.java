package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Matches2Repository extends JpaRepository<Match2, Integer> {
	List<Match2> findBySectionAndHomeId(int section, int homeId);
}
