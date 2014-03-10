package net.neoremind.navi.client.selector;

public enum NaviFailStrategy {

	FAILOVER(1), 
	FAILFAST(2);

	private int strategy;
	
	private NaviFailStrategy(int strategy) {
		this.strategy = strategy;
	}

	public int getStrategy() {
		return strategy;
	}
	
	public void setStrategy(int strategy) {
		this.strategy = strategy;
	}
	
}
