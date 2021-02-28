import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ryanbosher
 */
public class FishShoal {
	public static final double EAT_DISTANCE_X = 1.5;
	public static final double EAT_DISTANCE_Y = 1.2;
	public static final double EAT_DIFFICULTY = 0.6;
	private final List<Fish> fishList;
	
	public FishShoal() {
		fishList = new ArrayList<>();
	}
	
	public synchronized void add(Fish fish) {
		fishList.add(fish);
	}
	
	public synchronized void remove(Fish fish) {
		// Sanity check:
		if (!fish.isAlive()) {
			fishList.remove(fish);
		}
		
	}
	
	public void draw(Graphics2D graphics) {
		// Can't draw nothing.
		if (!fishList.isEmpty()) {
			for (Fish fish : fishList) {
				// Tell each fish to draw passing parent graphics through.
				fish.draw(graphics);
			}
		}
	}
	
	public Fish canEat(Fish eater) {
		Fish eaten = null;
		// Can't eat nothing.
		if (!fishList.isEmpty()) {
			for (Fish target : fishList) {
				// Can't eat self
				if (target != eater) {
					// Check X is within range
					if (target.getxPosition() < eater.getxPosition() + eater.getSize() / EAT_DISTANCE_X && target.getxPosition() > eater.getxPosition() - eater.getSize() / EAT_DISTANCE_X) {
						// Check Y is within range
						if (target.getyPosition() < eater.getyPosition() + eater.getSize() / EAT_DISTANCE_Y && target.getyPosition() > eater.getyPosition() - eater.getSize() / EAT_DISTANCE_Y) {
							// Check Size is within range.
							if (eater.getSize() * EAT_DIFFICULTY > target.getSize()) {
								eaten = target;
							}
						}
					}
				}
			}
		}
		return eaten;
	}
}
