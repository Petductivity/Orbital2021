package com.example.petductivity.planner;

/**
 * A helper class for the PlannerWeekActivity class.
 *
 * @author Team Petductivity
 */
public class PlannerWeekEventModel {

    /**
     * date: The date selected.
     * activity: The title of the activity.
     * id: The position of the activity in the database.
     * startTime: The start time of the activity.
     * endTime: The end time of the activity.
     * duration: The duration of the activity.
     */
    private String date, activity, id, startTime, endTime;
    private int duration;

    /**
     * Null constructor for an instance of the class.
     */
    public PlannerWeekEventModel() {
    }

    /**
     * Constructor.
     *
     * @param date The date selected.
     * @param activity The title of the activity.
     * @param id The position of the activity in the database.
     * @param startTime The start time of the activity.
     * @param endTime The end time of the activity.
     * @param duration The duration of the activity.
     */
    public PlannerWeekEventModel(String date, String activity, String id, String startTime, String endTime, int duration) {
        this.date = date;
        this.activity = activity;
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    /**
     * Getter for the date.
     *
     * @return A String.
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for the date.
     *
     * @param date The selected date.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter for the activity title.
     *
     * @return A String.
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Setter for the activity title.
     *
     * @param activity The activity title.
     */
    public void setActivity(String activity) {
        this.activity = activity;
    }

    /**
     * Getter for the position of the activity in the database.
     *
     * @return A String.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for the position of the activity in the database.
     *
     * @param id The position of the activity in the database.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for the start time.
     *
     * @return A String.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Setter for the start time.
     *
     * @param startTime The start time.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter for the end time.
     *
     * @return A String.
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Setter for the end time.
     *
     * @param endTime The end time.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Getter for the duration.
     *
     * @return An integer.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Setter for the duration.
     *
     * @param duration The duration of the activity.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
