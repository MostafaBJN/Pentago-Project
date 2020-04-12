import java.io.*;
import java.util.*;

/**
 * Run of game includes menus
 *
 * @version 0
 * @author Mostafa_BJN
 */
public class Run {
    //Scanner for getting input
    private static Scanner scan;
    //Pentago game
    private static Game game;
    //player One's name
    private static String playerOne;
    //player Two's name
    private static String playerTwo;
    //player which starts the game , 0 = Red , 1 = White
    private static int turn;
    //time of game
    private static int time;
    //If a game starts, you can resume it
    private static boolean resumeble;
    //If undo in game
    private static boolean undoed;
    private static boolean putted;
    //If redo is allow in game
    private static boolean redoable;
    //AI, On or Off
    private static boolean systemPlayer;
    //AI Color if Turned On
    private static int systemColor;
    //Auto Save
    private static boolean autoSave;
    //In game menu On or Off
    private static boolean inGameMenu;
    private static Scanner read;
    private static File saveData;
    //Number of Slots with a game saved on it
    private static HashMap<Integer,String> saveSlots;


    public static void main(String[] args) {
        scan = new Scanner(System.in);
        saveSlots = new HashMap<Integer,String>();
        autoSave = true;
        inGameMenu = false;
        mainMenu();

    }

    public static void undo() {
        putted = false;
        time--;
        if (time == -1) {
            System.out.println("You Can't Undo in This Move");
        } else {
            game.loadStates(time);
            time--;
        }
    }


    /**
     * Run of Reversi game
     */
    public static void playGame(){
        //if it is new game
        if(!resumeble) {
            turn = 0;
            systemColor = 0;
            resumeble = true;
            //select system color
            if (systemPlayer) {
                while (true) {
                    System.out.println("*Select Your Color*");
                    System.out.println("1)Red");
                    System.out.println("2)Black");
                    try {
                        systemColor = scan.nextInt();
                        //for being out of menu
                        if (systemColor != 2 && systemColor != 1) {
                            System.out.println("!!! It's Unavailable !!!");
                            continue;
                        }
                        systemColor = 3 - systemColor;
                        break;
                    } catch (Exception e) {
                        scan.nextLine();
                        System.out.println("!!! WRONG INPUT !!!");
                    }
                }
            }
            //select who start game
            while (true) {
                System.out.println("*Select Who Start Game*");
                System.out.println("1)Red");
                System.out.println("2)Black");
                try {
                    turn = scan.nextInt();
                    //for being out of menu
                    if (turn != 2 && turn != 1) {
                        System.out.println("!!! It's Unavailable !!!");
                        continue;
                    }
                    turn--;
                    break;
                } catch (Exception e) {
                    scan.nextLine();
                    System.out.println("!!! WRONG INPUT !!!");
                }
            }
            time = 0;
        }
        for (; time < 36; time++) {
            int player = ((time + turn) % 2) + 1;
            if(putted) {
                game.saveStates(time);
            }
            game.getGameBoard().print();
            if(!systemPlayer) {
                if (player == 1)
                    System.out.println("Red Turn");
                else
                    System.out.println("Black Turn");
            }
            if(player == systemColor) {
                //game.putter(game.playWithSystem(), player);
                break;///////delete
            }
            else {
                if(systemPlayer)
                    System.out.println("Your Turn");
                if(inGameMenu)
                    gameMenu(player);
                else {
                    Block block;
                    do {
                        block = putInputGetter();
                        if(block == null){
                            break;
                        }
                    } while (!game.putter(block, player));
                    if(block != null) {
                        game.getGameBoard().print();
                        game.router(routeInputGetter());
                    }
                }
            }

            if(game.winConditions(1)) {
                System.out.println("!!! RED WINS !!!");
                break;
            }
            if(game.winConditions(2)) {
                System.out.println("!!!! BLACK WINS !!!!");
                break;
            }
        }
        resumeble = false;
        mainMenu();
    }

    /**
     * Showing in-Game menu dynamical
     */
    private static int dynamicGameMenuShow(){
        int i = 0;
        System.out.println("1)Put");
        System.out.println("2)Undo");
//        if (!autoScoreShow) {
//            System.out.println((2 + i) + ")Show Score");
//            i++;
//        }
        if (!autoSave) {
            System.out.println((2 + i) + ")Save");
            i++;
        }
        System.out.println((2 + i) + ")Main Menu");
        //get input for in-game choice
        while (true) {
            try {
                int choice = scan.nextInt();
                //for being out of menu
                if (choice < 1 || choice > (2 + i)) {
                    System.out.println("!!! It's Unavailable !!!");
                    continue;
                }
                //for Main Menu
                if (choice > (1 + i))
                    choice = 4;
                    //for Save
                else if (!autoSave && (choice == 1 + i))
                    choice = 3;
                return choice;
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("!!! WRONG INPUT !!!");
            }
        }
    }

    /**
     * In-Game Menu which you can :
     * Put
     * Show Score, If auto Show is off
     * Save, If auto Save is off
     * Back to the Main Menu
     *
     * @param player player who its turn
     */
    public static void gameMenu(int player) {
        int choice = 0;
        while (choice != 1) {
            choice = dynamicGameMenuShow();
            switch (choice) {
                case 1:
                    while (!game.putter(putInputGetter(), player));
                    game.getGameBoard().print();
                    game.router(routeInputGetter());
                    break;
                case 2:
                    undo();
                    break;
                case 3:
                    save();
                    break;
                case 4:
                    mainMenu();
                    return;
            }
        }
    }

