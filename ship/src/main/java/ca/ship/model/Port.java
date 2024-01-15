package ca.ship.model;


import ca.ship.utils.Utilities;

import java.io.Serializable;

public class Port implements Serializable {
    private int internationalPortCode = -1;

    private String name = "Unknown";

    private String country = "Unknown";

    private MyNeatList<Ship> shipList = new MyNeatList<>();

    private MyNeatList<Container> containers = new MyNeatList<>();


    public Port(String name, String country, int code) {
        if (Utilities.validStringlength(name,25))
            this.name = name;
        else this.name = Utilities.truncateString(name,25);

        if (Utilities.validStringlength(country,25))
            this.country = country;
        else this.country = Utilities.truncateString(country,25);

        setInternationalPortCode(code);
    }

    public String getName() {
        return name;
    }

    public int getInternationalPortCode() {
        return internationalPortCode;
    }

    public String getCountry() {
        return country;
    }

    public MyNeatList<Ship> getShipList() {
        return shipList;
    }

    public MyNeatList<Container> getContainers() {
        return containers;
    }

    public void setInternationalPortCode(int code) {
        if (Utilities.validRange(code,0,999999))
            this.internationalPortCode = code;
    }

    public void setName(String name) {
        if (Utilities.validStringlength(name,25))
            this.name = name;
    }

    public void setCountry(String country) {
        if (Utilities.validStringlength(country,25))
            this.country = country;
    }

    @Override
    public String toString() {
        return "PORT: " +
                "|INTERNATIONAL PORT CODE = " + internationalPortCode +
                "|, |NAME = '" + name + '\'' +
                "|, |COUNTRY = '" + country + '\'' +
                '|';
    }
}
