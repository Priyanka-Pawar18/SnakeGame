import java.awt.*;         //used to create graphical interface objects
import java.awt.event.*;     //defines classes and interfaces used for event handling like KeyEvent,KeyListener
import javax.swing.*;        //provides interfaces, so that the program works the same on all platforms
import java.util.*;           // contains collection framework, collection classes,
 

public class Snake {                //class snake defining the attributes of snake, board
    // GUI components
    private JPanel board;
    private JButton[] snakeBodyPart;
    private JButton bonusfood;
    private JTextArea scoreViewer;

    // Constants
    private final int SNAKE_RUNNING_SPEED_FASTEST = 25;       //for level 3
    private final int SNAKE_RUNNING_SPEED_FASTER = 50;        //level 2
    private final int SNAKE_RUNNING_SPEED_FAST = 100;          //level 1
    private final int BOARD_WIDTH = 700;
    private final int BOARD_HEIGHT = 550;
    private final int SCORE_BOARD_HEIGHT = 20;
    private final int SNAKE_LENGTH_DEFAULT = 4;
    private final int SNAKE_BODY_PART_SQURE = 10;     //size of the body of snake according to the grid
    private final int BONUS_FOOD_SQURE = 15;         //size of the bonus food according to the grid, bonus food will appear bigger than snake
    private final Point INIT_POINT = new Point(100, 150);         //when the game begins, snake begins to move from coordinates x=100,y=150

    // Others values
    private enum GAME_TYPE {NO_MAZE, BORDER, TUNNEL};        //game type is the class passing methods no maze, border, tunnel
    private int selectedSpeed = SNAKE_RUNNING_SPEED_FAST;      //level 2
    private GAME_TYPE selectedGameType = GAME_TYPE.NO_MAZE;       //selecting type of game, that is no maze
    private int totalBodyPart;      //variable to calculate to growth of snake
    private int directionX;          //variable for direction left 
    private int directionY;          //variable for direction right
    private int score;               //variable for counting score
    private Point pointOfBonusFood = new Point();     //point where bonus food will be generated
    private boolean isRunningLeft;              //variable of direction of boolean type
    private boolean isRunningRight;  
    private boolean isRunningUp;
    private boolean isRunningDown;
    private boolean isBonusFoodAvailable;
    private boolean isRunning;
    private Random random = new Random();         //creating new object of random

    Snake() {         //constructor
        //initialize all variables
        resetDefaultValues();            //class containing default startup of game
        // initialize GUI
        init();                         //class in GUI 
        // Create Initial body of a snake. 
        createInitSnake();                 //class with default snake body
        // Initialize Thread
        isRunning = true;                 //assigning "true" value to isRunning
        createThread();                    //create thread class is called
    }

    public void init() {
        JFrame frame = new JFrame("Snake");      //naming the Jframe that is the layout as snake
        frame.setSize(700, 625);                  //setting size of frame ; the size of Jframe should be equal to JPanel board

        //Create Menu bar with functions
        setJMenueBar(frame);      //frame is passed as parameter
        // Start of UI design
        JPanel scorePanel = new JPanel();            //creating Panel that is the area containing componenets
        scoreViewer = new JTextArea("Score ==>" + score);           //object scoreViewer is declared as TextArea since it will display only score
        scoreViewer.setEnabled(false);      //setEnabled is kept false so that score cannot be modified by user
        scoreViewer.setBackground(Color.BLACK);            //setting textArea scoreViewer to black colour

        board = new JPanel();        //creating board object of JPanel type for movement of snake
        board.setLayout(null);      //using null layout of JPanel to track the cells in form of x,y coordinates
        board.setBounds(0, 0, BOARD_WIDTH, BOARD_HEIGHT);    //used in a situation to set the position and size 
        board.setBackground(Color.WHITE);      //setting background colour as white
        scorePanel.setLayout(new GridLayout(0, 1));       //creates a grid layout with specified number of rows and columns
        scorePanel.setBounds(0, BOARD_HEIGHT, BOARD_WIDTH, SCORE_BOARD_HEIGHT);       //setting size of the following wrt JPanel as a whole
        scorePanel.add(scoreViewer);     // will contain score board, will add the updated score

        frame.getContentPane().setLayout(null);     //used to hold objects that were defined before
        frame.getContentPane().add(board);           //board objects are held
        frame.getContentPane().add(scorePanel);      //scorePanel objects are held
        frame.setVisible(true);                      //setVisible method is set to true so that frame is visible to user
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     //method set to close the JFrame when user clicks on exit
        frame.addKeyListener(new KeyAdapter() {     //using KeyListener with its adapter class
            //overriding 
            public void keyPressed(KeyEvent e) {       //e is an event of Key for reading key pressed by user for snake's movement
                snakeKeyPressed(e);    //snakeKeyPressed is a class with parameter e passed , that is its movement
            }
        });
        frame.setResizable(false);     //size of frame cannot be changed
    }

