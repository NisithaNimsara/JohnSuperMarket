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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.johnsupermarket.Models.Dealer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SelectdealersPageController {
    //links between randomDealersPage-view.fxml and the controller
    @FXML private TableView<Dealer> dealerTable;
    @FXML private TableColumn<Dealer, String> nameCol;
    @FXML private TableColumn<Dealer, String> contactCol;
    @FXML private TableColumn<Dealer, String> locationCol;
    @FXML private Label OutputPanel;

    private Stage stage;
    private Scene scene;

    //when function calls, automatically table column binds to relevant properties of dealer(class)
    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
    }

    //selecting dealers' method
    @FXML
    private void handleClickHereButton() {
        try {
            //read dealers' file
            List<Dealer> allDealers = readDealersFromFile("src/main/resources/Dealers/DealerDetails.txt");
            if (allDealers.isEmpty()) {
                showError("No dealers found in the file.");
                return;
            }
            //to randomly pick
            Collections.shuffle(allDealers);
            //add to the array that has a limit of 4
            List<Dealer> selectedDealers = allDealers.stream().limit(4).collect(Collectors.toList());
            //sorting by location(Method calling)
            bubbleSortDealersByLocation(selectedDealers);
            //sent to the dealers' table
            dealerTable.getItems().setAll(selectedDealers);
            OutputPanel.setText("4 Dealers are randomly selected.");
            OutputPanel.setTextFill(Color.GREEN);
        } catch (FileNotFoundException e) {
            OutputPanel.setText("File not found.");
            OutputPanel.setTextFill(Color.RED);
        } catch (IOException e) {
            OutputPanel.setText("Error reading file.");
            OutputPanel.setTextFill(Color.RED);
        } catch (Exception e) {
            OutputPanel.setText("unexpected error.");
            OutputPanel.setTextFill(Color.RED);
        }
    }
    //get dealer details only from the file(not items of them)
    private List<Dealer> readDealersFromFile(String filePath) throws IOException {
        List<Dealer> dealerList = new ArrayList<>();
        //reading file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                //separating using ","
                String[] dealerParts = line.split(",");
                //dealers' details only have 3 properties so,
                if (dealerParts.length < 3) continue;
                //assign them
                String name = dealerParts[0].trim();
                String contact = dealerParts[1].trim();
                String location = dealerParts[2].trim();
                //add them to the dealer arraylist
                dealerList.add(new Dealer(name, contact, location));
                // Skip items
                while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                }
            }
        }
        //return dealer list to the "handleClickHereButton()"
        return dealerList;
    }
    //sorting method according to the location
    private void bubbleSortDealersByLocation(List<Dealer> dealers) {
        int n = dealers.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                String loc1 = dealers.get(j).getLocation().toLowerCase();
                String loc2 = dealers.get(j + 1).getLocation().toLowerCase();

                if (loc1.compareTo(loc2) > 0) {
                    Collections.swap(dealers, j, j + 1);
                }
            }
        }
    }
    //error showing
    private void showError(String message) {
        OutputPanel.setText(message);
        OutputPanel.setTextFill(Color.RED);
    }
    //app close
    public void handleExit(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    //navigate to dealer's item page with selected dealer name
    public void GotoDealerItemsPage(ActionEvent event) throws IOException {
        Dealer selectedDealer = dealerTable.getSelectionModel().getSelectedItem();
        if (selectedDealer == null) {
            showError("Please select a dealer first.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dealerItemsPage-view.fxml"));
        Parent root = loader.load();

        DealerItemsPageController controller = loader.getController();
        controller.setSelectedDealer(selectedDealer.getName());

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Dealer's Items Page");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
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
