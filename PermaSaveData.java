import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class PermaSaveData 
{
    // instance variables - replace the example below with your own

    

    /**
     * Constructor for objects of class PermaSavData
     */
    public PermaSaveData()
    {

    }

    public void saveRoom(Room savedRoom) 
    {

        try
        {
            JLabel label = savedRoom.getLabel();
            saveJLabel(label);
        }
        catch(IOException e)
        {
            System.out.println("Error");
        }

        try
        {
            FileOutputStream roomSave = new FileOutputStream("room.sav");
            ObjectOutputStream newRoom = new ObjectOutputStream(roomSave);
            newRoom.writeObject(savedRoom);
            newRoom.close();
        }
        catch(IOException e)
        {
            System.out.println("Error");
        }

    }
    
    public Room loadRoom() throws IOException, ClassNotFoundException
    {
        Room loadedRoom = null;
        try
        {
            FileInputStream roomLoad = new FileInputStream("room.sav");
            ObjectInputStream newRoom = new ObjectInputStream(roomLoad);
            loadedRoom = (Room) newRoom.readObject();
            newRoom.close();
        }
        catch(IOException e)
        {
            System.exit(0);
        }
        catch(ClassNotFoundException e)
        {
            System.exit(0);
        }
        return loadedRoom;
    }

    private void saveJLabel(JLabel label) throws IOException
    {
        try
        {
            Icon icon = label.getIcon();
            FileWriter writer = new FileWriter("Icon.txt");
            writer.write(icon.toString());
            writer.close();
        }
        catch(IOException e)
        {
            System.out.println("Error!");
        }
    }

    public void saveEatenMush(boolean eatenMush) throws IOException
    {
        try
        {
            String str = String.valueOf(eatenMush);
            FileWriter writer = new FileWriter("EatenMush.txt");
            writer.write(str);
            writer.close();
        }
        catch(IOException e)
        {
            System.out.println("ERROR!!!");
        }
    }

    public void saveHaveMush(boolean haveMush) throws IOException
    {
        try
        {
            String str = String.valueOf(haveMush);
            FileWriter writer = new FileWriter("HaveMush.txt");
            writer.write(str);
            writer.close();
        }
        catch(IOException e)
        {
            System.out.println("ERROR!");
        }
    }
    
    public String loadIcon()
    {
        String icon = "";
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("Icon.txt"));
            icon = reader.readLine();
            reader.close();
        }
        catch(IOException e)
        {
            System.out.println("ERROROR");
            System.exit(0);
        }
        return icon;
    }
    
    public boolean loadEatenMush() throws IOException
    {
        boolean eatenMush = true;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("EatenMush.txt"));
            String line = reader.readLine();
            eatenMush = Boolean.parseBoolean(line);
            reader.close();
        }
        catch(IOException e)
        {
            System.out.println("ERRORRRR");
            System.exit(0);
        }
        return eatenMush;
    }

    public boolean loadHaveMush() throws IOException
    {
        boolean haveMush = true;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("HaveMush.txt"));
            String line = reader.readLine();
            haveMush = Boolean.parseBoolean(line);
            reader.close();
        }
        catch(IOException e)
        {
            System.out.println("ERRORRRR");
            System.exit(0);
        }
        return haveMush;
    }

}

