package com.mygdx.game.base;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.math.MatrixUtils;
import com.mygdx.game.math.Rect;

public class BaseScreen implements Screen, InputProcessor {
    protected SpriteBatch batch; // Рисует пакетные четырехугольники, используя индексы
    private Rect screenBounds; // 1-й экран с координатами в px
    private Rect worldBounds; // Рабочая координатная сетка
    private Rect glBounds; // Координатная сетка OpenGL

    private Matrix4 worldToGl; // Матрица проекции для batch (worldBounds->glBounds)
    private Matrix3 screenToWorld; // Матрица проекции (screenBounds->worldBounds)

    private Vector2 touch;

    private Music music;
    // Screen methods

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();

        screenBounds = new Rect();
        worldBounds = new Rect();
        glBounds = new Rect(0, 0, 1f, 1f);
        worldToGl = new Matrix4();
        screenToWorld = new Matrix3();
        touch = new Vector2();

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.play();
        music.setLooping(true);
    }

    // delta - отрезок времени рассчитываемый libGDX
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.2f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        // Приходят границы игрового мира
        screenBounds.setSize(width, height);
        // Точка с нулевыми координатами установливается, как нижняя левая точка
        screenBounds.setLeft(0);
        screenBounds.setBottom(0);
        // перспектива - переменная которая рассчитывается для разного размера окна
        // и определяет размер рабочей координатной сетки
        float aspect = width / (float) height;
        // Сетка размером 1f на 1f * aspect
        worldBounds.setHeight(1f);
        worldBounds.setWidth(1f * aspect);
        // Расчет матриц проекции
        // Аргументы: 1 - кого заполняем (матрица), 2 - от куда проецируемся, 3 - куда проецируемся
        MatrixUtils.calcTransitionMatrix(worldToGl, worldBounds, glBounds);
        MatrixUtils.calcTransitionMatrix(screenToWorld, screenBounds, worldBounds);
        batch.setProjectionMatrix(worldToGl);
        resize(worldBounds);
    }

    // Изменение размеров в новой заданной системе координат
    public void resize(Rect worldBounds) {
        /*System.out.println("resize worldBounds.Height = " + worldBounds.getHeight() + " worldBounds.Width = " + worldBounds.getWidth());*/
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        music.dispose();
        batch.dispose();
    }

    // InputProcessor Methods

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Метод touchDown - переводится в новую координатную систему: переворачивается ось Y,
        // затем mul - умножение на матрицу (преобразований)
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDown(touch, pointer, button);
        return false;
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        /*System.out.println("touchDown touch.x = " + touch.x + " touch.y = " + touch.y);*/
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchUp(touch, pointer, button);
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer, int button) {
        /*System.out.println("touchUp touch.x = " + touch.x + " touch.y = " + touch.y);*/
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDragged(touch, pointer);
        return false;
    }

    public boolean touchDragged(Vector2 touch, int pointer) {
        /*System.out.println("touchDragged touch.x = " + touch.x + " touch.y = " + touch.y);*/
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
