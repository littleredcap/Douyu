package liang.zhou.lane8.no5.my_player;

import java.util.ArrayList;

public class AnchorEvent {

    private Anchor anchor;
    private String date;
    private ArrayList<EventComment> comments;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AnchorEvent(){
        comments=new ArrayList<>();
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<EventComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<EventComment> comments){
        this.comments=comments;
    }

    public void setComment(EventComment comment) {
        comments.add(comment);
    }
}
