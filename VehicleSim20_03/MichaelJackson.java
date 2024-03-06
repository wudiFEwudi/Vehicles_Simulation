/**
 * Write a description of class MichaelJackson here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import greenfoot.*;
import java.util.ArrayList;

public class MichaelJackson extends Pedestrian
{
    // instance variables
    private boolean dancersAdded;
    private Dancer left, right;
    private int d;
    private int changeDirDuration;
    private int moonWalkDuration;
    private int offSet; // dancer spacing
    private int showDuration; // least amount of time on screen
    private GreenfootSound sound = new GreenfootSound("MJ.mp3");

    public MichaelJackson(int direction)
    {
        // create instance variables
        super(direction);
        dancersAdded = false;
        speed = 1;
        maxSpeed = 1;
        offSet = 50;
        d = 1;
        changeDirDuration = 30;
        showDuration = 800;
        moonWalkDuration = 0;
    }
    
    public void addedToWorld(World w){
        // Everybody stops to appreciate MJ's dance
        // When added to world set stop all vehicles
        ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) w.getObjects(Vehicle.class);
        for (Vehicle V : vehicles){
            if (V != null){
                V.setSpeed(0);
            }
        }
        // stop all pedestrians
        ArrayList<Pedestrian> P = (ArrayList<Pedestrian>) w.getObjects(Pedestrian.class);
        for (Pedestrian p : P){
            if (p != null){
                p.setSpeed(0);
            }
        }
        // stop any police siren
        ArrayList<Police> polices = (ArrayList<Police>) w.getObjects(Police.class);
        for (Police p : polices){
            if (p != null){
                p.stopSound();
            }
        }
        w.removeObjects(w.getObjects(Blackhole.class));
        sound.play();
    }
    
    public void act(){
        World w = getWorld();
        // if dancers are not added yet add them
        if (!dancersAdded){
            left = new Dancer(1);
            right = new Dancer(1);
            w.addObject(left, 0, 0);
            w.addObject(right, 0, 0);
            dancersAdded = true;
        }
        else{
            showDuration--; 
            // if moonwalking slide to left or right for some time
            if (moonWalkDuration > 0){
                setLocation(getX() - d, getY());
                int posX = getX(), posY = getY();
                left.setLocation(posX - offSet, posY);
                right.setLocation(posX + offSet, posY);
                if (--moonWalkDuration <= 0){
                    moonWalkDuration = -1;
                }
                return; // stop further movement while moon walking
            }
            // when to moonwalk decided randomly
            if (Greenfoot.getRandomNumber(400) == 0 && moonWalkDuration == 0){
                moonWalkDuration = 200;
            }
            // change direction to simulate random movement and dance
            if (--changeDirDuration <= 0){
                d = Greenfoot.getRandomNumber(2) == 0 ? 1 : -1;
                direction = Greenfoot.getRandomNumber(4) == 0 ? -1 : 1;
                changeDirDuration = 30;
            }
            // if show not reached least amount of time but reaches the edge then adjust the direction
            if (showDuration > 0){
                changeDir();
            }
            setLocation (getX(), getY() + (int)(speed * direction));  
            setLocation (getX() + (int)(speed * d), getY()); 
            int posX = getX(), posY = getY();
            // left and right dancers follows MJ
            left.setLocation(posX - offSet, posY);
            right.setLocation(posX + offSet, posY);
            // if least amount show time reached and reaches the edge
            if (checkEdge() && showDuration <= 0){
                // remove the dancers
                w.removeObject(left);
                w.removeObject(right);
                // set all vehicles and pedestrians to regular speed
                ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) w.getObjects(Vehicle.class);
                for (Vehicle V : vehicles){
                    if (V != null){
                        V.goFullSpeed();
                    }
                }
                ArrayList<Pedestrian> P = (ArrayList<Pedestrian>) w.getObjects(Pedestrian.class);
                for (Pedestrian p : P){
                    if (p != null){
                        p.goFullSpeed();
                    }
                }
                // tell the world MJ is gone and remove me
                VehicleWorld v = (VehicleWorld)getWorld();
                v.setMJpresent(false);
                sound.stop();
                w.removeObject(this);
            }
        }
    }
    
    // override some methods
    public void removeMe(){
        
    }
    
    public void setSpeed(double s){
        
    }
    
    public void knockDown(){
        
    }
    
    public void stop(){
        
    }
    
    // adjust direction when reaches the edge so don't go off screen
    public void changeDir(){
        int posX = getX(), posY = getY();
        if (d == 1 && posX > 750){
            d = -1;
        }
        else if (d == -1 && posX < 50){
            d = 1;
        }
        if (direction == 1 && posY > 550){
            direction = -1;
        }
        else if (direction == -1 && posY < 50){
            direction = 1;
        }
    }
    
    // check if reached the edge
    public boolean checkEdge(){
        if (getY() > 550 || getY() < 30 || getX() < 50 || getX() > 750){
            return true;
        }
        return false;
    }
}
