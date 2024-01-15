package ca.ship.controllers;

import ca.ship.HelloApplication;
import ca.ship.model.*;
import ca.ship.utils.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PortAPI implements Initializable{

    private static PortAPI portAPI;

    @FXML
    private TextField name, country, internationalCode;

    @FXML
    private ListView<String> listOfPort = new ListView<>();


    @FXML
    public void addPort() {
        if (!name.getText().isEmpty()&&!country.getText().isEmpty()&&!internationalCode.getText().isEmpty()) { //checking if each text box is filled
             String portName = name.getText();
             String portCountry = country.getText();
             int portCode=0;
             boolean validPortCode=true;
             try {
                 portCode = Integer.parseInt(internationalCode.getText());//getting each attribute of port
             } catch (NumberFormatException e){
                 validPortCode=false;
             }

             Port portToAdd = new Port(portName, portCountry, portCode);
             boolean validToAdd = true;

             if (portToAdd.getInternationalPortCode() != -1 && validPortCode) {  //checks if values passed validation
                 for (Port port : HelloApplication.getPorts()) {// checks to see if another port in the system has same code
                     if (port.getInternationalPortCode() == portToAdd.getInternationalPortCode()) {
                         validToAdd = false;
                         break;
                     }
                 }
                 if (validToAdd) {
                     HelloApplication.getPorts().add(portToAdd);
                     listOfPort.getItems().add(portToAdd.toString());
                     ShipAPI.getShipAPI().getPorts().getItems().add(portToAdd.toString());
                     ContainerAPI.getContainerAPI().getPorts().getItems().add(portToAdd.toString());//add port to system and all lists and choiceboxes
                 } else Utilities.showWarningAlert("WARNING", "ID must be unique");
             } else Utilities.showWarningAlert("WARNING", "ID must be a valid number");
        }else Utilities.showWarningAlert("WARNING", "Fill all boxes");
    }


    @FXML
    public void drillDown(){
        if (listOfPort.getSelectionModel().getSelectedItem()!= null) { //checks if option is selected
            String strPortToDrillDown = listOfPort.getSelectionModel().getSelectedItem();
            Port portToDrillDown = null;
            for (Port port : HelloApplication.getPorts()) {
                if (strPortToDrillDown.contains(String.valueOf(port.getInternationalPortCode()))) {
                    portToDrillDown = port; //matches option in list to port in system
                    break;
                }
            }

            for (Ship ship : portToDrillDown.getShipList()) {
                DrillDownAPI.getDrillDownAPI().getDrillDownView().getItems().add(ship.toString()); //adds its ships, its subsequent containers and pallets to drill down list
                for (Container container : ship.getContainers()) {
                    DrillDownAPI.getDrillDownAPI().getDrillDownView().getItems().add("-----"+container.toString());
                    for (Pallet pallet : container.getPallets()) {
                        DrillDownAPI.getDrillDownAPI().getDrillDownView().getItems().add("----------"+pallet.toString());
                    }
                }
            }

            for (Container container : portToDrillDown.getContainers()) {
                DrillDownAPI.getDrillDownAPI().getDrillDownView().getItems().add(container.toString());//adds its containers and pallets to drill down list
                for (Pallet pallet : container.getPallets()) {
                    DrillDownAPI.getDrillDownAPI().getDrillDownView().getItems().add("-----"+pallet.toString());
                }
            }

            HelloApplication.mainStage.setScene(HelloApplication.drillDownS); //changes scene to drilldown view

        } else Utilities.showWarningAlert("WARNING", "Pick a Port");
    }

    @FXML
    public void shipScene() throws IOException {
        HelloApplication.mainStage.setScene(HelloApplication.shipsS); //changes scene to ship view
    }

    @FXML
    public void containerScene()throws IOException{
        HelloApplication.mainStage.setScene(HelloApplication.containersS);//changes scene to container view
    }

    @FXML
    public void palletScene()throws IOException{
        HelloApplication.mainStage.setScene(HelloApplication.palletsS); //changes scene to pallet view
    }

    public void clear(){
        //CLEAR FRONTEND PORTS
        listOfPort.getItems().clear();
        ShipAPI.getShipAPI().getPorts().getItems().clear();
        ContainerAPI.getContainerAPI().getPorts().getItems().clear();

        //CLEAR FRONTEND SHIPS
        ShipAPI.getShipAPI().getListOfShip().getItems().clear();
        ContainerAPI.getContainerAPI().getShips().getItems().clear();

        //CLEAR FRONTEND CONTAINERS
        ContainerAPI.getContainerAPI().getListOfContainers().getItems().clear();
        PalletAPI.getPalletAPI().getContainers().getItems().clear();

        //CLEAR FRONTEND PALLETS
        PalletAPI.getPalletAPI().getListOfPallets().getItems().clear();
        GoodsAPI.getGoodsAPI().getListOfGoods().getItems().clear();
        PalletAPI.getPalletAPI().getSearchedListOfPallets().getItems().clear();

        //CLEAR BACKEND LISTS
        HelloApplication.getPorts().clear();
        HelloApplication.getShips().clear();
        HelloApplication.getShipsAtSea().clear();
        HelloApplication.getContainers().clear();
        HelloApplication.getPallets().clear();
    }


    public void clearButton(){
        clear();
        Utilities.showWarningAlert("YOU'RE A STAR!", "System has been cleared.");
    }


    // SAVE AND LOAD
    public void save() throws Exception {
        try {
            HelloApplication.save();
            Utilities.showWarningAlert("WOW!", "Save Successful!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Saving.");
        }
    }

    public void load() throws Exception{
        clear();
        HelloApplication.load();//clears everything before loading file

        for (Port port:HelloApplication.getPorts()){
            listOfPort.getItems().add(port.toString());
            ShipAPI.getShipAPI().getPorts().getItems().add(port.toString());
            ContainerAPI.getContainerAPI().getPorts().getItems().add(port.toString());
            for (Ship ship : port.getShipList()){
                HelloApplication.getShips().add(ship);
                ShipAPI.getShipAPI().getListOfShip().getItems().add(ship.toString() + " At: " + port.getInternationalPortCode());
                ContainerAPI.getContainerAPI().getShips().getItems().add(ship.toString());

                for (Container container:ship.getContainers()){
                    HelloApplication.getContainers().add(container);
                    ContainerAPI.getContainerAPI().getListOfContainers().getItems().add(container.toString()+" On Ship: " + ship.getShipIdentifier());
                    PalletAPI.getPalletAPI().getContainers().getItems().add(container.toString());

                    for (Pallet pallet:container.getPallets()){
                        HelloApplication.getPallets().add(pallet);
                        PalletAPI.getPalletAPI().getListOfPallets().getItems().add(pallet.toString()+ " In: "+container.getIdentifier());

                    }
                }
            }
            for (Container container:port.getContainers()){
                HelloApplication.getContainers().add(container);
                ContainerAPI.getContainerAPI().getListOfContainers().getItems().add(container.toString()+" At Port: " + port.getInternationalPortCode());
                PalletAPI.getPalletAPI().getContainers().getItems().add(container.toString());

                for (Pallet pallet:container.getPallets()){
                    HelloApplication.getPallets().add(pallet);
                    PalletAPI.getPalletAPI().getListOfPallets().getItems().add(pallet.toString() +" In: "+container.getIdentifier());

                }
            }
        }
        for (Ship ship : HelloApplication.getShipsAtSea()){
            HelloApplication.getShips().add(ship);
            ShipAPI.getShipAPI().getListOfShip().getItems().add(ship.toString() + " At Sea");
            for (Container container : ship.getContainers()){
                HelloApplication.getContainers().add(container);
                ContainerAPI.getContainerAPI().getListOfContainers().getItems().add(container.toString() + " On Ship: " + ship.getShipIdentifier());

                for (Pallet pallet:container.getPallets()){
                    HelloApplication.getPallets().add(pallet);
                    PalletAPI.getPalletAPI().getListOfPallets().getItems().add(pallet.toString() + " In: "+ container.getIdentifier());

                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portAPI=this;//grabs everything from the portapi and reassigns it to the current portapi
    }


}