package com.geektcp.alpha.game.chess;

import com.geektcp.alpha.game.chess.chess.Board;
import com.geektcp.alpha.game.chess.control.GameController;
import com.geektcp.alpha.game.chess.view.GameView;


public class Application {
    private Board board;

    private GameController controller;
    private GameView view;

    public static void main(String[] args) throws InterruptedException {
        Application game = new Application();
        game.init();
        game.run();
    }

    public void init() {
        controller = new GameController();
        board = controller.playChess();

        view = new GameView(controller);
        view.init(board);
    }

    public void run() throws InterruptedException {
        while (controller.hasWin(board) == 'x') {
            view.showPlayer('r');
            /* User in. */
            while (board.player == 'r')
                Thread.sleep(1000);

            if (controller.hasWin(board) != 'x')
                view.showWinner('r');
            view.showPlayer('b');
            /* AI in. */
            controller.responseMoveChess(board, view);
        }
        view.showWinner('b');
    }
}