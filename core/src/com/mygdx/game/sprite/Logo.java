package com.mygdx.game.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.math.Rect;

public class Logo extends Sprite {

    private Vector2 v;
    private Vector2 touch;
    private Vector2 common;
    private static final float V_LEN = 0.01f;

    public Logo(Texture texture) {
        super(new TextureRegion(texture));
        v = new Vector2();
        common = new Vector2();
        touch = new Vector2(this.pos);
    }

    @Override
    public void resize(Rect worldBounds){
        setHeightProportion(0.2f);
        Vector2 v = new Vector2(worldBounds.getLeft() + this.getHalfWidth(), worldBounds.getBottom() + this.getHalfHeight());
        this.pos.set(v); // выравнивание по центру
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.touch.set(touch);
        v.set(touch.cpy().sub(this.pos));
        v.setLength(V_LEN);
        return super.touchDown(touch, pointer, button);
    }

    public void changePosition() {
        common.set(this.touch);
        if(common.sub(this.pos).len() > V_LEN){
            this.pos.add(v);
        } else {
            this.pos.set(this.touch);
            v.setZero();
        }
    }
}
