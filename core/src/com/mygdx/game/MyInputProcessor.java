package com.mygdx.game;
//Lucas Cordeiro da Silva
//Uff-Puro
//Prof. Lauro Eduardo Kozovits 

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.input.GestureDetector;

public class MyInputProcessor extends GestureDetector implements InputProcessor 
{
	public MyInputProcessor(float halfTapSquareSize, float tapCountInterval,float longPressDuration, float maxFlingDelay,GestureListener listener) 
	{
		super(halfTapSquareSize, tapCountInterval, longPressDuration, maxFlingDelay,listener);
		// TODO Auto-generated constructor stub
	}

	boolean keyPressed=false;
	
	@Override
	public boolean keyDown(int keycode) 
	{
		 if (keycode == Input.Keys.SPACE) 
		 {
			 	System.out.println("hfhfhf :)");
	            keyPressed = true;
	     }

	        return false;	
	}

	@Override
	public boolean keyUp(int keycode) 
	{
		if(keycode == Input.Keys.SPACE)
		{
			System.out.println("hfhf,,,,,,,,,,, :)");
			keyPressed = false;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