    /**
     * Main Menu which you can :
     * Resume a started game
     * Start a new game with one or two players
     * Change options
     */
    public static void mainMenu() {
        int choice = 0;
        System.out.println("***MAIN MENU***");
        System.out.println("1)Resume a Game");
        System.out.println("2)Single Player Game");
        System.out.println("3)Multi Player Game");
        System.out.println("4)Load a Game");
        System.out.println("5)History of Games");
        System.out.println("6)Options");
        System.out.println("0)Exit");
        while (true) {
            try {
                choice = scan.nextInt();
                //for being out of menu
                if (choice < 0 || choice > 6) {
                    System.out.println("!!! It's Unavailable !!!");
                    continue;
                }
                if(choice == 1 && !resumeble){
                    System.out.println("You Haven't Started a Game Yet !");
                    continue;
                }
                break;
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("!!! WRONG INPUT !!!");
            }
        }
        switch (choice) {
            case 1:
                playGame();
                break;
            case 2:
                resumeble = false;
                game = new Game();
                systemPlayer = true;
                playGame();
                break;
            case 3:
                resumeble = false;
                game = new Game();
                systemPlayer = false;
                playGame();
                break;
            case 4:
                load();
                break;
            case 5:
                gamesHistory();
                break;
            case 5:
                options();
                break;
            case 0:
                System.out.println("***Good Bye***");
                System.out.println(":-)");
                System.exit(0);
                break;
        }
    }

    /**
     * Options Menu which you can turn On or Off :
     * Auto Save
     * Auto Score Show
     * Guide for Putting
     * In-Game Menu
     */
    public static void options(){
        int choice = 0;
        while (true) {
            System.out.println("***OPTIONS***");
            System.out.println("1)Auto Save On");
            System.out.println("-1)Auto Save Off");
            System.out.println("2)Auto Score Show On");
            System.out.println("-2)Auto Score Show Off");
            System.out.println("3)Guide for Putting On");
            System.out.println("-3)Guide for Putting Off");
            System.out.println("4)In-Game Menu On");
            System.out.println("-4)In-Game Menu Off");
            System.out.println("0)Back to Main Menu");
            while (true) {
                try {
                    choice = scan.nextInt();
                    //for being out of menu
                    if ((choice < -5 || choice > 5)) {
                        System.out.println("!!! It's Unavailable !!!");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("!!! WRONG INPUT !!!");
                }
            }
            switch (choice) {
                case 1:
                    System.out.println("This Feature is Unavailable Now");
                    autoSave = true;
                    break;
                case -1:
                    System.out.println("This Feature is Unavailable Now");
                    autoSave = false;
                    break;
                case 4:
                    inGameMenu = true;
                    break;
                case -4:
                    inGameMenu = false;
                    break;
                case 0:
                    mainMenu();
                    return;
            }
        }
    }



    public static void redo(){

    }


    public static void save(int currentSlot){
        try(FileWriter fw = new FileWriter(saveSlots.get(currentSlot) + ".data", false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(saveSlots.size());
            for (int slot : saveSlots.keySet()) {
                out.println(slot);
                out.println(saveSlots.get(slot));
            }
            out.println(turn);
            out.println(time);
            out.println(systemColor);
            out.println(resumeble);
            out.println(undoed);
            out.println(redoable);
            out.println(systemPlayer);
            out.println(autoSave);
            out.println(inGameMenu);
            for (int i = 0; i < 36; i++) {
                game.loadStates(i);
                for (int state : game.getStatesList().get(i)) {
                    out.println(state);
                }
            }
            out.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
        System.out.println("Save Completes Successfully");
    }


    public static void load(){
        int choice;
        System.out.println("*Select a Slot to Load*");
        for (int slot : saveSlots.keySet()) {
            saveSlots.get(slot);
            System.out.println("Slot Number " + slot + ": "/* + name*/);
        }
        while (true) {
            try {
                choice = scan.nextInt();
                //for being out of menu
                if (!saveSlots.containsKey(choice)) {
                    System.out.println("!!! It's Unavailable !!!");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("!!! WRONG INPUT !!!");
            }
        }
        try{
            saveData = new File(saveSlots.get(choice) + ".data");
            read = new Scanner(saveData);
            game = new Game();
            turn = read.nextInt();
            time = read.nextInt();
            systemColor = read.nextInt();
            resumeble = read.nextBoolean();
            undoed = read.nextBoolean();
            redoable = read.nextBoolean();
            systemPlayer= read.nextBoolean();
            autoSave = read.nextBoolean();
            inGameMenu = read.nextBoolean();
            for (int i = 0; read.hasNextInt(); i++) {
                for (int j = 0; j < 36; j++) {
                    game.getStatesList().get(i).add(j, read.nextInt());
                }
            }
            read.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        } catch (Exception e){
            System.out.println("An erro d.");
        }
        System.out.println("Load Completes Successfully");
    }


    public static void gamesHistory(){

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
                //when ingame-menu is off , won't return null
                if(!inGameMenu){
                    if(redoable && n == 2){
                        redo();
                        return null;
                    }
                    switch (n){
                        case 1:
                            undo();
                            return null;
                        case 0:
                            mainMenu();
                            return null;
                    }
                }
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
