import java.io.*;
import java.util.*;

/**
 * Run of game includes menus
 *
 * @version 1.5
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
    //if Game will Save or not
    private static boolean saveGame;
    //If a game starts, you can resume it
    private static boolean resumeble;
    //If putting has done in a move in game
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
    //Current Slot To Save Game
    private static int currentSlotToSave;
    //Number of Slots with a game saved on it
    private static HashMap<Integer,String> slots;

    public static void main(String[] args) {
        scan = new Scanner(System.in);
        slots = new HashMap<Integer,String>();
        autoSave = true;
        load(true);
        mainMenu();
    }

    /**
     * Run of Pentago game
     *
     * a tip : when in-game menu is off ,you can press 0 for backing to main menu and 1 for undo instead of position to put
     */
    public static void playGame(){
        //if it is new game
        if(!resumeble) {
            putted = true;
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
                        System.out.println("Enter Your Name : ");
                        if(systemColor == 1) {
                            playerTwo = scan.next();
                            playerOne = "System";
                        }
                        else {
                            playerOne = scan.next();
                            playerTwo = "System";
                        }
                        break;
                    } catch (Exception e) {
                        scan.nextLine();
                        System.out.println("!!! WRONG INPUT !!!");
                    }
                }
            }
            else {
                System.out.println("Enter Red Player Name : ");
                playerOne = scan.next();
                System.out.println("Enter Black Player Name : ");
                playerTwo = scan.next();
            }
            //select who start game
            while (true) {
                System.out.println("*Select Who Start Game*");
                System.out.println("1)" + playerOne);
                System.out.println("2)" + playerTwo);
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
            putted = false;
            game.getGameBoard().print();
            if(!systemPlayer) {
                if (player == 1)
                    System.out.println(playerOne + "'s Turn");
                else
                    System.out.println(playerTwo + "'s Turn");
            }
            if(player == systemColor) {
                putted = true;
                game.playWithSystem(player);
            }
            else {
                if(systemPlayer)
                    System.out.println("Your Turn");
                if(saveGame && autoSave)
                    save();
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
                        putted = true;
                    }
                }
            }
            if(game.winConditions(1)) {
                System.out.println("!!! " + playerOne + " WINS !!!");
                break;
            }
            if(game.winConditions(2)) {
                System.out.println("!!!! " + playerTwo + " WINS !!!!");
                break;
            }
        }
        save();
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
        if (!autoSave) {
            System.out.println("3)Save");
            i++;
        }
        System.out.println("0)Main Menu");
        //get input for in-game choice
        while (true) {
            try {
                int choice = scan.nextInt();
                //for being out of menu
                if (choice < 0 || choice > (2 + i)) {
                    System.out.println("!!! It's Unavailable !!!");
                    continue;
                }
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
        choice = dynamicGameMenuShow();
        switch (choice) {
            case 1:
                putted = true;
                while (!game.putter(putInputGetter(), player)) ;
                game.getGameBoard().print();
                game.router(routeInputGetter());
                break;
            case 2:
                undo();
                break;
            case 3:
                time--;
                save();
                break;
            case 0:
                mainMenu();
                return;
        }
    }

    /**
     * Main Menu which you can :
     * Resume a started game
     * Start a new game with one or two players
     * Change options
     */
    public static void mainMenu() {
        while (true) {
            int choice = 0;
            System.out.println("***MAIN MENU***");
            System.out.println("1)Resume a Game");
            System.out.println("2)Single Player Game");
            System.out.println("3)Multi Player Game");
            System.out.println("4)Load a Game");
            System.out.println("5)Options");
            System.out.println("0)Exit");
            while (true) {
                try {
                    choice = scan.nextInt();
                    //for being out of menu
                    if (choice < 0 || choice > 5) {
                        System.out.println("!!! It's Unavailable !!!");
                        continue;
                    }
                    if (choice == 1 && !resumeble) {
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
                    saveSlotsMenu();
                    playGame();
                    break;
                case 3:
                    resumeble = false;
                    game = new Game();
                    saveSlotsMenu();
                    systemPlayer = false;
                    playGame();
                    break;
                case 4:
                    load(false);
                    break;
                case 5:
                    options();
                    break;
                case 0:
                    System.out.println("***Good Bye***");
                    System.out.println("***See You Next Time***");
                    System.out.println(":-)");
                    System.exit(0);
                    break;
            }
        }
    }

    /**
     * save menue if you want to save a new game
     */
    public static void saveSlotsMenu(){
        int choice = 0;
        System.out.println("Do you want to save this game ?");
        System.out.println("1)Yes");
        System.out.println("0)No");
        while (true) {
            try {
                choice = scan.nextInt();
                //for being out of menu
                if ((choice < 0 || choice > 1)) {
                    System.out.println("!!! It's Unavailable !!!");
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
                saveGame = true;
                break;
            case 0:
                saveGame = false;
                autoSave = false;
                return;
        }
        while (true) {
            System.out.println("***SAVE SLOTS***");
            for (int i = 1; i <= 10; i++) {
                System.out.print("Slot " + i + ") ");
                if(slots.containsKey(i)){
                    System.out.print(slots.get(i));
                }
                System.out.println();
            }
            while (true) {
                System.out.println("Enter Number of Slot You Want to Save : ");
                try {
                    choice = scan.nextInt();
                    //for being out of menu
                    if ((choice < 1 || choice > 10)) {
                        System.out.println("!!! It's Unavailable !!!");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    scan.nextLine();
                    System.out.println("!!! WRONG INPUT !!!");
                }
            }
            if(slots.containsKey(choice)) {
                System.out.println("It's Already Taken !");
                System.out.println("Do You Want to Rewrite ? 1)Yes 0)No");
                int select;
                while (true) {
                    try {
                        select = scan.nextInt();
                        //for being out of menu
                        if ((select < 0 || select > 1)) {
                            System.out.println("!!! It's Unavailable !!!");
                            continue;
                        }
                        break;
                    } catch (Exception e) {
                        scan.nextLine();
                        System.out.println("!!! WRONG INPUT !!!");
                    }
                }
                if (select == 1) {
                    System.out.println("Enter Name of Slot " + choice);
                    String name = scan.next();
                    slots.replace(choice, slots.get(choice), name);
                    currentSlotToSave = choice;
                    break;
                }
            }
            else{
                System.out.println("Enter Name of Slot " + choice);
                String name = scan.next();
                slots.put(choice,name);
                currentSlotToSave = choice;
                break;
            }
        }
    }

    /**
     * Delete a saved slot
     */
    public static void deleteSaveSlot() {
        int choice;
        while (true) {
            System.out.println("***SAVE SLOTS***");
            if(slots.size() == 0){
                System.out.println("There is no Saved Data");
                return;
            }
            for (int i = 1; i < 10; i++) {
                if(slots.containsKey(i)) {
                    System.out.print("Slot " + i + ") ");
                    System.out.println(slots.get(i));
                }
            }
            System.out.println("Enter Number of Slot You Want to Delete : ");
            try {
                choice = scan.nextInt();
                //for being out of menu
                if (!slots.containsKey(choice)) {
                    System.out.println("!!! It's Unavailable !!!");
                    continue;
                }
                slots.remove(choice);
                currentSlotToSave = 0;
                save();
                break;
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("!!! WRONG INPUT !!!");
            }
        }
    }

    /**
     * Options Menu which you can :
     * Turn On or Off Auto Save
     * Turn On or Off In-Game Menu
     * Remove a save slot
     * Remove History
     *
     */
    public static void options(){
        int choice = 0;
        while (true) {
            System.out.println("***OPTIONS***");
            System.out.println("1)Auto Save On");
            System.out.println("-1)Auto Save Off");
            System.out.println("2)In-Game Menu On");
            System.out.println("-2)In-Game Menu Off");
            System.out.println("3)Change Players Name");
            System.out.println("4)Remove a Save Slot");
            System.out.println("5)Credits");
            System.out.println("0)Back to Main Menu");
            while (true) {
                try {
                    choice = scan.nextInt();
                    //for being out of menu
                    if ((choice < -2 || choice > 5)) {
                        System.out.println("!!! It's Unavailable !!!");
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
                    autoSave = true;
                    break;
                case -1:
                    autoSave = false;
                    break;
                case 2:
                    inGameMenu = true;
                    break;
                case -2:
                    inGameMenu = false;
                    break;
                case 3:
                    System.out.println("Enter Red Player Name : ");
                    playerOne = scan.next();
                    System.out.println("Enter Black Player Name : ");
                    playerTwo = scan.next();
                    break;
                case 4:
                    deleteSaveSlot();
                    break;
                case 5:
                    System.out.println("***Credits***");
                    System.out.println("** Developer : Mostafa Bijani **");
                    System.out.println("** Tester : Mostafa Bijani **");
                    System.out.println("** Influencer : Mostafa Bijani **");
                    System.out.println("** Special Thanks : Mr.Zeinali & Mostafa Bijani **");
                    System.out.println("** (:-D) **");
                    System.out.println();
                    break;
                case 0:
                    return;
            }
        }
    }

    /**
     * Undo a move in game
     */
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
     * save all data in a game in 10 different slot include options setting and undo ability.
     */
    public static void save(){
        //save slots
        try(FileWriter fw = new FileWriter("saves\\" + "slots" + ".txt", false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(slots.size());
            for (int slot : slots.keySet()) {
                out.println(slot);
                out.println(slots.get(slot));
            }
        } catch (IOException e) {
            System.out.println("An Error Occurred.");
        }
        //
        try(FileWriter fw = new FileWriter("saves\\" + slots.get(currentSlotToSave) + ".data", false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(playerOne);
            out.println(playerTwo);
            out.println(turn);
            out.println(time);
            out.println(systemColor);
            out.println(resumeble);
            out.println(putted);
            out.println(redoable);
            out.println(systemPlayer);
            out.println(autoSave);
            out.println(inGameMenu);
            for (int i = 0; i < 36; i++) {
                for (int state : game.getStatesList().get(i)) {
                    out.println(state);
                }
            }
            out.close();
        } catch (IOException e) {
            System.out.println("An Error Occurred.");
        }
    }

    /**
     * load all data from a .data file (it is a text file)
     *
     * @param start if it's very first load
     */
    public static void load(boolean start){
        int choice;
        Scanner read;
        try{
            read = new Scanner(new File("saves\\" + "slots" + ".txt"));
            int numberOfSlots = read.nextInt();
            for (int i = 0; i < numberOfSlots; i++) {
                slots.put(read.nextInt(), read.next());
            }
            read.close();
            if(start)
                return;
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't Read File Correctly");
        }
        while (true) {
            System.out.println("***SAVE SLOTS***");
            if(slots.size() == 0){
                System.out.println("There is no Saved Data");
                return;
            }
            for (int i = 1; i < 10; i++) {
                if(slots.containsKey(i)) {
                    System.out.print("Slot " + i + ") ");
                    System.out.println(slots.get(i));
                }
            }
            System.out.println("Enter Number of Slot You Want to Load : ");
            try {
                choice = scan.nextInt();
                //for being out of menu
                if (!slots.containsKey(choice)) {
                    System.out.println("!!! It's Unavailable !!!");
                    continue;
                }
                currentSlotToSave = choice;
                break;
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("!!! WRONG INPUT !!!");
            }
        }
        try{
            read = new Scanner(new File("saves\\" + slots.get(choice) + ".data"));
            game = new Game();
            playerOne = read.next();
            playerTwo = read.next();
            turn = read.nextInt();
            time = read.nextInt();
            systemColor = read.nextInt();
            resumeble = read.nextBoolean();
            putted = read.nextBoolean();
            redoable = read.nextBoolean();
            systemPlayer= read.nextBoolean();
            autoSave = read.nextBoolean();
            inGameMenu = read.nextBoolean();
            for (int i = 0; read.hasNextInt(); i++) {
                for (int j = 0; j < 36; j++) {
                    game.getStatesList().get(i).add(j, read.nextInt());
                }
            }
            currentSlotToSave = choice;
            game.loadStates(time);
            read.close();
            System.out.println("Load Completes Successfully");
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (Exception e) {
            System.out.println("Can't Read File Correctly");
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
                scan.nextLine();
                System.out.println("!!! WRONG INPUT !!!");
                continue;
            }
            System.out.print("Enter Direction : ");
            try {
                dir = scan.next().charAt(0);
                if(checkRotateDirectionInput(dir))
                    break;
            }
            catch (Exception e){
                scan.nextLine();
                System.out.println("!!! WRONG INPUT !!!");
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
                scan.nextLine();
                System.out.println("!!! WRONG INPUT!!!");
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
