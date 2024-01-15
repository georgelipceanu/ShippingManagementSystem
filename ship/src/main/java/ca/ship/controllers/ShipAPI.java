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

public class ShipAPI implements Initializable {

    private static ShipAPI shipAPI;

    public static ShipAPI getShipAPI() {
        return shipAPI;
    }

    @FXML
    private TextField name, shipIdentifier, flagState, photograph;
    @FXML
    private ListView<String> listOfShip = new ListView<>();

    public ListView<String> getListOfShip() {
        return listOfShip;
    }


    @FXML
    private ChoiceBox<String> ports = new ChoiceBox<>();

    public ChoiceBox<String> getPorts() {
        return ports;
    }

    @FXML
    public void addShipAtSea() {
        if (!name.getText().isEmpty() && !shipIdentifier.getText().isEmpty()&& !flagState.getText().isEmpty() && !photograph.getText().isEmpty()) {//checks if boxes are filled
            String shipName = name.getText();
            String id = shipIdentifier.getText();
            String shipFlag = flagState.getText();
            String shipPhoto = photograph.getText();

            Ship shipToAdd = new Ship(shipName, id, shipFlag, shipPhoto);
            boolean validToAdd = true;

            if (!shipToAdd.getPhotograph().equals("NOTVALID")) { //checks validation
                for (Ship ship : HelloApplication.getShips()) {
                    if (shipToAdd.getShipIdentifier().equals(ship.getShipIdentifier())) {//checks unique id
                        validToAdd = false;
                        break;
                    }
                }
                if (validToAdd){//adds if all checks are passed
                    HelloApplication.getShips().add(shipToAdd); //adds ship to back end
                    HelloApplication.getShipsAtSea().add(shipToAdd);//adds ship to back end list of ships
                    listOfShip.getItems().add(shipToAdd.toString() + " At Sea");//adds ship to list
                } else Utilities.showWarningAlert("WARNING", "ID must be unique.");
            } else Utilities.showWarningAlert("WARNING", "Invalid URL");

        } else Utilities.showWarningAlert("WARNING", "Fill in the boxes.");
    }

    @FXML
    public void launchShip() {
        if (listOfShip.getSelectionModel().getSelectedItem() != null) {//checks if a ship is selected

            String strShipToLaunch = listOfShip.getSelectionModel().getSelectedItem(); //getting string of chosen ship


            Port portToRemoveFrom = null;
            for (Port port : HelloApplication.getPorts()) {
                if (strShipToLaunch.contains(String.valueOf(port.getInternationalPortCode()))) {
                    portToRemoveFrom = port;//checks to see if ship is at a port, assigns port if it is found
                    break;
                }
            }

            if (portToRemoveFrom != null) {
                for (Ship ship : HelloApplication.getShips()) {
                    if (strShipToLaunch.contains(ship.toString())) {
                        listOfShip.getItems().remove(strShipToLaunch);
                        listOfShip.getItems().add(ship.toString() + " At Sea");
                        ContainerAPI.getContainerAPI().getShips().getItems().remove(strShipToLaunch);
                        portToRemoveFrom.getShipList().remove(ship);
                        HelloApplication.getShipsAtSea().add(ship);//changes ship to at sea front end and back
                        break;
                    }
                }
            } else Utilities.showWarningAlert("WARNING!", "This ship is at sea already!");
        }else Utilities.showWarningAlert("WARNING!", "Select a Ship");
    }

