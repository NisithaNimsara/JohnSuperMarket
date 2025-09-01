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
import org.example.johnsupermarket.Models.Item;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeletePageController {

    //links between deletePage-view.fxml and the controller
    @FXML private TableView<Item> itemTable;
    @FXML private TableColumn<Item, String> codeCol, nameCol, brandCol, categoryCol, imageCol;
    @FXML private TableColumn<Item, BigDecimal> priceCol;
    @FXML private TableColumn<Item, Integer> quantityCol, thresholdCol;
    @FXML private TableColumn<Item, LocalDate> dateCol;
    @FXML private Label OutputPanel;
    @FXML private TextField itemCodeInput;

    private Stage stage;
    private Scene scene;

    //list for hold items
    private List<Item> allItems = new ArrayList<>();
    //search for item by code(method)
    @FXML
    private void handleSearchItem() {
        String code = itemCodeInput.getText().trim();
        //if item code panel emplty
        if (code.isEmpty()) {
            OutputPanel.setText("Enter item code to search.");
            OutputPanel.setTextFill(Color.RED);
            return;
        }
        //find the relevant details of given item code
        List<Item> found = allItems.stream()
                .filter(item -> item.getCode().equalsIgnoreCase(code))
                .collect(Collectors.toList());
        if (found.isEmpty()) {
            OutputPanel.setText("Item not found.");
            OutputPanel.setTextFill(Color.RED);
            itemTable.getItems().clear();
        } else {
            itemTable.getItems().setAll(found);
            OutputPanel.setText("Item found.");
            OutputPanel.setTextFill(Color.GREEN);
        }
    }
    //load items data to the itemlist
    private List<Item> loadItemsFromFile(String path) throws IOException {
        List<Item> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length != 9) {
                    OutputPanel.setText("Line " + lineNumber + ": Wrong number of fields.");
                    continue;
                }
                //assigning each value to the item by oder
                try {
                    String code = parts[0].trim();
                    String name = parts[1].trim();
                    String brand = parts[2].trim();
                    BigDecimal price = new BigDecimal(parts[3].trim());
                    int quantity = Integer.parseInt(parts[4].trim());
                    String category = parts[5].trim();
                    LocalDate date = LocalDate.parse(parts[6].trim());
                    int threshold = Integer.parseInt(parts[7].trim());
                    String imagePath = parts[8].trim();
                    //make it as require order
                    list.add(new Item(code, name, brand, price, quantity, category, date, threshold, imagePath));
                } catch (Exception e) {
                    OutputPanel.setText("Error at line " + lineNumber + ": " + e.getMessage());
                }
            }
        }
        return list;
    }
    //set table columns to values
    public void initialize() {
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        thresholdCol.setCellValueFactory(new PropertyValueFactory<>("threshold"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        try {
            //getting details from details file
            allItems = loadItemsFromFile("src/main/resources/Inventory/details.txt");
        } catch (IOException e) {
            OutputPanel.setText("Error loading file.");
            OutputPanel.setTextFill(Color.RED);
        }
    }

    //delete item from table
    @FXML
    private void handleDeleteItem() {
        Item selected = itemTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            OutputPanel.setText("Select item in table to delete.");
            OutputPanel.setTextFill(Color.RED);
            return;
        }

        allItems.removeIf(item -> item.getCode().equals(selected.getCode()));
        itemTable.getItems().clear();
        OutputPanel.setText("Item deleted.");
        OutputPanel.setTextFill(Color.ORANGE);
    }

    //save all items back to file
    @FXML
    private void handleSaveItems() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/Inventory/details.txt"))) {
            for (Item item : allItems) {
                String line = String.join(",",
                        item.getCode(),
                        item.getName(),
                        item.getBrand(),
                        item.getPrice().toString(),
                        String.valueOf(item.getQuantity()),
                        item.getCategory(),
                        item.getDate().toString(),
                        String.valueOf(item.getThreshold()),
                        item.getImagePath()
                );
                bw.write(line);
                bw.newLine();
            }
            OutputPanel.setText("Item deleted and Saved successfully.");
            OutputPanel.setTextFill(Color.GREEN);
        } catch (IOException e) {
            OutputPanel.setText("Error saving file.");
            OutputPanel.setTextFill(Color.RED);
        }
    }
    //navigate to home page
    public void GotoHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Main Page");
        stage.setScene(scene);
        stage.show();
    }
    //close the app
    public void handleExit(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
