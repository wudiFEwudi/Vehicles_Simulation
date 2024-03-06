import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Effect here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Effect extends Actor
{
    protected int totalActs, actCounter;
    
    public Effect (int totalActs){
        this.totalActs = totalActs;
        actCounter = totalActs;
    }
    
    
    public void act()
    {
        // subtract duration
        if (actCounter > 0){
            actCounter--;
        }
    }
}