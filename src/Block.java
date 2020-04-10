import java.util.Objects;

/**
 * This Class is for each Block in the Board
 *
 * @version 0.2
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
    //State of block : Empty = 0 , White = 1 , Black = 2
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
        if(player == 1) {
            sign = RED_CIRCLE;
        }
        else if(player == 2) {
            sign = BLACK_CIRCLE;
        }
    }

    public void changeCoordinates(int horizonNum, int verticalNum){
        this.horizonNum = horizonNum;
        this.verticalNum = verticalNum;
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

    public String getSign() {
        return sign;
    }

    public int getHorizonNum() {
        return horizonNum;
    }

    public int getVerticalNum() {
        return verticalNum;
    }

    public int getSquareNum() {
        return squareNum;
    }

    public int getState() {
        return state;
    }

    public void setSquareNum(int squareNum) {
        this.squareNum = squareNum;
    }
}
