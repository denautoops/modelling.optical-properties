package app.gaussian.controllers;

import app.gaussian.models.MembraneStructure;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HeadController implements Initializable {

    private MembraneStructure membraneStructure;

    public void setMembraneStructure(MembraneStructure membraneStructure) {
        this.membraneStructure = membraneStructure;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void modeling(ActionEvent event){

    }

    public void configureLayoutSystem(ActionEvent event){

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/ui/Configurator.fxml"/*Environment.get().getFxmlPath())*/));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void showHistory(ActionEvent event){

    }

    public void clearLayoutSystem(ActionEvent event){

    }
}
