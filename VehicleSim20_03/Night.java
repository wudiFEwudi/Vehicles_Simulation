import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Write a description of class Effect here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Night extends Effect
{
    private GreenfootImage image = new GreenfootImage("night.png");
    private GreenfootSound sound = new GreenfootSound("windblow.mp3");
    public Night (int duration){
        super(duration);
        // set sound to avoid not playing
        sound.setVolume(0);
        sound.play();
        sound.stop();
        sound.setVolume(70);
    }
    
    public void addedToWorld (World w){
        sound.play();
        // change the background to night
        VehicleWorld v = (VehicleWorld)getWorld();
        if (v != null){
            v.changeBackground(image);
            v.setNight(true);
        }
        // get all vehicles and tell them that it is night time
        ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) w.getObjects(Vehicle.class);
        for (Vehicle V : vehicles){
            if (V != null){
                V.setNight(true);
            }
        }
        // get all pedestrians and tell them that it is night time
        ArrayList<Pedestrian> p = (ArrayList<Pedestrian>) w.getObjects(Pedestrian.class);
        for (Pedestrian P : p){
            if (P != null)
                P.setNight(true);
        }
    }
    
    public void act(){
        super.act();
        VehicleWorld v = (VehicleWorld)getWorld();
        // if duration is over
        if (actCounter == 0){
            if (v != null){
                v.setNight(false); // tell the world that night time is over
            }
            // tell all vehicles to go full speed because night time is over
            ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) v.getObjects(Vehicle.class);
            for (Vehicle V : vehicles){
                if (V != null){
                    V.setNight(false);
                    V.goFullSpeed();
                }
            }
            if (sound.isPlaying()){
                sound.stop();
            }
            // change the background back
            v.changeBackground(new GreenfootImage("background01.png"));
            getWorld().removeObject(this);
        }
    }
}