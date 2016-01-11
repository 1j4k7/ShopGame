package com.shop.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopGame extends ApplicationAdapter {

    //file reading
    private FileHandle itemListHandle;
    private FileHandle itemAssemblyHandle;
    private FileHandle itemDescriptionHandle;

    //general
    private SpriteBatch batch;
    private Texture[] itemTextures;
    private Sprite[] itemSprites;

    //map of all items (key: name, value:Item)
    private HashMap<String, Item> itemsDict;

    //text
    private int guessesLeft;
    private BitmapFont guessesLeftText;
    private int score;
    private BitmapFont scoreText;
    private int consecutive;//number correct in a row
    private BitmapFont consecutiveText;

    //graphics
    private ShapeRenderer shapeRenderer;
    private Rectangle mainItemBox;
    private ArrayList<Rectangle> componentBoxes;
    private ArrayList<Rectangle> choiceBoxes;

    @Override
    public void create() { //imports all the assets
        //81 complex items and 65 basic items
        //basic items are 0-64 and complex items are 65-146
        batch = new SpriteBatch();//contains all sprites to be drawn to the screen
        itemTextures = new Texture[147];
        itemSprites = new Sprite[147];
        shapeRenderer = new ShapeRenderer();

        //readFilesWindows();
        readFilesOSX();

        guessesLeft = 3;
        guessesLeftText = new BitmapFont();
        score = 0;
        scoreText = new BitmapFont();
        consecutive = 0;
        consecutiveText = new BitmapFont();

        //puts items in the item dict with icons
        String itemsString = itemListHandle.readString();
        String[] itemStrings = itemsString.split("\n");
        itemsDict = new HashMap<String, Item>();
        for(int i = 0; i < itemStrings.length; i++) {
           itemsDict.put(itemStrings[i], new Item(itemStrings[i], itemSprites[i]));
        }

        //gives all items their build components
        String recipesString = itemAssemblyHandle.readString();
        String[] recipeStrings = recipesString.split("\n");
        for(String recipeString: recipeStrings) {
            String item = recipeString.substring(0, recipeString.indexOf(":"));
            String[] componentStrings = recipeString.substring(recipeString.indexOf(":")+1).split(",");
            ArrayList<Item> components = new ArrayList<Item>();
            for (String componentString: componentStrings) {
                components.add(itemsDict.get(componentString));
            }
            itemsDict.get(item).setComponents(components);
        }

        //gives all items their descriptions
        String descriptionsString = itemDescriptionHandle.readString();
        String[] descriptionStrings = descriptionsString.split("///\n");
        for (String descriptionString: descriptionStrings) {
            String item = descriptionString.substring(0, descriptionString.indexOf("\n"));
            String description = descriptionString.substring(descriptionString.indexOf("\n")+1);
            itemsDict.get(item).setDescription(description);
        }

        //System.out.println(itemsDict.get("Magic Stick"));
    }

    public void readFilesWindows() {
        for(int i=0;i<65;i++){ //imports the basic items
            String name = "Item-Images\\Basic-Items\\BI";
            if(i<10)
                name = name+"0"+i+".png";
            else
                name = name+i+".png";
            itemTextures[i] = new Texture(Gdx.files.internal(name));
            itemSprites[i] = new Sprite(itemTextures[i]);
        }
        for(int i=0;i<82;i++){ //imports the complex items
            String name = "Item-Images\\Complex-Items\\CI";
            int index = i+65;
            if(i<9)
                name = name+"0"+(i+1)+".png";
            else
                name = name+(i+1)+".png";
            itemTextures[index] = new Texture(Gdx.files.internal(name));
            itemSprites[index] = new Sprite(itemTextures[index]);
        }
        itemListHandle = Gdx.files.internal("Spreadsheets\\ItemList.txt");
        itemAssemblyHandle = Gdx.files.internal("Spreadsheets\\ItemAssembly.txt");
        itemDescriptionHandle = Gdx.files.internal("Spreadsheets\\ItemDescription.txt");
    }

    public void readFilesOSX() {
        for(int i=0;i<65;i++){ //imports the basic items
            String name = "core/assets/Item-Images/Basic-Items/BI";
            if(i<10)
                name = name+"0"+i+".png";
            else
                name = name+i+".png";
            itemTextures[i] = new Texture(Gdx.files.internal(name));
            itemSprites[i] = new Sprite(itemTextures[i]);
        }
        for(int i=0;i<82;i++){ //imports the complex items
            String name = "core/assets/Item-Images/Complex-Items/CI";
            int index = i+65;
            if(i<9)
                name = name+"0"+(i+1)+".png";
            else
                name = name+(i+1)+".png";
            itemTextures[index] = new Texture(Gdx.files.internal(name));
            itemSprites[index] = new Sprite(itemTextures[index]);
        }
        itemListHandle = Gdx.files.internal("core/assets/Spreadsheets/ItemList.txt");
        itemAssemblyHandle = Gdx.files.internal("core/assets/Spreadsheets/ItemAssembly.txt");
        itemDescriptionHandle = Gdx.files.internal("core/assets/Spreadsheets/ItemDescription.txt");
    }

    @Override
    public void dispose() { //disposes of all the assets
        for(Texture itemTexture: itemTextures) {
            itemTexture.dispose();
        }
        batch.dispose();
        shapeRenderer.dispose();
        guessesLeftText.dispose();
        scoreText.dispose();
        consecutiveText.dispose();
    }

    //continuously called during runtime
    @Override
    public void render() {

        getInput();
        update();

        Gdx.gl.glClearColor(0,0,0,1); //clears the background and set it black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();//draw stuff after here
        guessesLeftText.draw(batch, "Guesses Left: " + guessesLeft, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 - 10);
        scoreText.draw(batch, "Score: " + score, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 - 30);
        consecutiveText.draw(batch, consecutive +" in a row", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 - 50);
        itemSprites[itemSprites.length - 1].draw(batch);
        //Item item = itemsDict.get("Blink Dagger");
        //item.getIcon().draw(batch);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        //shapeRenderer.rect();
        shapeRenderer.end();
    }

    /**
     * Checks for Input and updates anything accordingly
     */
    public void getInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            itemsDict.get("Blink Dagger").getIcon().setPosition(itemsDict.get("Blink Dagger").getIcon().getX() + 5, itemsDict.get("Blink Dagger").getIcon().getY());
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            itemsDict.get("Blink Dagger").getIcon().setPosition(0, 0);
            guessesLeft--;
        }
    }

    /**
     * Method to update the state of the game but doesn't actually "render anything to the screen"
     */
    public void update() {
        score = (int) itemSprites[0].getX();
    }
}
