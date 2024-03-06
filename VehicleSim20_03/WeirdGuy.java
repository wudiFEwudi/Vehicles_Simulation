import greenfoot.*;

public class WeirdGuy extends Pedestrian
{    
    
    public WeirdGuy(int direction){
        super(direction);
    }
    
    public void act()
    {
        // hit by black hole suck me in
        if (sucked){
            suckMeIn();
            return;
        }
        if (awake){
            // simulate random movement because this guy is weird
            if (getOneObjectAtOffset(0, (int)(direction * getImage().getHeight()/2 + (int)(direction * speed)), Vehicle.class) == null){
                setLocation (getX(), getY() + (int)(speed * direction));
            }
            if (direction == -1 && getY() < 300){
                if (Greenfoot.getRandomNumber(240) == 0){
                    direction = 1;
                }
            }
            else if (direction == 1 && getY() > 300){
                if (Greenfoot.getRandomNumber(240) == 0){
                    direction = -1;
                }
            }
            if (getX() > 100 && getX() < 500){
                setLocation (getX() + (int)(speed * direction), getY()); 
            }
            if (direction == -1 && getY() < 100){
                getWorld().removeObject(this);
            } else if (direction == 1 && getY() > 550){
                getWorld().removeObject(this);
            }
            
        }
        // night time slowly disappear
        if (isnight){
            getImage().setTransparency(nightduration);
            if (--nightduration == 0){
                removeMe();
            }
        }
        // hit by bus pick me up
        if (hitByBus != -1){
            hitByBus--;
            if (hitByBus == 0){
                getWorld().removeObject(this);
            }
        }
    }
}

