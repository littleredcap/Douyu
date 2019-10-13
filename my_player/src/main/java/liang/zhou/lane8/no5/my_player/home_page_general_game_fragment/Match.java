package liang.zhou.lane8.no5.my_player.home_page_general_game_fragment;

import java.sql.Date;
import java.sql.Time;

public class Match {

	private int id;
	private String matchName;
	private Date matchDate;
	private Time matchTime;
	private String videoRecord;
	private String teamA;
	private String teamB;
	private int teamAWin;
	private int teamBWin;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMatchName() {
		return matchName;
	}
	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}
	public Date getMatchDate() {
		return matchDate;
	}
	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}
	public Time getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(Time matchTime) {
		this.matchTime = matchTime;
	}
	public String getVideoRecord() {
		return videoRecord;
	}
	public void setVideoRecord(String videoRecord) {
		this.videoRecord = videoRecord;
	}
	public String getTeamA() {
		return teamA;
	}
	public void setTeamA(String teamA) {
		this.teamA = teamA;
	}
	public String getTeamB() {
		return teamB;
	}
	public void setTeamB(String teamB) {
		this.teamB = teamB;
	}
	public int getTeamAWin() {
		return teamAWin;
	}
	public void setTeamAWin(int teamAWin) {
		this.teamAWin = teamAWin;
	}
	public int getTeamBWin() {
		return teamBWin;
	}
	public void setTeamBWin(int teamBWin) {
		this.teamBWin = teamBWin;
	}
	
}
