package com.shop.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

/**
 * Created by Aaron on 1/5/2016.
 */
public class Item {

    private String name;
    private Sprite icon;
    private String description;
    private ArrayList<Item> components;

    public Item(String name, Sprite icon, String description, ArrayList<Item> components) {
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.components = components;
    }

    public Item(String name, Sprite icon) {
        this.name = name;
        this.icon = icon;
        this.description = "";
        this.components = new ArrayList<Item>();

    }

    public String getName() {
        return name;
    }

    public Sprite getIcon() { return icon; }

    public String getDescription() {
        return description;
    }

    public ArrayList<Item> getComponents() {
        return components;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Sprite icon) { this.icon = icon; }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setComponents(ArrayList<Item> components) {
        this.components = components;
    }

    /**
     * Checks whether all the items in <code>components</code> are the correct components of the recipe of this item
     * @param components the list of items being compared to the components of this item
     * @return true if the components are the same, otherwise false
     */
    public boolean isRecipe(ArrayList<Item> components){
        ArrayList<Item> tempComponents = new ArrayList<Item>(this.components);
        boolean isComplete=true;
        boolean found = false;
        for(int i=0;i<components.size();i++){
            for(int j=0;j<tempComponents.size();j++){
                if(components.get(i).getName().equals(tempComponents.get(j).getName())){
                    tempComponents.remove(j);
                    found = true;
                }
            }
            if(!found) {
                isComplete = false;
                i=tempComponents.size();
            }
            found = false;
        }
        return isComplete;
    }
}
