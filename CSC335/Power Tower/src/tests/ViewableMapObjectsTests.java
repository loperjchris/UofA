package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import viewable.mapObjects.*;

class ViewableMapObjectsTests {

	@Test
	void PathTest() {
		Path p = new Path();
		p.getResource();
	}
	
	@Test
	void PlaceableTest() {
		Placeable p = new Placeable();
		p.getResource();
	}

}
