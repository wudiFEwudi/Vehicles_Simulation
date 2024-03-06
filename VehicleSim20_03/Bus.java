import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Bus subclass
 */
public class Bus extends Vehicle
{
    private int stop_duration = 60;
    private boolean hit = false;
    public Bus(VehicleSpawner origin, boolean slowdown){
        super (origin); // call the superclass' constructor first
        
        //Set up values for Bus
        maxSpeed = 1.5 + ((Math.random() * 10)/5);
        speed = maxSpeed;
        originalSpeed = maxSpeed;
        speeder = false;
        notPolice = true;
        // used for night time spawning
        if (slowdown){
            maxSpeed = Math.max(1, maxSpeed / 2);
        }
        // because the Bus graphic is tall, offset it a up (this may result in some collision check issues)
        yOffset = 15;
    }
    
    /**
     * Act - do whatever the Bus wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // if hit a black hole suck me in and stop further movement
        if (sucked){
            suckMeIn();
            return;
        }
        // if night time don't pick up anyone
        if (isnight){
            drive();
            return;
        }
        // if not hit anyone then drive regularly
        if (!hit){
            speed = maxSpeed;
            drive();
        }
        // if just hits a pedestrian 
        if (!hit && checkHitPedestrian()){
            Pedestrian p = (Pedestrian)getOneObjectAtOffset((int)((speed + getImage().getWidth()/2) * direction), 0, Pedestrian.class);
            // if they are awake (alive) then stop them
            if (p.isAwake()){
                p.stop();
                hit = true;
                speed = 0;
            }
        };
        if (checkEdge() && getWorld() != null){
            getWorld().removeObject(this);
        }
        // if hits a pedestrian stop for 60 acts
        if (hit){
            stop_duration--;
            if (stop_duration == 0){
                stop_duration = 60;
                hit = false;
            }
        }
    }

    public boolean checkHitPedestrian () {
        Pedestrian p = (Pedestrian)getOneObjectAtOffset((int)((speed + getImage().getWidth()/2) * direction), 0, Pedestrian.class);
        
        if (p != null){
            return true;
        }
        return false;
    }
    
}
