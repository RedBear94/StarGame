package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.BaseScreen;

public class MenuScreen extends BaseScreen {
    private Texture img;
    private Texture testImg;

    private Vector2 pos;
    private Vector2 v;
    private Vector2 v2;
    private Vector2 gravity;
    private Vector2 touch;

    @Override
    public void show() {
        super.show();
        img = new Texture("space.jpg");
        pos = new Vector2();
        v = new Vector2(1,1);

        v2 = new Vector2(0,0);

        gravity = new Vector2(0,-0.005f);
        testImg = new Texture("badlogic.jpg");
        touch = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0.5f, 0.6f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Math.abs(touch.x-pos.x)>0.5 && Math.abs(touch.y-pos.y)>0.5) {
            pos.add(v2);
        }

        batch.begin();
        batch.draw(img, 0, 0);
        batch.draw(testImg, pos.x, pos.y);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        System.out.println("touch.x = " + touch.x + " touch.y = " + touch.y);
        System.out.println("pos.x = " + pos.x + " pos.y = " + pos.y);
        v2.set(pos.x, pos.y);
        v2 = touch.cpy().sub(v2);
        v2.nor();
        v2.set(v2.x, v2.y);
        return super.touchDown(screenX, screenY, pointer, button);
    }
}
