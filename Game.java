import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.util.Scanner;
import javax.sound.sampled.*;

public class Game 
{
	private boolean haveMush;
    private boolean eatenMush;
    private Room currentRoom;
    private ImageViewer gui;
    private CommandWords commands;
    volatile boolean playing;
    private Clip test;
    private JTextArea textArea;
    private SaveData data;
    private boolean gameD;
    volatile boolean answer;
    private PermaSaveData permaData = new PermaSaveData();
    
    public Game()
    {
    	 answer = false;

         newOrLoad();

         while(answer == false)
         {

         }

         if(gameD == true)
         {
             createGame();

         }
         else
         {
             createLoadedGame();
         }
    }
    
    private void messageBox()
    {
        Frame frame = new Frame();
        JOptionPane.showMessageDialog(frame, 
            "This feature is not yet implented.", "About Game", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void createGame()
    {
        haveMush = false;
        eatenMush = false;
        textArea = new JTextArea();
        textArea.setColumns(40);
        textArea.setRows(1);
        textArea.setEditable(false);
        commands = new CommandWords();
        gui = new ImageViewer();
        createRooms();
        playing = true;
        printWelcome();
        play();
    }
    
    private void createLoadedGame()
    {
        try
        {
            haveMush = permaData.loadHaveMush();
            eatenMush = permaData.loadEatenMush();
            textArea = new JTextArea();
            textArea.setColumns(40);
            textArea.setRows(1);
            textArea.setEditable(false);
            commands = new CommandWords();
            gui = new ImageViewer();
            createRooms();
            loadRoom();
            playing = true;
            printWelcome();
            play();
        }
        catch(IOException e)
        {
            System.exit(0);
        }
    }
    
    private void loadRoom()
    {
        try
        {
            String icon = permaData.loadIcon();
            Room loadedRoom = permaData.loadRoom();
            loadedRoom.setJLabel(icon);
            currentRoom = loadedRoom;
            doThis();
        }
        catch(IOException e)
        {
            System.exit(0);
        }
        catch(ClassNotFoundException e)
        {
            System.exit(0);
        }
    }
    
    private void doThis()
    {
        JLabel pic = currentRoom.getLabel();
        gui.openRoom(pic);
        goToTextArea(currentRoom.getLongDescription());
    }
    
    private void newOrLoad()
    {
        {
            JFrame okay = new JFrame("World of Zuul");
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (int) ((dimension.getWidth() - okay.getWidth()) / 3);
            int y = (int) ((dimension.getHeight() - okay.getHeight()) / 3);
            okay.setLocation(x, y);
            Container content = okay.getContentPane();
            content.setLayout(new FlowLayout());

            JButton newGame = new JButton("New Game");
            newGame.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event){  okay.dispose(); answer = true; gameD = true;   }
                });

            JButton loadGame = new JButton("Load Game");
            loadGame.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) { okay.dispose(); answer = true; gameD = false; }
                });

            content.add(newGame);
            content.add(loadGame);

            okay.pack();
            okay.setVisible(true);
        }
    }
    
    private void createRooms()
    {
        Room cave, crossroad, forestEntrance, fountain, forest, outsideTown;

        JLabel caveI, crossroadI, forestEntranceI, fountainI, forestI, outsideTownI;

        // create the rooms
        cave = new Room("cave", "You are in a dank and dark cave \n", caveI = new JLabel(new ImageIcon("Images/room1x2.png")));
        crossroad = new Room("crossroad", "You are at a crossroads \n", crossroadI = new JLabel(new ImageIcon("Images/room2x2.png")));
        forestEntrance = new Room("forestEntrance", "You are at the endge of a forest \n", forestEntranceI = new JLabel(new ImageIcon("Images/room4x2.png")));
        fountain = new Room("fountain", "You are in an area with a fountain \n", fountainI = new JLabel(new ImageIcon("Images/room3x2.png")));
        forest = new Room("forest", "You are in the Ganchi forest \n", forestI = new JLabel(new ImageIcon("Images/room5x2.png")));
        outsideTown = new Room("outsideTown", "You are outside a town \n", outsideTownI = new JLabel(new ImageIcon("Images/room6x2.png")));

        // initialise room exits
        cave.setExit("east", crossroad);

        crossroad.setExit("west", cave);
        crossroad.setExit("south", fountain);
        crossroad.setExit("north", forestEntrance);

        fountain.setExit("east", crossroad);

        forestEntrance.setExit("south", crossroad);
        forestEntrance.setExit("north", forest);

        forest.setExit("west", forestEntrance);
        forest.setExit("east", outsideTown);

        outsideTown.setExit("west", forest);

        currentRoom = cave;  
        JLabel label = currentRoom.getLabel();
        gui.openRoom(label);
    }
    
    private void play() 
    {            

        while (true)
        {
            while(playing == false)
            {
                playSecond();
            }

            try{
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Legend of Zelda (NES) Intro copy.wav"));
                test = AudioSystem.getClip();  

                test.open(ais);
                test.start();

                while (!test.isRunning())

                    Thread.sleep(10);
                while (test.isRunning())
                    Thread.sleep(10);

                test.close();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        }

    }
    
    private void playSecond()
    {

        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Great Fairys Fountain - The Legend of Zelda Ocarina of Time.wav"));
            test = AudioSystem.getClip();  

            test.open(ais);
            test.start();

            while (!test.isRunning())

                Thread.sleep(10);
            while (test.isRunning())
                Thread.sleep(10);

            test.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        playing = true;
    }
    
    private void quitImmediate()
    {
    	System.exit(0);
    }
    
    private void printWelcome()
    {
        goToTextArea("Welcome to the World of Zuul!");
        goToTextArea("\n");
        goToTextArea("World of Zuul is a new, incrediblish adventure game.");
        goToTextArea("\n");
        goToTextArea("Type 'help' if you need help. (Especialy if your a teacher)");
        goToTextArea("\n");
        goToTextArea(currentRoom.getLongDescription());
    }
    
    
    private void processCommand(Command command) 
    {
        boolean toQuit = false;

        if(command.isUnknown()) {
            goToTextArea("I don't know what you mean... \n \n");
            return;
        }

        String commandWord = command.getCommandWord();
        if(commandWord.equals("help")) 
        {
            printHelp(command);
        }
        else if(commandWord.equals("touch"))
        {
            touchCommands(command);
        }
        else if(commandWord.equals("look"))
        {
            lookCommands(command);
        }
        else if(commandWord.equals("pickup"))
        {
            pickupCommands(command);
        }
        else if(commandWord.equals("eat"))
        {
            eat(command);
        }
        else if(commandWord.equals("quit")) 
        {
            toQuit = quit(command);

        }

        if(toQuit == true)
        {
            quitImmediate();
        }

    }
    
    private void eat(Command eCom)
    {
        if(!eCom.hasSecondWord())
        {
            goToTextArea("Eat what? \n \n");
            return;
        }

        String toEat = eCom.getSecondWord();

        if(toEat.equals("mushroom") && haveMush == true)
        {
            eatenMush = true;
            haveMush = false;
            goToTextArea("For some reason you eat the mushroom. For some reason. \n \n");
        }
        else
        {
            goToTextArea("You have nothing to eat. \n \n");
        }
    }
    
    private void pickupCommands(Command pCom)
    {
        if(!pCom.hasSecondWord())
        {
            goToTextArea("Pickup what? \n \n");
            return;
        }

        String roomName = currentRoom.getName();
        String toPick = pCom.getSecondWord();

        if(roomName.equals("cave"))
        {
            if(toPick.equals("mushroom"))
            {
                if(eatenMush == true || haveMush == true)
                {
                    goToTextArea("There is no need to pick that up again. \n \n");
                }
                else
                {
                    haveMush = true;
                    goToTextArea("You pickup a mushroom from the ground. \n \n");
                }
            }
            else
            {
                goToTextArea("You can't pick that up. \n \n");
            }
        }
        else
        {
            goToTextArea("You can't pick that up. \n \n");
        }
    }
    
    private void lookCommands(Command lCom)
    {
        if(!lCom.hasSecondWord())
        {
            goToTextArea("Look at what? \n \n");
            return;
        }

        String roomName = currentRoom.getName();
        String toLook = lCom.getSecondWord();

        if(roomName.equals("forestEntrance"))
        {
            if(toLook.equals("statue"))
            {
                goToTextArea("A statue that seems so life like it is startling!"); 
                goToTextArea("\n");
                goToTextArea("It is wrapped in vines so you feel weary about them. \n \n");
            }
            else if(toLook.equals("vines"))
            {
                goToTextArea("There are vines every where! It is as if they are");
                goToTextArea("\n");
                goToTextArea("trying to engulf everything in the path! \n \n");
            }
            else if(toLook.equals("around"))
            {
                goToTextArea("You see a statue covered in vines and a town in the distance. \n \n");
            }
            else
            {
                goToTextArea("Not a valid word. \n \n");
            }
        }
        else if(roomName.equals("forest"))
        {
            if(toLook.equals("trees"))
            {
                goToTextArea("The trees seem to be the only intresting thing here... \n \n");
            }
            else if(toLook.equals("around"))
            {
                goToTextArea("You look around and see only trees. \n \n");
            }
            else
            {
                goToTextArea("Not a valid word. \n \n");
            }
        }
        else if(roomName.equals("crossroad"))
        {
            if(toLook.equals("sun"))
            {
                goToTextArea("You stare up at Aperion, but look away quickly for the sake of your eyes. \n \n");
            }
            else if(toLook.equals("sign"))
            {
                goToTextArea("It says: South to the Fairy Fountain");
                goToTextArea("\n");
                goToTextArea("North to Crakerhold");
                goToTextArea("\n");
                goToTextArea("West to Cave of False Wonderment \n \n");
            }
            else
            {
                goToTextArea("Not a valid word. \n \n");
            }
        }
        else if(roomName.equals("fountain"))
        {
            if(toLook.equals("statue"))
            {
                goToTextArea("It is a beautiful statue that was once used for something more... \n \n");
            }
            else if(toLook.equals("heart"))
            {
                goToTextArea("Strange... It is doubful if even the creator of this world");
                goToTextArea("\n");
                goToTextArea("knows the purpose of this. \n \n");
            }
            else
            {
                goToTextArea("Not a valid word. \n \n");
            }
        }
        else if(roomName.equals("cave"))
        {
            if(toLook.equals("around"))
            {
                goToTextArea("The cave is small and holds a lot of mushrooms. \n \n");
            }
            else if(toLook.equals("mushrooms"))
            {
                goToTextArea("These mushrooms look tasty... \n \n");
            }
            else
            {
                goToTextArea("Not a valid word. \n \n");
            }
        }
        else if(roomName.equals("outsideTown"))
        {
            if(toLook.equals("town"))
            {
                goToTextArea("It is the town of Crakehold! \n \n");
            }
            else if(toLook.equals("river"))
            {
                goToTextArea("It is a rushing stream. Better not fall! \n \n");
            }
            else if(toLook.equals("bridge"))
            {
                goToTextArea("It seems like a sturdy bridge. \n \n");
            }
            else
            {
                goToTextArea("Not a valid word. \n \n");
            }
        }
    }
    
    private void goToTextArea(String text)
    {
        textArea.append(text);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    
    private void touchCommands(Command tCom)
    {
        if(!tCom.hasSecondWord())
        {
            goToTextArea("Touch what? \n \n");
            return;
        }

        String roomName = currentRoom.getName();
        String toTouch = tCom.getSecondWord();

        if(roomName.equals("forestEntrance"))
        {
            if(toTouch.equals("statue"))
            {
                goToTextArea("You touch the statue and feel how rough the stone is. \n \n"); 
            }
            else if(toTouch.equals("vines") || toTouch.equals("bush"))
            {
                goToTextArea("You reach out to touch the vines but stop midway.");
                goToTextArea("\n");
                goToTextArea("You have a feeling it is not a good idea to touch them... \n \n");
            }
            else
            {
                goToTextArea("Not a valid word. \n \n");
            }
        }
        else if(roomName.equals("fountain"))
        {
            if(toTouch.equals("statue"))
            {
                goToTextArea("As you touch the statue you begin to hear soft music... \n \n");
                playing = false;
                test.stop();
            }
            else
            {
                goToTextArea("Not a valid word. \n \n");
            }
        }
        else if(roomName.equals("forest"))
        {
            if(toTouch.equals("trees"))
            {
                goToTextArea("You touch the tree and are surprised to find flowers literaly");
                goToTextArea("\n");
                goToTextArea("growing from the tree! How strange! \n \n");
            }
            else
            {
                goToTextArea("Not a valid word. \n \n");
            }
        }
        else
        {
            goToTextArea("Nothing to touch here. \n \n");
        }
    }
    
    private void helpTeacher()
    {
        goToTextArea("The objective is simple. You can look at things in each room. touch some things.");
        goToTextArea("\n");
        goToTextArea("Pickup things. and eat things. You will not be able to get into forest untill");
        goToTextArea("\n");
        goToTextArea("you pickup mushroom from cave and eat it. That is basically the objective. otherside of forest. \n \n");
    }
    
    private void printHelp(Command command) 
    {
        if(!command.hasSecondWord())
        {
            goToTextArea("You are lost. You are alone. You wake");
            goToTextArea("\n");
            goToTextArea("up in a cave. Good luck.");
            goToTextArea("\n");
            goToTextArea("Your command words are:");
            goToTextArea("\n");
            goToTextArea("look, touch, help, quit, pickup, and eat");
            goToTextArea("\n");
            goToTextArea("NOTE: IF YOU ARE TEACHER. TYPE help teacher \n \n");
            return;
        }

        String second = command.getSecondWord();
        if(second.equals("teacher"))
        {
            helpTeacher();
        }
    }
    
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            goToTextArea("Go where? \n \n");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) 
        {
            //if outside of town this message will appear if player tries to go east:
            if(currentRoom.getName().equals("outsideTown") && direction.equals("east"))
            {
                goToTextArea("Sorry this is the end of the game. There were no more backgrounds");
                goToTextArea("\n");
                goToTextArea("from the pack. \n \n");
                return;
            }
            goToTextArea("There is no door! \n \n");
        }
        else 
        {
            //Will not allow player to go through forest until player has eaten mushroom
            if(currentRoom.getName().equals("forestEntrance") && direction.equals("north"))
            {
                if(eatenMush == false)
                {
                    goToTextArea("You know you should not go towards the vines.");
                    goToTextArea("\n");
                    goToTextArea("Perhaps something will help. \n \n");
                    return;
                }
            }
            currentRoom = nextRoom;
            JLabel pic = currentRoom.getLabel();
            gui.openRoom(pic);
            goToTextArea(currentRoom.getLongDescription());
        }
    }
    
    private void load()
    {
        haveMush = data.getHaveMush();
        eatenMush = data.getEatenMush();
        currentRoom = data.getLoadedRoom();
        JLabel pic = currentRoom.getLabel();
        gui.openRoom(pic);
        goToTextArea(currentRoom.getLongDescription());
    }
    
    private boolean getEatenMush()
    {
        return eatenMush;
    }

    private boolean getHaveMush()
    {
        return haveMush;
    }

    private void saveGame()
    {
        data = new SaveData(currentRoom, getEatenMush(), getHaveMush());
    }
    
    private void loadGame()
    {
        if(data == null)
        {
            goToTextArea("No data to load! \n \n");
        }
        else
        {
            createRooms();
            load();
        }
    }
    
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            goToTextArea("Quit what? \n \n");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    public static void main(String[] args) 
	{
		Game supergame = new Game();

	}
    
    public class ImageViewer 
    {
        private JFrame frame;
        private Container contentPane;
        private JLabel image;
        // private JScrollPane scrollPane;

        /**
         * Creates the GUI for the game
         */
        public ImageViewer()
        {
            makeFrame();
        }

        /**
         * opends a room when player enters a new room
         * @param label of the room being entered
         */
        public void openRoom(JLabel label)
        {
            Icon icon = label.getIcon();
            image.setIcon(icon);
            frame.pack();
        }

        /**
         * pops up a dialog box about the game.
         */
        private void aboutGame()
        {
            JOptionPane.showMessageDialog(frame, 
                "World of Zuul\n Version three", "About Game", JOptionPane.INFORMATION_MESSAGE);
        }

        /**E
         * process the direction the player is going
         * @param the direction as a string
         */
        private void processComm(String word)
        {
            if(word.equals("East"))
            {
                Command commandWord = new Command("go", "east");
                goRoom(commandWord);
                frame.pack();
                return;
            }
            else if(word.equals("North"))
            {
                Command commandWord = new Command("go", "north");
                goRoom(commandWord);
                frame.pack();
                return;
            }
            else if(word.equals("South"))
            {
                Command commandWord = new Command("go", "south");
                goRoom(commandWord);
                frame.pack();
                return;
            }
            else if(word.equals("West"))
            {
                Command commandWord = new Command("go", "west");
                goRoom(commandWord);
                frame.pack();
                return;
            }

        }

        /**
         * process command the player inputs into the text field
         * @param string of text the player inputed
         */
        private void getKey(String command)
        {
            String word1 = null;
            String word2 = null;

            Scanner reader = new Scanner(command);
            if(reader.hasNext())
            {
                word1 = reader.next();
                if(reader.hasNext())
                {
                    word2 = reader.next();
                }
            }

            if(commands.isCommand(word1))
            {
                Command others = new Command(word1, word2);
                processCommand(others);
            }
            else
            {
                Command others = new Command(null, word2);
                processCommand(others);
            }
        }

        /**
         * Creates the entire GUI of the game
         */
        private void makeFrame()
        {
            frame = new JFrame("World of Zuul");
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (int) ((dimension.getWidth() - frame.getWidth()) / 4);
            int y = (int) ((dimension.getHeight() - frame.getHeight()) / 5);
            frame.setLocation(x, y);
            makeMenuBar();
            BorderLayout border = new BorderLayout();
            // border.setVgap(-80);

            contentPane = frame.getContentPane();
            contentPane.setLayout(border);

            image = new JLabel();
            contentPane.add(image, BorderLayout.CENTER);

            JPanel directionPanel = new JPanel(new BorderLayout());
            JButton eastButton = new JButton("East");
            eastButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) { String commandW = event.getActionCommand(); processComm(commandW); }
                });
            directionPanel.add(eastButton, BorderLayout.EAST);

            JButton northButton = new JButton("North");
            northButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) { String commandW = event.getActionCommand(); processComm(commandW); }
                });
            directionPanel.add(northButton, BorderLayout.NORTH);

            JButton southButton = new JButton("South");
            southButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) { String commandW = event.getActionCommand(); processComm(commandW); }
                });
            directionPanel.add(southButton, BorderLayout.SOUTH);

            JButton westButton = new JButton("West");
            westButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) { String commandW = event.getActionCommand(); processComm(commandW); }
                });
            directionPanel.add(westButton, BorderLayout.WEST);

            JScrollPane scrollPane = new JScrollPane(textArea);

            JTextField textInput = new JTextField(40);
            textInput.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) { textInput.setText(""); String otherCommands = event.getActionCommand(); getKey(otherCommands); }
                });
            JPanel borderPanel = new JPanel(new BorderLayout());         
            borderPanel.add(directionPanel, BorderLayout.EAST);
            borderPanel.add(scrollPane, BorderLayout.WEST);
            borderPanel.add(textInput, BorderLayout.SOUTH);

            contentPane.add(borderPanel, BorderLayout.SOUTH);

            frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);

            frame.pack();
            //frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        /**
         * creates the menubar for the game
         */
        private void makeMenuBar()
        {
            JMenuBar menubar = new JMenuBar();
            frame.setJMenuBar(menubar);

            //create the File menu:
            JMenu fileMenu = new JMenu("File");
            menubar.add(fileMenu);

            JMenuItem loadItem = new JMenuItem("Load Temporary");
            loadItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) { loadGame(); }
                });
            fileMenu.add(loadItem);

            JMenuItem saveItem = new JMenuItem("Save Temporary");
            saveItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) { saveGame();     }
                });
            fileMenu.add(saveItem);

            JMenuItem savePItem = new JMenuItem("Save Permanent");
            savePItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) { try{permaData.saveRoom(currentRoom);  permaData.saveHaveMush(haveMush); permaData.saveEatenMush(eatenMush);} catch(IOException e)
                        {System.out.println("ERROR!!");} goToTextArea("Saved. \n \n"); }
                });
            fileMenu.add(savePItem);

            //create the Help menu:
            JMenu helpMenu = new JMenu("Help");
            menubar.add(helpMenu);

            JMenuItem aboutHelp = new JMenuItem("About");
            aboutHelp.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) { aboutGame(); }
                });
            helpMenu.add(aboutHelp);
        }
    }

}
