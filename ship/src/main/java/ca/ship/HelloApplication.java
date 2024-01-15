package ca.ship;

import ca.ship.model.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;



public class HelloApplication extends Application {

    public static Scene portsS,shipsS,palletsS,containersS,goodS, drillDownS;
    public static Stage mainStage;
    private static MyNeatList<Port> ports = new MyNeatList<>();
    private static MyNeatList<Ship> ships = new MyNeatList<>();
    private static MyNeatList<Container> containers = new MyNeatList<>();
    private static MyNeatList<Pallet> pallets = new MyNeatList<>();

    public static MyNeatList<Port> getPorts() {
        return ports;
    }

    public static MyNeatList<Ship> getShips() {
        return ships;
    }

    public static MyNeatList<Container> getContainers() {
        return containers;
    }

    public static MyNeatList<Pallet> getPallets() {
        return pallets;
    }

    private static MyNeatList<Ship> shipsAtSea = new MyNeatList<>();

    public static MyNeatList<Ship> getShipsAtSea() {
        return shipsAtSea;
    }





    public static Scene changeScene(String file) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(file));
        return new Scene(fxmlLoader.load(), 900, 600);
    }

    @Override
    public void start(Stage stage) throws IOException {

        mainStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("port-view.fxml"));
        Parent root = fxmlLoader.load();
        portsS = new Scene(root, 900, 600);

        shipsS = changeScene("ship-view.fxml");
        containersS = changeScene("container-view.fxml");
        palletsS = changeScene("pallet-view.fxml");
        goodS = changeScene("goods-view.fxml");
        drillDownS = changeScene("drilldown-view.fxml");

        stage.setTitle("Ship App");
        stage.setScene(portsS);
        stage.show();
    }


    public static void load() throws Exception { // load
        //list of classes that you wish to include in the serialisation, separated by a comma
        Class<?>[] classes = new Class[]{Port.class, Ship.class, Container.class,
                Pallet.class, MyNeatList.class};

        //setting up the xstream object with default security and the above classes
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);

        //doing the actual serialisation to an XML file
        ObjectInputStream in = xstream.createObjectInputStream(new FileReader("ships.xml"));
        ports = (MyNeatList<Port>) in.readObject();
        shipsAtSea = (MyNeatList<Ship>) in.readObject();//loading data from ships.xml
        in.close();
    }

    public static void save() throws Exception { // save
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter("ships.xml"));
        out.writeObject(ports);
        out.writeObject(shipsAtSea);//saving only ports and ships at sea to ships.fxml since all data is linked to both of these

        out.close();
    }



    public static void main(String[] args) {
        launch();
    }
}