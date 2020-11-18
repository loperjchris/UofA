package tests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import viewable.cards.towers.*;
import viewable.gameObjects.*;

class ViewableCardsTowersTests {
		
	@Test
	void ArcherTowerCardTest() {
		ArcherTowerCard c = new ArcherTowerCard();
		ArcherTower t = new ArcherTower();
		c.getResource();
		c.getTower();
		c.Upgrade(t);
	}
	
	@Test
	void CannonTowerCardTest() {
		CannonTowerCard c = new CannonTowerCard();
		CannonTower t = new CannonTower();
		c.getResource();
		c.getTower();
		c.Upgrade(t);
	}
	
	@Test
	void CurrencyTowerCard() {
		CurrencyTowerCard c = new CurrencyTowerCard();
		CannonTower t = new CannonTower();
		c.getResource();
		c.getTower();
		c.Upgrade(t);
	}
	
	@Test
	void FreezeTowerCard() {
		FreezeTowerCard c = new FreezeTowerCard();
		FreezeTower t = new FreezeTower();
		c.getResource();
		c.getTower();
		c.Upgrade(t);
	}
	
	@Test
	void MageTowerCard() {
		MageTowerCard c = new MageTowerCard();
		MageTower t = new MageTower();
		c.getResource();
		c.getTower();
		c.Upgrade(t);
	}
	
	@Test
	void MinionTowerCard() {
		MinionTowerCard c = new MinionTowerCard();
		MinionTower t = new MinionTower();
		c.getResource();
		c.getTower();
		c.Upgrade(t);
	}
}
