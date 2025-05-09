package com.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ControllerDesktop {

    @FXML private ListView<HBox> listView;
    @FXML private ImageView loadImage;
    @FXML private Text loadTitle;
    @FXML private Text loadDesc;
    @FXML private ChoiceBox<String> choiceBox;


    public class Item {
        private Map<String, Object> properties = new HashMap<>();
        public void addProperty(String key, Object value) { properties.put(key, value); }
        public String getNom() { return (String) properties.get("nom"); }
        public String getImatge() { return (String) properties.get("imatge"); }
        public Map<String, Object> getProperties() { return properties; }
    }

    @FXML
    public void initialize() {
        ObservableList<String> categories = FXCollections.observableArrayList("Consolas", "Jocs", "Personatges");
        choiceBox.setItems(categories);
        choiceBox.setValue("Jocs");
        choiceBox.setOnAction(e -> {
            String selectedCategory = choiceBox.getValue();
            loadJSONData(selectedCategory);
        });
    
        listView.setOnMouseClicked(e -> handleListItemClick());
        loadJSONData("Jocs");
    }

    private void handleListItemClick() {
        HBox selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        Item item = (Item) selectedItem.getUserData();
        if (item == null) return;

        loadTitle.setText(item.getNom());
        String imagePath = "assets/info/images/" + item.getImatge();

        try (InputStream imageStream = getClass().getClassLoader().getResourceAsStream(imagePath)) {
            if (imageStream != null) {
                loadImage.setImage(new javafx.scene.image.Image(imageStream));
            } else {
                System.out.println("Imagen no encontrada: " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("Error cargando imagen: " + imagePath);
            e.printStackTrace();
        }

        StringBuilder description = new StringBuilder();
        for (Map.Entry<String, Object> entry : item.getProperties().entrySet()) {
            if (!entry.getKey().equals("nom") && !entry.getKey().equals("imatge")) {
                description.append(Character.toUpperCase(entry.getKey().charAt(0)))
                           .append(entry.getKey().substring(1))
                           .append(": ")
                           .append(entry.getValue())
                           .append("\n");
            }
        }

        loadDesc.setText(description.toString());
    }

    private void loadJSONData(String category) {
        String jsonFilePath = getJSONFilePathForCategory(category);
        if (jsonFilePath == null) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) jsonContent.append(line);

            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            ObservableList<HBox> items = FXCollections.observableArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Item item = new Item();

                for (String key : jsonObject.keySet()) {
                    item.addProperty(key, jsonObject.get(key));
                }

                HBox hBox = new HBox(10);
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView();
                String imagePath = "assets/info/images/" + item.getImatge();

                try (InputStream imageStream = getClass().getClassLoader().getResourceAsStream(imagePath)) {
                    if (imageStream != null) {
                        imageView.setImage(new javafx.scene.image.Image(imageStream));
                    }
                }

                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                javafx.scene.control.Label label = new javafx.scene.control.Label(item.getNom());
                label.setStyle("-fx-font-weight: bold;");

                hBox.getChildren().addAll(imageView, label);
                hBox.setUserData(item);
                items.add(hBox);
            }

            listView.setItems(items);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getJSONFilePathForCategory(String category) {
        return switch (category) {
            case "Consolas" -> "src/main/resources/assets/info/data/consoles.json";
            case "Jocs" -> "src/main/resources/assets/info/data/jocs.json";
            case "Personatges" -> "src/main/resources/assets/info/data/personatges.json";
            default -> null;
        };
    }
}
