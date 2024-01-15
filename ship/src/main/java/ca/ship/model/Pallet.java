package ca.ship.model;

import ca.ship.utils.Utilities;

import java.io.Serializable;

public class Pallet implements Serializable {
    private String description="Unknown";
    private int quantity = -1;
    private double unitValue=-1;
    private double totalSize=-1;
    private double totalWeight=-1;

    public Pallet(String description, int quantity, double unitValue, double size, double weight){
        if (Utilities.validStringlength(description,100))
            this.description = description;
        else this.description = Utilities.truncateString(description,100);
        setQuantity(quantity);
        setTotalSize(size);
        setTotalWeight(weight);
        setUnitValue(unitValue);
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitValue() {
        return unitValue;
    }

    public double getTotalSize() {
        return totalSize;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setDescription(String description) {
        if (Utilities.validStringlength(description,100))
            this.description = description;
    }

    public void setQuantity(int quantity) {
        if (quantity<Integer.MAX_VALUE && quantity>=0)
            this.quantity = quantity;
    }

    public void setTotalSize(double totalSize) {
        if (totalSize<=2560 && totalSize>=0)
            this.totalSize = Utilities.toTwoDecimalPlaces(totalSize);
    }

    public void setTotalWeight(double totalWeight) {
        if (totalWeight<Double.MAX_VALUE && totalWeight>=0)
            this.totalWeight = Utilities.toTwoDecimalPlaces(totalWeight);
    }

    public void setUnitValue(double unitValue) {
        if (unitValue<Double.MAX_VALUE && unitValue>=0)
            this.unitValue = unitValue;
    }

    public double getTotalValue(){
        return unitValue*quantity;
    }

    @Override
    public String toString() {
        return "PALLET: " +
                "| DESCRIPTION = '" + description + '\'' +
                "|, | QUANTITY = " + quantity +
                "|, | unitValue=" + unitValue +
                "|, | totalSize=" + totalSize +
                "|, | totalWeight=" + totalWeight +
                "|, | TOTAL VALUE = " + getTotalValue()+"|";
    }
}
