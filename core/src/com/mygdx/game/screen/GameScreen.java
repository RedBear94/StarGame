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
import com.mygdx.game.sprite.Bullet;
import com.mygdx.game.sprite.Enemy;
import com.mygdx.game.sprite.GameOver;
import com.mygdx.game.sprite.MainShip;
import com.mygdx.game.sprite.ButtonNewGame;
import com.mygdx.game.sprite.Star;
import com.mygdx.game.utils.EnemyEmitter;

import java.util.List;

public class GameScreen extends BaseScreen {
    public enum State {PLAYING, GAME_OVER}
    private State state;

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
    private GameOver gameOver;
    private ButtonNewGame buttonNewGame;

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
        gameOver = new GameOver(textureAtlas);
        state = State.PLAYING;
        buttonNewGame = new ButtonNewGame(textureAtlas, state, mainShip, enemyPool, bulletPool);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollision();
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
        gameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    private void checkCollision(){
        if(state != State.PLAYING){
            return;
        }
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for(Enemy enemy : enemyList){
            float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if(mainShip.pos.dst(enemy.pos) < minDist){
                enemy.destroy();
                mainShip.damage(enemy.getDamage());
                continue;
            }

            for(Bullet bullet : bulletList){
                if(bullet.getOwner() != mainShip || bullet.isDestroyed()){
                    continue;
                }
                if(enemy.isBulletCollision(bullet)){
                    enemy.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }
            if(mainShip.isDestroyed()){
                state = State.GAME_OVER;
            }
        }

        for(Bullet bullet : bulletList){
            if(bullet.getOwner() == mainShip || bullet.isDestroyed()){
                continue;
            }
            if(mainShip.isBulletCollision(bullet)){
                mainShip.damage(bullet.getDamage());
                bullet.destroy();
            }
        }
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
        if (state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            mainShip.touchDown(touch, pointer, button);
        } else if(state == State.GAME_OVER){
            buttonNewGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            mainShip.touchUp(touch, pointer, button);
        } else if(state == State.GAME_OVER){
            buttonNewGame.touchUp(touch, pointer, button);
            state = buttonNewGame.getState();
        }
        return false;
    }

    private void update(float delta){
        for (Star star : stars){
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if(state == State.PLAYING) {
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta);
        }
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
        if(state == State.PLAYING){
            mainShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        } else if(state == State.GAME_OVER){
            gameOver.draw(batch);
            buttonNewGame.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        batch.end();
    }
}
