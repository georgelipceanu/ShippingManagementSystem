package ca.ship.model;

import ca.ship.model.MyNeatList;
import ca.ship.model.Port;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyNeatListTest {

    @Test
    void sizeTest() {
        MyNeatList<Port> ports = new MyNeatList<>();
        Port p =new Port("3","4",4);
        ports.add(p);
        ports.add(p);
        ports.add(p);
        assertEquals(3,ports.size());
        ports.remove(p);
        assertEquals(2,ports.size());
    }

    @Test
    void addTest() {
        MyNeatList<Port> ports = new MyNeatList<>();
        Port p =new Port("3","4",4);
        ports.add(p);
        assertEquals(1,ports.size());
        assertEquals("3",ports.get(0).getName());
        assertEquals("4",ports.get(0).getCountry());
        assertEquals(4,ports.get(0).getInternationalPortCode());
        Port p3 = null;
        assertFalse(ports.add(p3));
    }

    @Test
    void removeTest(){
        MyNeatList<Port> ports = new MyNeatList<>();
        Port p =new Port("3","4",4);
        Port p1 =new Port("5","5",5);

        ports.add(p);
        assertEquals(1,ports.size());
        ports.remove(p);
        assertEquals(0,ports.size());

        ports.add(p);
        ports.add(p1);
        assertEquals(2,ports.size());
        ports.remove(p);
        assertEquals(1,ports.size());
    }
}