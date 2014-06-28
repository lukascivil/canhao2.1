package com.mygdx.game;
//Lucas Cordeiro da Silva
//Uff-Puro
//Prof. Lauro Eduardo Kozovits 
//Matt buckland game ai


import com.badlogic.gdx.Application.ApplicationType;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.assets.AssetManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;

import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;

import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;

import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.Input.Keys;
import com.mygdx.game.Constants;

import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;


//game logic
public class WorldController 
{	
	//private static final String TAG = WorldController.class.getName();
   //private static final int SPACE = 0;
	//public static Model model;
	//public static ModelInstance instance;
	//teste git linux

	public Model modelcanhao;
	public ModelInstance instancecanhao;
	public Model modelshot;
	//public ModelInstance instanceshot;
	public Model modelground;
	public ModelInstance instanceground;
	MyInputProcessor processor;
	Model ship; 
	
	
	//public ArrayList<ModelInstance>instancesshot = new ArrayList<ModelInstance>();
	ArrayMap<String,GameObject> instances;
	ArrayMap<String,GameObject> instanceshots;
	//instancesshot=canhao1.getcanhaoShot;
	Model model;
	
	Array<GameObject> objects = new Array<GameObject>();
	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	MyContactListener contactListener;
	btBroadphaseInterface broadphase;
	btCollisionWorld collisionWorld;
	MyInputProcessor input;
	
	public boolean loading;
	
	Quaternion rotation2;
	Quaternion rotation;
	 Vector3 scalecanhao;
	 Vector3  scalealvo;
	Vector3 teste;
	float x;
	float y;
	float z;
	Vector3 realPositionAlvo;
	Vector3 realPosition;
	int i=0;
	int hash;
	int  cont=0;
	ArrayMap<String, GameObject.Constructor> constructors;
	public AssetManager assets;
	//-------------------------------------------------------------------------------------------------------------------
	 float spawnTimer;
    
	 final static short GROUND_FLAG = 1<<8;
	 final static short OBJECT_FLAG = 1<<9;
	 final static short ALL_FLAG = -1;
	 String a,b;
	
	class MyContactListener extends ContactListener 
	{
		@Override
		public boolean onContactAdded (int userValue0, int partId0, int index0, int userValue1, int partId1, int index1)
		{
			
				
				a=instances.getKeyAt(userValue0);
				b=instances.getKeyAt(userValue1);
				System.out.println("aaaaaaaaaaaaaaa"+userValue0);
				System.out.println("bbbbbbbbbbbbbbb"+userValue1);
				//instances.get(userValue0).moving = false;    //quem leva
				//instances.get(userValue1).moving = false;
			    
				//--------------------------------------------------------------
				
				instances.get(a).moving = false;    //quem leva
				instances.get(b).moving = false;
				//--------------------------------------------------------------
				  //if (userValue1 == 0)
			      //      instances.get(a).moving = false;
			      //  else if (userValue0 == 0)
			      //      instances.get(b).moving = false;;
				return true;
		}
	}

	static class GameObject extends ModelInstance implements Disposable 
	{
		public final btCollisionObject body;
		public boolean moving;

		public GameObject (Model model, String node, btCollisionShape shape)
		{
			super(model, node);
			body = new btCollisionObject();
			body.setCollisionShape(shape);
		}

		@Override
		public void dispose () 
		{
			body.dispose();
		}

		static class Constructor implements Disposable 
		{
			public final Model model;
			public final String node;
			public final btCollisionShape shape;

			public Constructor (Model model, String node, btCollisionShape shape)
			{
				this.model = model;
				this.node = node;
				this.shape = shape;
			}

			public GameObject construct () 
			{
				return new GameObject(model, node, shape);
			}

