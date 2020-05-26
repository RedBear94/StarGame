package com.mygdx.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.base.ScaledButton;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.EnemyPool;
import com.mygdx.game.screen.GameScreen;

import java.util.List;

public class ButtonNewGame extends ScaledButton {
    private MainShip mainShip;
    GameScreen.State state;
    EnemyPool enemyPool;
    BulletPool bulletPool;

    public ButtonNewGame(TextureAtlas atlas, GameScreen.State state, MainShip mainShip, EnemyPool enemyPool, BulletPool bulletPool) {
        super(atlas.findRegion("button_new_game"));
        this.mainShip = mainShip;
        this.state = state;
        this.enemyPool = enemyPool;
        this.bulletPool = bulletPool;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.07f);
        setTop(-0.05f);
    }

    @Override
    public void action() {
        state = GameScreen.State.PLAYING;
        mainShip.resetShip();
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for(Enemy enemy : enemyList){
            enemy.destroy();
        }
        for(Bullet bullet : bulletList){
            bullet.destroy();
        }
    }

    public GameScreen.State getState() {
        return state;
    }
}