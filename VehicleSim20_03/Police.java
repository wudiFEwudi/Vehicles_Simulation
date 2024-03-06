import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

public class Police extends Vehicle
{
    
    private boolean atWork;
    private int workDuration;
    private GreenfootSound sound = new GreenfootSound("policesiren.mp3");
    
    public Police(VehicleSpawner origin, boolean slowdown) {
        super(origin); // call the superclass' constructor
        maxSpeed = Math.min(4, 1.5 + ((Math.random() * 30)/5));
        speed = maxSpeed;
        originalSpeed = maxSpeed;
        speeder = false;
        if (slowdown){
            maxSpeed = Math.max(1, maxSpeed / 2);
        }
        yOffset = 0;
        notPolice = false; // I am police
        atWork = false; // pulling someone over or not
        workDuration = 600;
    }
    
    public void act()
    { 
        // black hole suck me in and stop further movement
        if (sucked){
            suckMeIn();
            return;
        }
        // if pulling someone over currently then stop for some time
        if (atWork){
            // down working with speeder
            if (workDuration-- <= 0){
                // if changed lane then stop police siren
                if (changeLane()){
                    atWork = false;
                    maxSpeed = originalSpeed;
                    workDuration = 500;
                    sound.stop();
                }
            }
        }
        // regular driving and cheking speeders
        else{
            drive();
            checkHitPedestrian();
            checkSpeeders();
        }
        if (checkEdge()){
            VehicleWorld v = (VehicleWorld) getWorld();
            ArrayList<Vehicle> vehicles = (ArrayList<Vehicle>) v.getObjects(Vehicle.class);
            for (Vehicle V : vehicles){
                V.policehere(false);
            }
            getWorld().removeObject(this);
        }
    }
    
    public boolean checkHitPedestrian () {
        Pedestrian p = (Pedestrian)getOneObjectAtOffset((int)speed + getImage().getWidth()/2, 0, Pedestrian.class);
        
        if (p != null){
            p.knockDown();
            return true;
        }
        return false;
    }
    
    // pulls them to the side
    public void pullOver(Car V){
        int yPos;
        // if speeder on other side of the road do nothing
        if (V.getDirection() == direction){
            sound.setVolume(90);
            sound.play();
            V.setCaught(true);
            atWork = true;
            // determine which corner to pull them into
            if (direction == 1){
                yPos = 576;
            }
            else{
                yPos = 206;
            }
            setLocation(35, yPos);
            if (isTouching(Vehicle.class)){
                V.setLocation(800 - V.getImage().getWidth(), yPos);
                setLocation(800 - V.getImage().getWidth() - image.getWidth() - 5, yPos);
            }
            else{
                V.setLocation(image.getWidth() + 5 + V.getImage().getWidth() / 2, yPos);
            }
        
        }
    }
    
    // get surrounding cars and check if they are a speeder
    public void checkSpeeders(){
        ArrayList<Car> vehicles = (ArrayList<Car>) getObjectsInRange(200, Car.class);
        for (Car V : vehicles){
            if (V != null){
                // if they are a speeder and not caught and I am not dealing with other speeders 
                if (V.isSpeeder() && !V.isCaught() && 100 <= V.getX() && V.getX() <= 750 && !atWork){
                    pullOver(V); // pull the car over
                    break;
                }
            }
        }
    }
    
    // override the change lane
    // only changes lane when done pulling speeder over
    // don't change lane when driving regularly
    public boolean changeLane(){
        if (552 <= getY() && getY() <= 600){
            if (!checkpoints(true)){
                setLocation(getX(), getY() - 48);
                return true;
            }
        }
        else if (getY() <= 240){
            if (!checkpoints(false)){
                setLocation(getX(), getY() + 48);
                return true;
            }
        }
        return false;
    }
    
    // called by MJ class
    public void stopSound(){
        if (sound.isPlaying()){
            sound.stop();
        }
    }
    
    public void removeMe(){
        if (getWorld() != null){
            sound.stop();
            getWorld().removeObject(this);
        }
    }
}
