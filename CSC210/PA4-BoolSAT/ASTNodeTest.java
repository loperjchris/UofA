package bool_exp;

import org.junit.Assert;
import org.junit.Test;
public class ASTNodeTest {

    @Test
    public void testNullChildrenId() {
        ASTNode myNode = ASTNode.createIdNode("test");
        Assert.assertNull(myNode.child1);
        Assert.assertNull(myNode.child2);
    }

    @Test
    public void testNullChildrenNand() {
        ASTNode myNode = ASTNode.createNandNode(null, null);
        Assert.assertNull(myNode.child1);
        Assert.assertNull(myNode.child2);
    }

    @Test
    public void testASTNodesChildren() {
        ASTNode node1 = ASTNode.createIdNode("Node 1");
        ASTNode node2 = ASTNode.createIdNode("Node 2");
        ASTNode rootNode = ASTNode.createNandNode(node1, node2);
        Assert.assertNotNull(rootNode.child1);
        Assert.assertNotNull(rootNode.child2);
        Assert.assertTrue(rootNode.child1.isId());
        Assert.assertTrue(rootNode.child2.isId());
    }

    @Test
    public void testIsNand() {
        ASTNode node1 = ASTNode.createNandNode(null, null);
        ASTNode node2 = ASTNode.createIdNode("Node 2");
        Assert.assertTrue(node1.isNand());
        Assert.assertFalse(node2.isNand());
    }

    @Test
    public void testIsId() {
        ASTNode node1 = ASTNode.createNandNode(null, null);
        ASTNode node2 = ASTNode.createIdNode("Node 2");
        Assert.assertFalse(node1.isId());
        Assert.assertTrue(node2.isId());
    }

    @Test
    public void testGetId() {
        ASTNode node1 = ASTNode.createIdNode("Node 1");
        ASTNode node2 = ASTNode.createIdNode("Node 2");
        Assert.assertEquals("Node 1", node1.getId());
        Assert.assertNotEquals("Node 1", node2.getId());
    }

}
