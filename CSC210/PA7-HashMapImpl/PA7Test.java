import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class PA7Test {

    @Test
    public void testPut() {
        MyHashMap<String, Integer> map = new MyHashMap<String, Integer>();
        map.put("B", 2);
        map.put("A", 1);
        map.put("C", 3);
        Assert.assertEquals("[],[key: A, value: 1],[key: B, value: 2],"
                + "[key: C, value: 3],[],[],[],[],[]", map.toString());
        Assert.assertNotEquals("[],[key: A, value: 1],[key: B, value: 15],"
                + "[key: C, value: 3],[],[],[],[],[]", map.toString());
        map.put("B", 15);
        Assert.assertEquals("[],[key: A, value: 1],[key: B, value: 15],"
                + "[key: C, value: 3],[],[],[],[],[]", map.toString());
    }

    @Test
    public void testGet() {
        MyHashMap<String, Integer> map = new MyHashMap<String, Integer>();
        map.put("B", 2);
        map.put("A", 1);
        map.put("C", 3);
        int A = map.get("A");
        int B = map.get("B");
        int C = map.get("C");
        Assert.assertEquals(1, A);
        Assert.assertEquals(2, B);
        Assert.assertEquals(3, C);
        Assert.assertNotEquals(1, B);
        map.put("B", 15);
        B = map.get("B");
        Assert.assertEquals(15, B);
    }

    @Test
    public void testContainsKey() {
        MyHashMap<String, Integer> map = new MyHashMap<String, Integer>();
        map.put("B", 2);
        map.put("A", 1);
        map.put("C", 3);
        Assert.assertTrue(map.containsKey("A"));
        Assert.assertTrue(map.containsKey("B"));
        Assert.assertTrue(map.containsKey("C"));
        Assert.assertFalse(map.containsKey("D"));
        Assert.assertFalse(map.containsKey("a"));
        Assert.assertFalse(map.containsKey("b"));
        Assert.assertFalse(map.containsKey("c"));
        map.put("c", 16);
        Assert.assertTrue(map.containsKey("c"));
    }

    @Test
    public void testKeySet() {
        MyHashMap<String, Integer> map = new MyHashMap<String, Integer>();
        map.put("B", 2);
        map.put("A", 1);
        map.put("C", 3);
        Set<String> keys = map.keySet();
        Assert.assertEquals("[A, B, C]", keys.toString());
        Assert.assertNotEquals("[A, B, C, D]", keys.toString());
        Assert.assertNotEquals("[A, C, B]", keys.toString());
    }

}
