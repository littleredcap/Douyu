package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import java.util.ArrayList;

public class GameClass {
	private int classId;
	private String className;
    private ArrayList<Game> items;

    public GameClass(){
        setItems(new ArrayList<>());
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

   

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public ArrayList<Game> getItems() {
		return items;
	}

	public void setItems(ArrayList<Game> items) {
		this.items = items;
	}
}
