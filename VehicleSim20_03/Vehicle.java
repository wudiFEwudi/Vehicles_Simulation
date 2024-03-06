import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * This is the superclass for Vehicles.
 * 
 */
public abstract class Vehicle extends SuperSmoothMover
{
    // instance variables
    protected double maxSpeed;
    protected double speed;
    protected double originalSpeed; // the speed I have when spawned
    protected int direction; // 1 = right, -1 = left
    protected boolean moving;
    protected boolean speeder; // speeder or not
    protected int yOffset;
    protected VehicleSpawner origin;
    protected boolean isnight = false; // night or not
    protected boolean policeNearby = false, notPolice; // for police purposes
    protected int honkTimes = 0;
    // black hole variables
    protected boolean sucked;
    protected int suckDuration = 120;
    protected int holeX, holeY;
    // sound
    protected GreenfootSound honk = new GreenfootSound("car_horn.mp3");
    protected GreenfootSound vroom = new GreenfootSound("vrooom.mp3");
    protected GreenfootImage image;
    
    protected abstract boolean checkHitPedestrian ();

    public Vehicle (VehicleSpawner origin) {
        this.origin = origin;
        moving = true;
        sucked = false;
        if (origin.facesRightward()){
            direction = 1;
        } else {
            direction = -1;
            getImage().mirrorHorizontally();
        }
        // set the sound 
        honk.setVolume(0);
        honk.play();
        honk.stop();
        honk.setVolume(100);
        vroom.setVolume(70);
        image = getImage();
    }
    
    public void addedToWorld (World w){
        setLocation (origin.getX() - (direction * 100), origin.getY() - yOffset);
    }

    /**
     * A method used by all Vehicles to check if they are at the edg
     */
    protected boolean checkEdge() {
        if (direction == 1){
            if (getX() > getWorld().getWidth() + 200){
                return true;
            }
        } else {
            if (getX() < -200){
                return true;
            }
        }
        return false;
    }
    
    protected void setNight(boolean b){
        isnight = b;
    }
    
    protected boolean isSpeeder(){
        return speeder;
    }
    
    protected void setSpeed(double s){
        maxSpeed = s;
    }
    
    protected void goFullSpeed(){
        maxSpeed = originalSpeed;
    }
    
    protected void policehere(boolean b){
        policeNearby = b;
    }
    
    // check surrounding police officers
    protected void checkPolice(){
        ArrayList<Police> polices = (ArrayList<Police>) getObjectsInRange(300, Police.class);
        if (polices.size() > 0){
            policehere(true);
        }
        else policehere(false);
    }
    
    protected int getDirection(){
        return direction;
    }
    
    protected void removeMe(){
        if (getWorld() != null){
            getWorld().removeObject(this);
        }
    }
    
    // called by black hole class
    protected void getsucked(int X, int Y){
        sucked = true;
        holeX = X;
        holeY = Y;
    }
    
    // suck me in when hit a black hole
    protected void suckMeIn(){
        if (sucked){
            setLocation(holeX, holeY);
            suckDuration--;
            setRotation(120 - suckDuration);
            image.setTransparency(suckDuration);
            if (suckDuration <= 0){
                removeMe();
            }
        }
    }
    
    // check collision in order lanes
    // used for lane change method
    protected boolean checkpoints(boolean up){
        int yOffSet = 50; // space between each lane is about 50
        int width = image.getWidth();
        if (up){ // if checking the upper lane 
            yOffSet *= -1; // multiply y by -1
        }
        // if the point directly above or below me has a car then return true
        if (getOneObjectAtOffset(0, yOffSet, Vehicle.class) != null){
            return true;
        }
        // check all points within an X range to leave enough space for lane change
        for (int i=0; i<=10; i++){
            if (getOneObjectAtOffset(width / 2 + i, yOffSet, Vehicle.class) != null){
                return true;
            }
        }
        for (int i=0; i<=10; i++){
            if (getOneObjectAtOffset(-1 * width / 2 - i, yOffSet, Vehicle.class) != null){
                return true;
            }
        }
        return false; // safe to lane change, no cars collided
    }
    
    // lane change algorithm
    protected boolean changeLane(){
        boolean changed = false;
        int posX = getX(), posY = getY();
        // if in the bottom lane the check the upper lane
        if (504 <= posY && posY <= 600){
            if (!checkpoints(true)){
                setLocation(posX, posY - 54);
                changed = true;
            }
        }
        // if the middle lane in the bottom half then check both sides 
        else if (450 <= posY && posY <= 498){
            if (!checkpoints(false)){
                setLocation(posX, posY + 54);
                changed = true;
            }
            else if (!checkpoints(true)){
                setLocation(posX, posY - 54);
                changed = true;
            }
        }
        // if the top lane in the bottom half then check the lane below it
        else if (396 <= posY && posY <= 444){
            if (!checkpoints(false)){
                setLocation(posX, posY + 54);
                changed = true;
            }
        }
        // if the bottom lane in the upper half then check the lane above it
        else if (342 <= posY && posY <= 390){
            if (!checkpoints(true)){
                setLocation(posX, posY - 54);
                changed = true;
            }
        }
        // if the middle lane in the upper half then check both sides
        else if (294 <= posY && posY <= 336){
            if (!checkpoints(false)){
                setLocation(posX, posY + 54);
                changed = true;
            }
            else if (!checkpoints(true)){
                setLocation(posX, posY - 54);
                changed = true;
            }
        }
        // if the upper lane in the upper half then check the lane below it
        else if (288 <= posY && posY <= 240){
            if (!checkpoints(false)){
                setLocation(posX, posY + 54);
                changed = true;
            }
        }
        // if not in all of those lanes
        // used for police pulling cars to the side
        else{
            if (!checkpoints(false)){
                setLocation(posX, posY + 54);
                changed = true;
            }
        }
        return changed;
    }
    /**
     * Method that deals with movement. Speed can be set by individual subclasses in their constructors
     */
    public void drive() 
    {
        // Ahead is a generic vehicle - we don't know what type BUT
        // since every Vehicle "promises" to have a getSpeed() method,
        // we can call that on any vehicle to find out it's speed
        
        Vehicle ahead = (Vehicle) getOneObjectAtOffset (direction * (int)(speed + getImage().getWidth()/2 + 4), 0, Vehicle.class);
        checkPolice(); // check if any police are near by
        if (ahead == null)
        {
            if (isnight){ // night time reduce speed by half
                speed = maxSpeed / 2;
            }
            else{
                speed = maxSpeed;
            }

        } else {
            // if not honked 
            if (honkTimes < 2){
                honkTimes++;
                honk.play(); // honk
            }
            // if changed lane with a speed >= 4 then play the vroom sound
            if (changeLane() && maxSpeed >= 4){
                vroom.play(); 
            }
            speed = ahead.getSpeed();
        }
        // if police is nearby and not a speeder and not a police myself then slow down
        if (policeNearby && !speeder && notPolice){
            speed = Math.min(speed, 1);
        }
        move (speed * direction);
    }   

    /**
     * An accessor that can be used to get this Vehicle's speed. Used, for example, when a vehicle wants to see
     * if a faster vehicle is ahead in the lane.
     */
    public double getSpeed(){
        return speed;
    }
}
