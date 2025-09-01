package org.example.johnsupermarket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.johnsupermarket.Models.Item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    private Stage stage;
    private Scene scene;

    //this will close the window(Exit function)
    public void handleExit(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    //this will navigate to Add function
    public void GotoAddPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addPage-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Add Page");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //this will navigate to Delete function
    public void GotoDIDPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("deletePage-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Delete Page");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //this will navigate to update function
    public void GotoUpdatePage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("updatePage-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Update Page");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //this will navigate to inventory page
    public void GotoInventry(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("viewINVENPage-view.fxml"));
        Parent root = loader.load();

        ViewinventryPageController controller = loader.getController();
        controller.refreshInventory();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Inventory Page");
        stage.setScene(scene);
        stage.show();
    }

    //this will navigate to Dealer select page
    public void GotoSelectDealersPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("randomDealersPage-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Dealers Page");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //this will navigate to dealer's item page
    public void GotoDealerItemsPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("dealerItemsPage-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Dealer's Items Page");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //links with FXML
    @FXML
    private TableView<Item> lowStockTable;
    private ObservableList<Item> itemList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Item, String> colCode;
    @FXML
    private TableColumn<Item, String> colName;
    @FXML
    private TableColumn<Item, String> colBrand;
    @FXML
    private TableColumn<Item, Double> colPrice;
    @FXML
    private TableColumn<Item, Integer> colQty;
    @FXML
    private TableColumn<Item, String> colCategory;
    @FXML
    private TableColumn<Item, String> colDate;
    @FXML
    private TableColumn<Item, Integer> colThreshold;

    //load table columns
    @Override
    public void initialize(URL location, ResourceBundle resources){
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colThreshold.setCellValueFactory(new PropertyValueFactory<>("threshold"));
        loadLowstockTableData();

    }

    //method that uses to load low stock table
    private void loadLowstockTableData() {
        //this will clear the table data (if exist)
        itemList.clear();
        //this will open and read the inventory file
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/Inventory/details.txt"))) {
            //declaration
            String line;
            //check the nullness
            while ((line = br.readLine()) != null) {
                //creating String array using ","
                String[] data = line.split(",");
                //check array length (because if the array dosen't have 9 parts means its invalid)
                if (data.length == 9) {
                    //value assigning
                    BigDecimal price = new BigDecimal(data[3]);
                    int quantity = Integer.parseInt(data[4]);
                    int threshold = Integer.parseInt(data[7]);
                    //check quantity is less than the threshold
                    if (quantity < threshold) {
                        //if "yes" creat ITEM obj
                        Item item = new Item(
                                data[0],
                                data[1],
                                data[2],
                                price,
                                quantity,
                                data[5],
                                LocalDate.parse(data[6]),
                                threshold,
                                data[8]
                        );
                        //add to the itemlist array
                        itemList.add(item);
                    }
                }
            }
            //Sorting (bubble sort) according to the category
            for (int i = 0; i < itemList.size() - 1; i++) {
                for (int j = 0; j < itemList.size() - i - 1; j++) {
                    Item item1 = itemList.get(j);
                    Item item2 = itemList.get(j + 1);

                    int categoryCompare = item1.getCategory().compareToIgnoreCase(item2.getCategory());

                    if (categoryCompare > 0 || (categoryCompare == 0 &&
                            item1.getCode().compareToIgnoreCase(item2.getCode()) > 0)) {
                        itemList.set(j, item2);
                        itemList.set(j + 1, item1);
                    }
                }
            }
            //then pass to lowStock table
            lowStockTable.setItems(itemList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}