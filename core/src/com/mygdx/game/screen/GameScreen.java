package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.math.Rect;
import com.mygdx.game.sprite.Background;
import com.mygdx.game.sprite.Ship;
import com.mygdx.game.sprite.Star;

public class GameScreen extends BaseScreen {

    private Texture bg;
    private Background background;
    private Star[] stars;
    private TextureAtlas atlas;

    private TextureAtlas textureAtlas;
    private TextureRegion texture;
    private TextureRegion shipParts[][];
    private Ship ship;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);

        textureAtlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        texture = textureAtlas.findRegion("main_ship");
        shipParts = texture.split(texture.getRegionWidth()/2, texture.getRegionHeight());
        ship = new Ship(shipParts[0]);

        atlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        stars = new Star[64];
        for(int i = 0; i < stars.length; i++){
            stars[i] = new Star(atlas);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        ship.resize(worldBounds);
        for(Star star : stars){
            star.resize(worldBounds);
        }
    }

    @Override
    public void dispose() {
        bg.dispose();
        textureAtlas.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        ship.touchDown(touch, pointer, button);
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return super.touchUp(touch, pointer, button);
    }

    private void update(float delta){
        for (Star star : stars){
            star.update(delta);
        }
        ship.update(delta);
    }

    private void draw(){
        batch.begin();
        background.draw(batch);
        for(Star star : stars){
            star.draw(batch);
        }
        ship.draw(batch);
        batch.end();
    }
}
