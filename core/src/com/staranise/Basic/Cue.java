package com.staranise.Basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.staranise.thing.Vec2;

/**
 * Created by 현성 on 2016-04-23..
 * cue
 */
public class Cue extends TexturedObject {

    private float _fScrolled;
    private float _fAngle;
    private BilliardBall _targetBall = null;
    private Vec2 _targetPos = null;
    private Vec2 _oldDir = null;

    private static final float _fCueEndXPosition = 7.f;
    private static final float _fCueEndYPosition = 238.f;

    public Cue(){
        _sprite = new Sprite(new Texture("cue.png"));
        setVisible(false);
        setWidth(_sprite.getWidth());
        setHeight(_sprite.getHeight());
        setZIndex(6);
    }

    public void setTargetBall(BilliardBall targetBall){
        _targetBall = targetBall;
        _targetPos = new Vec2(_targetBall.getEngine().getPosition());
    }

    public void realTimeCueEvent(int step){
        // step 1 : Ball Click -> Ignore All Cue Event
        if(_targetBall != null) {
            // step 2 : Setting cue direction -> Another Ball Click Enabled, Dragging Enabled
            if (step == 2) {
                float curX = Gdx.input.getX();
                float curY = Gdx.graphics.getHeight() - Gdx.input.getY();
                Vec2 direction = new Vec2(curX, curY);
                Vec2 newdir = direction.minus(_targetBall.getEngine().getPosition());
                if (Gdx.input.isTouched()) {
                    if (_oldDir == null)
                        _oldDir = newdir;
                    _fScrolled += (newdir.getLength() - _oldDir.getLength());
                    _oldDir = new Vec2(newdir);
                }
                else{
                    if (!Gdx.input.isTouched()) _oldDir = null;

                    Vec2 origin = new Vec2(0.f, -1.f);

                    float cosvalue = origin.getCos(newdir);
                    _fAngle = (float) Math.acos(cosvalue) * (newdir.x >= 0 ? 1.f : -1.f);
                }
                positioning();
            }

            // step 3 : shot the ball -> Ignore All Cue Event(return to step 1)
            if (step == 3) {
                float curX = Gdx.input.getX();
                float curY = Gdx.graphics.getHeight() - Gdx.input.getY();

                Vec2 direction = new Vec2(curX, curY);
                Vec2 newdir = direction.minus(_targetBall.getEngine().getPosition());
                _fScrolled = 0.f;
                _oldDir = null;
                Vec2 vecBallSpd = newdir.multi(-1.f);

                BallSpinDecider decider = GameManager.getInstance().decider;
                float x = decider.getSpinX();
                float y = decider.getSpinY();

                _targetBall.getEngine().setAccComponent("수직스핀", vecBallSpd.norm().multi(y * GameConfig.VERTICAL_SPIN_FACTOR));
                _targetBall.getEngine().setLinearSpeed(vecBallSpd.multi(GameConfig.CUE_POWER_FACTOR));

                _targetBall.setHorizontalSpin(-x);

                positioning();
                _targetBall = null;
            }
        }

    }

    public void positioning(){
        float finalPosX;
        float finalPosY;

        //기준 : 큐의 회전축
        if(_targetBall != null) {
            _sprite.setOrigin(7.f, _fCueEndYPosition + GameConfig.BALL_RADIUS + _fScrolled);
            finalPosX = _targetPos.x - _fCueEndXPosition;         //기준 x좌표 : 공의 x좌표/7.f : 이미지 위치 보정
            finalPosY = _targetPos.y - (_fCueEndYPosition + GameConfig.BALL_RADIUS + _fScrolled);       //기준 y좌표 : 공의 y좌표/252.f : 이미지 위치 보정

            _sprite.setPosition(finalPosX, finalPosY);
            _sprite.setRotation((float) Math.toDegrees(_fAngle));
        }
    }
}
