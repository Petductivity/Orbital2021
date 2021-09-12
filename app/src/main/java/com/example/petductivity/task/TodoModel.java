package com.example.petductivity.task;

/**
 * A helper class for the TodoMainActivity class.
 *
 * @author Team Petductivity
 */
public class TodoModel {
    /**
     * task: Title of task
     * description: Description of task
     * id: Position in database
     * date: Due date of task
     * isSet: Indicates if notification is set
     * order: Order to display on the device
     */
    private String task, description, id, date, order;
    boolean isSet;

    /**
     * Null constructor for an instance of the class.
     */
    public TodoModel() {
    }

    /**
     * Constructor for TodoModel instance.
     *
     * @param task Title of task
     * @param description Description of task
     * @param id Position in database
     * @param date Due date of task
     * @param isSet Indicates if notification is set
     */
    public TodoModel(String task, String description, String id, String date, boolean isSet, String order) {
        this.task = task;
        this.description = description;
        this.id = id;
        this.date = date;
        this.isSet = isSet;
        this.order = order;
    }

    /**
     * Getter for the title of task.
     *
     * @return A string indicating title of task.
     */
    public String getTask() {
        return task;
    }

    /**
     * Setter for the title of task.
     *
     * @param task Title of task.
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * Getter for the description of task.
     *
     * @return A string indicating description of task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description of task.
     *
     * @param description Description of task.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the id of task.
     *
     * @return A string indicating id of task.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for the id of task.
     *
     * @param id Id of task.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for the due date of task.
     *
     * @return A string indicating due date of task.
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for the due date of task.
     *
     * @param date Due date of task.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter for the boolean indicating if the notification is set for the task.
     *
     * @return A boolean indicating if the notification is set for the task.
     */
    public boolean getIsSet() {
        return isSet;
    }

    /**
     * Setter for the boolean indicating if the notification is set for the task.
     *
     * @param isSet A boolean indicating if the notification is set for the task.
     */
    public void setIsSet(boolean isSet) {
        this.isSet = isSet;
    }

    /**
     * Getter for order of the task.
     *
     * @return A string indicating the month-day.
     */
    public String getOrder() {
        return order;
    }

    /**
     * Setter for order of the task.
     *
     * @param order A string indicating the month-day.
     */
    public void setOrder(String order) {
        this.order = order;
    }
}
