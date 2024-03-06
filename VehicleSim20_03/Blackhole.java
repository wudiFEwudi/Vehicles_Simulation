/**
 * Write a description of class BlackHole here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;
import java.util.ArrayList;

public class Blackhole extends Effect
{
    // instance variables - replace the example below with your own
    private boolean added;
    private GreenfootImage image;
    /**
     * Constructor for objects of class BlackHole
     */
    public Blackhole(int duration)
    {
        super(duration);
        added = false;
    }
    
    public void act(){
        super.act();
        // if not added
        if (!added){
            VehicleWorld V = (VehicleWorld)getWorld();
            V.modifyB(1); // add the total number of black holes in the world
            added = true;
        }
        // suck all vehicles that are touching the hole
        ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) getIntersectingObjects(Vehicle.class);
        for (Vehicle v : vehicles){
            if (v != null){
                v.getsucked(getX(), getY());
            }
        }
        // suck all pedestrians that are touching the hole
        ArrayList<Pedestrian> P = (ArrayList<Pedestrian>) getIntersectingObjects(Pedestrian.class);
        for (Pedestrian p : P){
            if (p != null){
                p.getsucked(getX(), getY());
            }
        }
        // if duration is over 
        if (actCounter <= 0){
            VehicleWorld V = (VehicleWorld)getWorld();
            V.modifyB(-1); // subtract 1 from the total number of black holes in the world
            getWorld().removeObject(this);
        }
    }
}
