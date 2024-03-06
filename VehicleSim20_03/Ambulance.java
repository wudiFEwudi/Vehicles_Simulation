import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Ambulance subclass
 */
public class Ambulance extends Vehicle
{
    public Ambulance(VehicleSpawner origin, boolean slowdown){
        super (origin); // call the superclass' constructor first
        
        maxSpeed = 2.5;
        speed = maxSpeed;
        originalSpeed = maxSpeed;
        speeder = false;
        notPolice = true;
        // used for night time spawning
        if (slowdown){
            maxSpeed = 1.25;
        }
    }

    /**
     * Act - do whatever the Ambulance wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // if hit black hole suck me in
        if (sucked){
            suckMeIn();
            return;
        }
        drive();
        checkHitPedestrian();
        if (checkEdge()){
            getWorld().removeObject(this);
        }

    }

    public boolean checkHitPedestrian () {
        int halfMyWidth = getImage().getWidth()/2;
        int halfMyHeight = getImage().getHeight()/2;
       
        Pedestrian p = (Pedestrian)getOneObjectAtOffset((int)((speed + halfMyWidth)*direction), 0, Pedestrian.class);
        Pedestrian p2 = (Pedestrian)getOneObjectAtOffset((int)((speed + halfMyWidth)*direction), halfMyHeight, Pedestrian.class);
        Pedestrian p3 = (Pedestrian)getOneObjectAtOffset((int)((speed + halfMyWidth)*direction), -halfMyHeight, Pedestrian.class);
             
        if (p != null){
            p.healMe();
            return true;
        } else if (p2 != null){
            p2.healMe();
            return true;
        } else if (p3 != null){
            p3.healMe();
            return true;
        }
        return false;
    }
    
    // MJ can't stop Ambulance because they need to save people
    // respect to these heros!
    public void setSpeed(double s){
        if (s != 0){
            maxSpeed = s;
        }
    }
}
