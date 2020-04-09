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

    public void router (int sd){
        int square;
        if (sd > 0)
        {
            square = (sd - 1);
            game_board_temp = game_board[s][2][0];
            game_board[s][2][0] = game_board[s][0][0];
            game_board[s][0][0] = game_board[s][0][2];
            game_board[s][0][2] = game_board[s][2][2];
            game_board[s][2][2] = game_board_temp;
            Block tempBlock = gameBoard.getSquaredBlocks().get(square).get(6);
            gameBoard.getSquaredBlocks().get(square).remove(6);
            gameBoard.getSquaredBlocks().get(square).add(6,gameBoard.getSquaredBlocks().get(square).get(0));

            game_board_temp = game_board[s][0][1];
            game_board[s][0][1] = game_board[s][1][2];
            game_board[s][1][2] = game_board[s][2][1];
            game_board[s][2][1] = game_board[s][1][0];
            game_board[s][1][0] = game_board_temp;

        }
        else
        {
            square = -(sd + 1);
            game_board_temp = game_board[s][0][2];
            game_board[s][0][2] = game_board[s][0][0];
            game_board[s][0][0] = game_board[s][2][0];
            game_board[s][2][0] = game_board[s][2][2];
            game_board[s][2][2] = game_board_temp;

            game_board_temp = game_board[s][1][0];
            game_board[s][1][0] = game_board[s][2][1];
            game_board[s][2][1] = game_board[s][1][2];
            game_board[s][1][2] = game_board[s][0][1];
            game_board[s][0][1] = game_board_temp;
        }//ravesh copy kardan (abcd)
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
