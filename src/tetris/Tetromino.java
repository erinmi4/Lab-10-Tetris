package tetris;

import tileengine.TETile;

import java.awt.*;

/**
 *  Provides the logic for Tetris.
 *
 *  @author Erik Nelson, Omar Yu, and Noah Adhikari
 */

public enum Tetromino {
    // colors from tetris wiki
    I(new Color(0x31e7ef), new boolean[][]{
            {false, false, false, false},
            {true, true, true, true},
            {false, false, false, false},
            {false, false, false, false}
    }),
    J(new Color(0x5a65ad), new boolean[][]{
            {true, false, false},
            {true, true, true},
            {false, false, false},
    }),
    L(new Color(0xef7921), new boolean[][]{
            {false, false, true},
            {true, true, true},
            {false, false, false},
    }),
    O(new Color(0xf7d308), new boolean[][]{
            {true, true},
            {true, true},
    }),
    S(new Color(0x42b642), new boolean[][]{
            {false, true, true},
            {true, true, false},
            {false, false, false}
    }),
    T(new Color(0xad4d9c), new boolean[][]{
            {false, true, false},
            {true, true, true},
            {false, false, false},
    }),
    Z(new Color(0xef2029), new boolean[][]{
            {true, true, false},
            {false, true, true},
            {false, false, false}
    });

    private final TETile tile;
    boolean[][] shape;
    int width;
    int height;

    Point pos;

    Tetromino(Color color, boolean[][] s) {
        this.tile = new TETile('█', color, Color.BLACK, "", 0);
        // need to convert from ij to xy coords because tile renderer coordinates are mismatched
        //需要从ij转换为xy坐标，因为tile渲染器坐标不匹配
        this.shape = ijToXY(s);
        this.width = shape[0].length;
        this.height = shape.length;
        this.pos = new Point(3, 20);
    }

    /** Converts from ij coordinates to xy coordinates. This is specifically for converting
     * the 2D boolean array representation of a piece to the tile rendering coordinates, since
     * orientation is not aligned.
     *
     * 从ij坐标转换为xy坐标。这是专门用于转换的
     * 块的二维布尔数组表示为平铺渲染坐标，因为
     * 方向未对齐。
     */
    private static boolean[][] ijToXY(boolean[][] ijArr) {
        int numRows = ijArr.length;
        int numCols = ijArr[0].length;
        boolean[][] result = new boolean[numCols][numRows];
        for (int x = 0; x < numCols; x++) {
            for (int y = 0; y < numRows; y++) {
                result[x][y] = ijArr[numRows - y - 1][x];
            }
        }
        return result;
    }


    /**
     * Draws the piece at the given coordinates of the given board. (x,y) = 0,0 is bottom-left.
     * Does not do bounds-checking.
     * 在给定棋盘的指定坐标处绘制方块，(x, y) = 0,0 为左下角。
     * 不进行边界检查。
     * ij坐标是七巧板自己设定在一个board上的坐标
     * 如果需要将七巧板投影到board上就需要重新
     */
    public static void draw(Tetromino t, TETile[][] board, int bx, int by) {
        for (int tx = 0; tx < t.width; tx++) {
            for (int ty = 0; ty < t.height; ty++) {
                if (t.shape[tx][ty]) {
                    board[bx + tx][by + ty] = t.tile;
                }
            }
        }
    }

    /**
     * Sets the point of a Tetromino to (3, 20), specifically for spawning.
     * 将Tetromino的点设置为(3,20)，专门用于刷出。
     */
    public void reset() {
        this.pos = new Point(3, 20);
    }

}
