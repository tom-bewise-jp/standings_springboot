package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="standings")
public class Standing {
	@Id
	private String team;
	private int pts;
	@Column(name="W")
	private int wins;
	@Column(name="D")
	private int draws;
	@Column(name="L")
	private int loses;
	@Column(name="GFs")
	private int goalsFors;
	@Column(name="GAs")
	private int goalsAgainsts;
	@Column(name="GDs")
	private int goalsDifference;
	public String getTeam() {
		return team;
	}
	public int getPts() {
		return pts;
	}
	public int getWins() {
		return wins;
	}
	public int getDraws() {
		return draws;
	}
	public int getLoses() {
		return loses;
	}
	public int getGoalsFors() {
		return goalsFors;
	}
	public int getGoalsAgainsts() {
		return goalsAgainsts;
	}
	public int getGoalsDifference() {
		return goalsDifference;
	}
}
