package com.shop.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
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
    private FileHandle musicHandle;

    //general
    private Item mainItem;
    private ArrayList<Item> componentItems;
    private ArrayList<Item> choiceItems;
    private ArrayList<Item> selectedItems;
    private ArrayList<String> itemsNameArray = new ArrayList<String>();

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
    private SpriteBatch batch;
    private Texture[] itemTextures;
    private Sprite[] itemSprites;
    private Sprite backgroundSprite;
    private ShapeRenderer shapeRenderer;
    private Rectangle mainItemBox;
    private ArrayList<Rectangle> componentBoxes;
    private Rectangle[] choiceBoxes;
    private Rectangle recipeBox;
    private Rectangle[] selectedBoxes;
    public static final float RECTANGLE_WIDTH = 60;
    public static final float RECTANGLE_HEIGHT = 45;

    //audio
    private Music backgroundMusic;

    @Override
    public void create() {
        Gdx.graphics.setDisplayMode(1000, 600, false);
        //81 complex items and 65 basic items
        //basic items are 0-64 and complex items are 65-146
        batch = new SpriteBatch();//contains all sprites to be drawn to the screen
        itemTextures = new Texture[147];
        itemSprites = new Sprite[147];
        shapeRenderer = new ShapeRenderer();
        Gdx.graphics.setTitle("The Shopkeeper's Quiz");

        readFilesWindows();
        //readFilesOSX();

        createItemDict();

        mainItem = itemsDict.get(itemsNameArray.get((int)(Math.random()*81)+65));
        componentItems = new ArrayList<Item>(5);
        for (Item component: mainItem.getComponents()) {
            componentItems.add(component);
        }
        choiceItems = new ArrayList<Item>();
        choiceItems.addAll(mainItem.getComponents());
        if(choiceItems.indexOf(itemsDict.get("Recipe")) != -1){
            choiceItems.remove(itemsDict.get("Recipe"));
        }
        for(int i=0;choiceItems.size()<8;i++){
            choiceItems.add(itemsDict.get(itemsNameArray.get((int)(Math.random()*147))));
        }

        //graphics
        guessesLeft = 3;
        guessesLeftText = new BitmapFont();
        score = 0;
        scoreText = new BitmapFont();
        consecutive = 0;
        consecutiveText = new BitmapFont();
        mainItemBox = new Rectangle(Gdx.graphics.getWidth()/2 - RECTANGLE_WIDTH/2, Gdx.graphics.getHeight()/2 + 100, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        choiceBoxes = new Rectangle[8];
        for (int i = 0; i < choiceBoxes.length; i++) {
            choiceBoxes[i] = new Rectangle(Gdx.graphics.getWidth()/2 - 310 + i*70, Gdx.graphics.getHeight()/2 - 70, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        }
        recipeBox = new Rectangle(Gdx.graphics.getWidth()/2 + 250, Gdx.graphics.getHeight()/2 - 70, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);

        selectedBoxes = new Rectangle[mainItem.getComponents().size()];
        drawSelectedBoxes();

        //Music
        try {
            backgroundMusic = Gdx.audio.newMusic(musicHandle);
            //backgroundMusic.play();
            backgroundMusic.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            itemSprites[i].setSize(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
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
            itemSprites[index].setSize(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        }
        backgroundSprite = new Sprite(new Texture(Gdx.files.internal("shopGameBackground.png")));
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        itemListHandle = Gdx.files.internal("Spreadsheets\\ItemList.txt");
        itemAssemblyHandle = Gdx.files.internal("Spreadsheets\\ItemAssembly.txt");
        itemDescriptionHandle = Gdx.files.internal("Spreadsheets\\ItemDescription.txt");
        musicHandle = Gdx.files.internal("Music/Dota_2_-_Reborn.mp3");
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
            itemSprites[i].setSize(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
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
            itemSprites[index].setSize(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        }
        backgroundSprite = new Sprite(new Texture(Gdx.files.internal("core/assets/shopGameBackground.png")));
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        itemListHandle = Gdx.files.internal("core/assets/Spreadsheets/ItemList.txt");
        itemAssemblyHandle = Gdx.files.internal("core/assets/Spreadsheets/ItemAssembly.txt");
        itemDescriptionHandle = Gdx.files.internal("core/assets/Spreadsheets/ItemDescription.txt");
        musicHandle = Gdx.files.internal("core/assets/Music/Dota_2_-_Reborn.mp3");
    }

    public void createItemDict() {
        //puts items in the item dict with icons
        String itemsString = itemListHandle.readString();
        String[] itemStrings = itemsString.split("\r\n");
        itemsDict = new HashMap<String, Item>();
        for(int i = 0; i < itemStrings.length; i++) {
            itemsDict.put(itemStrings[i], new Item(itemStrings[i], itemSprites[i]));
            itemsNameArray.add(itemStrings[i]);
        }

        //gives all items their build components
        String recipesString = itemAssemblyHandle.readString();
        String[] recipeStrings = recipesString.split("\r\n");
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
        String[] descriptionStrings = descriptionsString.split("///\r\n");
        for (String descriptionString: descriptionStrings) {
            String item = descriptionString.substring(0, descriptionString.indexOf("\r\n"));
            String description = descriptionString.substring(descriptionString.indexOf("\r\n")+1);
            itemsDict.get(item).setDescription(description);
        }
    }

    private void drawSelectedBoxes() {
        if(selectedBoxes.length == 1){
            selectedBoxes[0] = new Rectangle(Gdx.graphics.getWidth()/2 - RECTANGLE_WIDTH/2, Gdx.graphics.getHeight()/2 + 15, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        }else if(selectedBoxes.length == 2){
            for(int i=0;i<2;i++){
                selectedBoxes[i] = new Rectangle(Gdx.graphics.getWidth()/2 - 65 + i*70, Gdx.graphics.getHeight()/2 + 15, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
            }
        }else if(selectedBoxes.length == 3){
            for(int i=0;i<3;i++) {
                selectedBoxes[i] = new Rectangle(Gdx.graphics.getWidth()/2 - 100 + i*70, Gdx.graphics.getHeight()/2 + 15, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
            }
        }else if(selectedBoxes.length == 4){
            for(int i=0;i<4;i++) {
                selectedBoxes[i] = new Rectangle(Gdx.graphics.getWidth()/2 - 135 + i*70, Gdx.graphics.getHeight()/2 + 15, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
            }
        }else{
            System.out.println("Aaron can't count and the max number of items in a recipe is "+selectedBoxes.length);
            System.exit(2);
        }
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
        backgroundMusic.dispose();
    }

    //continuously called during runtime
    @Override
    public void render() {

        getInput();
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1); //clears the background and set it black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();//draw stuff after here
        backgroundSprite.draw(batch);
        mainItem.getIcon().draw(batch);
        mainItem.getIcon().setPosition(mainItemBox.getX(), mainItemBox.getY());
        for(int i=0;i<8;i++){
            choiceItems.get(i).getIcon().draw(batch);
            choiceItems.get(i).getIcon().setPosition(choiceBoxes[i].getX(), choiceBoxes[i].getY());
        }
        itemsDict.get("Recipe").getIcon().draw(batch);
        itemsDict.get("Recipe").getIcon().setPosition(recipeBox.getX(), recipeBox.getY());
        guessesLeftText.draw(batch, "Guesses Left: " + guessesLeft, Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 100);
        scoreText.draw(batch, "Score: " + score, Gdx.graphics.getWidth()/2 - 30, Gdx.graphics.getHeight()/2 - 120);
        consecutiveText.draw(batch, consecutive +" in a row", Gdx.graphics.getWidth()/2 - 35, Gdx.graphics.getHeight()/2 - 140);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(mainItemBox.getX(), mainItemBox.getY(), mainItemBox.getWidth(), mainItemBox.getHeight());
        for (int i = 0; i < choiceBoxes.length; i++) {
            shapeRenderer.rect(choiceBoxes[i].getX(), choiceBoxes[i].getY(), choiceBoxes[i].getWidth(), choiceBoxes[i].getHeight());
        }
        shapeRenderer.rect(recipeBox.getX(), recipeBox.getY(), recipeBox.getWidth(), recipeBox.getHeight());
        for(int i=0;i<selectedBoxes.length;i++) {
            shapeRenderer.rect(selectedBoxes[i].getX(), selectedBoxes[i].getY(), selectedBoxes[i].getWidth(), selectedBoxes[i].getHeight());
        }
        shapeRenderer.end();
    }

    /**
     * Checks for Input and updates anything accordingly
     */
    public void getInput() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Item itemSelected = null;
            for(int i=0;i<8;i++){
                if(choiceBoxes[i].contains(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY())){
                    itemSelected = choiceItems.get(i);
                }
            }
            if(recipeBox.contains(Gdx.input.getX(), Gdx.input.getY())){
                itemSelected = itemsDict.get("Recipe");
            }
            if(itemSelected != null){
                guessesLeft--;
            }
            /**itemsDict.get("Blink Dagger").getIcon().setPosition(0, 0);
            guessesLeft--;*/
        }
    }

    /**
     * Method to update the state of the game but doesn't actually "render anything to the screen"
     */
    public void update() {

        score = (int) itemSprites[0].getX();
    }
}
