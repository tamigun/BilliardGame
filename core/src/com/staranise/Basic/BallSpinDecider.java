package com.staranise.Basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by 현성 on 2016-04-23.
 */
public class BallSpinDecider extends Actor {
    private Sprite _sprite;
    private float _fSpinX = -1.f;
    private float _fSpinY = -1.f;
    private static float _rootX = Gdx.graphics.getWidth() - 128.f;

    //private static final InputListener listener;

    public void setSpinPos(float spinX, float spinY){
        _fSpinX = spinX;
        _fSpinY = spinY;
    }

    public BallSpinDecider(){
        _sprite = new Sprite(new Texture("BallSpinDecider.png"));
        _sprite.setCenter(Gdx.graphics.getWidth()-64.f, 64.f);

        setVisible(false);
        setWidth(_sprite.getWidth());
        setHeight(_sprite.getHeight());
        setBounds(Gdx.graphics.getWidth()-_sprite.getWidth(), 0.f, _sprite.getWidth(), _sprite.getHeight());

        addListener(/*listener*/new InputListener(){

            private Actor _point;
            private boolean _touched = false;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                _point = event.getStage().getActors().get(2);
                _point.setPosition(_rootX + x-26.f, y-26.f);
                _point.setVisible(true);
                _touched = true;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if(_touched){
                    _point.setPosition(_rootX + x-26.f, y-26.f);
                }
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                BallSpinDecider target = (BallSpinDecider)event.getTarget();
                target.setSpinPos(x-64.f, y-64.f);
                target.setVisible(false);
                _point.setVisible(false);
                _touched = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }


    @Override
    public void draw(Batch batch, float parentAlpha){
        _sprite.draw(batch);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(visible){
            if(_fSpinX != -1.f){
                getStage().getActors().get(2).setVisible(true);
            }
        }
    }
}