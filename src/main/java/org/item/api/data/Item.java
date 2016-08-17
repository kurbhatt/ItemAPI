package org.item.api.data;

/**
 * Created by Keyur on 12-08-2016.
 */
public class Item {

    private Integer id;
    private String name;
    private Integer priority;

    public Item() {
    }

    public Item(String name, Integer priority) {
        this.name = name;
        this.priority = priority;
    }

    public Item(Integer id, String name, Integer priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
