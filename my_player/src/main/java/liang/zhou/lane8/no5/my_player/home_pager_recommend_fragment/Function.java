package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import java.sql.Date;
import java.sql.Time;

public class Function {

	private int id;
	private String functionName;
	private Date functionDate;
	private Time functionTime;
	private Date postDate;
	private Time postTime;
	private int subPopulation;
	
	public Time getFunctionTime() {
		return functionTime;
	}
	public void setFunctionTime(Time functionTime) {
		this.functionTime = functionTime;
	}
	public Time getPostTime() {
		return postTime;
	}
	public void setPostTime(Time postTime) {
		this.postTime = postTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public Date getFunctionDate() {
		return functionDate;
	}
	public void setFunctionDate(Date functionDate) {
		this.functionDate = functionDate;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	public int getSubPopulation() {
		return subPopulation;
	}
	public void setSubPopulation(int subPopulation) {
		this.subPopulation = subPopulation;
	}
	
	
}
