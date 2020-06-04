package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.base.Font;
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
    private static final float TEXT_MARGIN = 0.01f;
    private static final float FONT_SIZE = 0.02f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

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

    private int frags;
    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;

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
        font = new Font("font/font.fnt", "font/font.png");
        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();
        state = State.PLAYING;
        buttonNewGame = new ButtonNewGame(textureAtlas, this);
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
        font.setSize(FONT_SIZE);
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
                    if(enemy.isDestroyed()){
                        frags += 1;
                    }
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
        font.dispose();
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
            enemyEmitter.generate(delta, frags);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.update(delta);
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
        printInfo();
        batch.end();
    }

    private void printInfo(){
        sbFrags.setLength(0);
        sbHp.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + TEXT_MARGIN, worldBounds.getTop() - TEXT_MARGIN);
        font.draw(batch, sbHp.append(HP).append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - TEXT_MARGIN, Align.center);
        // вывод жизней под кораблем
        // font.draw(batch, sbHp, mainShip.pos.x, mainShip.getBottom() - TEXT_MARGIN, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - TEXT_MARGIN, worldBounds.getTop() - TEXT_MARGIN, Align.right);
    }

    public void startNewGame(){
        frags = 0;
        mainShip.resetShip();
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        state = GameScreen.State.PLAYING;
    }
}
