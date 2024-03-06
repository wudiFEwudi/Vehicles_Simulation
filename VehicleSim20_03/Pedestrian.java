import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Pedestrian that tries to walk across the street
 */
public abstract class Pedestrian extends SuperSmoothMover
{
    protected double speed;
    protected double maxSpeed;
    protected int direction;
    protected boolean awake, isnight;
    protected int nightduration;
    protected int hitByBus;
    protected boolean sucked;
    protected int suckDuration = 120;
    protected boolean isCreeper = false;
    protected int holeX, holeY;
    protected GreenfootImage image;
    
    public Pedestrian(int direction) {
        // choose a random speed
        maxSpeed = Math.random() * 2 + 1;
        speed = maxSpeed;
        // start as awake 
        awake = true;
        isnight = false; // for night purposes
        sucked = false; // for black hole purposes
        image = getImage();
        nightduration = 120;
        hitByBus = -1; // for bus purposes
        this.direction = direction;
    }

    /**
     * Act - do whatever the Pedestrian wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // If there is a v
        
        if (awake){
            if (getOneObjectAtOffset(0, (int)(direction * getImage().getHeight()/2 + (int)(direction * speed)), Vehicle.class) == null){
                setLocation (getX(), getY() + (int)(speed * direction));
            }
            if (direction == -1 && getY() < 100){
                getWorld().removeObject(this);
            } else if (direction == 1 && getY() > 550){
                getWorld().removeObject(this);
            }
        }
        // if it is night time 
        if (isnight){
            getImage().setTransparency(nightduration); // gradually disappear
            if (--nightduration == 0){
                removeMe(); 
            }
        }
        // if hit by bus then after some time remove me 
        if (hitByBus != -1){
            hitByBus--;
            if (hitByBus == 0){
                removeMe();
            }
        }
    }

    /**
     * Method to cause this Pedestrian to become knocked down - stop moving, turn onto side
     */
    public void knockDown () {
        speed = 0;
        setRotation (90);
        awake = false;
    }
    
    protected void removeMe(){
        if (getWorld() != null){
            getWorld().removeObject(this);
        }
    }
    
    protected void setNight(boolean b){
        isnight = b;
    }
    /**
     * Method to allow a downed Pedestrian to be healed
     */
    protected void healMe () {
        speed = maxSpeed;
        setRotation (0);
        awake = true;
    }
    
    protected void getsucked(int X, int Y){
        sucked = true;
        holeX = X;
        holeY = Y;
    }
    
    // sucked by a black hole
    protected void suckMeIn(){
        if (sucked){
            setLocation(holeX, holeY); // set location to the center of black hole
            suckDuration--;
            setRotation(120 - suckDuration); // rotate
            image.setTransparency(suckDuration); // gradually disappear
            if (suckDuration <= 0){
                removeMe();
            }
        }
    }
    
    protected boolean isAwake () {
        return awake;
    }
    
    protected void setSpeed(double s){
        speed = s;
    }
    
    protected void goFullSpeed(){
        speed = maxSpeed;
    }
    
    // called when hit by bus
    protected void stop(){
        speed = 0;
        awake = false;
        hitByBus = 60;
    }
    
    protected double getSpeed(){
        return speed;
    }
    
    protected boolean getCreeper(){
        return isCreeper;
    }
}