    public void setJMenueBar(JFrame frame) {    //class setMenueBar used to set the dropdown list(selecting levels) under snake title

        JMenuBar mymbar = new JMenuBar();    //created menu bar named mymbar

        JMenu game = new JMenu("Game");         //creating a menu Game under which we have options new game,exit
        JMenuItem newgame = new JMenuItem("New Game");     //under game
        JMenuItem exit = new JMenuItem("Exit");            //under game
        newgame.addActionListener(new ActionListener() {     //creating actionListener to record the response of user,what component the user has clicked
            public void actionPerformed(ActionEvent e) {       //changes made in the game after user's response is recorded using event e
                startNewGame();                //class startNewGame called by default 
            }
        });
        exit.addActionListener(new ActionListener() {      //actionListener for recording user's response is exit
            public void actionPerformed(ActionEvent e) {    //changes made in game to exit the game
                System.exit(0);              //syntax to exit program
            }
        });
        game.add(newgame);   //drop down option under game
        game.addSeparator();   //a JSwing framework, here used to divide new game and exit components
        game.add(exit);      //dropdown option exit under game
        mymbar.add(game);    //game option will be displayed on menu bar

        JMenu type = new JMenu("Type");      //creating a menu "Type" in menu bar
        JMenuItem noMaze = new JMenuItem("No Maze");      //no maze option under Type
        noMaze.addActionListener(new ActionListener() {    //ActionListener when user selects no maze option
            public void actionPerformed(ActionEvent e) {    //through event e changes will happen in game when user selects no maze option
                selectedGameType = GAME_TYPE.NO_MAZE;       //in selectedgametype method ,game type.no 
                startNewGame();   //when user selects no maze, again startNewGame class is called
            }
        });
        JMenuItem border = new JMenuItem("Border Maze");       //creating option border maze under "Type"
        border.addActionListener(new ActionListener() {      //actionListener to record response when user selects border maze
            public void actionPerformed(ActionEvent e) {     //through event e changes will happen in game when user selects border maze
                selectedGameType = GAME_TYPE.BORDER;        //selectedgametype method will 
                startNewGame();            //startNewGame class called after user selects border maze
            }
        });
        type.add(noMaze);       //creating an item noMaze under Type
        type.add(border);      //creating an item border under Type
        mymbar.add(type);      //Type option will be displayed on menu bar

        JMenu level = new JMenu("Level");   //menu called Level created on menu bar
        JMenuItem level1 = new JMenuItem("Level 1");      //level 1 option under Level created
        level1.addActionListener(new ActionListener() {      //ActionListener when user response is level1
            public void actionPerformed(ActionEvent e) {       
                selectedSpeed = SNAKE_RUNNING_SPEED_FAST;    //assigning variable SNAKE_RUNNING_SPEED_FAST=100 to selectedSpeed
                startNewGame();                     
            }
        });
        JMenuItem level2 = new JMenuItem("Level 2");       //level 2 option created in dropdown of Level
        level2.addActionListener(new ActionListener() {     //ActionListener when user response is level2
            public void actionPerformed(ActionEvent e) {     
                selectedSpeed = SNAKE_RUNNING_SPEED_FASTER ;     //assigning variable SNAKE_RUNNING_SPEED_FASTER=50 to selected speed
                startNewGame();
            }
        });
        JMenuItem level3 = new JMenuItem("Level 3");      //level 3 option created in dropdown of Level
        level3.addActionListener(new ActionListener() {     //ActionListener when user response is level3
            public void actionPerformed(ActionEvent e) {
                selectedSpeed = SNAKE_RUNNING_SPEED_FASTEST;    //assigning variable SNAKE_RUNNING_SPEED_FASTEST=25 to selected speed
                startNewGame();
            }
        });
        level.add(level1);        //creating level1 option under level
        level.add(level2);         //creating level 2 option under level
        level.add(level3);        //creating level 3 option under level
        mymbar.add(level);        //creating menu level on menu bar

       

        frame.setJMenuBar(mymbar);      //for displaying menu bar
    }

