package com.example.demo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchesRepository extends JpaRepository<Match, Integer> {
	List<Match> findByOrderByDateAscHomeAsc();
	List<Match> findByDateAndHome(LocalDate date, String home);
	List<Match> findByDateAndAway(LocalDate date, String home);
	List<Match> findBySectionAndHome(int section, String home);
	List<Match> findBySectionAndAway(int section, String home);
}
