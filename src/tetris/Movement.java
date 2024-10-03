package tetris;

import tileengine.TETile;
import tileengine.Tileset;

/**
 *  Provides the logic for movement of Tetris pieces.
 *
 *  @author Erik Nelson, Omar Yu, and Jasmine Lin
 */

public class Movement {

    private int WIDTH;

    private int GAME_HEIGHT;

    Tetris tetris;

    public Movement (int width, int game_height, Tetris tetris) {
        this.WIDTH = width;
        this.GAME_HEIGHT = game_height;
        this.tetris = tetris;
    }

    /**
     * Rotate the current Tetromino 90 degrees to the right (clockwise).
     */
    public void rotateRight() {
        rotate(Rotation.RIGHT);
    }

    /**
     * Rotate the current Tetromino 90 degrees to the left (counter-clockwise).
     */
    public void rotateLeft() {
        rotate(Rotation.LEFT);
    }

    /**
     * Attempts to move the current Tetromino by a shift of deltaX and deltaY.
     * If the Tetromino cannot move and will collide with a boundary or existing piece,
     * it is placed at its current position and nullified so a new Tetromino can spawn.
     * 尝试通过移动 deltaX 和 deltaY 的偏移量来移动当前的 Tetromino（俄罗斯方块）。
     * 如果 Tetromino 无法移动并且会与边界或现有的方块碰撞，它将被放置在当前位置并置空，以便生成一个新的 Tetromino。
     * @param deltaX
     * @param deltaY
     */
    public void tryMove(int deltaX, int deltaY) {
        Tetromino t = tetris.getCurrentTetromino();

        if (canMove(deltaX, deltaY)) {
            t.pos.x += deltaX;
            t.pos.y += deltaY;
        } else {
            if (deltaY < 0) {
                TETile[][] board = tetris.getBoard();
                Tetromino.draw(t, board, t.pos.x, t.pos.y);
                tetris.fillAux();

                tetris.setAuxTrue();
                tetris.setCurrentTetromino();
            }
        }
    }

    /**
     * Checks whether moving the current Tetromino by a shift of deltaX and deltaY
     * is valid, i.e. within bounds and does not collide with other pieces.
     * 检查将当前 Tetromino（俄罗斯方块）按 deltaX 和 deltaY 的偏移量移动是否有效，
     * 即是否在边界内并且不会与其他方块碰撞。
     * @param deltaX
     * @param deltaY
     * @return a boolean representing if the move is possible or not
     */
    public boolean canMove(int deltaX, int deltaY) {
        Tetromino t = tetris.getCurrentTetromino();

        for (int tx = 0; tx < t.width; tx++){
            for (int ty = 0; ty < t.height; ty++){
                if (t.shape[tx][ty]) {

                    // Out of bounds check
                    if (t.pos.x + tx + deltaX >= WIDTH ||
                            t.pos.x + tx + deltaX < 0 ||
                            t.pos.y + ty + deltaY >= GAME_HEIGHT ||
                            t.pos.y + ty + deltaY < 0) {
                        return false;
                    }

                    // Board check
                    TETile[][] board = tetris.getBoard();
                    if (board[t.pos.x + tx + deltaX][t.pos.y + ty + deltaY] != Tileset.NOTHING) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Moves the current Tetromino down one tile, if not able to move down,
     * set the block in place and allow for a new Tetromino to be spawned.
     * 将当前的 Tetromino（俄罗斯方块）向下移动一格，
     * 如果无法向下移动，则将方块固定在当前位置，并允许生成一个新的 Tetromino。
     */
    public void dropDown() {
        Tetromino t = tetris.getCurrentTetromino();

        if (canMove(0, -1)) {
            t.pos.y -= 1;
        } else {
            TETile[][] board = tetris.getBoard();
            Tetromino.draw(t, board, t.pos.x, t.pos.y);
            tetris.fillAux();

            tetris.setAuxTrue();
            tetris.setCurrentTetromino();
        }
    }

    /**
     * Checks whether rotating the current Tetromino is valid,
     * i.e. it will remain within bounds and does not rotate/collide into
     * other pieces.
     * 检查当前 Tetromino（俄罗斯方块）的旋转是否有效，即它是否仍然在边界内，并且不会在旋转时与其他方块碰撞。
     * @param newShape
     * @return a boolean representing if the rotation is possible or not
     */
    public boolean canRotate(boolean[][] newShape) {
        Tetromino t = tetris.getCurrentTetromino();
        boolean valid = true;
        for (int tx = 0; tx < newShape.length; tx++) {
            for (int ty = 0; ty < newShape[0].length; ty++) {
                if (newShape[tx][ty]) {
                    if (t.pos.x + tx < 0 || t.pos.y + ty < 0
                            || t.pos.x + tx >= tetris.getAuxiliary().length
                            || t.pos.y + ty >= tetris.getAuxiliary()[0].length
                            || tetris.getAuxiliary()[t.pos.x + tx][t.pos.y + ty] != Tileset.NOTHING) {
                        valid = false;
                    }
                }
            }
        }
        return valid;
    }

    /**
     * Rotation enum used to discern between left and right rotations.
     */
    public enum Rotation {
        RIGHT, LEFT
    }

    /**
     * Attempts to rotate the current Tetromino by the given direction r (left or right).
     * If the Tetromino cannot rotate, it will remain in its current orientation.
     * 尝试按照给定的方向 r（左或右）旋转当前的 Tetromino（俄罗斯方块）。
     * 如果 Tetromino 无法旋转，它将保持当前的方向不变。
     * @param r
     */
    public void rotate(Rotation r) {
        Tetromino t = tetris.getCurrentTetromino();
        int h = t.shape.length;
        int w = t.shape[0].length;
        boolean[][] newShape = new boolean[h][w];
        if (r == Rotation.LEFT) {
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++){
                    newShape[i][j] = t.shape[j][h - i - 1];
                }
            }
        } else if (r == Rotation.RIGHT) {
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    newShape[i][j] = t.shape[w - j - 1][i];
                }
            }
        }

        if (canRotate(newShape)) {
            t.shape = newShape;
            t.height = t.shape[0].length;
            t.width = t.shape.length;
        }
    }
}
