package ca.ship.controllers;

import ca.ship.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GoodsAPI implements Initializable {
    private static GoodsAPI goodsAPI;

    public static GoodsAPI getGoodsAPI() {
        return goodsAPI;
    }

    @FXML
    private ListView<String> listOfGoods;

    public ListView<String> getListOfGoods() {
        return listOfGoods;
    }
    @FXML
    private Label totalValue, valueOfPorts,valueOfShips,valueOfShipsAtSea;

    public Label getTotalValue() {
        return totalValue;
    }

    public Label getValueOfPorts() {
        return valueOfPorts;
    }

    public Label getValueOfShips() {
        return valueOfShips;
    }

    public Label getValueOfShipsAtSea() {
        return valueOfShipsAtSea;
    }

    @FXML
    public void goBackFromGoodsView()throws IOException {
        listOfGoods.getItems().clear();
        HelloApplication.mainStage.setScene(HelloApplication.palletsS);//clears list before returning to pallets
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goodsAPI = this;
    }
}
