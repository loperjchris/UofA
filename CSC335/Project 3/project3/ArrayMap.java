import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class ArrayMap<K, V> extends AbstractMap<K, V> {
	
	// Variable fields used for ArrayMap construction.
	private Object[] keys;
	private Object[] values;
	private int size;
	
	/*
     * Purpose: Setups up an ArrayMap object to have all the right
     * arrays to keep track of the keys and values and an integer to keep track
     * of the size of the map.
     * 
     * @param None.
     * 
     * @return None.
     */
	public ArrayMap() {
		keys = new Object[10];
		values = new Object[10];
		size = 0;
	}
	
	/*
     * Purpose: Places a new key/value pair into the the arraymap. Replaces
     * the current value with the new given value if the given key is already
     * in the map. Also, doubles the size of the array once an object is going
     * to be added but the current array is full.
     * 
     * @param key - is a generic type K and represents the keys to be placed in
     * the map.
     * 
     * @param value - is a generic type V and represents the values that will
     * be mapped to keys in the arraymap.
     * 
     * @return None.
     */
	@SuppressWarnings("unchecked")
	@Override
	public V put(K key, V value) {
		// Doubling the size of keys and values if they are full.
		if (size == keys.length) {
			Object[] keysTemp = new Object[keys.length * 2];
			Object[] valuesTemp = new Object[values.length * 2];
			// Filling the temp arrays with the items of keys and values.
			for (int i = 0; i < keys.length; i++) {
				keysTemp[i] = keys[i];
				valuesTemp[i] = values[i];
			}
			// Replacing keys and values with the larger arrays.
			keys = keysTemp;
			values = valuesTemp;
		}
		// Checking if the key is already in the map and getting its index if
		// it is.
		int index = -1;
		for (int i = 0; i < size; i++) {
			if (keys[i].equals(key)) {
				index = i;
			}
		}
		// Replacing the current value if a key already in the map is given.
		if (index >= 0) {
			Object temp = values[index];
			keys[index] = key;
			values[index] = value;
			return (V) temp;
		}
		// Creating a new key/value pair.
		keys[size] = key;
		values[size] = value;
		size++;
		return null;
	}
	
	/*
     * Purpose: Returns the number of objects added to the map.
     * 
     * @param None.
     * 
     * @return size - an integer representing the number of objects in the map.
     */
	@Override
	public int size() {
		return size;
	}

	/*
     * Purpose: Creates an ArrayMapEntrySet in order for the ArrayMap class to
     * have access to an iterator.
     * 
     * @param None.
     * 
     * @return ArrayMapEntrySet object - which is a Set<Entry<K, V>> object
     * used to construct an iterator.
     */
	@Override
	public Set<Entry<K, V>> entrySet() {
		return new ArrayMapEntrySet();
	}
	
	private class ArrayMapEntrySet extends AbstractSet<Entry<K, V>> {
		
		/*
	     * Purpose: Creates an iterator for the arraymap.
	     * 
	     * @param None.
	     * 
	     * @return ArrayMapEntrySetIterator object which will be used to
	     * iterate over the arraymap.
	     */
		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new ArrayMapEntrySetIterator<Entry<K, V>>();
		}

		/*
	     * Purpose: Returns the number of objects in the arraymap.
	     * 
	     * @param None.
	     * 
	     * @return size - an integer representing the number of items in the
	     * arraymap.
	     */
		@Override
		public int size() {
			return size;
		}
		
		/*
	     * Purpose: Determines if the arraymap currently holds a given
	     * key/value pair.
	     * 
	     * @param o - an Object, more specifically an Entry that holds the key
	     * and value pair being tested.
	     * 
	     * @return a boolean if the entry is in the map or not.
	     */
		@Override
		public boolean contains(Object o) {
			if (o instanceof Entry) {
				@SuppressWarnings("unchecked")
				// Casting o to an Entry.
				Entry<K, V> entry = (SimpleEntry<K, V>) o;
				Iterator<Entry<K, V>> itr = iterator();
				// Iterating over the entries in the arraymap.
				while (itr.hasNext()) {
					Entry<K, V> inMap = itr.next();
					// Checking if the key is associated with the value
					if (entry.getKey().equals(inMap.getKey()) && entry.getValue().equals(inMap.getValue())) {
						return true;
					}
				}
			}
			return false;
		}
	
		private class ArrayMapEntrySetIterator<T>implements Iterator<T> {
			
			// Variable fields used for iterator construction.
			private int curPos;
			private boolean canRemove;
			
			/*
		     * Purpose: Initializes the variables used to construct a working
		     * iterator.
		     * 
		     * @param None.
		     * 
		     * @return None.
		     */
			public ArrayMapEntrySetIterator() {
				curPos = 0;
				// boolean variable used for remove method.
				canRemove = false;
			}
	
			/*
		     * Purpose: Determines if there is another unchecked item in the 
		     * arraymap.
		     * 
		     * @param None.
		     * 
		     * @return true if there is an unchecked item in the arraymap,
		     * false otherwise.
		     */
			@Override
			public boolean hasNext() {
				return curPos < size;
			}
	
			/*
		     * Purpose: Gives the entry pair at the current position of the 
		     * iterator and increments the position.
		     * 
		     * @param None.
		     * 
		     * @return n - an Entry of key/value pairs at the current position.
		     */
			@SuppressWarnings("unchecked")
			@Override
			public T next() {
				if (keys[curPos] != null) {
					Entry<K, V> n = new SimpleEntry<K, V>((K) keys[curPos], (V) values[curPos]);
					curPos++;
					// Setting boolean variable to show that next had been called
					// so remove can now be called again.
					canRemove = true;
					return (T) n;
				} else {
					throw new NoSuchElementException();
				}
				
			}
			
			/*
		     * Purpose: Removes the current key/value pair at the iterators
		     * position.
		     * 
		     * @param None.
		     * 
		     * @return None.
		     */
			@Override
			public void remove() {
				// Check if remove has been called twice before a next has been
				// called.
				if (canRemove) {
					canRemove = false;
					if (!keys[curPos].equals(null)) {
						// Temp variables hold current values.
						Object[] tempkeys = keys;
						Object[] tempvalues = values;
						int newPos = 0;
						for (int i = 0; i < size; i++) {
							if (curPos - 1 != i) {
								// All but current item are added back to the
								// original keys and values arrays.
								keys[newPos] = tempkeys[i];
								values[newPos] = tempvalues[i];
								newPos++;
							}
						}
						curPos--;
						size--;
					} 
				} else {
					throw new IllegalStateException();
				}
			}
			
		}
		
	}

}
