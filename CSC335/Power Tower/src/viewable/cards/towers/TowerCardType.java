package viewable.cards.towers;

import viewable.gameObjects.*;

public enum TowerCardType{
	ArcherTowerCard(ArcherTowerCard.class, ArcherTower.class), 
	CannonTowerCard(CannonTowerCard.class, CannonTower.class), 
	FreezeTowerCard(FreezeTowerCard.class, FreezeTower.class),
	CurrencyTowerCard(CurrencyTowerCard.class, CurrencyTower.class),
	MinionTowerCard(MinionTowerCard.class, MinionTower.class),
	MageTowerCard(MageTowerCard.class, MageTower.class);
	
	private Class<? extends TowerCard> c;
	private Class<? extends Tower> cT;
	private TowerCardType(Class<? extends TowerCard> c, Class<? extends Tower> cT) {
		this.c = c;
		this.cT = cT;
	}
	
	public Class<? extends Tower> getTower() {
		return cT;
	}
	
	public Class<? extends TowerCard> getTowerCard(){
		return c;
	}
}
