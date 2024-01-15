package ca.ship.controllers;

import ca.ship.HelloApplication;
import ca.ship.model.Container;
import ca.ship.model.Pallet;
import ca.ship.model.Ship;
import ca.ship.utils.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DrillDownAPI implements Initializable {

    private static DrillDownAPI drillDownAPI;

    public static DrillDownAPI getDrillDownAPI() {
        return drillDownAPI;
    }

    @FXML
    private ListView<String> drillDownView;

    public ListView<String> getDrillDownView() {
        return drillDownView;
    }

    @FXML
    public void drillDown(){
        if (drillDownView.getSelectionModel().getSelectedItem()!=null) {

            String strThingToDrillDown = drillDownView.getSelectionModel().getSelectedItem();
            int option = 3;
            if (strThingToDrillDown.contains("SHIP"))
                option = 2;
            else if (strThingToDrillDown.contains("CONTAINER"))
                option = 1;
            else if (strThingToDrillDown.contains("PALLET"))
                option=0;//assigns option to each potential case, stays at 3 if there's

            switch (option) {
                case 3 -> Utilities.showWarningAlert("WARNING", "Cannot drill down any further.");
                case 2 -> {
                    drillDownView.getItems().clear();
                    for (Ship ship : HelloApplication.getShips()) {
                        if (strThingToDrillDown.contains(ship.toString())) {
                            for (Container container : ship.getContainers()) {
                                drillDownView.getItems().add(container.toString());
                                for (Pallet pallet : container.getPallets()) {
                                    drillDownView.getItems().add("-----"+pallet.toString());//adds all containers and their respective pallets in the ship in the listview
                                }
                            }
                            break;
                        }
                    }
                }
                case 1 -> {
                    drillDownView.getItems().clear();
                    for (Container container : HelloApplication.getContainers()) {
                        if (strThingToDrillDown.contains(container.toString())) {
                            for (Pallet pallet : container.getPallets()) {
                                drillDownView.getItems().add(pallet.toString());//adds all respective pallets in the container in the listview
                            }
                            break;
                        }
                    }
                }
                case 0 -> {
                    drillDownView.getItems().clear();
                    for (Pallet pallet : HelloApplication.getPallets()) {
                        if (strThingToDrillDown.contains(pallet.toString())) {
                            drillDownView.getItems().add("GOODS: "+pallet.getDescription());// adds the goods of the pallet to the listview
                            break;
                        }
                    }
                }
            }
        } else Utilities.showWarningAlert("Warning", "Please select an option");
    }

    @FXML
    public void goBack()throws IOException {
        drillDownView.getItems().clear();
        HelloApplication.mainStage.setScene(HelloApplication.portsS);//clears everything before returning to main page
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drillDownAPI=this;
    }
}
