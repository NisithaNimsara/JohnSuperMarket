package org.example.johnsupermarket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AddPageController {

    //links between addPage-view.fxml and controller
    @FXML private TextField AddItemCode;
    @FXML private TextField AddItemName;
    @FXML private TextField AddItemBrand;
    @FXML private TextField AddItemPrice;
    @FXML private TextField AddItemQuantity;
    @FXML private TextField AddItemCategory;
    @FXML private TextField AddItemThreshold;
    @FXML private DatePicker AddItemPurchDate;
    @FXML private Button browseImageButton;
    @FXML private Label OutputPanel;
    @FXML private TextField AddItemImagePath;

    private Stage stage;
    private Scene scene;

    //runs after the FXML is loaded
    public void initialize() {
        browseImageButton.setOnAction(e -> browseForImage());
    }

    //clear all input fields
    @FXML
    private void clearFields() {
        AddItemCode.clear();
        AddItemName.clear();
        AddItemBrand.clear();
        AddItemPrice.clear();
        AddItemQuantity.clear();
        AddItemCategory.clear();
        AddItemThreshold.clear();
        AddItemPurchDate.setValue(null);
        AddItemImagePath.clear();
    }

    //browse button for image selector
    private void browseForImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Item Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        //if selected image is not available in the resource/image folder, create a copy
        if (selectedFile != null) {
            try {
                String fileName = selectedFile.getName();
                File destDir = new File("src/main/resources/images/");
                if (!destDir.exists()) destDir.mkdirs();

                File destFile = new File(destDir, fileName);
                java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                AddItemImagePath.setText(fileName);
            } catch (IOException e) {
                OutputPanel.setText("Failed to copy image.");
            }
        }
    }

    //Main save button logic
    @FXML
    public void onSaveButtonClick() {
        try {
            //assigning inputs that came from fxml file
            String code = AddItemCode.getText().trim();
            String name = AddItemName.getText().trim();
            String brand = AddItemBrand.getText().trim();
            BigDecimal price = new BigDecimal(AddItemPrice.getText().trim());
            int quantity = Integer.parseInt(AddItemQuantity.getText().trim());
            String category = AddItemCategory.getText().trim();
            LocalDate date = AddItemPurchDate.getValue();
            String imagePath = AddItemImagePath.getText();
            int threshold = Integer.parseInt(AddItemThreshold.getText().trim());

            //this will check that all the input fields are filed?
            if (code.isEmpty() || name.isEmpty() || brand.isEmpty() || category.isEmpty() ||
                    AddItemPrice.getText().trim().isEmpty() || AddItemQuantity.getText().trim().isEmpty() ||
                    date == null || AddItemThreshold.getText().trim().isEmpty() || imagePath.isEmpty()) {
                OutputPanel.setText("Please fill all required fields.");
                return;
            }
            //this will check the item code's validation
            if (code.length() != 3) {
                OutputPanel.setText("Invalid code. Must be 3 characters.");
                return;
            }
            //this will avoid the duplicate item codes
            File file = new File("src/main/resources/Inventory/details.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length > 0 && parts[0].equals(code)) {
                        OutputPanel.setText("Item code already exists.");
                        OutputPanel.setTextFill(Color.RED);
                        reader.close();
                        return;
                    }
                }
                reader.close();
            }
            //this will avoid negative numbers and 0
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                OutputPanel.setText("Price must be greater than 0.");
                return;
            }
            //this will avoid negative numbers and 0
            if (quantity <= 0) {
                OutputPanel.setText("Quantity must be greater than 0.");
                return;
            }
            if (threshold <= 0) {
                OutputPanel.setText("Threshold must be greater than 0.");
                return;
            }
            //append item to file
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/Inventory/details.txt", true));
            writer.write(String.join(",", escape(code), escape(name), escape(brand), price.toString(),
                    String.valueOf(quantity), escape(category), date.toString(), String.valueOf(threshold), escape(imagePath)));
            writer.newLine();
            writer.close();
            //send to user a message
            OutputPanel.setText("Item added successfully.");
            OutputPanel.setTextFill(Color.GREEN);
            clearFields();

        } catch (NumberFormatException e) {
            OutputPanel.setText("Invalid numeric input.");
        } catch (Exception e) {
            OutputPanel.setText("Error: " + e.getMessage());
        }
    }
    //helper method to escape commas
    private String escape(String input) {
        return input.replace(",", "\\,");
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
    //navigate to inventory page
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
    //close the app
    public void handleExit(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
