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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ControllerMobil {

    @FXML private ListView<HBox> listView;
    @FXML private VBox detailsBox;
    @FXML private ImageView imageViewLarge;
    @FXML private Text titleLarge;
    @FXML private Text descriptionLarge;
    @FXML private Button buttonConsolas;
    @FXML private Button buttonPersonajes;
    @FXML private Button buttonJuegos;

    public class Item {
        private Map<String, Object> properties = new HashMap<>();
        public void addProperty(String key, Object value) { properties.put(key, value); }
        public Object getProperty(String key) { return properties.get(key); }
        public String getNom() { return (String) properties.get("nom"); }
        public String getImatge() { return (String) properties.get("imatge"); }
        public Map<String, Object> getProperties() { return properties; }
    }

    @FXML
    public void initialize() {
        buttonPersonajes.setOnAction(e -> handleCategoryButton("Personatges"));
        buttonJuegos.setOnAction(e -> handleCategoryButton("Jocs"));
        buttonConsolas.setOnAction(e -> handleCategoryButton("Consolas"));
        

        listView.setVisible(false);
        listView.setManaged(false);
        detailsBox.setVisible(false);
        detailsBox.setManaged(false);
    }

    private void handleCategoryButton(String category) {
        loadJSONData(category);
        listView.setVisible(true);
        listView.setManaged(true);
        detailsBox.setVisible(false);
        detailsBox.setManaged(false);
    }

    private void handleListItemClick() {
        HBox selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        Item item = (Item) selectedItem.getUserData();
        if (item == null) return;

        titleLarge.setText(item.getNom());
        titleLarge.setStyle("-fx-font-weight: bold;");
        String imagePath = "assets/info/images/" + item.getImatge();

        try (InputStream imageStream = getClass().getClassLoader().getResourceAsStream(imagePath)) {
            if (imageStream != null) {
                imageViewLarge.setImage(new Image(imageStream));
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

        descriptionLarge.setText(description.toString());
        descriptionLarge.setStyle("-fx-text-alignment: center;");

        listView.setVisible(false);
        listView.setManaged(false);
        detailsBox.setVisible(true);
        detailsBox.setManaged(true);
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
                ImageView imageView = new ImageView();
                String imagePath = "assets/info/images/" + item.getImatge();

                try (InputStream imageStream = getClass().getClassLoader().getResourceAsStream(imagePath)) {
                    if (imageStream != null) {
                        imageView.setImage(new Image(imageStream));
                    }
                }

                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                Label label = new Label(item.getNom());
                label.setStyle("-fx-font-weight: bold;");

                hBox.getChildren().addAll(imageView, label);
                hBox.setUserData(item);
                items.add(hBox);
            }

            listView.setItems(items);
            listView.setOnMouseClicked(e -> handleListItemClick());

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

    public void setResponsiveListeners(Stage stage) {
        // No aplica tanto en modo móvil, pero puedes ajustarlo si usas en tablets
    }
}
