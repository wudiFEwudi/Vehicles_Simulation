import greenfoot.*;
import java.util.ArrayList;

public class Explosion extends Actor
{
    private double size;
    private int duration = 60;
    private GreenfootImage image;
    private GreenfootSound sound = new GreenfootSound("explosion.mp3");
    
    public Explosion(double size)
    {
        this.size = size;
        image = getImage();
        // scale the image by a percentage determined by the size of the explosion
        image.scale((int)(image.getWidth() * (1 + size / 100)), (int)(image.getHeight() * (1 + size / 100)));
        sound.setVolume(0);
        sound.play();
        sound.stop();
        sound.setVolume(80);
    }
    
    public void act(){
        // if just exploded
        if (duration == 60){
            // tell the vehicles and pedestrians to remove themselves
            ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) getObjectsInRange((int)size, Vehicle.class);
            for (Vehicle V : vehicles){
                if (V != null){
                    V.removeMe();
                }
            }
            ArrayList<Pedestrian> pedestrians = (ArrayList<Pedestrian>) getObjectsInRange((int)size, Pedestrian.class);
            for (Pedestrian p : pedestrians){
                if (p != null){
                    p.removeMe();
                }
            }
            sound.play();
        }
        // play explosion animation until duration is over
        if (--duration == 0){
            sound.stop();
            getWorld().removeObject(this);
        }
    }
}
