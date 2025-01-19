package com.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ControllerMobile {

    @FXML
    private ListView<String> basicListView;

    @FXML
    private Label titleLabel;

    @FXML
    private AnchorPane anchorPane;

    private JSONArray jsonArray;

    public void initialize() {
        basicListView.getItems().addAll("Personatges", "Jocs", "Consoles");

        basicListView.setOnMouseClicked(event -> {
            String selectedItem = basicListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                loadSubOptions(selectedItem);
            }
        });
    }

    private void loadSubOptions(String category) {
        titleLabel.setText(category);

        String jsonPath = switch (category) {
            case "Personatges" -> "/assets/info/data/personatges.json";
            case "Jocs" -> "/assets/info/data/jocs.json";
            case "Consoles" -> "/assets/info/data/consoles.json";
            default -> null;
        };

        if (jsonPath != null) {
            try {
                URL resource = getClass().getResource(jsonPath);

                BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()));
                StringBuilder jsonContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
                reader.close();

                jsonArray = new JSONArray(jsonContent.toString());
                basicListView.getItems().clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    basicListView.getItems().add(obj.getString("nom"));
                }

                basicListView.setOnMouseClicked(event -> {
                    int index = basicListView.getSelectionModel().getSelectedIndex();
                    if (index >= 0) {
                        JSONObject selectedItem = jsonArray.getJSONObject(index);
                        showDetails(selectedItem);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showDetails(JSONObject item) {
        titleLabel.setText(item.getString("nom"));
        basicListView.setVisible(false);
        anchorPane.getChildren().clear();

        StackPane detailPane = new StackPane();
        detailPane.setPrefSize(anchorPane.getWidth(), anchorPane.getHeight());
        detailPane.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Nombre: " + item.getString("nom"));
        Label descriptionLabel = new Label("Descripción: " + item.optString("descripcio", "No disponible"));

        ImageView imageView = null;
        if (item.has("imatge")) {
            String imagePath = item.getString("imatge");
            try {
                imageView = loadImageView(imagePath);
                imageView.setFitWidth(200);
                imageView.setPreserveRatio(true);
            } catch (Exception e) {
                System.out.println("Error cargando la imagen: " + imagePath);
            }
        }

        VBox detailsBox = new VBox(10, nameLabel, descriptionLabel);
        detailsBox.setAlignment(Pos.CENTER);

        if (imageView != null) {
            detailsBox.getChildren().add(0, imageView);
        }

        detailPane.getChildren().add(detailsBox);
        anchorPane.getChildren().add(detailPane);
    }

    private ImageView loadImageView(String imagePath) {
        try {
            URL imageURL = getClass().getResource("/assets/info/images/" + imagePath);
            if (imageURL == null) {
                System.out.println("Imagen no encontrada: " + imagePath);
                return null;
            }

            Image image = new Image(imageURL.toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);

            return imageView;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
