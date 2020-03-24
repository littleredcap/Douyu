package liang.zhou.lane8.no5.my_player.home_pager_recommend_fragment;

public class LiveRoom {

    private int id;
    private int gameId;
    private String roomName;
    private String roomTitle;
    private String roomPicUrl;
    private int roomHeat;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getRoomPicUrl() {
        return roomPicUrl;
    }

    public void setRoomPicUrl(String roomPicUrl) {
        this.roomPicUrl = roomPicUrl;
    }

    public int getRoomHeat() {
        return roomHeat;
    }

    public void setRoomHeat(int roomHeat) {
        this.roomHeat = roomHeat;
    }
}
