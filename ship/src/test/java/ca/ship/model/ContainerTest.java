package ca.ship.model;

import ca.ship.model.Container;
import ca.ship.model.Pallet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContainerTest {

    @Test
    void addPalletTest() {
        Container c = new Container("3", 10);
        assertEquals(640,c.getContainerSize());
        Pallet p = new Pallet("can fit",4,4,4,4);
        assertTrue(c.addPallet(p));
        Pallet p2 = new Pallet("cant fit",4,4,637,4);//would be able to fit if p wasn't added first
        assertFalse(c.addPallet(p2));
    }

}