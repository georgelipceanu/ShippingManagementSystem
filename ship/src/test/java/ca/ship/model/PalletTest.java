package ca.ship.model;

import ca.ship.model.Pallet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PalletTest {

    @Test
    void validateTotalValue() {
        Pallet p = new Pallet("goods",20,20,30,30);
        assertEquals(400,p.getTotalValue());
    }
}