    public void resetDefaultValues() {       //default setup when game is started
        snakeBodyPart = new JButton[2000];    //DOUBT, WHY 2000
        totalBodyPart = SNAKE_LENGTH_DEFAULT;      //variable SNAKE_LENGTH_DEFAULT=4 is assigned to totalBodyPart, snake body initially
        directionX = SNAKE_BODY_PART_SQURE;        //variable SNAKE_BODY_PART_SQURE=10 assigned to directionX, DOUBT
        directionY = 0;       //doubt
        score = 0;                                //by default initially
        isRunningLeft = false;        //running left set to false because snake cant go backwards  
        isRunningRight = true;         //snake can go right
        isRunningUp = true;            //snake can go up
        isRunningDown = true;          //snake can go down
        isBonusFoodAvailable = false;        //initially no bonus food will be given
    }

    void startNewGame() {      //class for new game
        resetDefaultValues();      //restDefaultValues class is called within startNewGame class
        board.removeAll();         //food and snake present on screen will be removed, board will be cleared
        createInitSnake();         //snake of default size will appear on screen since its a new game
        scoreViewer.setText("Score==>" + score);      //scoreViewer object is used to display score using setText method using score variable
        isRunning = true;          //as soon as the game window opens, snakes starts moving by default
    }

    /**
     * This method is responsible to initialize the snake with four body part.
     */
    public void createInitSnake() {        
        // Location of the snake's head.
        int x = (int) INIT_POINT.getX();             //DOUBT
        int y = (int) INIT_POINT.getY();             //DOUBT

        // Initially the snake has three body parts
        for (int i = 0; i < totalBodyPart; i++) {
            snakeBodyPart[i] = new JButton();            
            snakeBodyPart[i].setBounds(x, y, SNAKE_BODY_PART_SQURE, SNAKE_BODY_PART_SQURE);
            snakeBodyPart[i].setBackground(Color.GRAY);         //setting snake colour to gray
            board.add(snakeBodyPart[i]);
            // Set location of the next body part of the snake.
            x = x - SNAKE_BODY_PART_SQURE;
        }

        // Create food.
        createFood();
    }

    /**
     * This method is responsible to create food of a snake.
     * The last part of this snake is treated as a food, which has not become a body part of the snake yet.
     * This food will be the body part if and only if when snake head will touch it.
    */
    void createFood() {
        int randomX = SNAKE_BODY_PART_SQURE + (SNAKE_BODY_PART_SQURE * random.nextInt(48));    //variableSNAKE_BODY_PART_SQURE=10 + 10 *48 wrt X
        int randomY = SNAKE_BODY_PART_SQURE + (SNAKE_BODY_PART_SQURE * random.nextInt(23));     //SNAKE_BODY_PART_SQURE=10+ 10 * 23 wrt Y

        snakeBodyPart[totalBodyPart] = new JButton();      //DOUBT
        snakeBodyPart[totalBodyPart].setEnabled(false);      //DOUBT, WHY ENABLED FALSE
        snakeBodyPart[totalBodyPart].setBounds(randomX, randomY, SNAKE_BODY_PART_SQURE, SNAKE_BODY_PART_SQURE);  //DOUBT 
        board.add(snakeBodyPart[totalBodyPart]);        //DOUBT

        totalBodyPart++;     //the body size increases, therefore ++
    }

    private void createBonusFood() {    
        bonusfood = new JButton();          //bonus food is declared as JButton so that when snake's head touches the food, button is pressed
        bonusfood.setEnabled(false);        //DOUBT
        //Set location of the bonus food.
        int bonusFoodLocX = SNAKE_BODY_PART_SQURE * random.nextInt(50);      //location of bonus food wrt x, 10*50
        int bonusFoodLocY = SNAKE_BODY_PART_SQURE * random.nextInt(25);      //location of bonus food wrt y, 10*25

        bonusfood.setBounds(bonusFoodLocX, bonusFoodLocY, BONUS_FOOD_SQURE, BONUS_FOOD_SQURE);   //DOUBT   
        pointOfBonusFood = bonusfood.getLocation();        //getLocation method is called by bonusfood and then assigned to point of bonus food, where bonus food will be created.
        board.add(bonusfood);                              //we add bonus food to the board
        isBonusFoodAvailable = true;                      //is assigned to true because we have to create bonus food
    }

