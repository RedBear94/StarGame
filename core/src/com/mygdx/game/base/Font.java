package com.mygdx.game.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Font extends BitmapFont {
    public Font(String fontFile, String imageFile) {
        super(Gdx.files.internal(fontFile), Gdx.files.internal(imageFile), false, false);
        // Сглаживание тектуры шрифта
        getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void setSize(float charSize){
        getData().setScale(charSize/getCapHeight());
    }

    public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, int halign) {
        return super.draw(batch, str, x, y, 0f, halign, false);
    }
}
