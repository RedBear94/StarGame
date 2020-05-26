package com.mygdx.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.base.ScaledButton;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.EnemyPool;
import com.mygdx.game.pool.ExplosionPool;
import com.mygdx.game.screen.GameScreen;

import java.util.List;

public class ButtonNewGame extends ScaledButton {

    private static final float ANIMATE_INTERVAL = 0.5f;

    private float animateTimer;
    private boolean scaleUp = true;

    GameScreen gameScreen;

    public ButtonNewGame(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
    }

    @Override
    public void update(float delta) {
        animateTimer += delta;
        if(animateTimer >= ANIMATE_INTERVAL){
            animateTimer = 0;
            scaleUp = !scaleUp;
        }
        if(scaleUp){
            setScale(getScale() + 0.002f);
        } else {
            setScale(getScale() - 0.002f);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.07f);
        setTop(-0.05f);
    }

    @Override
    public void action() {
        gameScreen.startNewGame();
    }
}