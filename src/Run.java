import java.util.Scanner;

/**
 * Run of game includes menus
 *
 * @version 0
 * @author Mostafa_BJN
 */
public class Run {
    //Scanner for getting input
    private static Scanner scan = new Scanner(System.in);
    //Pentago game
    private static Game game;

    public static void main(String[] args) {
        /*for (Block block:board.getBlocks()){
            System.out.println(block.getSquareNum());
        }*/
        game = new Game();
        for (int i = 0; i < 36; i++) {
            int player = i%2 + 1;
            game.getGameBoard().print();
            while (!game.putter(putInputGetter(), player));
            //game.getGameBoard().print();
            //game.router(routeInputGetter());
            if(game.winConditions(player)) {
                System.out.println("!!!!WIN!!!!");
                break;
            }
        }

    }

    /**
     * Get Input for routing
     *
     * @return Square with Direction to route
     */
    public static int routeInputGetter() {
        int s = 0;
        char dir;
        while(true) {//getting input
            System.out.print("Enter Square : ");
            try {
                s = scan.nextInt();
                if(!checkRouteInput(s))
                    continue;
            }
            catch (Exception e){
                System.out.println("!!! WRONG INPUT !!!");
                scan.nextLine();
                continue;
            }
            System.out.print("Enter Direction : ");
            try {
                dir = scan.next().charAt(0);
                if(checkRotateDirectionInput(dir))
                    break;
            }
            catch (Exception e){
                System.out.println("!!! WRONG INPUT !!!");
                scan.nextLine();
            }
        }
        if(dir == '-')
            return -s;
        else
            return s;
    }

    /**
     * Get Input for Putting
     *
     * @return Bock to put
     */
    public static Block putInputGetter() {
        int v = 0;
        int h = 0;
        while(true) {//getting input
            System.out.print("Enter Position : ");
            try {
                int n = scan.nextInt();
                h = n/10;
                v = n%10;
                if(checkPutInput(h, v))
                    break;
            }
            catch (Exception e){
                System.out.println("!!! WRONG INPUT !!!");
                scan.nextLine();
            }
        }
        return new Block(h, v);
    }

    /**
     * Check is Route Input Correct
     *
     * @param s square of board
     * @return is input correct
     */
    private static boolean checkRouteInput(int s) {
        if (s > 0 && s < 5)
            return true;
        else
        {
            System.out.println("!!! There is Only 4 Squares !!!");
            return false;
        }
    }

    /**
     * Check is Route Input Correct
     *
     * @param dir Direction to Route
     * @return is input correct
     */
    private static boolean checkRotateDirectionInput(char dir) {
        if (dir == '+' || dir == '-')
            return true;
        else
        {
            System.out.println("!!! Directions are +/- !!!");
            return false;
        }

    }

    /**
     * Check is Putting Inputs Correct
     *
     * @param h Horizontal Coordinates
     * @param v Vertical Coordinates
     * @return is input correct
     */
    private static boolean checkPutInput(int h, int v) {
        if ((h > 0 && h < 7) && (v > 0 && v < 7))
            return true;
        else
        {
            System.out.println("!!! Your Input is Invalid !!!");
            return false;
        }
    }
}