    /**
     * Process next step of the snake.
     * And decide what should be done.
     */
    void processNextStep() {
        boolean isBorderTouched = false;       //bordertouched is set false as snake will not hit the border immediately as soon as the game starts
        // Generate new location of snake head.
        int newHeadLocX = (int) snakeBodyPart[0].getLocation().getX() + directionX;     //DOUBT
        int newHeadLocY = (int) snakeBodyPart[0].getLocation().getY() + directionY;     //DOUBT

        // last part of the snake is food
        int foodLocX = (int) snakeBodyPart[totalBodyPart - 1].getLocation().getX();      //DOUBT
        int foodLocY = (int) snakeBodyPart[totalBodyPart - 1].getLocation().getY();      //DOUBT

        // Check does snake cross the border of the board?
        if (newHeadLocX >= BOARD_WIDTH - SNAKE_BODY_PART_SQURE) {      //headlocation >= boardwidth - 10
            newHeadLocX = 0;                                          //snake dies, starts back from location 0
            isBorderTouched = true;                                   //DOUBT
        } else if (newHeadLocX <= 0) {                            //DOUBT     
            newHeadLocX = BOARD_WIDTH - SNAKE_BODY_PART_SQURE;
            isBorderTouched = true;
        } else if (newHeadLocY >= BOARD_HEIGHT - SNAKE_BODY_PART_SQURE) {        
            newHeadLocY = 0;
            isBorderTouched = true;
        } else if (newHeadLocY <= 0) {
            newHeadLocY = BOARD_HEIGHT - SNAKE_BODY_PART_SQURE;
            isBorderTouched = true;
        }

        // Check has snake touched the food?
        if (newHeadLocX == foodLocX && newHeadLocY == foodLocY) {      
            // Set score
            score += 5;                       //score will be increamented by 5
            scoreViewer.setText("Score==>" + score);           //displaying the updated score

            // Check bonus food should be given or not?
            if (score % 50 == 0 && !isBonusFoodAvailable) {      //whenever score is multiple of 50 i.e 100,15,200..,no bonusfood on screen currently then
                createBonusFood();                               //class that will create bonus food on screen
            }
            // Create new food
            createFood();                                      //after eating bonus food, again normal food will be displayed on screen
        }

        // Check has snake touched the bonus food?
        if (isBonusFoodAvailable &&
                pointOfBonusFood.x <= newHeadLocX &&
                pointOfBonusFood.y <= newHeadLocY &&
                (pointOfBonusFood.x + SNAKE_BODY_PART_SQURE) >= newHeadLocX &&
                (pointOfBonusFood.y + SNAKE_BODY_PART_SQURE) >= newHeadLocY) {      //condition of snake eating bonus food
            board.remove(bonusfood);                                   //bonus food will vanish after snake has eaten it
            score += 100;                                     //if snake eats bonus food, score will increamented by 100
            scoreViewer.setText("Score ==>" + score);          //score will displayed using setText method
            isBonusFoodAvailable = false;                      //bonus food set to false because it is already and no longer available on screen
        }
        
        // Check is game over?
        if(isGameOver(isBorderTouched, newHeadLocX, newHeadLocY)) {        //parameters that will affect gameOver class
           scoreViewer.setText("GAME OVER	" + score);                //final score will be displayed
           isRunning = false;                                    //since the game is over ,snake wont be in motion
           return;                                               //DOUBT, returning what
        } else {
            // Moves the entire snake body forward            
            moveSnakeForward(newHeadLocX, newHeadLocY);            //parameters affecting snake's movement wrt x,y axis.
        }

        board.repaint();                                //every time that the snake moves,its body remains gray and other remaining cells of the board are painted white
    }

    /**
     * This method is responsible to detect is game over or not?
     * Game should be over while snake is touched by any maze or by itself.
     * If anyone wants to add new type just declare new GAME_TYPE enum value and put logic in this method     //IMPROVISATION for future
     * @param isBorderTouched
     * @param headLocX
     * @param headLocY           //DOUBT
     * @return
     */
    private boolean isGameOver(boolean isBorderTouched, int headLocX, int headLocY) {        //class to check if game is over, factors affecting are passed as arguments
        switch(selectedGameType) {                                       //selectedGameType is checked                 
            case BORDER:                                               //if game type is border
                if(isBorderTouched) {                                    //DOUBT, what  isBorderTouched condition
                    return true;                                       //since class isGameOver is boolean, we return true
                }
                break;
            case TUNNEL:
                // TODO put logic here...
                throw new UnsupportedOperationException();              //DOUBT
            default:
                break;                                            //if none of the above conditions are satisfied, game is not over.
        }
        
        for (int i = SNAKE_LENGTH_DEFAULT; i < totalBodyPart - 2; i++) {             //Tail biting condition, DOUBT
            Point partLoc = snakeBodyPart[i].getLocation();                   //DOUBT
            System.out.println("("+partLoc.x +", "+partLoc.y+")  ("+headLocX+", "+headLocY+")");
            if (partLoc.equals(new Point(headLocX, headLocY))) {              //DOUBT
                return true;
            }
        }

        return false;        //DOUBT
    }

