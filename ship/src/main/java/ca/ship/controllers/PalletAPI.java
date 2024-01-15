package ca.ship.controllers;

import ca.ship.HelloApplication;
import ca.ship.model.*;
import ca.ship.utils.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PalletAPI implements Initializable {
    private static PalletAPI palletAPI;
    public static PalletAPI getPalletAPI() {
        return palletAPI;
    }

    @FXML
    private TextField description,size,unitValue,weight,quantity,searchBar;

    @FXML
    private ListView<String> listOfPallets, searchedListOfPallets;
    public ListView<String> getListOfPallets() {
        return listOfPallets;
    }


    public ListView<String> getSearchedListOfPallets() {
        return searchedListOfPallets;
    }

    @FXML
    private ChoiceBox<String> containers;

    public ChoiceBox<String> getContainers() {
        return containers;
    }



    @FXML
    public void addPalletToContainer() {
        if (!(description.getText().isEmpty() && quantity.getText().isEmpty()&& size.getText().isEmpty() && weight.getText().isEmpty()&&unitValue.getText().isEmpty())) {//checks if boxes are filled
            String palletDesc = description.getText();
            int palletQuantity = Integer.parseInt(quantity.getText());
            double palletSize = Double.parseDouble(size.getText());
            double palletWeight = Double.parseDouble(weight.getText());
            double palletUnitValue = Double.parseDouble(unitValue.getText());

            if (!containers.getItems().isEmpty() && containers.getValue()!=null) {//checks if there are containers available for adding pallets
                String strContainerToAddTo = containers.getValue();
                Container containerToAddTo = null;
                for (Container container : HelloApplication.getContainers()) {//searches for container selected
                    if (strContainerToAddTo.equals(container.toString())) {
                        containerToAddTo = container;
                        break;
                    }
                }

                Pallet palletToAdd = new Pallet(palletDesc, palletQuantity, palletUnitValue, palletSize, palletWeight);
                if (palletToAdd.getQuantity() != -1 && palletToAdd.getTotalWeight() != -1 && palletToAdd.getTotalSize() != -1 && palletToAdd.getUnitValue() != -1) {
                    if (containerToAddTo.addPallet(palletToAdd)) {//checks if container has enough space for pallet
                        listOfPallets.getItems().add(palletToAdd.toString() + " In: " + containerToAddTo.getIdentifier());
                        HelloApplication.getPallets().add(palletToAdd); //adds pallet
                    } else Utilities.showWarningAlert("WARNING!", "This pallet is too big for the container");
                }else Utilities.showWarningAlert("WARNING!", "Enter valid details");
            }else Utilities.showWarningAlert("WARNING!", "Choose container");
        }else Utilities.showWarningAlert("WARNING!", "Fill in details");
    }


    @FXML
    public void smarterSmartAdd() {
        //adds pallet to container with most available space, prioritises ports over ships since (in my opinion) it would make
        //more sense to try load a pallet into a container that's at a port rather than bringing the pallet up onto a ship and loading
        //it into a container from there
        if (!(description.getText().isEmpty() && quantity.getText().isEmpty()&& size.getText().isEmpty() && weight.getText().isEmpty()&&unitValue.getText().isEmpty())) {//checks if boxes are filled

            String palletDesc = description.getText();
            int palletQuantity=0;
            double palletSize=0;
            double palletWeight=0;
            double palletUnitValue=0;
            boolean validNumbers=true;
            try {//checks if numbers are entered in the correct text fields
                palletQuantity = Integer.parseInt(quantity.getText());
                palletSize = Double.parseDouble(size.getText());
                palletWeight = Double.parseDouble(weight.getText());
                palletUnitValue = Double.parseDouble(unitValue.getText());

            } catch (NumberFormatException e){
                validNumbers=false;//sets valid numbers to false otherwise
            }

            Pallet palletToAdd = new Pallet(palletDesc, palletQuantity, palletUnitValue, palletSize, palletWeight);
            if (validNumbers && palletToAdd.getQuantity() != -1 && palletToAdd.getTotalWeight() != -1 && palletToAdd.getTotalSize() != -1 && palletToAdd.getUnitValue() != -1) {//checks if valid details are entered
                boolean canFit=false;
                Container containerWithMostSpaceAtAPort = null;//initialising container with most space
                for (Port port : HelloApplication.getPorts())
                    for (Container container : port.getContainers())//going through all containers at every port
                        if (container!=null){
                            containerWithMostSpaceAtAPort=container; //getting the first container that is not null so it's available space can be compared, leaves value if no containers at ports
                            break;
                        }
                if (containerWithMostSpaceAtAPort!=null) {//checks if there are containers on ships
                    for (Port port : HelloApplication.getPorts())
                        for (Container container : port.getContainers())  //searching through all docked containers at each port
                            if (containerWithMostSpaceAtAPort.getAvailableSpace() < container.getAvailableSpace())
                                containerWithMostSpaceAtAPort = container;//sets the container with most space to the current container in loop if it has more available space

                    if (containerWithMostSpaceAtAPort.addPallet(palletToAdd)) {//checks if it can be added, says that there's no space if not
                        listOfPallets.getItems().add(palletToAdd.toString() + " In: " + containerWithMostSpaceAtAPort.getIdentifier());
                        HelloApplication.getPallets().add(palletToAdd); //adds pallet
                        canFit = true;
                    }
                }
                if (!canFit){//checking if it was added already
                    Container containerWithMostSpaceOnAShip= null;
                    for (Port port : HelloApplication.getPorts())
                        for (Ship ship : port.getShipList())
                            for (Container container : ship.getContainers()) //runs through all containers in all docked ships at each port
                                if (container!=null) {
                                    containerWithMostSpaceOnAShip = container;//getting the first container that is not null so it's available space can be compared, leaves value if no containers on docked ships
                                    break;
                                }
                    if (containerWithMostSpaceOnAShip!=null) {//checking if there are containers on ships
                        for (Port port : HelloApplication.getPorts())
                            for (Ship ship : port.getShipList())
                                for (Container container : ship.getContainers()) //runs through all containers in all docked ships at each port
                                    if (containerWithMostSpaceOnAShip.getAvailableSpace() < container.getAvailableSpace())
                                        containerWithMostSpaceOnAShip = container;//sets the container with most space to the current container in loop if it has more available space
                        if (containerWithMostSpaceOnAShip.addPallet(palletToAdd)) {//checks if it can be added, says that there's no space if not
                            listOfPallets.getItems().add(palletToAdd.toString() + " In: " + containerWithMostSpaceOnAShip.getIdentifier());
                            HelloApplication.getPallets().add(palletToAdd); //adds pallet
                            Utilities.showWarningAlert("ADDED", "Pallet successfully added to Container: " + containerWithMostSpaceOnAShip.getIdentifier());
                        }else Utilities.showWarningAlert("WARNING!", "No available containers can fit a pallet of this size");
                    }else Utilities.showWarningAlert("WARNING!", "There are no available containers at the moment");
                } else Utilities.showWarningAlert("ADDED", "Pallet successfully added to Container: " + containerWithMostSpaceAtAPort.getIdentifier());
            } else Utilities.showWarningAlert("WARNING!", "Fill in valid details");
        }else Utilities.showWarningAlert("WARNING!", "Fill in details");
    }

    @FXML
    public void removePallet(){
        if (listOfPallets.getSelectionModel().getSelectedItem()!=null) {//checks if a pallet is selected
            String strPalletToRemove = listOfPallets.getSelectionModel().getSelectedItem();
            Container containerToRemoveFrom = null;

            for (Container container : HelloApplication.getContainers())
                if (strPalletToRemove.contains(container.getIdentifier()))
                    containerToRemoveFrom = container; //searches for container that pallet is in

            for (Pallet pallet : HelloApplication.getPallets()) {
                if (strPalletToRemove.contains(pallet.toString())) { //searches for selected pallet in system
                    listOfPallets.getItems().remove(strPalletToRemove);
                    HelloApplication.getPallets().remove(pallet);
                    containerToRemoveFrom.getPallets().remove(pallet);//removes pallet
                    break;
                }
            }
        }else Utilities.showWarningAlert("WARNING!","Select a pallet");
    }

    @FXML
    public void searchForPalletsWithGoods(){
        if (!searchBar.getText().isEmpty()) {
            searchedListOfPallets.getItems().clear();
            String goods = searchBar.getText();
            MyNeatList<String> strSearchedPallets = new MyNeatList<>();

            for (Container container : HelloApplication.getContainers()) {
                for (Pallet pallet : container.getPallets()) {
                    if (pallet.getDescription().toLowerCase().contains(goods.toLowerCase())) {//searches through all containers and pallets to find goods
                        strSearchedPallets.add(pallet.toString() + " In: " + container.getIdentifier());
                    }
                }
            }

            if (!strSearchedPallets.isEmpty()) {//checks if any goods were found
                searchedListOfPallets.getItems().addAll(strSearchedPallets);//adds them to listview
            }else Utilities.showWarningAlert("WARNING!","No goods of this description found");
        }else Utilities.showWarningAlert("WARNING!","Search bar is empty");
    }

    public void setTotalValue(){
        double total = 0;
        for (Pallet pallet : HelloApplication.getPallets())
            total+=(pallet.getUnitValue())*(pallet.getQuantity());//gets each pallet in the system and adds it to total value
        GoodsAPI.getGoodsAPI().getTotalValue().setText("Total Value: €" + total);
    }

    public void setTotalValueOfAllPorts(){
        double total = 0;
        for (Port port : HelloApplication.getPorts()) {//goes through each port
            for (Ship ship : port.getShipList())
                for (Container container : ship.getContainers())
                    for (Pallet pallet : container.getPallets()) //gets each pallet at the ships at the port and adds it to total value of ports
                        total+=pallet.getUnitValue()*pallet.getQuantity();
            for (Container container : port.getContainers())
                for (Pallet pallet : container.getPallets())
                    total+=pallet.getUnitValue()*pallet.getQuantity(); //gets each pallet in the containers at the port and adds it to total value of all ports
        }
        GoodsAPI.getGoodsAPI().getValueOfPorts().setText("All ports: €" + total);// updates the label
    }

    public void setTotalValueOfAllShips(){
        double total = 0;
        for (Ship ship : HelloApplication.getShips())//goes through every ship
            for (Container container : ship.getContainers())
                for (Pallet pallet : container.getPallets())
                    total+=pallet.getUnitValue()*pallet.getQuantity(); //gets each pallet on the ship and adds it to total value of all ships
        GoodsAPI.getGoodsAPI().getValueOfShips().setText("All ships: €" + total); //updates label
    }


    public void setTotalValueOfAllShipsAtSea(){
        double total = 0;
        for (Ship ship : HelloApplication.getShipsAtSea())
            for (Container container : ship.getContainers())
                for (Pallet pallet : container.getPallets())
                    total+=pallet.getUnitValue()*pallet.getQuantity();//gets each pallet on the ship and adds it to total value of all ships at sea
        GoodsAPI.getGoodsAPI().getValueOfShipsAtSea().setText("All ships at sea: €" + total);
    }

    @FXML
    public void goodsView(){
        GoodsAPI.getGoodsAPI().getListOfGoods().getItems().clear(); //clears list from last time
        setTotalValue();
        setTotalValueOfAllPorts();
        setTotalValueOfAllShips();
        setTotalValueOfAllShipsAtSea(); //sets all total values in the labels
        for (Port port : HelloApplication.getPorts()){//goes through all ports in the system
            double totalOfPort = 0;//sets total value of port
            GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add(port.toString());//shows port
            for (Ship ship : port.getShipList()){//goes through all ships in the port
                double totalForShip=0;//sets total value of ship
                GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("---------------"+ship.toString());//shows ship
                for (Container container : ship.getContainers()){//goes through all containers in the ship
                    double totalForContainer=0;//sets total value of container
                    GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("------------------------------"+container.toString());//shows container
                    for (Pallet pallet : container.getPallets()){
                        GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("--------------------------------------------------"+pallet.toString());//shows pallet
                        totalOfPort+= pallet.getTotalValue();
                        totalForShip+=pallet.getTotalValue();
                        totalForContainer+=pallet.getTotalValue();//adds value of pallet to total of port, ship and container
                    }
                    GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("--------------------------------------------------");//break
                    GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("------------------------------Total value of Container: " + container.getIdentifier() + " = €" + totalForContainer);//shows container and value
                }
                GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("------------------------------");
                GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("---------------Total value of Ship: " + ship.getName() + " = €" + totalForShip);//shows ship and value
                GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("---------------");
            }
            for (Container container : port.getContainers()){
                double totalForContainer=0;
                GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("---------------"+container.toString());
                for (Pallet pallet : container.getPallets()){
                    GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("------------------------------"+pallet.toString());
                    totalOfPort+= pallet.getTotalValue();
                    totalForContainer+=pallet.getTotalValue();//adds value of pallet to total of port and container
                }
                GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("---------------Total value of Container: " + container.getIdentifier() + " = €" + totalForContainer);//shows container and value
                GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("---------------");
            }
            GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("Total value of Port: " + port.getName() + " = €" + totalOfPort); //shows port and value
            GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("--------------------------------------------------");
        }

        for (Ship ship : HelloApplication.getShipsAtSea()){//goes through all ships at sea in system
            double total = 0;//sets total for ships
            GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("---------------"+ship.toString());//shows ship
            for (Container container : ship.getContainers()){//goes through all containers on ship
                double totalForContainer = 0;
                GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("------------------------------"+container.toString());//shows container
                for (Pallet pallet : container.getPallets()){
                    GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("--------------------------------------------------"+pallet.toString());//shows pallet and value
                    total+= pallet.getUnitValue()*pallet.getQuantity();
                    totalForContainer+= pallet.getUnitValue()*pallet.getQuantity();//adds value of pallet to total of ship and container
                }
                GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("------------------------------");
                GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("------------------------------Total value of Container: " + container.getIdentifier() + " = €" + totalForContainer);//shows container and value
            }
            GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("---------------");
            GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("Total value of Ship At Sea: " + ship.getName() + " = €" + total);//shows ship and value
            GoodsAPI.getGoodsAPI().getListOfGoods().getItems().add("--------------------------------------------------");
        }


        HelloApplication.mainStage.setScene(HelloApplication.goodS); //changes scene
    }

    @FXML
    public void goBack()throws IOException {
        HelloApplication.mainStage.setScene(HelloApplication.portsS);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        palletAPI=this;
    }
}
