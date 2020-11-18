package viewable.gameObjects;

public enum TowerType{
	ArcherTower(ArcherTower.class), 
	CannonTower(CannonTower.class), 
	FreezeTower(FreezeTower.class),
	CurrencyTower(CurrencyTower.class),
	MinionTower(MinionTower.class),
	MageTower(MageTower.class),
	Deleted(null);
	
	private Class<? extends Tower> c;
	private TowerType(Class<? extends Tower> c) {
		this.c = c;
	}
	
	public Class<? extends Tower> getTower() {
		return c;
	}
}