			@Override
			public void dispose ()
			{
				shape.dispose();
			}
         }
	 }
	
	
	//-----------------------------------------------------------------------------------------------------------------------
	//public Canhao canhao1;
	//public ArrayList<Shot> shot2;  //Bullets ArrayList
	
	
	//constructor
	public WorldController()
	{
		
		init();	
		
	}
	//start the WorldController, good practice to not initialize the class with the Constructor(libgdx order)
	//iff we need to reset an object in the game. With this method we save performance
	private void init()
	{
		initTestObjects();
	}
	//game logic, it will be called secveral ties per second.it can aply updates 
	//to the game world according to the fraction of time that has passed since the last rendered time.
	private void initTestObjects() //1vez
	{
		Bullet.init();
		
        
		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		
		mb.node().id = "canhao";
		mb.part("canhao", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLUE))).box(5f, 5f, 5f);

		mb.node().id = "ground";
		mb.part("ground", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.PINK))).box(100f, 1f, 100f);
		
		mb.node().id = "shot";
		mb.part("shot", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN))).sphere(4f, 4f, 4f, 50, 50);	
	    
		//mb.node().id = "shot2";
		//mb.part("shot", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN))).sphere(6f, 6f, 6f, 50, 50);	
	    
		mb.node().id = "alvo";
		mb.part("alvo", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED))).box(30f, 30f, 1f);
		
		mb.node().id = "gol";
		mb.part("gol", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.WHITE))).box(100f, 10f, 1f);
		
		model = mb.end();
	
		
		
		constructors = new ArrayMap<String, GameObject.Constructor>(String.class, GameObject.Constructor.class);
		constructors.put("canhao", new GameObject.Constructor(model, "canhao", new btBoxShape(new Vector3(2.5f, 2.5f, 2.5f))));
		constructors.put("ground", new GameObject.Constructor(model, "ground", new btBoxShape(new Vector3(50f, 0.5f, 50f))));
		constructors.put("shot", new GameObject.Constructor(model, "shot", new btSphereShape(2f)));	
		//constructors.put("shot2", new GameObject.Constructor(model, "shot2", new btSphereShape(3f)));	
		//constructors.put("sphere", new GameObject.Constructor(model, "sphere", new btSphereShape(3f)));
		constructors.put("alvo", new GameObject.Constructor(model, "alvo", new btBoxShape(new Vector3(15f, 15f, 0.5f))));
		constructors.put("gol", new GameObject.Constructor(model, "gol", new btBoxShape(new Vector3(50f, 5f, 0.5f))));
		
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
		contactListener = new MyContactListener();
		
		//instanceshots = new ArrayMap<String,GameObject>();
		instances = new ArrayMap<String,GameObject>();
		
		//GameObject object = constructors.get("ground").construct();
		//instances.add(object);
		///collisionWorld.addCollisionObject(object.body);
		
		//GameObject object2 = constructors.get("alvo").construct();
		//object2.transform.translate(0, 0, -10);
		//instances.add(object2);
		//collisionWorld.addCollisionObject(object2.body);
		
		
		//funciona pq � depois da instancia criada
		
		alvo();
		canhaocreate();
		groundcreate();
		gol();
		//-------------------------------------------------------------------------
		 //input = new MyInputProcessor();
		
		//------------------------------------------------------------------------

		
	}
	public void update() //loop
	{
		
		final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());
		//realPosition=instances.get(1).transform.getTranslation(scale.X);
		
		realPosition=instances.get("canhao").transform.getTranslation(new Vector3());
		realPositionAlvo=instances.get("alvo").transform.getTranslation(new Vector3());
		
		
		//change the variable z to set the shot in front of the canhao
		//realPosition.z = realPosition.z-6;
		Constants.lifeTimeShot1+=0.1;
		Constants.lifeTimeShot2+=0.1;
		Constants.lifeTimeShot3+=0.1;
		Constants.lifeTimeShot4+=0.1;
		//System.out.println(Constants.lifeTimeShot);

