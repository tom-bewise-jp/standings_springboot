package com.example.demo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StandingsController {
	private static final List<String>teams = Arrays.asList(
			"札幌", "鹿島", "浦和", "柏", "FC東京", "川崎F", "横浜FM", "湘南", "清水",
			"磐田", "名古屋", "京都", "G大阪", "C大阪", "神戸", "広島", "福岡", "鳥栖"
	);
	
	@Autowired
	private MatchesRepository matchesRepository;
	
	@GetMapping("/standings")
	public String standings(Model model) {
		List<Match> matches = matchesRepository.findByOrderByDateAscHomeAsc();
		System.err.println(matches.get(0).getDate());
		model.addAttribute("matches", matches);
		return "standings";
	}
	
	@GetMapping("/new_match")
	public String newMatch(Model model) {
		model.addAttribute("teams", teams);
		return "new_match";
	}
	
	@PostMapping("/new_match")
	public String addNewMatch(
			@RequestParam("date") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam("section") int section,
			@RequestParam("home") String home,
			@RequestParam("away") String away,
			@RequestParam("goals_for") int goalsFor,
			@RequestParam("goals_against") int goalsAgainst,
			Model model) {
		if (section < 1 && section > 34) {
			model.addAttribute("error", "節は1から34で指定してください");
			return "new_match";
		}
		if (date.getYear() != 2022) {
			model.addAttribute("error", "2022年の日付を指定してください");
			return "new_match";
		}
		if (!teams.contains(home)) {
			model.addAttribute("error", "ホームの不明なチーム名が指定されています");
			return "new_match";
		}
		if (!teams.contains(away)) {
			model.addAttribute("error", "アウェイに不明なチーム名が指定されています");
			return "new_match";
		}
		if (goalsFor < 0 || goalsAgainst < 0) {
			model.addAttribute("error", "得点に負の値が指定されています");
			return "new_match";
		}
		List<Match> matches = matchesRepository.findByDateAndHome(date, home);
		if (matches != null && matches.size() > 0) {
			model.addAttribute("error",	"当日のホームチームの試合は指定済みです");
			return "new_match";
		}
		matches = matchesRepository.findByDateAndAway(date, home);
		if (matches != null && matches.size() > 0) {
			model.addAttribute("error",	"当日のホームチームの試合は指定済みです");
			return "new_match";
		}
		matches = matchesRepository.findByDateAndHome(date, away);
		if (matches != null && matches.size() > 0) {
			model.addAttribute("error",	"当日のアウェイチームの試合は指定済みです");
			return "new_match";
		}
		matches = matchesRepository.findByDateAndAway(date, away);
		if (matches != null && matches.size() > 0) {
			model.addAttribute("error",	"当日のアウェイチームの試合は指定済みです");
			return "new_match";
		}
		matches = matchesRepository.findBySectionAndHome(section, home);
		if (matches != null && matches.size() > 0) {
			model.addAttribute("error",	"当該節のホームチームの試合は指定済みです");
			return "new_match";
		}
		matches = matchesRepository.findBySectionAndAway(section, home);
		if (matches != null && matches.size() > 0) {
			model.addAttribute("error",	"当該節のホームチームの試合は指定済みです");
			return "new_match";
		}
		matches = matchesRepository.findBySectionAndHome(section, away);
		if (matches != null && matches.size() > 0) {
			model.addAttribute("error",	"当該節のアウェイチームの試合は指定済みです");
			return "new_match";
		}
		matches = matchesRepository.findBySectionAndAway(section, away);
		if (matches != null && matches.size() > 0) {
			model.addAttribute("error",	"当該節のアウェイチームの試合は指定済みです");
			return "new_match";
		}
		Match match = new Match();
		match.setSection(section);
		match.setDate(date);
		match.setHome(home);
		match.setAway(away);
		match.setGoalsFor(goalsFor);
		match.setGoalsAgainst(goalsAgainst);
		matchesRepository.saveAndFlush(match);
		
		return standings(model);
	}
}
