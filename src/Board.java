import java.util.ArrayList;

/**
 * Board class is for the board of Pentago game.
 *
 * @version 0
 * @author Mostafa_BJN
 */
public class Board {
    private static final String UP_LEFT_CORNER = "╭";
    private static final String DOWN_LEFT_CORNER = "╰";
    private static final String UP_RIGHT_CORNER = "╮";
    private static final String DOWN_RIGHT_CORNER = "╯";
    private static final String HORIZONTAL_LINE = "─";
    private static final String VERTICAL_LINE = "│";
    //number of Blocks in row of board
    static final int SIZE = 6;
    //number of squares of board
    static final int NUMBER_OF_SQUARE = 4;
    //number of Blocks in row of square of board
    static final int SIZE_OF_SQUARE = 3;
    //blocks of board separated by squares : NUMBER_OF_SQUARE * SIZE_OF_SQUARE * SIZE_OF_SQUARE
    private ArrayList<ArrayList<Block>> squaredBlocks;
    //blocks of board : SIZE * SIZE
    private ArrayList<Block> blocks;

    /**
     * Making a new Board for Game.
     */
    public Board(){
        blocks = new ArrayList<Block>();
        squaredBlocks = new ArrayList<ArrayList<Block>>();
        for (int i = 0; i < 4; i++) {
            squaredBlocks.add(new ArrayList<Block>());
        }
        emptyBoardMaker();
    }

    /**
     * Make a board with SIZE*SIZE Blocks
     * Make an exact board with NUMBER_OF_SQUARE*SIZE_OF_SQUARE*SIZE_OF_SQUARE
     */
    private void emptyBoardMaker(){
        for (int j = 1; j < SIZE + 1; j++) {
            for (int i = 1; i < SIZE + 1; i++) {
                blocks.add(new Block(i, j));
            }
        }
        for (int s = 0; s < NUMBER_OF_SQUARE; s++) {
            for (int j = 0; j < SIZE_OF_SQUARE ; j++) {
                for (int i = 0; i < SIZE_OF_SQUARE ; i++) {
                    Block block = blocks.get(i + (j * (SIZE)) + ((s % 2) * (SIZE_OF_SQUARE)) + ((s / 2) * (SIZE_OF_SQUARE * SIZE)));
                    block.setSquareNum(s+1);
                    squaredBlocks.get(s).add(block);
                }
            }
        }
    }

    /**
     * getter for blocks arraylist.
     */
    public ArrayList<ArrayList<Block>> getSquaredBlocks() {
        return squaredBlocks;
    }

    /**
     * getter for blocks arraylist.
     */
    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    /**
     * Show board to players.
     */
    public void print(){
        System.out.print("\u001B[34m");
        for (int j = 0; j < SIZE ; j++) {
            if(j==0){
                System.out.println("\r   1▪▩▪2▪▩▪3▪▪▩▪▪4▪▩▪5▪▩▪6");
            }
            if(j==3){
                System.out.print('\u258f' + "   ");
                for (int k = 0; k < 25; k++) {
                    if(k==12)
                        System.out.print("┼");
                    System.out.print(HORIZONTAL_LINE);
                }
                System.out.println();
            }
            for (int i = 0; i < SIZE ; i++) {
                if(i==0)
                    System.out.print((j + 1) + "  ");
                if(i==3)
                    System.out.print("  " + VERTICAL_LINE + "  ");
                System.out.print(blocks.get(j*SIZE+i).getSign());
                if(i!=2)
                    System.out.print("   ");
            }
            System.out.println();
            if(j!=2 && j!=SIZE-1)
                System.out.println('▏' + "               " + VERTICAL_LINE);
        }
        System.out.println("\u001B[0m");
    }
}