    @FXML
    public void addShipAtPort() {
        if (!name.getText().isEmpty() && !shipIdentifier.getText().isEmpty() && !flagState.getText().isEmpty() && !photograph.getText().isEmpty()) {//checks if boxes are filled

            String shipName = name.getText();
            String portCountry = shipIdentifier.getText();
            String shipFlag = flagState.getText();
            String shipPhoto = photograph.getText();

            if (!ports.getItems().isEmpty() && ports.getValue()!=null) { //checks if port is chosen
                String strPortToAddTo = ports.getValue();
                Port portToAddTo = null;
                for (Port port : HelloApplication.getPorts()) {
                    if (strPortToAddTo.equals(port.toString())) { //searches for port selected in system
                        portToAddTo = port;
                        break;
                    }
                }
                Ship shipToAdd = new Ship(shipName, portCountry, shipFlag, shipPhoto);
                boolean validToAdd = true;

                if (!shipToAdd.getPhotograph().equals("NOTVALID")) { //checks validation
                    for (Ship ship : HelloApplication.getShips()) {
                        if (shipToAdd.getShipIdentifier().equals(ship.getShipIdentifier())) { //checks unique id
                            validToAdd = false;
                            break;
                        }
                    }
                    if (validToAdd) { //adds if all checks are passed
                        HelloApplication.getShips().add(shipToAdd);
                        portToAddTo.getShipList().add(shipToAdd);
                        listOfShip.getItems().add(shipToAdd.toString() + " At: " + portToAddTo.getInternationalPortCode()); //adds to ship list, ships at sea list and listview
                        ContainerAPI.getContainerAPI().getShips().getItems().add(shipToAdd.toString()); //available for container loading
                    } else Utilities.showWarningAlert("WARNING", "ID must be unique");

                } else Utilities.showWarningAlert("WARNING", "URL not valid.");
            } else Utilities.showWarningAlert("WARNING", "Select Port.");
        } else Utilities.showWarningAlert("WARNING", "Fill in the boxes.");
    }

    @FXML
    public void dockShip() {
        if (listOfShip.getSelectionModel().getSelectedItem()!=null) {//checks if a ship is selected
            String strShipToDock = listOfShip.getSelectionModel().getSelectedItem();

            if (!ports.getItems().isEmpty() &&  ports.getValue()!=null) { //checks if port is chosen

                String strPortToAddTo = ports.getValue();
                Port portToAddTo = null;
                for (Port port : HelloApplication.getPorts()) {//searches port selected in system
                    if (strPortToAddTo.equals(port.toString())) {
                        portToAddTo = port;
                        break;
                    }
                }

                boolean shipLaunched=false;

                if (portToAddTo != null) {
                    for (Ship ship : HelloApplication.getShipsAtSea()) {
                        if (strShipToDock.contains(ship.toString())) {//checks all ships at sea to see if ship selected is at sea
                            listOfShip.getItems().remove(strShipToDock);
                            listOfShip.getItems().add(ship.toString() + " At: " + portToAddTo.getInternationalPortCode());//updates ship string
                            HelloApplication.getShipsAtSea().remove(ship);//removes from ships at sea
                            portToAddTo.getShipList().add(ship);//adds ship to port
                            ContainerAPI.getContainerAPI().getShips().getItems().add(ship.toString()); //makes ship available for loading
                            shipLaunched=true;
                            break;
                        }
                    }
                    if (!shipLaunched){//checks if ship got launched
                        Utilities.showWarningAlert("WARNING","Ship is at a port already.");
                    }
                }
            }else Utilities.showWarningAlert("WARNING","Choose a port.");
        } else Utilities.showWarningAlert("WARNING","Choose a ship.");
    }

    @FXML
    public void drillDown(){
        if (listOfShip.getSelectionModel().getSelectedItem()!= null) { //checks if option is selected

            String strPortToDrillDown = listOfShip.getSelectionModel().getSelectedItem();
            Ship shipToDrillDown = null;
            for (Ship ship : HelloApplication.getShips()) {
                if (strPortToDrillDown.contains(ship.toString())) {
                    shipToDrillDown = ship; //matches option in list to ship in system
                    break;
                }
            }

            for (Container container : shipToDrillDown.getContainers()) {
                DrillDownAPI.getDrillDownAPI().getDrillDownView().getItems().add(container.toString());//listing all containers
                for (Pallet pallet : container.getPallets()) { //listing all pallets
                    DrillDownAPI.getDrillDownAPI().getDrillDownView().getItems().add("-----"+pallet.toString());
                }
            }


            HelloApplication.mainStage.setScene(HelloApplication.drillDownS);//switches to drilldown
        } else Utilities.showWarningAlert("WARNING!", "Select a Ship.");
    }

    @FXML
    public void goBack()throws IOException {
        HelloApplication.mainStage.setScene(HelloApplication.portsS);//goes back to port
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shipAPI=this;
    }
}

