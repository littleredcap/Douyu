package liang.zhou.lane8.no5.my_business.data_model;

import android.os.Parcel;
import android.os.Parcelable;

public class User{
	
	private int userId=-1;
	private String username;
	private String password;
	private String rank;
	private String sex;
	private String updatedColumn;
	private String birthday;
	private String homeAddress;
	private String alwaysAppearance;
	private String mailBox;
	private String mobile;
	private String portrait;
	private String subscribedFuncId;
	private int daily_contribute_value;
	private int last_daily_contribute_value;

	public int getLast_daily_contribute_value() {
		return last_daily_contribute_value;
	}

	public void setLast_daily_contribute_value(int last_daily_contribute_value) {
		this.last_daily_contribute_value = last_daily_contribute_value;
	}

	public int getDaily_contribute_value() {
		return daily_contribute_value;
	}

	public void setDaily_contribute_value(int daily_contribute_value) {
		this.daily_contribute_value = daily_contribute_value;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUpdatedColumn() {
		return updatedColumn;
	}
	public void setUpdatedColumn(String updatedColumn) {
		this.updatedColumn = updatedColumn;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getAlwaysAppearance() {
		return alwaysAppearance;
	}
	public void setAlwaysAppearance(String alwaysAppearance) {
		this.alwaysAppearance = alwaysAppearance;
	}
	public String getMailBox() {
		return mailBox;
	}
	public void setMailBox(String mailBox) {
		this.mailBox = mailBox;
	}
	public String getSubscribedFuncId() {
		return subscribedFuncId;
	}
	public void setSubscribedFuncId(String subscribedFuncId) {
		this.subscribedFuncId = subscribedFuncId;
	}
}
