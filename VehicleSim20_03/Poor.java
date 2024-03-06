import greenfoot.*;

public class Poor extends Pedestrian
{
    // simplist pedestrian
    public Poor(int direction)
    {
        super(direction);
    }
    
    public void act(){
        super.act();
        // hits a black hole then suck me in
        if (sucked){
            suckMeIn();
        }
    }
}
