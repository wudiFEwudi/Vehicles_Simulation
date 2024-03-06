/**
 * Write a description of class Snowlane here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;
import java.util.ArrayList;

public class Snowlane extends SuperSmoothMover
{
    // instance variables - replace the example below with your own
    private double speed;

    /**
     * Constructor for objects of class Snowlane
     */
    public Snowlane(int direction)
    {
        // set up speed direction
        if (direction == 1){
            speed = 0.7;
        }
        else{
            speed = -0.7;
        }
    }

    public void act(){
        // if not at the middle move
        if (399 > getX() || getX() > 401){
            move(speed);
        }
        // if MJ is not present then set vehicles who is touching me to a slow speed 
        if (getWorld().getObjects(MichaelJackson.class).size() == 0){
            ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) getIntersectingObjects(Vehicle.class);
            for (Vehicle v : vehicles){
                if (v != null){
                    v.setSpeed(0.7);
                }
            }
        }
        // if pedestrians hit me and not a creeper then knock them down
        ArrayList<Pedestrian> P = (ArrayList<Pedestrian>) getIntersectingObjects(Pedestrian.class);
        for (Pedestrian p : P){
            if (p != null && !p.getCreeper()){
                p.knockDown();
            }
        }
    }
}
