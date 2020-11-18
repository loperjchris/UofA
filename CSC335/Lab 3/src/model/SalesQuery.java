package model;

@FunctionalInterface
public interface SalesQuery {
	double execute(MenuItem item);
}
