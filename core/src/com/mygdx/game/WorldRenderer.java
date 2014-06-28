package com.mygdx.game;
//Lucas Cordeiro da Silva
//Uff-Puro
//Prof. Lauro Eduardo Kozovits 

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.Material;
//2d
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//3d
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
//import com.badlogic.gdx.graphics.g3d.utils.CameraInputController; do not use this. USE MY CameraIputController , <3
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;



public class WorldRenderer implements Disposable 
{
	//about camera
	public static PerspectiveCamera camera;
	public CameraInputController cameraController;
	//Batches Renderable instances
	private ModelBatch modelBatch;
	private WorldController worldController;
	//------------------------------------------------
	boolean collision;
	//------------------------------------------------
	MyInputProcessor cam1;
	PerspectiveCamera camb;
	//-------------------------------------------------------------------------
	//2d text
	private SpriteBatch batch;
    private BitmapFont font=null;
    private BitmapFont font2;
    private BitmapFont fontFps;
    //-------------------------------------------------------------------------
    //Scenne
    public ModelInstance space;
    public AssetManager assets;
    public boolean loading;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
	//---------------------------------------------------------------------------
	//Coordenadas
	public Model axesModel;
	public ModelInstance axesInstance;
	public boolean showAxes = true;
	final float GRID_MIN = -10f;
	final float GRID_MAX = 10f;
	final float GRID_STEP = 1f;
	
	
	
	public WorldRenderer(WorldController worldController)
	{
		
		this.worldController = worldController;
		init();
		
	
	}

	private void init()
	{
		
		modelBatch = new ModelBatch();
		//camera
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(15f, 15f, 40f);
		camera.lookAt(0,0,0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();
		//controller cam
		cameraController = new CameraInputController(camera);
		
		//coodernadas
		createAxes();
		
		
		
		Gdx.input.setInputProcessor(cameraController); // this camera controller is my class not gdx.class ...
		//Gdx.input.getInputProcessor();
		//Gdx.input.setInputProcessor(worldController.processor);
		
		//2d text
		batch = new SpriteBatch();    
        font = new BitmapFont();
        font2 = new BitmapFont();
        fontFps = new BitmapFont();
        font.setColor(Color.RED);
        font2.setColor(Color.GREEN);
        fontFps.setColor(Color.RED);
        
        //3d Scene
        assets = new AssetManager();
        assets.load("invaderscene.g3db", Model.class);
        //assets.load("/C:/Users/lucas/Desktop/Canhao 2.1/android/assets/loadscene/data/invaderscene.g3db", Model.class);
        loading = true;
	}

	
	
	//method that will contain the logic to define in which order game
	//objects are drawn over others
	public void render()
	{
		
		
		 	//getfps
	        Constants.fpsInt = Gdx.graphics.getFramesPerSecond(); 
	        Constants.fpsString = String.valueOf(Constants.fpsInt);
			Constants.fpsCharSequence = new String(Constants.fpsString);
	        
	        
		renderTestObjects();	
		
	}
	//loop
	private void renderTestObjects()
	{
		//load scene
		if (loading && assets.update())
		{
			doneLoading();
		}	
            
         
         
		//a,w,s,d moves the cam.
		//cameraController.update();
	
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        

		
		modelBatch.begin(camera);
        modelBatch.render(worldController.instances.values(),Light.getLight());

        
        //coodernadas z, y, z
        if (showAxes)
        {
        	modelBatch.render(axesInstance);
        }
        //render 2d Scene
        if (space != null)
        {
        	modelBatch.render(space);
        }
        modelBatch.end();
        
        //2d text
        batch.begin();
        font.draw(batch, "Gols: "+Constants.contGolCharSequence, 80, 480);
        fontFps.draw(batch, "Fps: "+Constants.fpsCharSequence, 10, 480);
        if(Constants.contGolInt==4)
        {
        	font2.setScale(7);
        	font2.draw(batch, "VENCEDOR", 20, 200);
        }
        if((Constants.contGolInt<4) && (Constants.contShotInt==4))
        {
        	
        	font2.setScale(7);
        	font2.draw(batch, "GAME OVER", 20, 200);
        	
        }
        batch.end();
        //2d text end
	}
	//utilized to accommodate a new situation(whenever the screen size is changed) 
	//including the event start of the program
	public void resize(int width, int height)
	{
		
	}
	public void dispose()
	{
		//2d text dispose
		 font.dispose();
		 batch.dispose();
		 font2.dispose();
		 fontFps.dispose();
		 
		 //Objects dispose
		 worldController.dispose();
	
		 modelBatch.dispose();
		 worldController.model.dispose();
		 
		 //3d scene dispose
		 instances.clear();
		 assets.dispose();
	}
	private void doneLoading() 
	{
        //Model model = assets.get("/C:/Users/lucas/Desktop/Canhao 2.1/android/assets/loadscene/data/invaderscene.g3db", Model.class);
        Model model = assets.get("invaderscene.g3db", Model.class);
        for (int i = 0; i < model.nodes.size; i++) 
        {
            String id = model.nodes.get(i).id;
            ModelInstance instance = new ModelInstance(model, id);
            Node node = instance.getNode(id);

            instance.transform.set(node.globalTransform);
            node.translation.set(0,0,0);
            node.scale.set(1,1,1);
            node.rotation.idt();
            instance.calculateTransforms();

            if (id.equals("space")) 
            {
                space = instance;
                continue;
            }

            instances.add(instance);

        }
        loading = false;
	
	}
	//Coordenadas
	private void createAxes () 
	{
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_LINES, Usage.Position | Usage.Color, new Material());
		builder.setColor(Color.LIGHT_GRAY);
		for (float t = GRID_MIN; t <= GRID_MAX; t += GRID_STEP) {
			builder.line(t, 0, GRID_MIN, t, 0, GRID_MAX);
			builder.line(GRID_MIN, 0, t, GRID_MAX, 0, t);
		}
		builder = modelBuilder.part("axes", GL20.GL_LINES, Usage.Position | Usage.Color, new Material());
		builder.setColor(Color.RED);
		builder.line(0, 0, 0, 100, 0, 0);
		builder.setColor(Color.GREEN);
		builder.line(0, 0, 0, 0, 100, 0);
		builder.setColor(Color.BLUE);
		builder.line(0, 0, 0, 0, 0, 100);
		axesModel = modelBuilder.end();
		axesInstance = new ModelInstance(axesModel);
	}
	public static PerspectiveCamera getcamera()
	{
		return camera;
	}
}
