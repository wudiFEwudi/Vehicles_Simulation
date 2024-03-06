import greenfoot.*;

public class Creeper extends Pedestrian
{
    private int explosionDuration = 120;
    private boolean hit = false;
    
    public Creeper(int direction)
    {
        super(direction);
        isCreeper = true;
    }
    
    public void act(){
        // if being sucked stop exploding or moving
        if (sucked){
            suckMeIn();
            return;
        }
        // if not exploded yet move as regular or just got hit
        if (explosionDuration > 60){
            if (getOneObjectAtOffset(0, (int)(direction * getImage().getHeight()/2 + (int)(direction * speed)), Vehicle.class) == null){
                setLocation (getX(), getY() + (int)(speed * direction));
            }
        }
        // if not exploded yet
        if (explosionDuration == 120){
            if (direction == -1 && getY() < 100){
                getWorld().removeObject(this);
            } else if (direction == 1 && getY() > 550){
                getWorld().removeObject(this);
            }
        }
        // if hit by a car
        if (hit){
            // flash every 20 acts
            if (explosionDuration % 20 == 0){
                getImage().setTransparency(100);
            }
            else{
                getImage().setTransparency(255);
            }
            explosionDuration--;
            // if about to explode
            if (explosionDuration == 0){
                // add a explosion of some size
                int explosionsize = Greenfoot.getRandomNumber(100) + 100;
                getWorld().addObject(new Explosion (explosionsize), getX(), getY());
                getWorld().removeObject(this);
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
    
    // override knockDown
    public void knockDown(){
        hit = true;
        speed = 0.5;
    }
    
    public void healMe(){
        knockDown();
    }
}
