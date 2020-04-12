import javax.swing.*;
import java.util.ArrayList;

/**
 * Pentago game class
 *
 * @version 0
 * @author Mostafa_BJN
 */
public class Game {
    //Board of game
    private Board gameBoard;
    private ArrayList<ArrayList<Integer>> statesList;

    /**
     * Start a new Game
     */
    public Game(){
        gameBoard = new Board();
        statesList = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < 36; i++) {
            statesList.add(new ArrayList<Integer>());
        }
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

    /**
     *
     * @param a
     * @param b
     * @param square
     */
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

    /**
     *
     *
     * @param sd
     */
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
     * Check all directions to check if 5 same color is in a row
     *
     * @param player player to check
     * @return if player wins
     */
    public boolean winConditions(int player){
        //Horizontal Check
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 2; i++) {
                if(gameBoard.getBlocks().get(j * Board.SIZE + i).getState() == player) {
                    for (int e = 0; e < 4; e++) {
                        if (!(gameBoard.getBlocks().get((j * Board.SIZE) + (i + e)).stateEquality(gameBoard.getBlocks().get((j * Board.SIZE) + (i + e + 1))))) {
                            break;
                        }
                        if (e == 3) {
                            return true;
                        }
                    }
                }
            }
        }
        //Vertical Check
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                if(gameBoard.getBlocks().get(j * Board.SIZE + i).getState() == player) {
                    for (int e = 0; e < 4; e++) {
                        if (!(gameBoard.getBlocks().get(((j + e) * Board.SIZE) + i).stateEquality(gameBoard.getBlocks().get(((j + e + 1) * Board.SIZE) + i)))) {
                            break;
                        }
                        if (e == 3) {
                            return true;
                        }
                    }
                }
            }
        }
        //Crisscross Check Up to Left
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if(gameBoard.getBlocks().get(j * Board.SIZE + i).getState() == player) {
                    for (int e = 0; e < 4; e++) {
                        if (!(gameBoard.getBlocks().get(((j + e) * Board.SIZE) + (i + e)).stateEquality((gameBoard.getBlocks().get(((j + e + 1)* Board.SIZE) + (i + e + 1)))))) {
                            break;
                        }
                        if (e == 3) {
                            return true;
                        }
                    }
                }
            }
        }
        //Crisscross Check Up to Right
        for (int i = Board.SIZE - 1; i > 3; i--) {
            for (int j = 0; j < 2; j++) {
                if(gameBoard.getBlocks().get(j * Board.SIZE + i).getState() == player) {
                    for (int e = 0; e < 4; e++) {
                        if (!(gameBoard.getBlocks().get(((j + e) * Board.SIZE) + (i + e)).stateEquality((gameBoard.getBlocks().get(((j + e + 1)* Board.SIZE) + (i - (e + 1))))))) {
                            break;
                        }
                        if (e == 3) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     *
     * @param time
     */
    public void saveStates(int time){
        for (Block block:gameBoard.getBlocks()) {
            statesList.get(time).add(block.getState());
        }
    }

    /**
     *
     *
     * @param time
     */
    public void loadStates (int time) {
        System.out.println("Hello " + time + " Size : " + statesList.get(time).size());
        for (int i = 0; i < statesList.get(time).size(); i ++) {
            gameBoard.getBlocks().get(i).colorBlock(statesList.get(time).get(i));
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

    /**
     * getter for all states in game
     *
     * @return statesList
     */
    public ArrayList<ArrayList<Integer>> getStatesList() {
        return statesList;
    }
}
