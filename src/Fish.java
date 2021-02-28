import java.awt.*;
import java.security.SecureRandom;

/**
 * Should extend with Delegation
 *
 * @author ryanbosher
 */
public class Fish extends Thread {
	public static final double SIZE_INCREASE = 0.4;
	public static final int MOVEMENT_DENOMINATOR = 200;
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final int SLEEP = 50;
	private static final int COLOUR_MAX = 254;
	private static final int NOISE_CONDITION = 6;
	private static final double MIN_SIZE = 3.0;
	private static final double MAX_SIZE = 19.0;
	private static final double MAX_SPEED = 7.5;
	private static final double NOISE_ADJUSTMENT = 0.6;
	private static int worldHeight = 1;
	private static int worldWidth = 1;
	private final Color[] colour = new Color[3];
	private final FishShoal shoal;
	private double size;
	private double xPosition;
	private double yPosition;
	private double xMovement;
	private double yMovement;
	private boolean isAlive;
	
	public Fish(FishShoal shoal) {
		this.shoal = shoal;
		size = RANDOM.nextInt((int) MAX_SIZE) + MIN_SIZE;
		xPosition = RANDOM.nextInt(worldWidth);
		yPosition = RANDOM.nextInt(worldWidth);
		xMovement = xPosition / MOVEMENT_DENOMINATOR;
		yMovement = yPosition / MOVEMENT_DENOMINATOR;
		isAlive = true;
		for (int i = 0; i < colour.length; i++) {
			colour[i] = new Color(RANDOM.nextInt(COLOUR_MAX) + 1, RANDOM.nextInt(COLOUR_MAX) + 1, RANDOM.nextInt(COLOUR_MAX) + 1);
		}
	}
	
	static void setWorldWidth(int worldWidth) {
		Fish.worldWidth = worldWidth;
	}
	
	static void setWorldHeight(int worldHeight) {
		Fish.worldHeight = worldHeight;
	}
	
	double getxPosition() {
		return xPosition;
	}
	
	double getyPosition() {
		return yPosition;
	}
	
	double getSize() {
		return size;
	}
	
	@Override
	public void run() {
		while (!interrupted() && isAlive) {
			move();
			try {
				// Bus waiting
				sleep(RANDOM.nextInt(SLEEP));
			} catch (InterruptedException ignored) {
			}
		}
	}
	
	// Would be nice to make this 1 reusable function, called 4 times.
	private void move() {
		// Flip fish if they smack edges
		if (xPosition <= 0 || xPosition > worldWidth - size / 2) {
			// Stop fish getting stuck on top of world
			if (xPosition == 0) {
				++xPosition;
			} else if (xPosition >= worldHeight) {
				--xPosition;
			}
			xMovement = -1 * xMovement;
		}
		if (yPosition <= 0 || yPosition > worldHeight - size / 2) {
			if (yPosition == 0) {
				++yPosition;
			} else if (yPosition >= worldWidth) {
				--yPosition;
			}
			yMovement = -1 * yMovement;
		}
		
		// Add noise randomly to X
		if (RANDOM.nextInt(NOISE_CONDITION) == 0) {
			if (xMovement > -MAX_SPEED && xMovement < MAX_SPEED) {
				if (RANDOM.nextBoolean()) {
					xMovement += NOISE_ADJUSTMENT;
				} else {
					xMovement -= NOISE_ADJUSTMENT;
				}
			} else if (xMovement > MAX_SPEED) {
				xMovement -= NOISE_ADJUSTMENT;
			} else if (xMovement < -MAX_SPEED) {
				xMovement += NOISE_ADJUSTMENT;
			}
		}
		// Add noise randomly to Y
		if (RANDOM.nextInt(NOISE_CONDITION) == 0) {
			if (yMovement > -MAX_SPEED && yMovement < MAX_SPEED) {
				if (RANDOM.nextBoolean()) {
					yMovement += NOISE_ADJUSTMENT;
				} else {
					yMovement -= NOISE_ADJUSTMENT;
				}
			} else if (yMovement > MAX_SPEED) {
				yMovement -= NOISE_ADJUSTMENT;
			} else if (yMovement < -MAX_SPEED) {
				yMovement += NOISE_ADJUSTMENT;
			}
		}
		
		xPosition += xMovement;
		yPosition += yMovement;
		
		Fish target = shoal.canEat(this);
		if (target != null) {
			eat(target);
		}
	}
	
	public void eat(Fish targetFish) {
		double targetFishSize = targetFish.size;
		size += targetFishSize * SIZE_INCREASE;
		targetFish.kill();
	}
	
	public synchronized void kill() {
		// fish is kill. no.
		//		System.out.println(this + ": i am kill");
		isAlive = false;
		shoal.remove(this);
	}
	
	public void draw(Graphics2D graphics) {
		double speed = StrictMath.sqrt(StrictMath.pow(xMovement, 2) + StrictMath.pow(yMovement, 2));
		double velX = size * xMovement / (2 * speed);
		double velY = size * yMovement / (2 * speed);
		
		graphics.setColor(colour[0]);
		graphics.drawLine((int) xPosition, (int) yPosition, (int) (xPosition - velX + velY), (int) (yPosition - velX - velY));
		
		graphics.setColor(colour[1]);
		graphics.drawLine((int) xPosition, (int) yPosition, (int) (xPosition - 2 * velX), (int) (yPosition - 2 * velY));
		
		graphics.setColor(colour[2]);
		graphics.drawLine((int) xPosition, (int) yPosition, (int) (xPosition - velX - velY), (int) (yPosition + velX - velY));
		
	}
}
