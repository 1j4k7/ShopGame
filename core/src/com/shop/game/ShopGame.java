package com.shop.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture[] itemTextures;
    private Sprite[] itemSprites;
    private int guessesLeft;
    private BitmapFont guessesLeftText;
    private int score;
    private BitmapFont scoreText;
    private int consecutive;//number correct in a row
    private BitmapFont consecutiveText;
    private ArrayList<Integer> recipes;
    private HashMap itemsDict;

    @Override
    public void create() { //imports all the assets
        //81 complex items and 65 basic items
        //basic items are 0-64 and complex items are 65-145
        batch = new SpriteBatch();//contains all sprites to be drawn to the screen
        itemTextures = new Texture[146];
        itemSprites = new Sprite[146];
        /**
         * Windows
         */
		/*for(int i=0;i<65;i++){ //imports the basic items
			String name = "Item-Images\\Basic-Items\\BI";
			if(i<10)
				name = name+"0"+i+".png";
			else
				name = name+i+".png";
			itemTextures[i] = new Texture(Gdx.files.internal(name));
			itemSprites[i] = new Sprite(itemTextures[i]);
		}
		for(int i=0;i<81;i++){ //imports the complex items
			String name = "Item-Images\\Complex-Items\\CI";
			int index = i+65;
			if(i<9)
				name = name+"0"+(i+1)+".png";
			else
				name = name+(i+1)+".png";
			itemTextures[index] = new Texture(Gdx.files.internal(name));
			itemSprites[index] = new Sprite(itemTextures[index]);
		}
        FileHandle itemListHandle = Gdx.files.internal("Spreadsheets\\ItemList.txt");
        FileHandle itemAssemblyHandle = Gdx.files.internal("Spreadsheets\\ItemDescription.txt");*/
        /**
         * Windows
         */

        /**
         * WARNING I'M A MAC USER
         */
        for(int i=0;i<65;i++){ //imports the basic items
            String name = "core/assets/Item-Images/Basic-Items/BI";
            if(i<10)
                name = name+"0"+i+".png";
            else
                name = name+i+".png";
            itemTextures[i] = new Texture(Gdx.files.internal(name));
            itemSprites[i] = new Sprite(itemTextures[i]);
        }
        for(int i=0;i<81;i++){ //imports the complex items
            String name = "core/assets/Item-Images/Complex-Items/CI";
            int index = i+65;
            if(i<9)
                name = name+"0"+(i+1)+".png";
            else
                name = name+(i+1)+".png";
            itemTextures[index] = new Texture(Gdx.files.internal(name));
            itemSprites[index] = new Sprite(itemTextures[index]);
        }
        FileHandle itemListHandle = Gdx.files.internal("core/assets/Spreadsheets/ItemList.txt");
        FileHandle itemAssemblyHandle = Gdx.files.internal("core/assets/Spreadsheets/ItemDescription.txt");
        /**
         * WARNING I'M A MAC USER
         */

        guessesLeft = 3;
        guessesLeftText = new BitmapFont();
        score = 0;
        scoreText = new BitmapFont();
        consecutive = 0;
        consecutiveText = new BitmapFont();

        String itemList = itemListHandle.readString();
        String[] itemString = itemList.split("\n");
        itemsDict = new HashMap();
        for(int i=0;i<itemString.length;i++){
            itemsDict.put(itemString[i],i);
        }

        recipes = new ArrayList<Integer>();
        String recipesString = itemAssemblyHandle.readString();
        String[] recipeString = recipesString.split("\n");
        for(int i=0;i<recipeString.length;i++){
            //make an array list of the recipes using the numerical value of the items
        }
    }

    @Override
    public void dispose() { //disposes of all the assets
        for(Texture itemTexture: itemTextures){
            itemTexture.dispose();
        }
        batch.dispose();
        guessesLeftText.dispose();
        scoreText.dispose();
        consecutiveText.dispose();
    }

    //continuously called during runtime)
    @Override
    public void render() {

        getInput();
        update();

        /**
         * Drawing
         */
        Gdx.gl.glClearColor(0,0,0,1); //clears the background and set it black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //draw stuff here
        guessesLeftText.draw(batch, "Guesses Left: " + guessesLeft, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 - 10);
        scoreText.draw(batch, "Score: " + score, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 - 30);
        consecutiveText.draw(batch, consecutive +" in a row", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 - 50);
        itemSprites[0].draw(batch);
        batch.end();
    }

    /**
     * Checks for Input and updates anything accordingly
     */
    public void getInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            itemSprites[0].setPosition(itemSprites[0].getX() + 5, itemSprites[0].getY());
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            itemSprites[0].setPosition(0, 0);
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
