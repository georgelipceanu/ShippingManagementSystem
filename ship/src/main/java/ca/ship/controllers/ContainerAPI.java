package ca.ship.controllers;

import ca.ship.HelloApplication;
import ca.ship.model.Container;
import ca.ship.model.Pallet;
import ca.ship.model.Port;
import ca.ship.model.Ship;
import ca.ship.utils.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContainerAPI implements Initializable {

    private static ContainerAPI containerAPI;

    public static ContainerAPI getContainerAPI() {
        return containerAPI;
    }

    @FXML
    private TextField id;

    @FXML
    private RadioButton tenFT, twentyFT, fortyFT;

    @FXML
    private ChoiceBox<String> ports, ships;

    public ChoiceBox<String> getPorts() {
        return ports;
    }

    public ChoiceBox<String> getShips() {
        return ships;
    }

    @FXML
    private ListView<String> listOfContainers;

    public ListView<String> getListOfContainers() {
        return listOfContainers;
    }


    @FXML
    public void addContainerToPort() {
        if (!id.getText().isEmpty()) {//checks if details are filled
            String containerID = id.getText();
            int length = (tenFT.isSelected()) ? 10 : (twentyFT.isSelected()) ? 20 : (fortyFT.isSelected()) ? 40 : -1;


            if (!ports.getItems().isEmpty() &&  ports.getValue()!=null) { //checks if port is chosen
                String strPortToAddTo = ports.getValue();
                Port portToAddTo = null;
                for (Port port : HelloApplication.getPorts()) {
                    if (strPortToAddTo.equals(port.toString())) {//searches for chosen port
                        portToAddTo = port;
                        break;
                    }
                }


                Container containerToAdd = new Container(containerID, length);
                boolean validToAdd = true;

                for (Container container : HelloApplication.getContainers()) {
                    if (container.getIdentifier().equals(containerToAdd.getIdentifier())) { //checks for unique id
                        validToAdd = false;
                        break;
                    }
                }
                if (validToAdd) {
                    if (containerToAdd.getContainerSize() != -1 && !containerToAdd.getIdentifier().equals("Unknown")) { //checks for valid details
                        listOfContainers.getItems().add(containerToAdd.toString() + " At Port: " + portToAddTo.getInternationalPortCode());
                        portToAddTo.getContainers().add(containerToAdd);
                        HelloApplication.getContainers().add(containerToAdd);//adds container to system, port and lists and choiceboxes
                        PalletAPI.getPalletAPI().getContainers().getItems().add(containerToAdd.toString());
                    } else Utilities.showWarningAlert("WARNING!", "Enter valid details");
                } else Utilities.showWarningAlert("WARNING!", "Enter unique id");
            }else Utilities.showWarningAlert("WARNING!", "Choose a port");
        } else Utilities.showWarningAlert("WARNING!", "Fill in details");
    }


    @FXML
    public void addContainerToShip() {
        if (!id.getText().isEmpty()) {//checks if details are filled

            String containerID = id.getText();
            int length = (tenFT.isSelected()) ? 10 : (twentyFT.isSelected()) ? 20 : (fortyFT.isSelected()) ? 40 : -1;

            if (!ships.getItems().isEmpty() &&  ships.getValue()!=null) { //checks if ship is chosen

                String strShipToAddTo = ships.getValue();
                Ship shipToAddTo = null;
                for (Ship ship : HelloApplication.getShips()) {
                    if (strShipToAddTo.equals(ship.toString())) { //searches for ship selected
                        shipToAddTo = ship;
                        break;
                    }
                }

                Container containerToAdd = new Container(containerID, length);
                boolean validToAdd = true;

                for (Container container : HelloApplication.getContainers()) {
                    if (container.getIdentifier().equals(containerToAdd.getIdentifier())) { //checking if id is unique
                        validToAdd = false;
                        break;
                    }
                }

                if (validToAdd) {
                    if (containerToAdd.getContainerSize() != -1 && !containerToAdd.getIdentifier().equals("Unknown")) { //checks for valid details
                        listOfContainers.getItems().add(containerToAdd.toString() + " On Ship: " + shipToAddTo.getShipIdentifier());
                        shipToAddTo.getContainers().add(containerToAdd);
                        HelloApplication.getContainers().add(containerToAdd);
                        PalletAPI.getPalletAPI().getContainers().getItems().add(containerToAdd.toString());//adds container to system, ship and lists and choiceboxes
                    } else Utilities.showWarningAlert("WARNING!", "Enter valid details");
                } else Utilities.showWarningAlert("WARNING!","ID must be unique");
            } else Utilities.showWarningAlert("WARNING!", "Select a ship");
        } else Utilities.showWarningAlert("WARNING!", "Fill in details");
    }

    public void loadOntoShip() {
        if (listOfContainers.getSelectionModel().getSelectedItem() != null) {//checks if a ship is selected


            Port portToUnloadLoadFrom = null;
            for (Port port : HelloApplication.getPorts()) {
                if (listOfContainers.getSelectionModel().getSelectedItem().contains("Port") && listOfContainers.getSelectionModel().getSelectedItem().contains(String.valueOf(port.getInternationalPortCode()))) { //searches for port that the container is at, if it is a port
                    portToUnloadLoadFrom = port;
                    break;
                }
            }

            if (portToUnloadLoadFrom != null) {
                if (!ships.getItems().isEmpty() &&  ships.getValue()!=null) { //checks if ship is chosen
                    String strShipToLoadOnto = ships.getValue();
                    Ship shipToLoadOnto = null;
                    for (Ship ship : portToUnloadLoadFrom.getShipList()) {
                        if (ship.toString().contains(strShipToLoadOnto)) {//searches if ship selected is at the port
                            shipToLoadOnto = ship;
                            break;
                        }
                    }

                    if (shipToLoadOnto != null) {
                        String strContainerToLoad = listOfContainers.getSelectionModel().getSelectedItem();
                        for (Container container : portToUnloadLoadFrom.getContainers()) {
                            if (strContainerToLoad.contains(container.toString())) {
                                shipToLoadOnto.getContainers().add(container);
                                portToUnloadLoadFrom.getContainers().remove(container);
                                listOfContainers.getItems().remove(strContainerToLoad);
                                listOfContainers.getItems().add(container.toString() + " On Ship: " + shipToLoadOnto.getShipIdentifier());
                            }
                        }
                    } else Utilities.showWarningAlert("WARNING!", "Ship chosen is not at the chosen port");
                } else Utilities.showWarningAlert("WARNING!", "Choose a ship");
            } else Utilities.showWarningAlert("WARNING!", "Container is not at a port");
        } else Utilities.showWarningAlert("WARNING!", "Choose a container");
    }


    public void loadOntoPort(){
        if (listOfContainers.getSelectionModel().getSelectedItem() != null) {//checks if a ship is selected

            Ship shipToUnLoadFrom = null;
            for (Ship ship : HelloApplication.getShips()) {
                if (listOfContainers.getSelectionModel().getSelectedItem().contains("Ship") && listOfContainers.getSelectionModel().getSelectedItem().contains(ship.getShipIdentifier())) {//searches for ship the container is on, if it is on a ship
                    shipToUnLoadFrom = ship;
                    break;
                }
            }

            if (shipToUnLoadFrom != null) {
                Port portToLoadOnto = null;
                for (Port port : HelloApplication.getPorts()) {//searches for the port the ship is at, if it's at a port
                    for (Ship ship : port.getShipList()) {
                        if (ship == shipToUnLoadFrom) {
                            portToLoadOnto = port;
                            break;
                        }
                    }
                }

                if (portToLoadOnto != null) {
                    String strContainerToLoad = listOfContainers.getSelectionModel().getSelectedItem();
                    for (Container container : shipToUnLoadFrom.getContainers()) {
                        if (strContainerToLoad.contains(container.toString())) {
                            portToLoadOnto.getContainers().add(container);
                            shipToUnLoadFrom.getContainers().remove(container);
                            listOfContainers.getItems().remove(strContainerToLoad);
                            listOfContainers.getItems().add(container.toString() + " At Port: " + portToLoadOnto.getInternationalPortCode());
                        }
                    }
                } else Utilities.showWarningAlert("WARNING!", "The container is at sea!");
            } else Utilities.showWarningAlert("WARNING!", "Container is not on a ship");
        } else Utilities.showWarningAlert("WARNING!", "Select a container");
    }

    @FXML
    public void drillDown(){
        if (listOfContainers.getSelectionModel().getSelectedItem() !=null) {//checks if a container is selected
            String strPortToDrillDown = listOfContainers.getSelectionModel().getSelectedItem();
            Container containerToDrillDown = null;
            for (Container container : HelloApplication.getContainers()) {
                if (strPortToDrillDown.contains(container.toString())) { //searches for selected container
                    containerToDrillDown = container;
                    break;
                }
            }

            for (Pallet pallet : containerToDrillDown.getPallets()) {
                DrillDownAPI.getDrillDownAPI().getDrillDownView().getItems().add(pallet.toString()); //lists all pallets in container
            }

            HelloApplication.mainStage.setScene(HelloApplication.drillDownS);//changes scene
        } else Utilities.showWarningAlert("WARNING!", "Select a Ship.");
    }


    @FXML
    public void goBack()throws IOException {
        HelloApplication.mainStage.setScene(HelloApplication.portsS);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        containerAPI=this;
    }
}