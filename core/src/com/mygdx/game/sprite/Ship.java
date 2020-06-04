package com.mygdx.game.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Sprite;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.ExplosionPool;

public class Ship extends Sprite {

    private static final float DAMAGE_ANIMATE_INTERNAL = 0.1f;

    protected final Vector2 v0; // вектор направления
    protected final Vector2 v;  // вектор скорости

    protected Rect worldBounds;

    protected ExplosionPool explosionPool;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected Vector2 bulletV; // Направление пули
    protected Vector2 bulletPos;
    protected float bulletHeight;
    protected int damage;

    protected float reloadTimer;
    protected float reloadInterval;
    protected Sound shootSound;

    protected boolean pressedAttack;

    protected int hp;

    protected float damageAnimateTimer;

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
        v0 = new Vector2();
        v = new Vector2();
        bulletV = new Vector2();
        bulletPos = new Vector2();
        pressedAttack = false;
        damageAnimateTimer = DAMAGE_ANIMATE_INTERNAL;
    }

    public Ship(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound shootSound){
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.shootSound = shootSound;
        v0 = new Vector2();
        v = new Vector2();
        bulletV = new Vector2();
        bulletPos = new Vector2();
        pressedAttack = true;
        damageAnimateTimer = DAMAGE_ANIMATE_INTERNAL;
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
        damageAnimateTimer += delta;
        if(damageAnimateTimer >= DAMAGE_ANIMATE_INTERNAL){
            frame = 0;
        }
    }

    protected void autoShoot(float delta){
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

    public void damage(int damage){
        damageAnimateTimer = 0f;
        frame = 1;

        hp -= damage;
        if(hp<=0){
            hp = 0;
            destroy();
        }
    }

    public int getDamage() {
        return damage;
    }

    protected void shoot(){
        shootSound.play(1.0f);
        Bullet bullet = bulletPool.obtain();
        bullet.set(this,
                bulletRegion,
                bulletPos,
                bulletV,
                bulletHeight,
                worldBounds,
                damage);
    }

    private void boom(){
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }

    public int getHp() {
        return hp;
    }
}
