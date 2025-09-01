package org.example.johnsupermarket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.johnsupermarket.Models.Item;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UpdatePageController {
    @FXML private Stage stage;
    private Scene scene;

    //links between updatePage-view.fxml and controller
    @FXML private TextField codeInput;
    @FXML private TextField nameInput;
    @FXML private TextField brandInput;
    @FXML private TextField priceInput;
    @FXML private TextField quantityInput;
    @FXML private TextField categoryInput;
    @FXML private TextField thresholdInput;
    @FXML private TextField imagePathInput;
    @FXML private DatePicker dateInput;
    @FXML private Label OutputPanel;
    //declaration
    private List<Item> allItems = new ArrayList<>();
    private Item selectedItem = null;
    //load when call the function
    @FXML
    public void initialize() {
        loadItemsFromFile();
    }
    //-----------------------------------------------------------get items from file
    private void loadItemsFromFile() {
        allItems.clear();
        //open new file obj
        File file = new File("src/main/resources/Inventory/details.txt");
        if (!file.exists()) {
            System.out.println("No inventory file found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            //read line by line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                //make the item object
                if (parts.length == 9) {
                    Item item = new Item(
                            parts[0], parts[1], parts[2],
                            new BigDecimal(parts[3]),
                            Integer.parseInt(parts[4]),
                            parts[5],
                            LocalDate.parse(parts[6]),
                            Integer.parseInt(parts[7]),
                            parts[8]
                    );
                    //add to the list(all)
                    allItems.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading inventory file: " + e.getMessage());
        }
    }
    //search button method
    @FXML
    private void handleSearchItem() {
        //getting item code
        String code = codeInput.getText().trim();
        if (code.isEmpty()) {
            OutputPanel.setText("Enter item code.");
            OutputPanel.setTextFill(Color.RED);
            return;
        }
        //find the item details relate to the item code
        selectedItem = allItems.stream()
                .filter(item -> item.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
        //null handle
        if (selectedItem == null) {
            OutputPanel.setText("Item not found.");
            OutputPanel.setTextFill(Color.RED);
            clearInputFields();
            return;
        }
        //load the item details to related fields(in fxml)
        nameInput.setText(selectedItem.getName());
        brandInput.setText(selectedItem.getBrand());
        priceInput.setText(selectedItem.getPrice().toPlainString());
        quantityInput.setText(String.valueOf(selectedItem.getQuantity()));
        categoryInput.setText(selectedItem.getCategory());
        dateInput.setValue(selectedItem.getDate());
        thresholdInput.setText(String.valueOf(selectedItem.getThreshold()));
        imagePathInput.setText(selectedItem.getImagePath());
        //for user to view
        OutputPanel.setText("Item loaded.");
        OutputPanel.setTextFill(Color.GREEN);
    }
    //update and save method
    @FXML
    private void handleUpdateSave() {
        if (selectedItem == null) {
            OutputPanel.setText("No item selected to update.");
            OutputPanel.setTextFill(Color.RED);
            return;
        }
        //get updated/not details from each field
        String name = nameInput.getText().trim();
        String brand = brandInput.getText().trim();
        String priceText = priceInput.getText().trim();
        String quantityText = quantityInput.getText().trim();
        String category = categoryInput.getText().trim();
        LocalDate date = dateInput.getValue();
        String thresholdText = thresholdInput.getText().trim();
        String imagePath = imagePathInput.getText().trim();
        //check the field's completeness
        if (name.isEmpty() || brand.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()
                || category.isEmpty() || date == null || thresholdText.isEmpty() || imagePath.isEmpty()) {
            OutputPanel.setText("All fields must be filled.");
            OutputPanel.setTextFill(Color.RED);
            return;
        }
        BigDecimal price;
        int quantity;
        int threshold;
        try {
            price = new BigDecimal(priceText);
            quantity = Integer.parseInt(quantityText);
            threshold = Integer.parseInt(thresholdText);
        } catch (NumberFormatException e) {
            OutputPanel.setText("Invalid number input.");
            OutputPanel.setTextFill(Color.RED);
            return;
        }
        //make the item object
        selectedItem = new Item(
                selectedItem.getCode(), name, brand, price, quantity, category, date, threshold, imagePath
        );
        for (int i = 0; i < allItems.size(); i++) {
            if (allItems.get(i).getCode().equalsIgnoreCase(selectedItem.getCode())) {
                allItems.set(i, selectedItem);
                break;
            }
        }
        //update(write) in the details file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/Inventory/details.txt"))) {
            for (Item item : allItems) {
                String line = String.join(",",
                        item.getCode(), item.getName(), item.getBrand(),
                        item.getPrice().toPlainString(),
                        String.valueOf(item.getQuantity()), item.getCategory(),
                        item.getDate().toString(), String.valueOf(item.getThreshold()),
                        item.getImagePath()
                );
                bw.write(line);
                bw.newLine();
            }
            OutputPanel.setText("Item updated and saved.");
            OutputPanel.setTextFill(Color.GREEN);
        } catch (IOException e) {
            OutputPanel.setText("Error saving file.");
            OutputPanel.setTextFill(Color.RED);
        }
    }
    //browse button method
    @FXML
    private void handleBrowseImage() {
        //select image from computer
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Item Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String fileName = selectedFile.getName();
                File destDir = new File("src/main/resources/images/");
                //create a copy (If not exist in the images)
                if (!destDir.exists()) destDir.mkdirs();
                File destFile = new File(destDir, fileName);
                java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                //pass to imageth(on fxml)
                imagePathInput.setText(fileName);
                OutputPanel.setText("Image selected: " + fileName);
                OutputPanel.setTextFill(Color.GREEN);
            } catch (IOException e) {
                OutputPanel.setText("Image copy failed.");
                OutputPanel.setTextFill(Color.RED);
            }
        }
    }
    //clear field method
    private void clearInputFields() {
        nameInput.clear();
        brandInput.clear();
        priceInput.clear();
        quantityInput.clear();
        categoryInput.clear();
        dateInput.setValue(null);
        thresholdInput.clear();
        imagePathInput.clear();
    }

    //exit the app
    public void handleExit(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    //navigate to inventory
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
    //navigate to the home
    public void GotoHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Home Page");
        stage.setScene(scene);
        stage.show();
    }
}
