package app.gaussian.controllers;

import app.gaussian.models.MembraneStructure;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class ConfiguratorController implements Initializable {

    private List<CheckBox> actualCheckBoxes = new ArrayList<>();



    @FXML
    private FlowPane choosingActiveLayers;

    @FXML
    private FlowPane activeLayers;

    @FXML
    private ListView listView;

    @FXML
    private VBox layersSystem;

    @FXML
    private TextField name1;

    @FXML
    private TextField thilness1;

    @FXML
    private TextField rafractIndex1;

    @FXML
    private TextField name2;

    @FXML
    private TextField thilness2;

    @FXML
    private TextField rafractIndex2;



    private Map<String, Integer> choosingActiveLayersList = new LinkedHashMap<>();
    private List<String> activeLayersList = new ArrayList<>();


    private Map<String, Integer> finalControlls = new LinkedHashMap<>();

    private MembraneStructure membraneStructure;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void to(MouseEvent mouseEvent) {

        layersSystem.getChildren().add(new Label(name1.getText()));
    }

    public void test(MouseEvent mouseEvent) {

        /*for (int i=0; i<10; i++ ) {
            listView.getItems().add("Item "+i);
        }*/

        final ObjectProperty<ListCell<String>> dragSource = new SimpleObjectProperty<>();

        listView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>(){
                @Override
                public void updateItem(String item , boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };

            cell.setOnDragDetected(event -> {
                if (! cell.isEmpty()) {
                    Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString(cell.getItem());
                    db.setContent(cc);
                    dragSource.set(cell);
                }
            });

            cell.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            });

            cell.setOnDragDone(event -> listView.getItems().remove(cell.getItem()));

            cell.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasString() && dragSource.get() != null) {
                    ListCell<String> dragSourceCell = dragSource.get();
                    listView.getItems().add(dragSourceCell.getItem());
                    event.setDropCompleted(true);
                    dragSource.set(null);
                } else {
                    event.setDropCompleted(false);
                }
            });

            return cell ;
        });
    }


    public void save1(MouseEvent mouseEvent) {

        String layerName = name1.getText()+"("+thilness1.getText()+")";

        if (activeLayersList.contains(layerName)){
            return;
        }

        CheckBox checkBox = new CheckBox(layerName);
        checkBox.setPrefWidth(96);

        checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkBox.isSelected() && !choosingActiveLayersList.containsKey(layerName)){
                    HBox hBox = new HBox();
                    hBox.setPrefWidth(96);
                    hBox.setPrefHeight(20);

                    Label label = new Label(checkBox.getText());
                    label.setPrefWidth(50);

                    TextField textField = new TextField();
                    textField.setPrefWidth(50);

                    hBox.getChildren().addAll(label, textField);

                    choosingActiveLayers.getChildren().add(hBox);

                    //finalControlls.put()

                    choosingActiveLayersList.put(checkBox.getText(), choosingActiveLayersList.size()-1);
                }
                if (!checkBox.isSelected() && choosingActiveLayersList.containsKey(layerName)){
                    choosingActiveLayers.getChildren().clear();
                }
            }
        });

        activeLayers.getChildren().add(checkBox);
        activeLayersList.add(name1.getText());
    }

    public void save2(MouseEvent mouseEvent) {

        String layerName = name2.getText()+"("+thilness2.getText()+")";

        if (activeLayersList.contains(layerName)){
            return;
        }

        CheckBox checkBox = new CheckBox(layerName);
        checkBox.setPrefWidth(96);

        checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkBox.isSelected() && !choosingActiveLayersList.containsKey(checkBox.getText())){
                    HBox hBox = new HBox();
                    hBox.setPrefWidth(96);
                    hBox.setPrefHeight(20);

                    Label label = new Label(checkBox.getText());
                    label.setPrefWidth(50);

                    TextField textField = new TextField();
                    textField.setPrefWidth(50);

                    hBox.getChildren().addAll(label, textField);

                    choosingActiveLayers.getChildren().add(hBox);

                    choosingActiveLayersList.put(checkBox.getText(), choosingActiveLayersList.size()-1);
                }
                if (!checkBox.isSelected() && choosingActiveLayersList.containsKey(checkBox.getText())){
                    choosingActiveLayers.getChildren().remove(choosingActiveLayersList.get(checkBox.getText()));
                }
            }
        });


        activeLayers.getChildren().add(checkBox);
        activeLayersList.add(layerName);
    }
}
