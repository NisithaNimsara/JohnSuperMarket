package org.example.johnsupermarket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.johnsupermarket.Models.Item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


public class ViewinventryPageController {
    @FXML
    private Stage stage;
    private Scene scene;

    //link between viewINVENPage-view.fxml and the controller
    @FXML private TableView<Item> inventoryTable;
    @FXML private TableColumn<Item, String> colCode;
    @FXML private TableColumn<Item, String> colName;
    @FXML private TableColumn<Item, String> colBrand;
    @FXML private TableColumn<Item, Double> colPrice;
    @FXML private TableColumn<Item, Integer> colQty;
    @FXML private TableColumn<Item, String> colCategory;
    @FXML private TableColumn<Item, String> colDate;
    @FXML private TableColumn<Item, Integer> colThreshold;
    @FXML private TableColumn<Item, String> colImage;
    @FXML private TextField TotalItemID;
    @FXML private TextField TotalValueID;

    private final ObservableList<Item> itemList = FXCollections.observableArrayList();

    //this will call the inside methods to run when function called
    @FXML
    public void initialize() {
        setupColumns();
        refreshInventory();
    }
    //set columns
    private void setupColumns() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colThreshold.setCellValueFactory(new PropertyValueFactory<>("threshold"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        //image column
        colImage.setCellFactory(column -> new TableCell<Item, String>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                imageView.setPreserveRatio(true);
            }
            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.trim().isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(getClass().getResourceAsStream("/images/" + imagePath));
                        imageView.setImage(image);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });
    }
    //load data from the file
    private void loadInventoryData() {
        itemList.clear();
        BigDecimal totalValue = BigDecimal.ZERO;
        int totalItems = 0;
        //read the file
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/Inventory/details.txt"))) {
            String line;
            //line by line reading
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Item item = null;
                //if valid item,
                if (data.length == 9) {
                     item = new Item(
                            data[0], // code
                            data[1], // name
                            data[2], // brand
                            new BigDecimal(data[3]), // price
                            Integer.parseInt(data[4]), // quantity
                            data[5], // category
                            LocalDate.parse(data[6]), // date
                            Integer.parseInt(data[7]), // threshold
                            data[8]  // image
                    );
                     //add to item arraylist
                    itemList.add(item);
                }
                if (item != null) {
                    totalItems++;

                    BigDecimal quantityBD = BigDecimal.valueOf(item.getQuantity());
                    BigDecimal itemTotal = item.getPrice().multiply(quantityBD);
                    totalValue = totalValue.add(itemTotal);
                }
            }
            //sort
            for (int i = 0; i < itemList.size() - 1; i++) {
                for (int j = 0; j < itemList.size() - i - 1; j++) {
                    Item item1 = itemList.get(j);
                    Item item2 = itemList.get(j + 1);

                    int categoryCompare = item1.getCategory().compareToIgnoreCase(item2.getCategory());
                    if (categoryCompare > 0) {
                        //-----------------swap if item1 category is after item2 category
                        itemList.set(j, item2);
                        itemList.set(j + 1, item1);
                    } else if (categoryCompare == 0) {
                        //------------------if categories are same, sort by code
                        if (item1.getCode().compareToIgnoreCase(item2.getCode()) > 0) {
                            itemList.set(j, item2);
                            itemList.set(j + 1, item1);
                        }
                    }
                }
            }
            //set values
            TotalItemID.setText(String.valueOf(totalItems));
            TotalValueID.setText(totalValue.setScale(2, RoundingMode.HALF_UP).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //refresh method
    public void refreshInventory() {
        loadInventoryData();
        inventoryTable.setItems(itemList);
        inventoryTable.refresh();
    }
    //close app
    public void handleExit(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();}
    //navigate to add function
    public void GotoAddPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addPage-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Add Page");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();}
    //navigate to the home
    public void GotoHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Main Page");
        stage.setScene(scene);
        stage.show();
    }
}