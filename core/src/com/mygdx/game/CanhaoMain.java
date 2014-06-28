package com.mygdx.game;
//Lucas Cordeiro da Silva
//Uff-Puro
//Prof. Lauro Eduardo Kozovits 

//import com.badlogic.gdx.ApplicationAdapter;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputProcessor;
//import com.mygdx.game.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btSphereBoxCollisionAlgorithm;
//import com.mygdx.game.Light;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo;
import com.mygdx.game.WorldController.GameObject;

public class CanhaoMain implements ApplicationListener 
{
	//Tag to reference a name
	private static final String TAG = CanhaoMain.class.getName();
	
	//Game Logic
	private WorldController worldController;
	//acces the objects in the abstract class 
	private WorldRenderer worldRenderer;
	//we have to have some artifact to controller certain events on android
	private boolean paused;
	btCollisionWorld collisionWorld;
	 float spawnTimer;
	 MyInputProcessor input;
	
	boolean collision;
	
		@Override
		public void create ()
		{
			
			//if i had levels i have to put here
			//...
			//initialize controller and renderer
			worldController = new WorldController();
			worldRenderer = new WorldRenderer(worldController);
			
			//game world is active on start
			paused = false;
			
		}
	
		@Override
		public void render ()  //LOOP , LOOP , LOOP , LOOP , LOOP, LOOP , LOOP , LOOP, LOOP , LOOP , LOOP, LOOP , LOOP , LOOP
		{
			//dont update the world when paused
			//if (paused=false) = try , if (paused = true) = keep out
			if(!paused)
			{
				//Update game world by the time that has passed
				//since last rendered frame // pass information to the worldcontroler all the time when the game is running
				//worldController.update(Gdx.graphics.getDeltaTime());
			}
			
			worldRenderer.render();
			//O que pode acontecer com os objetos durante o jogo;
			worldController.update();
			

		}
		public void resize(int width, int height)
		{
			worldRenderer.resize(width, height);
		}
		public void dispose()
		{
			//worldController.dispose();
			worldRenderer.dispose();
			//worldRenderer.font.dispose();
			//worldRenderer.batch.dispose();
		}
		public void pause()
		{
			paused = true;
		}
		public void resume()
		{
			paused = false;
		}
}
