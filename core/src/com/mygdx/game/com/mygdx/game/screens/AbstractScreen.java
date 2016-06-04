package com.mygdx.game.com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.IcyTower;

/**
 * Created by pawel_000 on 2016-05-24.
 */
public abstract class AbstractScreen implements Screen{
    protected IcyTower game;

    protected Stage stage;
    public OrthographicCamera camera;
    public OrthographicCamera cameraScore;

    public SpriteBatch batch;

    public AbstractScreen(IcyTower game){
        this.game = game;
        createCamera();
        stage = new Stage(new StretchViewport(IcyTower.SCREEN_WIDTH, IcyTower.SCREEN_HEIGHT, camera));
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
    }

    private void createCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, IcyTower.SCREEN_WIDTH, IcyTower.SCREEN_HEIGHT);
        camera.update();

        cameraScore = new OrthographicCamera();
        cameraScore.setToOrtho(false, IcyTower.SCREEN_WIDTH, IcyTower.SCREEN_HEIGHT);
        cameraScore.update();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        clearScreen();
        camera.update();
        cameraScore.update();
        batch.setProjectionMatrix(camera.combined);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.6f, 0.8f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resume() {
        game.setPause(false);
    }

    @Override
    public void pause() {
        game.setPause(true);
    }

    @Override
    public void dispose() {
        game.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {

    }
}
