import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * 
 * @author (Michael Chen) 
 * @version (March 30, 2022)
 * 
 * Details of the simulation:
 * Worldwide effects:
 * Night - makes all pedestrians except creepers to fade away and slow down all cars
 * Michael Jackson - puts up a dance show and everyone except ambulance and zombie car halt to apprieciate the dance
 * 
 * Local effects:
 * Blackhole - sucking all pedestrians and vehicles to the center and make them spin and gradually make them disappear
 * Explosion - triggered by creepers, remove all pedestrians and vehicles within a radius
 * Snowlane - snow trail made by the ZombieCar, slows down all vehicles of that lane and trips pedestrians except creepers. PVZ!
 * Police - pulls over speeders and slows nearby non-speeder drivers
 * 
 * Pedestrians:
 * Creepers - spawn at day and night. When hit by a car flashes and explodes just like Minecraft creepers
 * Dancer - MJ's dancers
 * MichaelJackson - Rarely visits the city and puts up a insane dance show
 * Poor - Poor guy who just wants to cross the road
 * Wealthy - They are busy, so you might see them turnaround and run because they forgot something
 * WeirdGuy - This guy just wants to move randomly
 * 
 * Vehicles:
 * Ambulance - heal and save any pedestrians who was knocked down
 * Bus - picks up all pedestrians including creepers. Creepers need a bus ride too
 * Car - can be a speeder or not, just a regular car
 * Police - makes all nearby non-speeders slow down, and pulls over speeders to the side
 * 
 * Credits:
 * Music - www.zapslat.com, www.youtube.com, https://y2mate.is/en84/youtube-to-mp3.html for converting youtube to mp3
 * Images - all images came from Google search and edited using https://resizeimage.net
 * Code - no borrowed code
 */


public class VehicleWorld extends World
{
    private GreenfootImage background;

    // Color Constants
    public static Color GREY_BORDER = new Color (108, 108, 108);
    public static Color GREY_STREET = new Color (88, 88, 88);
    public static Color YELLOW_LINE = new Color (255, 216, 0);

