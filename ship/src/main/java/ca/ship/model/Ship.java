package ca.ship.model;

import ca.ship.utils.Utilities;

import java.io.Serializable;
import java.util.Objects;

public class Ship implements Serializable {
    private MyNeatList<Container> containers = new MyNeatList<>();
    private String name;
    private String shipIdentifier;
    private String flagState;
    private String photograph="NOTVALID";

    public Ship(String name, String id, String flagState, String photographURL){

        if (Utilities.validStringlength(name,25))
            this.name = name;
        else this.name = Utilities.truncateString(name,25);

        if (Utilities.validStringlength(id,10))
            this.shipIdentifier = id;
        else this.name = Utilities.truncateString(name,10);


        if (Utilities.validStringlength(flagState,25))
            this.flagState = flagState;
        else this.name = Utilities.truncateString(name,25);

        setPhotographURL(photographURL);

    }

    public String getName() {
        return name;
    }

    public String getShipIdentifier() {
        return shipIdentifier;
    }

    public String getFlagState() {
        return flagState;
    }

    public String getPhotograph() {
        return photograph;
    }

    public MyNeatList<Container> getContainers() {
        return containers;
    }

    public void setName(String name) {
        if (Utilities.validStringlength(name,25))
            this.name = name;
    }

    public void setPhotographURL(String photograph) {
        if (Utilities.isValidURL(photograph))
            this.photograph = photograph.toLowerCase();
    }

    public void setFlagState(String flagState) {
        if (Utilities.validStringlength(flagState,25))
            this.flagState = flagState;
    }

    public void setShipIdentifier(String shipIdentifier) {
        if (Utilities.validStringlength(shipIdentifier,10))
            this.shipIdentifier = shipIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return Objects.equals(containers, ship.containers) && Objects.equals(name, ship.name) && Objects.equals(shipIdentifier, ship.shipIdentifier) && Objects.equals(flagState, ship.flagState) && Objects.equals(photograph, ship.photograph);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containers, name, shipIdentifier, flagState, photograph);
    }

    @Override
    public String toString() {
        return "SHIP: " +
                "|NAME ='" + name + '\'' +
                "|, |SHIP ID = '" + shipIdentifier + '\'' +
                "|, |FLAG STATE = '" + flagState + '\'' +
                "|, |PHOTOGRAPH = '" + photograph + '\'' +
                '|';
    }
}
