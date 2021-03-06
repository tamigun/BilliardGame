package com.staranise.billiard;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.staranise.basic.BallSpinDecider;
import com.staranise.basic.BilliardBall;
import com.staranise.basic.Cue;
import com.staranise.basic.GNPBatchProcessor;
import com.staranise.screen.GameMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuTack on 2016-05-05..
 *
 * 게임 정보 저장, 관리, 모든 엑터 레퍼런스 저장
 */
public class GameManager {

    public interface ExecuteStepListener{
        void executeStep(GAME_STEP player);
    }

    public interface HitSuccessListener{
        void notifyHitSuccess(GAME_STEP player, boolean isThreeCushion);
    }

    public interface HitBadListener{
        void notifyHitBad(GAME_STEP player);
    }

    public enum GAME_STEP {
        player1,
        player2
    }

    private GameManager() {
    }

    public final static void startNewGame(Game game, int defaultScore, String player1Name, String player2Name, int gameMode) {
        instance = new GameManager();
        instance.player1point = defaultScore;
        instance.player2point = defaultScore;
        instance.gameMode = gameMode;
        //instance.GNPBatchProcessor = new GNPBatchProcessor(true, true);

        instance.initGame(game);
    }

    private static GameManager instance = new GameManager();

    public static GameManager getInstance() {
        return instance;
    }

    private int gameMode;
    private int player1point = 0;
    private int player2point = 0;

    private BilliardBall player1Ball;
    private BilliardBall player2Ball;

    private ExecuteStepListener onExecuteStepListener = null;
    private HitSuccessListener onHitSuccessListener = null;
    private HitBadListener onHitBadListener = null;

    public void setPlayer1Ball(BilliardBall player1Ball) {
        this.player1Ball = player1Ball;
    }

    public void setPlayer2Ball(BilliardBall player2Ball) {
        this.player2Ball = player2Ball;
    }

    List<BilliardBall> ballList = new ArrayList<BilliardBall>();

    public List<BilliardBall> getBilliardBallList() {
        return ballList;
    }

    private com.staranise.basic.GNPBatchProcessor GNPBatchProcessor; // 게임 메인에서 set써주면 사용가능, 나중에 수정
    public void setGNPBatchProcessor(GNPBatchProcessor GNPBatchProcessor) { this.GNPBatchProcessor = GNPBatchProcessor; }

    private Cue _cue;
    private float _fDeltaTime;

    private GAME_STEP _step = GAME_STEP.player1;

    public OrthographicCamera cam;
    private BallSpinDecider _decider;

    //쓰리쿠션 칠때인지 아닌지 반환
    public boolean isLastHit() {
        if(_step == GAME_STEP.player1 && player1point == 0 || _step == GAME_STEP.player2 && player2point == 0)
            return true;
        else
            return false;
    }

    private void initGame(Game game) {
        game.setScreen(new GameMain(gameMode));
    }

    public void setOnExecuteStepListener(ExecuteStepListener listener){
        this.onExecuteStepListener = listener;
    }

    public void setOnHitSuccessListener(HitSuccessListener listener){
        this.onHitSuccessListener = listener;
    }

    public void setOnHitBadListener(HitBadListener listener){
        this.onHitBadListener = listener;
    }

    public void setCue(Cue cue) {
        _cue = cue;
    }

    public void setSpinDecider(BallSpinDecider decider) {
        _decider = decider;
    }

    public void setDeltaTime(float fDeltaTime) {
        _fDeltaTime = fDeltaTime;
    }

    public float getDeltaTime() {
        return _fDeltaTime;
    }

    public Cue getCue() {
        return _cue;
    }

    public BallSpinDecider getSpinDecider() {
        return _decider;
    }

    public void setGameStep(GAME_STEP step) {
        _step = step;
    }

    public GAME_STEP getGameStep() {
        return _step;
    }

    public void executeStep(){
        _step = (_step == GAME_STEP.player1) ? GAME_STEP.player2 : GAME_STEP.player1;

        if(_step == GAME_STEP.player1) { player2Ball.setActive(false); player1Ball.setActive(true); }
        else { player2Ball.setActive(true); player1Ball.setActive(false); }

        if(onExecuteStepListener != null)
            onExecuteStepListener.executeStep(_step);
    }

    // 점수주고 턴 안넘김
    public void notifyHitSuccess(boolean isThreeCushion) {
        if(_step == GAME_STEP.player1) player1point -= 10;
        else player2point -= 10;

        if(onHitSuccessListener != null)
            onHitSuccessListener.notifyHitSuccess(_step, isThreeCushion);
    }

    // 턴넘김
    public void notifyHitMiss() {
        executeStep();
    }

    //감점 , 턴넘김
    public void notifyHitBad() {
        if(_step == GAME_STEP.player1) player1point += 10;
        else player2point += 10;

        onHitBadListener.notifyHitBad(_step);

        executeStep();
    }

}