    /**
     * Every body part should be placed to location of the front part
     * For example if part:0(100,150) , part: 1(90, 150), part:2(80,150) and new head location (110,150) then,
       Location of part:2 should be (80,150) to (90,150), part:1 will be (90,150) to (100,150) and part:3 will be (100,150) to (110,150)     //DOUBT
     * This movement process should be start from the last part to first part.
     * We must avoid the food that means last body part of the snake.
     * Notice that we write (totalBodyPart - 2) instead of (totalBodyPart - 1).
     * (totalBodyPart - 1) means food and (totalBodyPart - 2) means tail.
     * @param headLocX
     * @param headLocY
     */
    public void moveSnakeForward(int headLocX, int headLocY) {
        for (int i = totalBodyPart - 2; i > 0; i--) {               //DOUBT
            Point frontBodyPartPoint = snakeBodyPart[i - 1].getLocation();      //DOUBT
            snakeBodyPart[i].setLocation(frontBodyPartPoint);                 //DOUBT
        }
        snakeBodyPart[0].setBounds(headLocX, headLocY, SNAKE_BODY_PART_SQURE, SNAKE_BODY_PART_SQURE);    //DOUBT
    }

    public void snakeKeyPressed(KeyEvent e) {       
        // snake should move to left when player presses left arrow
        if (isRunningLeft == true && e.getKeyCode() == 37) {                 //DOUBT, is 37 value of left arrow key?
            directionX = -SNAKE_BODY_PART_SQURE; // means snake move right to left by 10 pixel, 10 pixel because snake bodypart square=10
            directionY = 0;                //moving upwards within x-axis therefore y=0
            isRunningRight = false;     // means snake cant move from left to right  , snake cant go backwards
            isRunningUp = true;         // means snake can move from down to up
            isRunningDown = true;       // means snake can move from up to down
        }
        // snake should move up when player presses up arrow key
        if (isRunningUp == true && e.getKeyCode() == 38) {          //38 is the value of upward arrow key
            directionX = 0;              //snake is moving within y-axis
            directionY = -SNAKE_BODY_PART_SQURE; // means snake moves from down to up by 10 pixel, 10 pixel because snake body partsquare=10
            isRunningDown = false;     // means snake cant go backwards wrt up arrow key, it cant go downwards
            isRunningRight = true;     // means snake can move from left to right
            isRunningLeft = true;      // means snake can move from right to left
        }
        // snake should move right when player presses right arrow
        if (isRunningRight == true && e.getKeyCode() == 39) {              //value of right arrow key=39
            directionX = +SNAKE_BODY_PART_SQURE; // means snake moves from left to right by 10 pixel, because bodypart square=10
            directionY = 0;                    //because snake is moving along x-axis
            isRunningLeft = false;             //because snake cant move backwards
            isRunningUp = true;                 //snake can upwards wrt x axis
            isRunningDown = true;            
        }
        // snake should move to down when player presses down arrow key
        if (isRunningDown == true && e.getKeyCode() == 40) {          //value for down arrow key=40
            directionX = 0;                              //as snake is coming on y-axis
            directionY = +SNAKE_BODY_PART_SQURE; // means snake move from left to right by 10 pixel, since snake bodypartsquare=10
            isRunningUp = false;                     //snake cant go backwards wrt down arrow key
            isRunningRight = true;
            isRunningLeft = true;
        }
    }

    private void createThread() {    //DOUBT     
        // start thread
        Thread thread = new Thread(new Runnable() {

            public void run() {
                runIt();
            }
        });
        thread.start(); // go to runIt() method
    }

    public void runIt() {         //DOUBT
        while (true) {
            if(isRunning) {
                // Process what should be next step of the snake.
                processNextStep();
                try {
                    Thread.sleep(selectedSpeed);                
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }
}
