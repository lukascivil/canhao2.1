package com.mygdx.game;
//Lucas Cordeiro da Silva
//Uff-Puro
//Prof. Lauro Eduardo Kozovits 

import com.badlogic.gdx.utils.Array;

public class Constants 
{
	//visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 5.0f;
	//visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT= 5.0f;
	//deltaTime.
	public static final float deltaTime=0.2f;
	//GameLauncher
	public static String gameLauncher;
	//key
	public static  boolean keyon;
	//---------------------------------------------------
						//CONTS
	//cont shot int (size)
	public static int  contShotInt=0;
	//cont shot String
	public static String contShotString;
	//cont alvo  int (size)
	public static int contAlvoInt=0;
	//cont canhao  int (size)
	public static int contCanhaoInt=0;
	//cont Alvo + canhao int (size)
	public static int contObjectsInt=-1;     //pois meu vetor come�a do 0;
	//cont Alvo + canhao String (size)
	public static String contObjectsString;
	//cont Gol
	public static int contGolInt=0;
	public static String contGolString;
	public static CharSequence contGolCharSequence="0";
	//FPS
	public static int fpsInt=0;
	public static String fpsString;
	public static CharSequence fpsCharSequence;
	//---------------------------------------------------
	//shot lifetime
	public static float lifeTimeShot1=0;
	public static float lifeTimeShot2=0;
	public static float lifeTimeShot3=0;
	public static float lifeTimeShot4=0;
	//shot flag
	public static boolean flagshot1=false;    // true = criada  , false = nao criada
	public static boolean flagshot2=false;
	public static boolean flagshot3=false;
	public static boolean flagshot4=false;
	public static boolean flagalvo=false;
	public static boolean flaggol=false;
	public static boolean flagshot4life=false;
	//---------------------------------------------------
	//pode mexer o alvo?
	public static boolean direita=false;
}
