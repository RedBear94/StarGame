package com.mygdx.game.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Sprite;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.ExplosionPool;

public class Ship extends Sprite {

    protected final Vector2 v0; // вектор направления
    protected final Vector2 v;  // вектор скорости

    protected Rect worldBounds;

    protected ExplosionPool explosionPool;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected Vector2 bulletV; // Направление пули
    protected float bulletHeight;
    protected int damage;

    protected float reloadTimer;
    protected float reloadInterval;
    protected Sound shootSound;

    protected boolean pressedAttack;

    protected int hp;

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
        v0 = new Vector2();
        v = new Vector2();
        pressedAttack = false;
    }

    public Ship(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound shootSound){
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.shootSound = shootSound;
        v0 = new Vector2();
        v = new Vector2();
        bulletV = new Vector2();
        pressedAttack = false;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        if(pressedAttack){
            reloadTimer += delta;
            if (reloadTimer >= reloadInterval){
                shoot();
                reloadTimer = 0f;
            }
        } else {
            reloadTimer = 0f;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    protected void shoot(){
        shootSound.play(1.0f);
        Bullet bullet = bulletPool.obtain();
        bullet.set(this,
                bulletRegion,
                pos,
                bulletV,
                bulletHeight,
                worldBounds,
                damage);
    }

    private void boom(){
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }
}
