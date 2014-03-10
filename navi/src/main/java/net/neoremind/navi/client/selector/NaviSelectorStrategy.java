package net.neoremind.navi.client.selector;

public enum NaviSelectorStrategy {

	RANDOM(1), 
	ROUNDROBIN(2);

	private int strategy;
	
	private NaviSelectorStrategy(int strategy) {
		this.strategy = strategy;
	}

	public int getStrategy() {
		return strategy;
	}
	
	public void setStrategy(int strategy) {
		this.strategy = strategy;
	}
	
}
