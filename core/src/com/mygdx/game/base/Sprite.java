package com.mygdx.game.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import com.mygdx.game.math.Rect;
import com.mygdx.game.utils.Regions;

// Базовый класс для графических объектов
public class Sprite extends Rect {
    protected float angle; // угол поворота
    protected float scale = 1f; // масштаб обхекта
    protected TextureRegion[] regions; // массив из текстур
    protected int frame = 0; // указатель на текущий кадр в массиве
    protected boolean destroyed; // флаг на то что спрайт уничтожен

    public Sprite(){}

    public Sprite(TextureRegion region) {
        regions = new TextureRegion[1];
        regions[0] = region;
    }

    // My ver (только для квадратного атласа)
    /*public Sprite(TextureRegion[] regions) {
        this.regions = new TextureRegion[regions.length];
        for(int i = 0; i < regions.length; i++) {
            this.regions[i] = regions[i];
        }
    }*/

    public Sprite(TextureRegion region, int rows, int cols, int frames){
        // свой split - не из gdx; учитывет число фреймов;
        regions = Regions.split(region, rows, cols, frames);
    }

    // Рассчет справйта от заданной высоты
    public void setHeightProportion(float height) {
        setHeight(height);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth(height * aspect);
    }

    public void update(float delta){

    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),
                halfWidth, halfHeight,
                getWidth(), getHeight(),
                scale, scale,
                angle
        );
    }

    public void resize(Rect worldBounds){
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void destroy(){
        destroyed = true;
    }

    // Сброс флага
    public void flushDestroy(){
        destroyed = false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
