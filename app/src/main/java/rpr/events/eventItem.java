package rpr.events;

/**
 * Created by naman on 13-04-2017.
 */

public class eventItem {
    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getCreator_user_id() {
        return creator_user_id;
    }

    public void setCreator_user_id(int creator_user_id) {
        this.creator_user_id = creator_user_id;
    }

    public int getUsertype_id() {
        return usertype_id;
    }

    public void setUsertype_id(int usertype_id) {
        this.usertype_id = usertype_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    private int event_id;
    private int category_id;
    private int creator_user_id;
    private int usertype_id;
    private String name;
    private String details;
    private String time;
    private String venue;


    public eventItem(int event_id, int category_id, int creator_user_id, int usertype_id, String name, String details, String time, String venue) {
        this.event_id = event_id;
        this.category_id = category_id;
        this.creator_user_id = creator_user_id;
        this.usertype_id = usertype_id;
        this.name = name;
        this.details = details;
        this.time = time;
        this.venue = venue;
    }
}
