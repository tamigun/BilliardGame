package com.staranise.basic;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by YuTack on 2015-11-16..
 */
public class GameResource {
    private static GameResource ourInstance = new GameResource();

    public static GameResource getInstance() {
        return ourInstance;
    }

    private Skin skin;

    private GameResource() {
        init();
    }

    private void init() {
        skin = new Skin(Gdx.files.internal("resources/uiskin.json"));
    }

    public Skin getSkin(){
        return skin;
    }

    public NinePatch getPatch(String name) {
        return skin.getPatch(name);
    }
}
