import javax.swing.*;

/**
 * Pentago game class
 *
 * @version 0
 * @author Mostafa_BJN
 */
public class Game {
    //Board of game
    private Board gameBoard;

    /**
     * Start a new Game
     */
    public Game(){
        gameBoard = new Board();
    }

    /**
     * put a dot in an available block.
     *
     * @param blockToPut Block to put
     * @param player player's turn
     * @return is it possible to put at this block.
     */
    public boolean putter (Block blockToPut, int player) {
        for (Block block : gameBoard.getBlocks()) {
            if(block.equals(blockToPut)){
                if(block.getState() == 0) {
                    block.colorBlock(player);
                    return true;
                }
                else{
                    System.out.println("It is Full Already");
                    return false;
                }
            }
        }
        System.out.println("!!! Something Goes Wrong !!!");
        return false;
    }

    private void changeTwoBlock (int a, int b, int square){
        Block blockA = getGameBoard().getSquaredBlocks().get(square).get(a);
        Block blockB = getGameBoard().getSquaredBlocks().get(square).get(b);

        gameBoard.getSquaredBlocks().get(square).set(a, blockB);
        gameBoard.getSquaredBlocks().get(square).set(b, blockA);

        int aa = a%3 + (a/3)*Board.SIZE + ((square % 2) * (Board.SIZE_OF_SQUARE)) + ((square / 2) * (Board.SIZE_OF_SQUARE * Board.SIZE));
        int bb = b%3 + (b/3)*Board.SIZE + ((square % 2) * (Board.SIZE_OF_SQUARE)) + ((square / 2) * (Board.SIZE_OF_SQUARE * Board.SIZE));

        gameBoard.getBlocks().set(bb, blockA);
        gameBoard.getBlocks().set(aa, blockB);

        blockB.changeCoordinates(a%3 + (square % 2) * (Board.SIZE_OF_SQUARE) + 1, a/3 + (square / 2) * (Board.SIZE_OF_SQUARE) + 1);
        blockA.changeCoordinates(b%3 + (square % 2) * (Board.SIZE_OF_SQUARE) + 1, b/3 + (square / 2) * (Board.SIZE_OF_SQUARE) + 1);
    }

    public void router (int sd){
        int square = -1;
        Block tempBlock;
        if (sd > 0)
        {
            square += sd;
            //four corners route
            changeTwoBlock(0, 2, square);
            changeTwoBlock(6, 0, square);
            changeTwoBlock(8, 6, square);
            //four middles route
            changeTwoBlock(1, 5, square);
            changeTwoBlock(3, 1, square);
            changeTwoBlock(7, 3, square);
        }
        else
        {
            square -= sd;
            //four corners route
            changeTwoBlock(8, 6, square);
            changeTwoBlock(6, 0, square);
            changeTwoBlock(0, 2, square);
            //four middles route
            changeTwoBlock(7, 3, square);
            changeTwoBlock(3, 1, square);
            changeTwoBlock(1, 5, square);
        }
    }

    /**
     * getter for Board of game
     *
     * @return gameBoard parameter
     */
    public Board getGameBoard() {
        return gameBoard;
    }
}