//----------------------------------------------------------------------
		
	
		for (GameObject objshot : instances.values())
		{		
			
				objshot.body.setWorldTransform(objshot.transform);
		
				if(objshot.getNode("gol")!=null)
				{
					//n�o bateu
					if(objshot.moving) 
					{
						
					}
					//bateu
					else
					{
						//System.out.println("1111111111111111111111111111111");
						Constants.contGolInt++;
						Constants.contGolString = String.valueOf(Constants.contGolInt);
						Constants.contGolCharSequence = new String(Constants.contGolString); //String.valueOf(Constants.contGolInt);
						//volta ao estado "n�o � mais batido"
						objshot.moving=true;
					}
				}
				
				if(objshot.getNode("shot")!=null)
				{		
							
						
								//NAO BATEU
								if (objshot.moving) 
								{						

									objshot.transform.translate(0,0,-1);

								}							
								//BATEU
								else
								{
									if(Constants.flagshot4==true)
									{
										Constants.flagshot4life=true;
									}
																					
									objshot.transform.trn(0f, 0, 1);				
									
								}						
						
			    }		
				
		}
		for (GameObject objalvo : instances.values())
		{		
			
				objalvo.body.setWorldTransform(objalvo.transform);
							
				if(objalvo.getNode("alvo")!=null)
				{
					//objshot.transform.translate(1,0,0);
						//if(realPositionAlvo.x<0||realPositionAlvo.x==25)
					//	{						
							
							if(Constants.direita==false)
							{
								objalvo.transform.translate(-1,0,0);
								if(realPositionAlvo.x==-25)
								{
									Constants.direita=true;
								}
							}
					//	}
						if(Constants.direita==true)
						{
							objalvo.transform.translate(1,0,0);
							if(realPositionAlvo.x==25)
							{
								System.out.println("bate na direita");
								Constants.direita=false;
							}
						}
				}
		}
		
		for (GameObject objshot : instances.values())
		{	
			objshot.body.setWorldTransform(objshot.transform);
				
		}

