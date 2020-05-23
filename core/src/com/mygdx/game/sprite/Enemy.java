package com.mygdx.game.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.ExplosionPool;

public class Enemy extends Ship {

    private static final float SPEED_V_Y = -0.3f;

    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound shootSound) {
        super(bulletPool, explosionPool, worldBounds, shootSound);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(getTop() < worldBounds.getTop()){
            v.set(v0);
            bulletPos.set(pos.x, pos.y - getHalfHeight());
            autoShoot(delta);
        }

        if(getBottom() <= worldBounds.getBottom()){
            destroy();
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
        setHeightProportion(height);
        v.set(0, SPEED_V_Y);
    }

    public boolean isBulletCollision(Rect bullet){
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < pos.y
        );
    }
}
