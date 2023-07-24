package pt.upskill.projeto1.rogue.tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;
import pt.upskill.projeto1.rogue.utils.Vector2D;
import pt.upskill.projeto1.rogue.utils.Position;

public class PositionTest {

	private Position position = new Position(2, 3);
	
	@Before
	public void prepare() {
		
	}
	
	@Test
	public void testConstructor() {
		assertEquals(2, position.getX());
		assertEquals(3, position.getY());
	}

	@Test
	public void testPlus() {
		
		assertEquals(new Position(5, 5), position.plus(new Vector2D(3, 2)));
		assertEquals(new Position(0, 0), position.plus(new Vector2D(-2, -3)));
	}

}
