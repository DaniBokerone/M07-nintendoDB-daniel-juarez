package com.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ControllerDesktop {

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private ImageView loadImage;
    @FXML
    private Text loadTitle;
    @FXML
    private Text loadDesc;

    @FXML
    private VBox vBoxContent;

    public void initialize() {
        choiceBox.getItems().addAll("Personatges", "Jocs", "Consoles");

        choiceBox.setValue("Personatges");

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "Personatges":
                    loadJsonData("/assets/info/data/personatges.json");
                    break;
                case "Jocs":
                    loadJsonData("/assets/info/data/jocs.json");
                    break;
                case "Consoles":
                    loadJsonData("/assets/info/data/consoles.json");
                    break;
            }
        });

        loadJsonData("/assets/info/data/personatges.json");
    }

    //  cargar los datos JSON
    private void loadJsonData(String filePath) {
        try {
            URL resource = getClass().getResource(filePath);
            if (resource == null) {
                System.out.println("Archivo no encontrado: " + filePath);
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(jsonContent.toString());

            vBoxContent.getChildren().clear();

            // Iterar JSON
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String name = obj.getString("nom");
                String imageName = obj.getString("imatge");

                HBox itemBox = new HBox(10);
                itemBox.setAlignment(Pos.CENTER_LEFT);

                ImageView imageView = loadImageView(imageName);
                if (imageView != null) {
                    itemBox.getChildren().add(imageView);
                }

                Text text = new Text(name);
                
                itemBox.getChildren().add(text);

                itemBox.setOnMouseClicked(event -> {
                    showDetails(obj);
                });

                vBoxContent.getChildren().add(itemBox);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ImageView loadImageView(String imagePath) {
        try {
            URL imageURL = getClass().getResource("/assets/info/images/"+imagePath);
            if (imageURL == null) {
                System.out.println("Imagen no encontrada: " + imagePath);
                return null;
            }

            // Crear ImageView 
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

    private void showDetails(JSONObject obj) {
        String name = obj.optString("nom", "Sense nom");
        String imageName = obj.optString("imatge", "");
    
        loadTitle.setText(name);
    
        StringBuilder fullDescription = new StringBuilder();
    
        for (String key : obj.keySet()) {
            if (!key.equals("imatge") && !key.equals("nom")) {
                String value = obj.get(key).toString();
                fullDescription.append(key).append(": ").append(value).append("\n\n");
            }
        }
    
        loadDesc.setText(fullDescription.toString());
    
        ImageView loadedImage = loadImageView(imageName);
        if (loadedImage != null) {
            loadImage.setImage(loadedImage.getImage());
        } else {
            loadImage.setImage(null);
        }
    }
}
