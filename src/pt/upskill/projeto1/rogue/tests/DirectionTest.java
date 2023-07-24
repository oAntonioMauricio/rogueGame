package pt.upskill.projeto1.rogue.tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import pt.upskill.projeto1.rogue.utils.Vector2D;
import pt.upskill.projeto1.rogue.utils.Direction;

public class DirectionTest {

	@Test
	public void testAsVector() {
		assertEquals(Direction.UP.asVector(), new Vector2D(0, -1));
		assertEquals(Direction.DOWN.asVector(), new Vector2D(0, 1));
		assertEquals(Direction.LEFT.asVector(), new Vector2D(-1, 0));
		assertEquals(Direction.RIGHT.asVector(), new Vector2D(1, 0));
	}

}
