package org.example.johnsupermarket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.johnsupermarket.Models.DealerItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DealerItemsPageController {
    //links between dealerItemPage-view.fxml and the controller
    @FXML private TableView<DealerItem> itemTable;
    @FXML private TableColumn<DealerItem, String> itemNameCol;
    @FXML private TableColumn<DealerItem, String> brandCol;
    @FXML private TableColumn<DealerItem, Double> priceCol;
    @FXML private TableColumn<DealerItem, Integer> quantityCol;
    @FXML private TableColumn<DealerItem, String> categoryCol;
    @FXML private Label OutputPanel;
    @FXML private TextField dealerNameInput;

    private String selectedDealer;
    private Stage stage;
    private Scene scene;

    //load table columns with properties of dealerItem(class)
    @FXML
    public void initialize() {
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
    }
    //this is navigating from another page (preload dealer names) and calling "handleSearchItems()" function at the begging
    public void setSelectedDealer(String dealerName) {
        this.selectedDealer = dealerName;
        dealerNameInput.setText(dealerName);
        handleSearchItems();
    }
    //main method: triggered when clicking search button
    @FXML
    private void handleSearchItems() {
        //get dealer's name
        String inputName = dealerNameInput.getText().trim();
        if (inputName.isEmpty()) {
            OutputPanel.setText("Please enter a dealer name.");
            OutputPanel.setTextFill(Color.RED);
            return;
        }
        //get dealer details the pass it to the table
        try {
            //get dealer details
            List<DealerItem> items = findItemsForDealer("dealers.txt", inputName);
            //employ handling
            if (items.isEmpty()) {
                OutputPanel.setText("No items found for dealer: " + inputName);
                OutputPanel.setTextFill(Color.RED);
            } else {
                //pass top the table
                itemTable.getItems().setAll(items);
                //pass to outputPanel
                OutputPanel.setText("Items loaded for: " + inputName);
                OutputPanel.setTextFill(Color.GREEN);
            }
        } catch (IOException e) {

            OutputPanel.setText("Error reading file.");
            OutputPanel.setTextFill(Color.RED);
        }
    }
    //file reading method
    private List<DealerItem> findItemsForDealer(String filePath, String dealerName) throws IOException {
        List<DealerItem> itemList = new ArrayList<>();
        //read the file
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/Dealers/DealerDetails.txt"))) {
            String line;
            boolean dealerFound = false;
            //read line by line
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                //dividing by ","
                String[] dealerParts = line.split(",");
                //skip dealer details
                if (dealerParts.length > 3) continue;

                String currentDealerName = dealerParts[0].trim();
                //check dealer in the file?
                if (currentDealerName.equalsIgnoreCase(dealerName)) {
                    dealerFound = true;

                    //If yes, load their items (until empty)
                    while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                        String[] itemParts = line.split(",");

                        if (itemParts.length >= 5) {
                            String name = itemParts[0].trim();
                            String brand = itemParts[1].trim();
                            double price = Double.parseDouble(itemParts[2].trim());
                            int quantity = Integer.parseInt(itemParts[3].trim());
                            String category = itemParts[4].trim();
                            //add to the itemList array
                            itemList.add(new DealerItem(name, brand, price, quantity, category));
                        }
                    }
                    break;
                } else {
                    //skip lines until next dealer
                    while ((line = br.readLine()) != null && !line.trim().isEmpty()) {}
                }
            }
            if (!dealerFound) {
                OutputPanel.setText("Dealer not found: " + dealerName);
                OutputPanel.setTextFill(Color.RED);
            }
        }
        //return itemList to "handleSearchItems()"
        return itemList;
    }
    //Navigate to Select Dealers page
    public void GotoSelectDealersPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("randomDealersPage-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Dealers Page");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    //Navigate to the home page
    public void GotoHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Main Page");
        stage.setScene(scene);
        stage.show();
    }
    //exit from app
    public void handleExit(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
