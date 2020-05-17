package com.mygdx.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.math.Rect;
import com.mygdx.game.sprite.Background;
import com.mygdx.game.sprite.ButtonExit;
import com.mygdx.game.sprite.ButtonPlay;
import com.mygdx.game.sprite.Star;

public class MenuScreen extends BaseScreen {
    private final Game game;
    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;
    private Star[] stars;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        atlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        buttonExit = new ButtonExit(atlas);
        buttonPlay = new ButtonPlay(atlas, game);
        stars = new Star[256];
        for(int i = 0; i < stars.length; i++){
            stars[i] = new Star(atlas);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
        for(Star star : stars){
            star.resize(worldBounds);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        super.dispose();
    }

    private void update(float delta){
        for (Star star : stars){
            star.update(delta);
        }
    }

    private void draw(){
        batch.begin();
        background.draw(batch);
        for(Star star : stars){
            star.draw(batch);
        }
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        batch.end();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        buttonExit.touchDown(touch, pointer, button);
        buttonPlay.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonExit.touchUp(touch, pointer, button);
        buttonPlay.touchUp(touch, pointer, button);
        return false;
    }
}
