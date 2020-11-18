package tests;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import game.TowerDefenseController;
import game.TowerDefenseView;
import viewable.Viewable;
import viewable.cards.*;
import viewable.cards.towers.ArcherTowerCard;
import viewable.gameObjects.*;

class ViewableGameObjectsTests {

	@Test
	void ArcherTowerTest() {
		Tower t = new ArcherTower();
		t.getResource();
	}
	
	@Test
	void BossTest() throws IOException {
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		Minion m = new Boss(p);
		m.getResource();
	}
	
	@Test
	void CannonTowerTest() {
		Tower t = new CannonTower();
		t.getResource();
	}
	
	@Test
	void ChargerTest() throws IOException {
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		Minion m = new Charger(p);
		m.getResource();
	}
	
	@Test
	void CurrencyTowerTest() {
		Tower t = new CurrencyTower();
		t.getResource();
	}
	
	@Test
	void DeckTest() {
		Deck f = new Deck();
		Card c = new ArcherTowerCard();
		Deck d = new Deck(30);
		d.drawCard();
		d.add(c);
		System.out.println(d);
		d.isEmpty();
		d.shuffle();
		d.drawCard();
		d.getDeck();
		d.empty();
		d.getSize();
		d.getDeckAsList();
	}
	
	@Test
	void FreezeTowerTest() {
		Tower t = new FreezeTower();
		t.getResource();
	}
	
	@Test
	void GoblinTest() throws IOException {
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		Minion m = new Goblin(p);
		m.getResource();
	}
	
	@Test
	void GoblinKnightTest() throws IOException {
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		Minion m = new GoblinKnight(p);
		m.getResource();
	}
	
	@Test
	void HoundTest() throws IOException {
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		Minion m = new Hound(p);
		m.getResource();
	}
	
	@Test
	void MageTowerTest() {
		Tower t = new MageTower();
		t.getResource();
	}
	
	@Test
	void MapTest() {
		Map m = new Map();
		m.setBoard(m.getBoard());
		m.flip();
		try {
			m.save("test.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			m.load("test.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	void MarketTest() throws IOException {
		TowerDefenseView v = new TowerDefenseView();
		TowerDefenseController c = new TowerDefenseController(v);
		Market m = new Market(v, c);
		m.populateForSale();
		Card card = new ArcherTowerCard();
		m.removeFromForSale(card);
		m.repopulateForSale();
		m.removeFromForSale(0);
		m.getRemovedIndex();
		m.repopulateImages();
	    m.getForSale();
	    m.setController(c);
	    m.setView(v);
	}
	
	@Test
	void MinionTest() throws IOException {
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		Minion m = new Charger(p);
		m.getPlayer();
		m.setHealth(100);
		m.setDamage(1);
		m.setSpeed(1);
		m.incrementStep();
		m.getStep();
		m.getHealth();
		m.getSpeed();
		m.getReward();
		m.takeDamage(1);
		m.getCurrentHealth();
		m.isDead();
		m.getDamage();
	}
	
	@Test
	void OgreTest() throws IOException {
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		Minion m = new Ogre(p);
		m.getResource();
	}
	
	@Test
	void PlayerTest() throws IOException {
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		Card card = new ArcherTowerCard();
		Deck deck = new Deck();
		p.getHand();
		p.discardHand();
		p.resetDraw();

		p.drawCards(5);
		p.isFinished();
		p.setComplete(false);
		p.gainLife(10);
		p.payLife(1);
		p.buffReward();
		p.damageOther(1);
		p.damageTaken(0);
		p.getHealth();
		p.setSelectedCard(card);
		p.getSelectedCard();
		p.getViewableHealth();
		p.getViewableGold();
		p.getDiscard();
		p.getCardHand();
		p.printCards(deck);
		
	}
	
	@Test
	void TowerTest() {
		Tower t = new ArcherTower();
		t.setAttack(1);
		t.setRange(1);
		t.setAttackSpeed(1.0);
		t.getAttack();
		t.getRange();
		t.getAttackSpeed();
		t.getName();
		t.startCooldown();
		t.endCooldown();
		t.canAttack();
		t.setUpgraded(true);
		t.getUpgraded();
	}
	
	@Test
	void WaveGeneratorTest() throws IOException {
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		WaveGenerator w = new WaveGenerator(p);
		w.generateRandom();
		w.addMinions(0);
	}
	
	@Test
	void WolfRiderTest() throws IOException {
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		Minion m = new WolfRider(p);
		m.getResource();
	}

	


}
