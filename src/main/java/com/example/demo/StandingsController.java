package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StandingsController {
	@Autowired
	private MatchesRepository matchesRepository;
	
	@GetMapping("/standings")
	public String standings(Model model) {
		List<Match> matches = matchesRepository.findByOrderByDateAscHomeAsc();
		System.err.println(matches.get(0).getDate());
		model.addAttribute("matches", matches);
		return "standings";
	}
}
