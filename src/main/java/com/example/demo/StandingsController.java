package com.example.demo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StandingsController {
	private static final List<String>teams = Arrays.asList(
			"札幌", "鹿島", "浦和", "柏", "FC東京", "川崎F", "横浜FM", "湘南", "清水",
			"磐田", "名古屋", "京都", "G大阪", "C大阪", "神戸", "広島", "福岡", "鳥栖"
	);
	
	@Autowired
	private MatchesRepository matchesRepository;
	@Autowired
	private Matches2Repository matches2Repository;
	@Autowired
	private StandingsRepository standingsRepository;
	@Autowired
	private TeamsRepository teamsRepository;
	
	@GetMapping("/matches")
	public String matches(Model model) {
		List<Match> matches = matchesRepository.findByOrderByDateAscHomeAsc();
		model.addAttribute("matches", matches);
		return "matches";
	}
	
	@GetMapping("/standings")
	public String standings(Model model) {
		List<Standing> standings = standingsRepository.findAll();
		model.addAttribute("standings", standings);
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
		
		return matches(model);
	}
	
	@GetMapping("/teams")
	public ModelAndView teams(ModelAndView mv) {
		List<Team> teams = teamsRepository.findAll();
		mv.addObject("teams", teams);
		mv.setViewName("teams");
		return mv;
	}
	
	@GetMapping("/teams/add")
	public ModelAndView teamsAdd(ModelAndView mv) {
		mv.setViewName("teams_add");
		return mv;
	}
	
	@PostMapping("/teams/add")
	public String teamsAdd(
			@RequestParam("name") String name,
			@RequestParam("abbr") String abbr,
			Model model) {
		Team team = new Team();
		team.setId(null);
		team.setName(name);
		team.setAbbr(abbr);
		teamsRepository.saveAndFlush(team);
		return "redirect:/teams";
	}
	
	@GetMapping("/teams/edit")
	public String teamsEdit(
			@RequestParam("id") int id,
			Model model) {
		Optional<Team> record = teamsRepository.findById(id);
		if (record == null || record.isEmpty()) {
			return "redirect:/teams";
		}
		Team team = record.get();
		model.addAttribute("id", team.getId());
		model.addAttribute("name", team.getName());
		model.addAttribute("abbr", team.getAbbr());
		return "teams_edit";
	}
	
	@PostMapping("/teams/edit")
	public String teamsEdit(
			@RequestParam("id") int id,
			@RequestParam("name") String name,
			@RequestParam("abbr") String abbr,
			Model model) {
		Optional<Team> record = teamsRepository.findById(id);
		if (record == null || record.isEmpty()) {
			return "redirect:/teams";
		}
		Team team = record.get();
		team.setName(name);
		team.setAbbr(abbr);
		teamsRepository.saveAndFlush(team);
		return "redirect:/teams";
	}

	@PostMapping("/teams/delete")
	public String teamsDelete(
			@RequestParam("id") int id,
			Model model) {
		Optional<Team> record = teamsRepository.findById(id);
		if (record == null || record.isEmpty()) {
			return "redirect:/teams";
		}
		Team team = record.get();
		teamsRepository.delete(team);
		return "redirect:/teams";
	}
	
	@GetMapping(value="/match_list", produces=MediaType.TEXT_XML_VALUE + "; charset=UTF-8")
	public String matchList(Model model) {
		List<Match> matches = matchesRepository.findAll();
		model.addAttribute("matches", matches);
		
		return "match_list";
	}
	
	@GetMapping("/upgrade")
	public String upgrade(Model model) {
		List<Match> matches = matchesRepository.findAll();
		for (Match match: matches) {
			Optional<Team> home = teamsRepository.findByAbbr(match.getHome());
			Optional<Team> away = teamsRepository.findByAbbr(match.getAway());
			if (home == null || home.isEmpty() || away == null || away.isEmpty()) {
				// 不正なチーム名を検出
				return "redirect:/teams";
			}
			List<Match2> matches2 = matches2Repository.findBySectionAndHomeId(match.getSection(), home.get().getId());
			if (matches2 == null || matches2.size() < 1) {
				// 試合を登録する
				Match2 match2 = new Match2();
				match2.setId(null);
				match2.setDate(match.getDate());
				match2.setSection(match.getSection());
				match2.setHome(home.get());
				match2.setAway(away.get());
				match2.setGoalsFor(match.getGoalsFor());
				match2.setGoalsAgainst(match.getGoalsAgainst());
				matches2Repository.saveAndFlush(match2);
			}
		}
			
		return "redirect:/teams";
	}
}
