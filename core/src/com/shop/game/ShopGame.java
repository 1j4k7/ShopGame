package com.shop.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShopGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture[] itemTextures;
	private Sprite[] itemSprites;

	@Override
	public void create() { //imports all the assets
		//81 complex items and 65 basic items
		//basic items are 0-64 and complex items are 65-145
		batch = new SpriteBatch();
		itemTextures = new Texture[146];
		itemSprites = new Sprite[146];
		for(int i=0;i<65;i++){ //imports the basic items
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
	}

	@Override
	public void dispose() { //disposes of all the assets
		for(int i=0;i<itemTextures.length;i++){
			itemTextures[i].dispose();
		}
		batch.dispose();
	}

	@Override
	public void render() { //renders the screen
		Gdx.gl.glClearColor(0,0,0,1); //clears the background and set it black
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		//draw everything here
		batch.end();
	}
}
