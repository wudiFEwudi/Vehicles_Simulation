import greenfoot.*;

public class Wealthy extends Pedestrian
{    
    private boolean turnAround; // turned around or not
    
    public Wealthy(int direction){
        super(direction);
        speed = 1;
        maxSpeed = 1;
        turnAround = false;
    }
    
    public void act()
    {
        // If hit a black hole suck me in
        if (sucked){
            suckMeIn();
            return;
        }
        if (awake){
            // decides randomly to turn around and speed up
            if (!turnAround && Greenfoot.getRandomNumber(600) == 0 && getY() > 200 && getY() < 450){
                direction *= -1;
                turnAround = true;
                speed = 2;
            }
            // move
            if (getOneObjectAtOffset(0, (int)(direction * getImage().getHeight()/2 + (int)(direction * speed)), Vehicle.class) == null){
                setLocation (getX(), getY() + (int)(speed * direction));
            }
            if (direction == -1 && getY() < 100){
                getWorld().removeObject(this);
            } else if (direction == 1 && getY() > 550){
                getWorld().removeObject(this);
            }
        }
        // if night time slowly disappear
        if (isnight){
            getImage().setTransparency(nightduration);
            if (--nightduration == 0){
                removeMe();
            }
        }
        // if hit by bus pick me up
        if (hitByBus != -1){
            hitByBus--;
            if (hitByBus == 0){
                getWorld().removeObject(this);
            }
        }
    }
}

