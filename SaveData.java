

public class SaveData 
{
    
    private Room loadedRoom;
    
    private boolean eatenMush;
    
    private boolean haveMush;

    /**
     * Constructor for objects of class SaveData
     */
    public SaveData(Room loadedRoom, boolean eatenMush, boolean haveMush)
    {
        this.loadedRoom = loadedRoom;
        this.eatenMush = eatenMush;
        this.haveMush = haveMush;
    }
    
    public Room getLoadedRoom()
    {
        return loadedRoom;
    }
    
    public boolean getEatenMush()
    {
        return eatenMush;
    }
    
    public boolean getHaveMush()
    {
        return haveMush;
    }
}

