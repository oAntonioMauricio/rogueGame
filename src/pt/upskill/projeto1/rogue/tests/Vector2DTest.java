package pt.upskill.projeto1.rogue.tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;
import pt.upskill.projeto1.rogue.utils.Vector2D;

public class Vector2DTest {
	
	private Vector2D vector = new Vector2D(2, 3);
	
	@Before
	public void prepare() {
		
	}
	
	@Test
	public void testConstructor() {
		assertEquals(2, vector.getX());
		assertEquals(3, vector.getY());
	}

	@Test
	public void testPlus() {		
		assertEquals(new Vector2D(5, 5), vector.plus(new Vector2D(3, 2)));
		assertEquals(new Vector2D(0, 0), vector.plus(new Vector2D(-2, -3)));
	}

	@Test
	public void testMinus() {		
		assertEquals(new Vector2D(-1, 1), vector.minus(new Vector2D(3, 2)));
		assertEquals(new Vector2D(4, 6), vector.minus(new Vector2D(-2, -3)));
	}

}
