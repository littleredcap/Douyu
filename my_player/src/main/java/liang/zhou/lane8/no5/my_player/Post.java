package liang.zhou.lane8.no5.my_player;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.sql.Date;

import liang.zhou.lane8.no5.my_business.data_model.User;

public class Post {

	private int postId;
	private int liveRoomId;
	private int userId;
	private int howManyRead;
	private int howManyLike;
	private String title;
	private String content;
	private Time postTime;
	private Date postDate;
	private User user;
	private int replyToWhom;

	public int getReplyToWhom() {
		return replyToWhom;
	}

	public void setReplyToWhom(int replyToWhom) {
		this.replyToWhom = replyToWhom;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	public int getLiveRoomId() {
		return liveRoomId;
	}
	public void setLiveRoomId(int liveRoomId) {
		this.liveRoomId = liveRoomId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getHowManyRead() {
		return howManyRead;
	}
	public void setHowManyRead(int howManyRead) {
		this.howManyRead = howManyRead;
	}
	public int getHowManyLike() {
		return howManyLike;
	}
	public void setHowManyLike(int howManyLike) {
		this.howManyLike = howManyLike;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Time getPostTime() {
		return postTime;
	}
	public void setPostTime(Time postTime) {
		this.postTime = postTime;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

}
