package com.mygdx.game.pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.base.SpritesPool;
import com.mygdx.game.math.Rect;
import com.mygdx.game.sprite.Enemy;

public class EnemyPool extends SpritesPool<Enemy> {
    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private Rect worldBounds;
    private Sound shootSound;

    public EnemyPool(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds){
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
    }

    @Override
    protected Enemy newObject() {
         return new Enemy(bulletPool, explosionPool, worldBounds, shootSound);
    }

    @Override
    public void dispose() {
        super.dispose();
        shootSound.dispose();
    }
}
