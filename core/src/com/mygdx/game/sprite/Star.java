package com.mygdx.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Sprite;
import com.mygdx.game.math.Rect;
import com.mygdx.game.math.Rnd;

public class Star  extends Sprite {

    private static Vector2 v;
    private Rect worldBounds;

    private float animateTimer;
    private float animateInterval;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        v = new Vector2();
        float vx = Rnd.nextFloat(-0.005f, 0.005f);
        float vy = Rnd.nextFloat(-0.2f, -0.1f);
        v.set(vx, vy);
        worldBounds = new Rect();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(0.01f);
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(posX, posY);
        setScale(Rnd.nextFloat(0.1f, 1f));
        animateInterval = Rnd.nextFloat(0.5f, 1.5f);
    }

    @Override
    public void update(float delta) {
        setScale(getScale() - 0.008f);
        animateTimer += delta;
        if (animateTimer >= animateInterval){
            setScale(1f);
            animateTimer = 0f;
        }
        pos.mulAdd(v, delta);   // mulAdd - сложение векторов и умножение векторов на скаляр
        checkBounds();
    }

    private void checkBounds(){
        if(getRight() < worldBounds.getLeft()){
            setLeft(worldBounds.getRight());
        }
        if(getLeft() > worldBounds.getRight()){
            setRight(worldBounds.getLeft());
        }
        if(getTop() < worldBounds.getBottom()){
            setBottom(worldBounds.getTop());
        }
    }
}
