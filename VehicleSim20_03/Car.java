import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Car subclass
 * Tomorrow: Implement Police Pullover method
 * Add a weirdGuy that takes the implementation of the curent wealthy class
 * wealthy guy can turn around at some point crossing the road
 * add terriost who fires at wealthy people 
 * bullet hits pedestrian, cannot be saved
 * hits creeper which explodes
 * hits car which does nothing
 * Another worldwide effects such as snowstorm where cars slow down and poor people dies
 * Black hole which pulls things inside it
 */
public class Car extends Vehicle
{
    
    private boolean pulledOver;
    private int pulledOverDuration;
    
    public Car(VehicleSpawner origin, boolean slowdown) {
        super(origin); // call the superclass' constructor
        maxSpeed = 1.5 + ((Math.random() * 30)/5);
        speed = maxSpeed;
        originalSpeed = maxSpeed;
        notPolice = true;
        pulledOver = false;
        pulledOverDuration = 600;
        if (slowdown){
            maxSpeed = Math.max(1, maxSpeed / 2);
        }
        yOffset = 0;
        // decide if driver is a speeder or not
        if (Greenfoot.getRandomNumber(2) == 0){
            speeder = false;
        }
        else{
            speeder = true;
            maxSpeed = 4.5;
            originalSpeed = maxSpeed;
        }
    }
    
    public void act()
    {
        // black hole suck me in and stop further movement
        if (sucked){
            suckMeIn();
            return;
        }
        // if not currently pulled over by police
        if (!pulledOver){
            drive(); 
            checkHitPedestrian();
            if (checkEdge()){
                getWorld().removeObject(this);
            }
        }
        // if pulled over by a police
        // stop for some time and slow down and becomes a non-speeder
        else{
            if (--pulledOverDuration <= 0){
                speeder = false;
                maxSpeed = 3;
                originalSpeed = 3;
                if (changeLane()){
                    pulledOver = false;
                }
            }
        }
    }
    
    // caught by police?
    public boolean isCaught(){
        return pulledOver;
    }
    
    public void setCaught(boolean b){
        pulledOver = b;
    }
    /**
     * When a Car hit's a Pedestrian, it should knock it over
     */
    public boolean checkHitPedestrian () {
        Pedestrian p = (Pedestrian)getOneObjectAtOffset((int)speed + getImage().getWidth()/2, 0, Pedestrian.class);
        
        if (p != null){
            p.knockDown();
            return true;
        }
        return false;
    }
}
