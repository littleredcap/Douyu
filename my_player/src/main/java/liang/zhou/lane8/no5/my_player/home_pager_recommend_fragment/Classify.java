package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

import java.util.ArrayList;

public class Classify {

    private String className;
    private ArrayList<StringBuffer> items;

    public Classify(){
        items=new ArrayList<>();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ArrayList<StringBuffer> getItems() {
        return items;
    }

    public void setItems(ArrayList<StringBuffer> items) {
        this.items = items;
    }
}
