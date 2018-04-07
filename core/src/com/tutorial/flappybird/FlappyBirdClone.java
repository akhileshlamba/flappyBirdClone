package com.tutorial.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBirdClone extends ApplicationAdapter {
	SpriteBatch batch;
	//ShapeRenderer shapeRenderer;
	Circle birdCircle;
	Rectangle[] topPipeRectnagles;
	Rectangle[] bottomPipeRectnagles;

	BitmapFont font;

	Texture background;
	Texture[] birds = new Texture[2];
	Texture topPipe;
	Texture bottomPipe;
	Texture gameOver;

	int birdState = 0;

	int gameState = 0;

	float birdY = 0;

	float speed = 2;
	float velocity = 0;

	float gap = 400;
	float maxOffset = 0;

	int noOfTubes = 4;
	float[] tubeX = new float[noOfTubes];
	float[] offSet = new float[noOfTubes];

	float distanceBetweenTubes;

	int score = 0;
	int scoringTube = 0;

	Random random;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("backgroundg.png");
		gameOver = new Texture("GameOver.png");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10,10);

		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		topPipeRectnagles = new Rectangle[noOfTubes];
		bottomPipeRectnagles = new Rectangle[noOfTubes];

		topPipe = new Texture("toptube.png");
		bottomPipe = new Texture("bottomtube.png");

		maxOffset = Gdx.graphics.getHeight() / 2 - gap /2 - 100;
		random = new Random();

		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
		startGame();


	}

	public void startGame(){

		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		for (int i = 0; i < noOfTubes; i++){

			offSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topPipe.getWidth() / 2 + Gdx.graphics.getWidth() + i* distanceBetweenTubes;
			topPipeRectnagles[i] = new Rectangle();
			bottomPipeRectnagles[i] = new Rectangle();
		}

	}


	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(birdState == 0){
			birdState = 1;
		}
		else
			birdState = 0;


		if (gameState == 1){

			if(Gdx.input.justTouched()){
				velocity = -20;

			}

			if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2){
				score ++;
				Gdx.app.log("Score", String.valueOf(score));

				if(scoringTube < noOfTubes-1)
					scoringTube++;
				else
					scoringTube = 0;

			}


			for (int i = 0; i < noOfTubes; i++) {

				if(tubeX[i] < - topPipe.getWidth()) {
					tubeX[i] = tubeX[i] +  noOfTubes * distanceBetweenTubes;
					offSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				} else {
					tubeX[i] -= 4;
				}
				batch.draw(topPipe, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + offSet[i]);
				batch.draw(bottomPipe, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomPipe.getHeight() + offSet[i]);

				topPipeRectnagles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + offSet[i], topPipe.getWidth(), topPipe.getHeight());
				bottomPipeRectnagles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomPipe.getHeight() + offSet[i], bottomPipe.getWidth(), bottomPipe.getHeight());


			}
			if (birdY > 0){
				velocity = velocity + speed;
				birdY -= velocity;
			} else {
				gameState = 2;
			}

		} else if(gameState == 0){

			if(Gdx.input.justTouched()){
				gameState = 1;
			}

		} else if (gameState == 2){
			batch.draw(gameOver, Gdx.graphics.getWidth() /2 - gameOver.getWidth() /2 , Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);

			if(Gdx.input.justTouched()){

				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
		}


		batch.draw(birds[birdState], Gdx.graphics.getWidth() / 2 - birds[birdState].getWidth() / 2, birdY);

		font.draw(batch, String.valueOf(score), 100, 200);

		batch.end();

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);*/

		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[birdState].getHeight()/2, birds[birdState].getWidth()/2);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < noOfTubes; i++) {
			/*shapeRenderer.rect(topPipeRectnagles[i].x, topPipeRectnagles[i].y, topPipeRectnagles[i].width, topPipeRectnagles[i].height);
			shapeRenderer.rect(bottomPipeRectnagles[i].x, bottomPipeRectnagles[i].y, bottomPipeRectnagles[i].width, bottomPipeRectnagles[i].height);*/

			if(Intersector.overlaps(birdCircle, topPipeRectnagles[i]) || Intersector.overlaps(birdCircle, bottomPipeRectnagles[i])){
				gameState = 2;
			}

		}

		//shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
