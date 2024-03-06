/**
 * Write a description of class ZombieCar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;
public class ZombieCar extends Vehicle
{
    // instance variables - replace the example below with your own
    private boolean snowAdded;

    /**
     * Constructor for objects of class ZombieCar
     */
    public ZombieCar(VehicleSpawner origin)
    {
        super(origin);
        snowAdded = false; // snow rail added or not
        // set speed
        speed = 0.7;
        maxSpeed = 0.7;
        originalSpeed = 0.7;
    }

    public void act(){
        isnight = false;
        // if hit a black hole suck me in and stop further movement
        if (sucked){
            suckMeIn();
            return;
        }
        drive();
        checkHitPedestrian();
        if (checkEdge()){
            removeMe();
        }
        // if snow hasn't been added add it and adjust according to direction 
        if (direction == 1 && !snowAdded){
            getWorld().addObject(new Snowlane(1), -530, getY());
            snowAdded = true;
        }
        else if (direction == -1 && !snowAdded){
            getWorld().addObject(new Snowlane(-1), 1330, getY());
            snowAdded = true;
        }
    }
    
    public boolean checkHitPedestrian () {
        Pedestrian p = (Pedestrian)getOneObjectAtOffset((int)speed + getImage().getWidth()/2, 0, Pedestrian.class);
        
        if (p != null){
            p.knockDown();
            return true;
        }
        return false;
    }
    
    // overrides the method to not change lane
    public boolean changeLane(){
        return false;
    }
    
    public void removeMe(){
        if (getWorld() != null){
            getWorld().removeObjects(getWorld().getObjects(Snowlane.class));
            getWorld().removeObject(this);
        }
    }
    
    // override to do nothing
    // zombie don't know who MJ is
    public void setspeed(double s){
        
    }
}
