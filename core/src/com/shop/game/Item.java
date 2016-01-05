package com.shop.game;

/**
 * Created by Aaron on 1/5/2016.
 */
public class Item {
    private String name;
    private String description;
    private Item[] components;

    public Item(String name, String description, Item[] components) {
        this.name = name;
        this.description = description;
        this.components = components;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Item[] getComponents() {
        return components;
    }

    /**
     * Checks whether <code>item</code> is a component of this <code>item</code>.
     * @param item the item being checked
     * @return true if <code>item</code> is a component of this <code>item</code>, false otherwise.
     */
    public boolean contains(Item item){
        if(java.util.Arrays.asList(components).indexOf(item) >= 0)
            return true;
        else
            return false;
    }
}
