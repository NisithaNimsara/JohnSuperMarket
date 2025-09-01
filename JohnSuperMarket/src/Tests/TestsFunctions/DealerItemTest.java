package TestsFunctions;

import org.example.johnsupermarket.Models.DealerItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DealerItemTest {
    @Test
    public void testDealerItemFields() {
        DealerItem item = new DealerItem("Chocolate", "Cadbury", 150.5, 20, "Snacks");

        assertEquals("Chocolate", item.getName());
        assertEquals("Cadbury", item.getBrand());
        assertEquals(150.5, item.getPrice());
        assertEquals(20, item.getQuantity());
        assertEquals("Snacks", item.getCategory());
    }
}