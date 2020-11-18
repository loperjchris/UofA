package controller;

import java.util.ArrayList;
import java.util.List;

import model.MenuItem;
import model.SalesQuery;

public class SalesLog {
	private List<MenuItem> sales;

	public SalesLog() {
		sales = new ArrayList<>();
	}
	
	public void addItem(MenuItem in) {
		sales.add(in);
	}
	
	public double runQuery(SalesQuery query) {
		double result = 0.0;
		for(MenuItem item: sales) {
			result += query.execute(item);
		}
		return result;
	}
}	
