package ca.ship.model;

import ca.ship.utils.Utilities;

import java.io.Serializable;

public class Container implements Serializable {
    private String identifier="Unknown";

    private MyNeatList<Pallet> pallets = new MyNeatList<>();
    private int containerSize=-1;

    public Container(String identifier, int length){
        setIdentifier(identifier);
        setContainerSize(length);
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getContainerSize() {
        return containerSize;
    }

    public MyNeatList<Pallet> getPallets() {
        return pallets;
    }


    public void setIdentifier(String identifier) {
        if (Utilities.validStringlength(identifier,10))
            this.identifier = identifier;
    }

    public void setContainerSize(int containerLength) {
        this.containerSize = (containerLength==10 || containerLength==20 || containerLength==40) ? containerLength*8*8 : -1;
    }

    public boolean addPallet(Pallet pallet){
        int size =0;
        for (int i=0; i<pallets.size();i++ )
            size+=pallets.get(i).getTotalSize(); //gets total size of all pallets in container
        if ((size + pallet.getTotalSize()) <= getContainerSize()) {
            pallets.add(pallet);//adds pallet and returns true if the pallet can fit
            return true;
        } return false;//returns false and doesn't add if it can't
    }

    public int getAvailableSpace(){
        int palletSpace = 0;
        for (Pallet pallet : pallets)
            palletSpace+=pallet.getTotalSize();
        return containerSize-palletSpace;
    }

    @Override
    public String toString() {
        return "CONTAINER: " +
                "|ID = " + identifier  +
                "|, |SIZE = " + containerSize + "|";
    }
}
