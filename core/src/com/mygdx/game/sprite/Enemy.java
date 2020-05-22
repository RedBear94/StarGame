package com.mygdx.game.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.ExplosionPool;

public class Enemy extends Ship {

    private final Vector2 fastSpeedV;
    private boolean shipIsReady = false;

    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound shootSound) {
        super(bulletPool, explosionPool, worldBounds, shootSound);
        fastSpeedV = new Vector2(0, -0.3f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(getBottom() <= worldBounds.getBottom()){
            destroy();
        }

        if(getTop() >= (worldBounds.getTop())){
            setSpeed(fastSpeedV);

            // ??? Без этой строчки маленькие корабли не стреляют сразу при появлении ???
            this.shipIsReady = false;
        } else {
            setSpeed(v0);
            if(shipIsReady == false){
                this.pressedAttack = true;
                this.reloadTimer = reloadInterval;
                this.shipIsReady = true;
            }
        }
    }

    public void set(
                TextureRegion[] regions,
                Vector2 v0,
                TextureRegion bulletRegion,
                float bulletHeight,
                float bulletVY,
                int damage,
                float reloadInterval,
                int hp,
                float height
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        this.hp = hp;
        this.v.set(v0);
        setHeightProportion(height);
    }

    public void setSpeed(Vector2 v){
        this.v.set(v);
    }
}