    // Instance variables / Objects
    private boolean twoWayTraffic, splitAtCenter;
    private int laneHeight, laneCount, spaceBetweenLanes;
    private int[] lanePositionsY;
    private VehicleSpawner[] laneSpawners;
    private boolean isnight = false; // is it night time
    private int nightduration = 2000;
    private boolean MJpresent = false;
    private int MJvisit = 6000;
    private int number_of_holes = 0;
    private GreenfootSound noise = new GreenfootSound("traffic_noise.mp3");
    
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public VehicleWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600, 1, false); 

        setPaintOrder (Pedestrian.class, Bus.class, Car.class, Ambulance.class);

        // set up background
        background = new GreenfootImage ("background01.png");
        setBackground (background);

        // Set critical variables
        laneCount = 6;
        laneHeight = 48;
        spaceBetweenLanes = 6;
        splitAtCenter = true;
        twoWayTraffic = true;
    
        // Init lane spawner objects 
        laneSpawners = new VehicleSpawner[laneCount];

        // Prepare lanes method - draws the lanes
        lanePositionsY = prepareLanes (this, background, laneSpawners, 222, laneHeight, laneCount, spaceBetweenLanes, twoWayTraffic, splitAtCenter);
        
        // set the background sound
        noise.setVolume(0);
        noise.play();
        noise.stop();
        noise.setVolume(60);
    }
    
    public void act () {
        spawn();
        MJvisit--; // countdown for MJ show
        // play background sound if not playing
        if (!noise.isPlaying()){
            noise.play();
        }
    }
    
    public void setMJpresent(boolean b){
        MJpresent = false;
    }
    
    public void setNight(boolean b){
        isnight = b;
    }
    
    // change the backgound and call the prepareLanes method
    // used when Night effect comes
    public void changeBackground(GreenfootImage image){
        setBackground(image);
        lanePositionsY = prepareLanes (this, image, null, 222, laneHeight, laneCount, spaceBetweenLanes, twoWayTraffic, splitAtCenter);
    }
    
    // Add or subtract the number of black holes 
    public void modifyB(int a){
        number_of_holes += a;
    }
    
    private void spawn () {
        // Chance to spawn a vehicle
        if (Greenfoot.getRandomNumber (60) == 0 && !MJpresent){
            int lane = Greenfoot.getRandomNumber(6);
            // is night = true means newly spawned vehicles will have lower speed
            if (!laneSpawners[lane].isTouchingVehicle()){
                int vehicleType = Greenfoot.getRandomNumber(5);
                if (vehicleType == 0){ // add car
                    addObject(new Car(laneSpawners[lane], isnight), 0, 0);
                } else if (vehicleType == 1 && !isnight){ // add bus when not night
                    addObject(new Bus(laneSpawners[lane], isnight), 0, 0);
                } else if (vehicleType == 2){ // add ambulance
                    addObject(new Ambulance(laneSpawners[lane], isnight), 0, 0);
                } else if (vehicleType == 3){ // add police
                    if (Greenfoot.getRandomNumber(2) == 0){ // smaller chance of spawning
                        addObject(new Police(laneSpawners[lane], isnight), 0, 0);
                    }
                } else if (vehicleType == 4){ // add zombie car when no other zombie cars are present in the world
                    if (Greenfoot.getRandomNumber(20) == 0 && getObjects(ZombieCar.class).size() == 0){
                        addObject(new ZombieCar(laneSpawners[lane]), 0, 0);
                    }
                }
                
            }
        }

        // Chance to spawn a Pedestrian if MJ is not present
        if (Greenfoot.getRandomNumber (60) == 0 && !MJpresent){
            int xSpawnLocation = Greenfoot.getRandomNumber (600) + 100; // random between 99 and 699, so not near edges
            boolean spawnAtTop = Greenfoot.getRandomNumber(2) == 0 ? true : false; // spawn at top or bottom
            int spawnType = Greenfoot.getRandomNumber(4);
            int dir, ySpawnLocation;
            if (spawnAtTop){
                dir = 1;
                ySpawnLocation = 50;
            } else {
                dir = -1;
                ySpawnLocation = 550;
            }
            // if night time half chance making spawnType 2 which spawns a creeper
            // spawnType = -1 means spawn nothing
            if (isnight){
                if (Greenfoot.getRandomNumber(2) == 0){
                    spawnType = 2;
                }
                else{
                    spawnType = -1;
                }
            }
            if (spawnType == 0){
                addObject (new Wealthy (dir), xSpawnLocation, ySpawnLocation); // add wealthy guy
            }
            else if (spawnType == 1){
                addObject (new Poor (dir), xSpawnLocation, ySpawnLocation); // add poor guy
            }
            else if (spawnType == 2 && Greenfoot.getRandomNumber(2) == 0){ // add a creeper with a smaller chance of spawning
                addObject (new Creeper (dir), xSpawnLocation, ySpawnLocation);
            }
            else if (spawnType == 3){ // add a weird guy
                addObject(new WeirdGuy (dir), xSpawnLocation, ySpawnLocation);
            }
        }
        
        // Night time cycles every 2000 acts
        if (nightduration > 0){
            nightduration--;
            if (nightduration == 0)
            {
                if (!MJpresent){
                    addObject(new Night(1000), 0, 0);
                }
                nightduration = 2000;
            }
        }
        
        // Michael Jackson comes in!!
        if (!MJpresent && Greenfoot.getRandomNumber(1500) == 0 && !isnight && MJvisit <= 0){
            addObject(new MichaelJackson(1), 400, 50);
            MJpresent = true;
            MJvisit = 4000;
        }
        
        // add a black hole if number of holes < 2 and MJ not present
        // small chance of spawning
        if (!MJpresent && Greenfoot.getRandomNumber(1500) == 0 && number_of_holes < 2){
            int randX = Greenfoot.getRandomNumber(600) + 100;
            int randY = Greenfoot.getRandomNumber(300) + 200;
            addObject(new Blackhole(500), randX, randY); 
        }
        
    }

       /**
     * <p>The prepareLanes method is a static (standalone) method that takes a list of parameters about the desired roadway and then builds it.</p>
     * 
     * <p><b>Note:</b> So far, Centre-split is the only option, regardless of what values you send for that parameters.</p>
     *
     * <p>This method does three things:</p>
     * <ul>
     *  <li> Determines the Y coordinate for each lane (each lane is centered vertically around the position)</li>
     *  <li> Draws lanes onto the GreenfootImage target that is passed in at the specified / calculated positions. 
     *       (Nothing is returned, it just manipulates the object which affects the original).</li>
     *  <li> Places the VehicleSpawners (passed in via the array parameter spawners) into the World (also passed in via parameters).</li>
     * </ul>
     * 
     * <p> After this method is run, there is a visual road as well as the objects needed to spawn Vehicles. Examine the table below for an
     * in-depth description of what the roadway will look like and what each parameter/component represents.</p>
     * 
     * <pre>
     *                  <=== Start Y
     *  ||||||||||||||  <=== Top Border
     *  /------------\
     *  |            |  
     *  |      Y[0]  |  <=== Lane Position (Y) is the middle of the lane
     *  |            |
     *  \------------/
     *  [##] [##] [##| <== spacing ( where the lane lines or borders are )
     *  /------------\
     *  |            |  
     *  |      Y[1]  |
     *  |            |
     *  \------------/
     *  ||||||||||||||  <== Bottom Border
     * </pre>
     * 
     * @param world     The World that the VehicleSpawners will be added to
     * @param target    The GreenfootImage that the lanes will be drawn on, usually but not necessarily the background of the World.
     * @param spawners  An array of VehicleSpawner to be added to the World
     * @param startY    The top Y position where lanes (drawing) should start
     * @param heightPerLane The height of the desired lanes
     * @param lanes     The total number of lanes desired
     * @param spacing   The distance, in pixels, between each lane
     * @param twoWay    Should traffic flow both ways? Leave false for a one-way street (Not Yet Implemented)
     * @param centreSplit   Should the whole road be split in the middle? Or lots of parallel two-way streets? Must also be two-way street (twoWay == true) or else NO EFFECT
     * 
     */
    public static int[] prepareLanes (World world, GreenfootImage target, VehicleSpawner[] spawners, int startY, int heightPerLane, int lanes, int spacing, boolean twoWay, boolean centreSplit){
        // Declare an array to store the y values as I calculate them
        int[] lanePositions = new int[lanes];
        // Pre-calculate half of the lane height, as this will frequently be used for drawing.
        // To help make it clear, the heightOffset is the distance from the centre of the lane (it's y position)
        // to the outer edge of the lane.
        int heightOffset = heightPerLane / 2;

        // draw top border
        target.setColor (GREY_BORDER);
        target.fillRect (0, startY, target.getWidth(), spacing);

        // Main Loop to Calculate Positions and draw lanes
        for (int i = 0; i < lanes; i++){
            // calculate the position for the lane
            lanePositions[i] = startY + spacing + (i * (heightPerLane+spacing)) + heightOffset ;

            // draw lane
            target.setColor(GREY_STREET); 
            // the lane body
            target.fillRect (0, lanePositions[i] - heightOffset, target.getWidth(), heightPerLane);
            // the lane spacing - where the white or yellow lines will get drawn
            target.fillRect(0, lanePositions[i] + heightOffset, target.getWidth(), spacing);

            // Place spawners and draw lines depending on whether its 2 way and centre split
            if (twoWay && centreSplit){
                // first half of the lanes go rightward (no option for left-hand drive, sorry UK students .. ?)
                if ( i < lanes / 2){
                    if (spawners != null){
                        spawners[i] = new VehicleSpawner(false, heightPerLane);
                        world.addObject(spawners[i], target.getWidth(), lanePositions[i]);
                    }
                } else { // second half of the lanes go leftward
                    if (spawners != null){
                        spawners[i] = new VehicleSpawner(true, heightPerLane);
                        world.addObject(spawners[i], 0, lanePositions[i]);
                    }
                }

                // draw yellow lines if middle 
                if (i == lanes / 2){
                    target.setColor(YELLOW_LINE);
                    target.fillRect(0, lanePositions[i] - heightOffset - spacing, target.getWidth(), spacing);

                } else if (i > 0){ // draw white lines if not first lane
                    for (int j = 0; j < target.getWidth(); j += 120){
                        target.setColor (Color.WHITE);
                        target.fillRect (j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                    }
                } 

            } else if (twoWay){ // not center split
                if ( i % 2 == 0){
                    if (spawners != null){
                        spawners[i] = new VehicleSpawner(false, heightPerLane);
                        world.addObject(spawners[i], target.getWidth(), lanePositions[i]);
                    }
                } else {
                    if (spawners != null){
                        spawners[i] = new VehicleSpawner(true, heightPerLane);
                        world.addObject(spawners[i], 0, lanePositions[i]);
                    }
                }

                // draw Grey Border if between two "Streets"
                if (i > 0){ // but not in first position
                    if (i % 2 == 0){
                        target.setColor(GREY_BORDER);
                        target.fillRect(0, lanePositions[i] - heightOffset - spacing, target.getWidth(), spacing);

                    } else { // draw dotted lines
                        for (int j = 0; j < target.getWidth(); j += 120){
                            target.setColor (YELLOW_LINE);
                            target.fillRect (j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                        }
                    } 
                }
            } else { // One way traffic
                if (spawners != null){
                    spawners[i] = new VehicleSpawner(true, heightPerLane);
                    world.addObject(spawners[i], 0, lanePositions[i]);
                }
                if (i > 0){
                    for (int j = 0; j < target.getWidth(); j += 120){
                        target.setColor (Color.WHITE);
                        target.fillRect (j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                    }
                }
            }
        }
        // draws bottom border
        target.setColor (GREY_BORDER);
        target.fillRect (0, lanePositions[lanes-1] + heightOffset, target.getWidth(), spacing);

        return lanePositions;
    }

}
