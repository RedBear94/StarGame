package com.mygdx.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Sprite;
import com.mygdx.game.math.Rect;

public class Ship extends Sprite {
    private Vector2 v;
    private Vector2 touch;
    private Vector2 common;

    private static final float V_LEN = 0.01f;

    public Ship(TextureRegion[] texture) {
        super(texture);
        this.frame = 1;
        v = new Vector2();
        common = new Vector2();
        touch = new Vector2(this.pos);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.12f);
        this.pos.set(0f,-0.4f);
    }

    @Override
    public void update(float delta){
        common.set(this.touch);
        if(common.sub(pos).len() > V_LEN){
            pos.add(v);
        } else {
            pos.set(touch);
            v.setZero();
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.touch.set(touch);
        v.set(touch.sub(this.pos));
        v.setLength(V_LEN);
        return super.touchDown(touch, pointer, button);
    }
}
