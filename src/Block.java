import java.util.Objects;

/**
 * This Class is for each Block in the Board
 *
 * @version 0.3
 * @author Mostafa_BJN
 */
public class Block {
    private static final String BLACK_CIRCLE = "\u001B[30m" + "◉" + "\u001B[34m";
    private static final String RED_CIRCLE = "\u001B[31m" + "◉" + "\u001B[34m";
    private static final String EMPTY_BLOCK = "\u001B[35m" + "▢" + "\u001B[34m";
    //Sign which shows in Board
    private String sign;
    //Horizontal coordinates : 1,2,3,4,5,6
    private int horizonNum;
    //Vertical coordinates : 1,2,3,4,5,6
    private int verticalNum;
    //square placed : 1,2,3,4
    private int squareNum;
    //State of block : Empty = 0 , Red = 1 , Black = 2
    private int state;

    /**
     * Make a new empty Block with given coordinates
     *
     * @param horizonNum Horizontal coordinates
     * @param verticalNum Vertical coordinates
     */
    public Block(int horizonNum, int verticalNum) {
        sign = EMPTY_BLOCK;
        this.verticalNum = verticalNum;
        this.horizonNum = horizonNum;
        state = 0;
        squareNum = 0;
    }

    /**
     * Make a block colored
     *
     * @param player the player which block fulled with its color
     */
    public void colorBlock(int player){
        state = player;
        if(player == 0) {
            sign = EMPTY_BLOCK;
        }
        if(player == 1) {
            sign = RED_CIRCLE;
        }
        else if(player == 2) {
            sign = BLACK_CIRCLE;
        }
    }

    /**
     * Change Coordinates of Block
     *
     * @param horizonNum new Horizontal Coordinate
     * @param verticalNum new Vertical Coordinate
     */
    public void changeCoordinates(int horizonNum, int verticalNum){
        this.horizonNum = horizonNum;
        this.verticalNum = verticalNum;
    }

    /**
     * get a block and check state equality
     *
     * @param block block to check
     * @return equality of states
     */
    public boolean stateEquality(Block block){
        return block.getState() == state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block block = (Block) o;
        return horizonNum == block.horizonNum &&
                verticalNum == block.verticalNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(horizonNum, verticalNum);
    }

    /**
     * getter for sign
     *
     * @return sign
     */
    public String getSign() {
        return sign;
    }

    /**
     * getter for horizonNum
     *
     * @return Horizontal Coordinates
     */
    public int getHorizonNum() {
        return horizonNum;
    }

    /**
     * getter for verticalNum
     *
     * @return Vertical Coordinates
     */
    public int getVerticalNum() {
        return verticalNum;
    }

    /**
     * getter for squareNum
     *
     * @return square of Block
     */
    public int getSquareNum() {
        return squareNum;
    }

    /**
     * getter for state
     *
     * @return state of Block
     */
    public int getState() {
        return state;
    }

    /**
     * setter for squareNum
     *
     * @param squareNum Number of Square of Block
     */
    public void setSquareNum(int squareNum) {
        this.squareNum = squareNum;
    }
}
