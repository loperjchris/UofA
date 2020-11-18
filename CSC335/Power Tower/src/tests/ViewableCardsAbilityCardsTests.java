package tests;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import game.TowerDefenseController;
import game.TowerDefenseView;
import viewable.cards.abilityCards.*;
import viewable.gameObjects.Player;

class ViewableCardsAbilityCardsTests {

	@Test
	void DamageCardTest() throws IOException {
		AbilityCard c = new DamageCard();
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		c.ability(p);
		c.getResource();
	}
	
	@Test
	void DrawCardTest() throws IOException {
		AbilityCard c = new DrawCard();
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		c.ability(p);
		c.getResource();
	}
	
	@Test
	void HealCardTest() throws IOException {
		AbilityCard c = new HealCard();
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		c.ability(p);
		c.getResource();
	}
	
	@Test
	void IncreaseRewardCardTest() throws IOException {
		AbilityCard c = new IncreaseRewardCard();
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		c.ability(p);
		c.getResource();
	}
	
	@Test
	void NecromancySummonCardTest() throws IOException {
		AbilityCard c = new NecromancySummonCard();
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		c.ability(p);
		c.getResource();
	}
	
	@Test
	void PlunderCardTest() throws IOException {
		AbilityCard c = new PlunderCard();
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		c.ability(p);
		c.getResource();
	}
	
	@Test
	void SummonMinionCardTest() throws IOException {
		AbilityCard c = new SummonMinionCard();
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		Player p = new Player(cont);
		c.ability(p);
		c.getResource();
	}

}
