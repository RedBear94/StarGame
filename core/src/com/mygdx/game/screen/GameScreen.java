package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.EnemyPool;
import com.mygdx.game.pool.ExplosionPool;
import com.mygdx.game.sprite.Background;
import com.mygdx.game.sprite.MainShip;
import com.mygdx.game.sprite.Star;
import com.mygdx.game.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private Texture bg;
    private Background background;
    private Star[] stars;
    private TextureAtlas atlas;

    private TextureAtlas textureAtlas;
    private MainShip mainShip;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private EnemyEmitter enemyEmitter;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);

        atlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        stars = new Star[64];
        for(int i = 0; i < stars.length; i++){
            stars[i] = new Star(atlas);
        }

        textureAtlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(textureAtlas);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds);
        mainShip = new MainShip(textureAtlas, bulletPool, explosionPool);
        enemyEmitter = new EnemyEmitter(textureAtlas, enemyPool);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        free();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for(Star star : stars){
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
        enemyEmitter.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        textureAtlas.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        mainShip.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        mainShip.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        mainShip.touchUp(touch, pointer, button);
        return false;
    }

    private void update(float delta){
        for (Star star : stars){
            star.update(delta);
        }
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        explosionPool.updateActiveSprites(delta);
        mainShip.update(delta);
        enemyEmitter.generate(delta);
    }

    private void free(){
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
    }

    private void draw(){
        batch.begin();
        background.draw(batch);
        for(Star star : stars){
            star.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        mainShip.draw(batch);
        enemyPool.drawActiveSprites(batch);
        explosionPool.drawActiveSprites(batch);
        batch.end();
    }
}