//----------------------------------------------------------------------		
		//"colis�o ativa"
			collisionWorld.performDiscreteCollisionDetection();
		
		//System.out.println(collisionWorld.getNumCollisionObjects());
		
		
		//shotRemove();
		rotation=instances.get("canhao").transform.getRotation(new Quaternion());
		//System.out.println("(x): "+rotation.x+" (y): "+rotation.y+" (z): "+rotation.z+" (w): "+rotation.w);
		
		handleDebugInput();
		updateTestObjects(Constants.deltaTime);
		//Gdx.input.setInputProcessor(this);
	}
	private void updateTestObjects(float deltaTime)
	{
		
		//if i want to rotate or etc
	}
	private void handleDebugInput()
	{
		//processor = new MyInputProcessor();	         //I DONT KNOW HOW TO INSERT MORE THAN 2 SETINPUTPROCESSOR
		//Gdx.input.setInputProcessor(processor);      //I DONT KNOW HOW TO INSERT MORE THAN 2 SETINPUTPROCESSOR
		//--------------------------------------------------------------------
		//CameraInputController processortest = new CameraInputController();
		//Gdx.input.setInputProcessor(processortest);
		//--------------------------------------------------------------------
		
		
		//float deltaTime=0.5f;
		if(Gdx.app.getType()==ApplicationType.Desktop)
		{
			 		
			float modMoveSpeed = 2*Constants.deltaTime;
			if(Gdx.input.isKeyPressed(Keys.A))
			{
				//instances.get(1).transform.translate(-modMoveSpeed, 0, 0);	
				instances.get("canhao").transform.translate(-modMoveSpeed, 0, 0);
				//instances.get("canhao").transform.rotate(0, 1, 0, 2);
			}
			if(Gdx.input.isKeyPressed(Keys.D))
			{
				//instances.get(1).transform.translate(modMoveSpeed, 0, 0);
				instances.get("canhao").transform.translate(modMoveSpeed, 0, 0);
				//instances.get("canhao").transform.rotate(0, -1, 0, 2);
				
			}
			if(Gdx.input.isKeyPressed(Keys.W))
			{
				//instances.get(1).transform.translate(0, 0,-modMoveSpeed);
				instances.get("canhao").transform.translate(0, 0, -modMoveSpeed);
			}
			if(Gdx.input.isKeyPressed(Keys.S))
			{
				//instances.get(1).transform.translate(0, 0,modMoveSpeed);
				instances.get("canhao").transform.translate(0, 0, modMoveSpeed);
			}
			
		
			if(Gdx.input.isKeyPressed(Keys.SPACE))
			{
				if(Constants.keyon==true)
				{
					//System.out.println("space apertado");
					shot();
					
				}
				Constants.keyon=false;
			}
		}
		if(Gdx.app.getType()==ApplicationType.Android)
		{
			
		}
	}
	
	public void alvo () 
	{
	
		Constants.contObjectsInt++;
		Constants.contObjectsString = String.valueOf(Constants.contObjectsInt);
		Constants.flagalvo=true;
		GameObject obj = constructors.get("alvo").construct();
		obj.moving = false;
		//obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));
		//obj.transform.trn(MathUtils.random(-2.5f, 2.5f), 9f, MathUtils.random(-2.5f, 2.5f));
		obj.transform.translate(-1,18,-35);
		obj.body.setWorldTransform(obj.transform);
		obj.body.setUserValue(instances.size);
		obj.body.setCollisionFlags(obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		instances.put("alvo", obj);
		collisionWorld.addCollisionObject(obj.body, GROUND_FLAG, ALL_FLAG);
		
	}
	public void gol() 
	{
	
		Constants.flaggol=true;
		Constants.contGolInt=0;
		Constants.contObjectsInt++;
		Constants.contObjectsString = String.valueOf(Constants.contObjectsInt);
		GameObject obj = constructors.get("gol").construct();
		obj.moving = true;
		//obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));
		//obj.transform.trn(MathUtils.random(-2.5f, 2.5f), 9f, MathUtils.random(-2.5f, 2.5f));
		obj.transform.translate(0,7,-38);
		obj.body.setWorldTransform(obj.transform);
		obj.body.setUserValue(instances.size);
		obj.body.setCollisionFlags(obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		instances.put("gol", obj);
		collisionWorld.addCollisionObject(obj.body, GROUND_FLAG, ALL_FLAG);
	
	}
	public void shot()
	{	
		if(Constants.flagshot1==false)
		{
			System.out.println("cria 1");
			Constants.contShotInt=1;
			//Constants.flagshot1=true;
			//Constants.lifeTimeShot1=0;
		}
		else
		{
			if(Constants.flagshot2==false)
			{
				System.out.println("cria 2");
				Constants.contShotInt=2;
				//Constants.flagshot2=true;
				//Constants.lifeTimeShot2=0;
				
			}
			else
			{
				if(Constants.flagshot3==false)
				{
					System.out.println("cria 3");
					Constants.contShotInt=3;
					//Constants.flagshot3=true;
					//Constants.lifeTimeShot3=0;
				}
				else
				{
					if(Constants.flagshot4==false)
					{
						System.out.println("cria 4");
						Constants.contShotInt=4;
						//Constants.flagshot4=true;
						//Constants.lifeTimeShot4=0;
					}
					else
					{
						//cut the bug, loop in 4
						Constants.contShotInt=-1;
					}
				}
				
			}
		}
		//numero max de tiros = 4;
		if(Constants.contShotInt<=4 && Constants.contShotInt>0) //0 Exception , loop in 4
		{
			//Constants.contShotInt = Constants.contShotInt + 1;
			Constants.contShotString = String.valueOf(Constants.contShotInt);
			System.out.println("-------------------------------------------");
			System.out.println("contShotInt: "+Constants.contShotInt);
			System.out.println("contShotString: "+Constants.contShotString);
			System.out.println("-------------------------------------------");
			
			GameObject obj = constructors.get("shot").construct();
			obj.moving = true;
			//obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));;
			//obj.transform.setToRotation(0, 0, 0, 0);
			obj.transform.translate(realPosition.x,realPosition.y,realPosition.z);	
			
			obj.transform.rotate(rotation);
			obj.body.setWorldTransform(obj.transform);
			obj.body.setUserValue(instances.size);
			obj.body.setCollisionFlags(obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
			
					
			instances.put(Constants.contShotString, obj);
			collisionWorld.addCollisionObject(obj.body, OBJECT_FLAG, GROUND_FLAG);
			//objects.add(objects.get(Constants.contShotInt));
			instances.get("1").transform.rotate(0, rotation.y, 0, rotation.w);
			if(Constants.contShotString.equals("1"))
			{
				Constants.flagshot1=true;
				Constants.lifeTimeShot1=0;
			}
			if(Constants.contShotString.equals("2"))
			{
				Constants.flagshot2=true;
				Constants.lifeTimeShot2=0;;
			}
			if(Constants.contShotString.equals("3"))
			{
				Constants.flagshot3=true;
				Constants.lifeTimeShot3=0;
			}
			if(Constants.contShotString.equals("4"))
			{
				Constants.flagshot4=true;
				Constants.lifeTimeShot4=0;
			}
			
		}
	}
	
	
	public void canhaocreate()
	{
		Constants.contObjectsInt++;
		Constants.contObjectsString = String.valueOf(Constants.contObjectsInt);
		
		GameObject obj = constructors.get("canhao").construct();
		obj.moving = false;
		//obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));;
		obj.transform.translate(0,4,30);
		obj.body.setWorldTransform(obj.transform);
		obj.body.setUserValue(instances.size);
		obj.body.setCollisionFlags(obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		instances.put("canhao", obj);
		//collisionWorld.addCollisionObject(obj.body, OBJECT_FLAG, GROUND_FLAG);
	

	}
	public void groundcreate()
	{
		Constants.contObjectsInt++;
		Constants.contObjectsString = String.valueOf(Constants.contObjectsInt);
		
		GameObject obj = constructors.get("ground").construct();
		obj.moving = false;
		//obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));;
		obj.transform.translate(0,0,0);
		obj.body.setWorldTransform(obj.transform);
		obj.body.setUserValue(instances.size);
		obj.body.setCollisionFlags(obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		instances.put("ground", obj);
		collisionWorld.addCollisionObject(obj.body, OBJECT_FLAG, GROUND_FLAG);
	

	}
	public void shotRemove()
	{
		
		if(Constants.flagshot1==true)
		{
				//if(instances.get("1")!=null)
				if(Constants.lifeTimeShot1>4f)
				{
					System.out.println("vai remover 1");		
					//collisionWorld.removeCollisionObject(instances.get("1").body);
		
					instances.removeKey("1");
					//System.out.println("fudel aqui");
					//instances.removeValue(instances.get("1"), false);
				
					//collisionWorld.getForceUpdateAllAabbs();
					//instances.removeKey(a);
					//instances.removeIndex(2);
					//Constants.contShotInt=0;	
					cont++;
					
				Constants.flagshot1=false;	
				//Constants.lifeTimeShot1=0;			
				}
		}
		
		if(Constants.flagshot2==true)
		{
				//if(instances.get("2")!=null)
				if(Constants.lifeTimeShot2>10f)
				{
					System.out.println("vai remover 2");
				
					//collisionWorld.removeCollisionObject(instances.get("2").body);
					
					//instances.values().
					instances.removeKey("2");
					//instances.removeValue(instances.get("2"), false);
					
	
					
					//collisionWorld.getForceUpdateAllAabbs();
					//instances.removeKey(a);
					//instances.removeIndex(3);
					//Constants.contShotInt=0;
					cont++;
				Constants.flagshot2=false;	
				//Constants.lifeTimeShot2=0;
				}
		}
		
		if(Constants.flagshot3==true)	
		{		
				//if(instances.get("3")!=null)
				if(Constants.lifeTimeShot3>2f)
				{
					System.out.println("vai remover 3");
					//collisionWorld.removeCollisionObject(instances.get("3").body);
					instances.removeKey("3");
					//instances.removeValue(instances.get("3"), false);
					//collisionWorld.updateAabbs();
					//instances.removeKey(a);
					//instances.removeIndex(4);
					//Constants.contShotInt=1;
					cont++;
					
				Constants.flagshot3=false;	
				//Constants.lifeTimeShot3=0;
				}
		}
	   
	    if(Constants.flagshot4==true)	   
		{		
				//if(instances.get("4")!=null)
			   if(Constants.lifeTimeShot4>2f)
				{
					System.out.println("vai remover 4");
					//collisionWorld.removeCollisionObject(instances.get("4").body);
					instances.removeKey("4");
					//instances.removeValue(instances.get("4"), false);
					//instances.removeKey(a);
					//instances.removeIndex(5);
					//Constants.contShotInt=1;
					cont++;
					
				Constants.flagshot4=false;	
				//Constants.lifeTimeShot4=0;
				}
		}
	    
	   
	}
	public void dispose()
	{
		for (GameObject obj : instances.values())
		obj.dispose();
		instances.clear();

		for (GameObject.Constructor ctor : constructors.values())
			ctor.dispose();
		constructors.clear();

		collisionWorld.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();

		contactListener.dispose();

			
	}
	
}
