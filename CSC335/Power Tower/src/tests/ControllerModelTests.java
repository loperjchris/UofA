package tests;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import game.InnerMessage;
import game.TowerDefenseBoard;
import game.TowerDefenseController;
import game.TowerDefenseView;
import game.ViewModel;
import viewable.cards.abilityCards.AbilityCard;
import viewable.cards.abilityCards.DamageCard;
import viewable.gameObjects.Boss;
import viewable.gameObjects.Minion;
import viewable.gameObjects.Player;

class ControllerModelTests {

	@Test
	void ControllerTest01() throws IOException {
		TowerDefenseController cont = new TowerDefenseController(new TowerDefenseView());
		AbilityCard aCard = new DamageCard();
		cont.getBoard();
		cont.getMapArray();
		cont.endTurn();
		cont.useTowerCard(0, 0);
		cont.canUpgrade(0, 0);
		Player p = new Player(cont);
		Minion minion = new Boss(p);
		cont.damageOther(minion);
		cont.killMinion(minion);
		cont.setSelectedCard(aCard);
		cont.setOtherPlayerFinished(false);
		cont.getPlayer();
		cont.getOtherPlayer();
		cont.getMarket();
		cont.getView();
		cont.setMinionsFinished(true);
		cont.isPaused();
		cont.setPaused(false);
		cont.isRunning();
		cont.setRunning(true);
		cont.setConnected(true);
		cont.getServer();
		cont.hasConnected();
		cont.getSocket();
		cont.getOut();
	}
	
	@Test
	void ModelTest01() {
		ViewModel m = new ViewModel(1,1);
		m.getHeight();
		m.setHeight(1);
		m.getWidth();
		m.setWidth(1);
		m.getEffectiveWidth();
		m.addSubWidth(1);
		m.setWidth(1);
		m.getSubtractedHeight();
		m.addSubHeight(1);
		m.getEffectiveBoardHeight();
		m.getCurrentRow();
		m.setCurrentRow(1);
		m.getCurrentCol();
		m.setCurrentCol(1);	
	}
	
	@Test
	void BoardTests() throws IOException {
		TowerDefenseView v = new TowerDefenseView();
		TowerDefenseController c = new TowerDefenseController(v);
		TowerDefenseBoard b = new TowerDefenseBoard(v, c);
		//b.triggerMinions();
		b.flip();
		b.setView(v);
		b.getBoard();
		b.getMarket();
		
	}
	
	@Test
	void InnerMessageTest() {
		InnerMessage m = new InnerMessage();
	}

}
