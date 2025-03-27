package org.example.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.example.ObservableResourceFactory;
import org.example.controller.PageController;
import org.example.model.LanguageLabel;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class Utils {

    public  static void gotoPage(String pageName, Button btn){

        FXMLLoader fxmlLoader = new FXMLLoader(Utils.class.getResource(pageName));
        Stage stage = (Stage) btn.getScene().getWindow();
        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/logIn_view.fxml"));
            Parent root = fxmlLoader.load();

//            var stage = getStage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void setupLanguageBox(
            ComboBox<LanguageLabel> languageBox,
            LanguageLabel[] supportedLanguages,
            ObservableResourceFactory RESOURCE_FACTORY,
            PageController pageController) {
        // get the supported languages from the resource bundle
        getAndSetSupportedLanguages(supportedLanguages, RESOURCE_FACTORY);

        // set the supportedLanguages to the languageBox
        languageBox.getItems().setAll(supportedLanguages);

        // Cell display logic
        languageBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<LanguageLabel> call(ListView<LanguageLabel> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(LanguageLabel item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? null : item.getText());
                    }
                };
            }
        });

        // Converter logic
        // this handle display logic in the dropdown
        languageBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(LanguageLabel object) {
                // 🟢 this displays the label in the dropdown
                return object == null ? "" : object.getText();
            }

            @Override
            public LanguageLabel fromString(String string) {
                return null;
            }
        });

        // Set default selection
        languageBox.setValue(supportedLanguages[0]);

        // Language change handler
        languageBox.setOnAction(event -> {
            LanguageLabel selected = languageBox.getValue();
            if (selected != null) {
                ResourceBundle bundle = getResourceBundleFromKey(selected.getKey());
                RESOURCE_FACTORY.setResources(bundle);
                updateLanguageBoxLabels(RESOURCE_FACTORY, languageBox);

                // call the UI update function from the page
                pageController.updateAllUIComponents();
//                updateNameLabelIfInputExists();
            }
        });

        // Initial label setup
        updateLanguageBoxLabels(RESOURCE_FACTORY, languageBox);
    }


    //    public LanguageLabel[] getSupportedLanguages() {
    public static void getAndSetSupportedLanguages(LanguageLabel[] supportedLanguages, ObservableResourceFactory RESOURCE_FACTORY) {
        String[] keys = {"english", "finnish", "chinese", "vietnamese"};
        ResourceBundle rb = RESOURCE_FACTORY.getResources();
        for (int i = 0; i < keys.length; i++) {
            String key  = keys[i];
            String value = rb.getString(key);
            supportedLanguages[i] = new LanguageLabel(key, value);
        }
    }

    private static ResourceBundle getResourceBundleFromKey(String key) {
        return switch (key) {
            case "english"-> ResourceBundle.getBundle("messages", new Locale("en", "US"));
            case "finnish" -> ResourceBundle.getBundle("messages", new Locale("fi", "FI"));
            case "chinese" -> ResourceBundle.getBundle("messages", new Locale("cn", "CN"));
            case "vietnamese" -> ResourceBundle.getBundle("messages", new Locale("vn", "VN"));
            default -> ResourceBundle.getBundle("messages", new Locale("en", "US"));
        };
    }


    private static void updateLanguageBoxLabels(ObservableResourceFactory RESOURCE_FACTORY, ComboBox<LanguageLabel> languageBox) {
        ResourceBundle rb = RESOURCE_FACTORY.getResources();
        languageBox.getItems();
        LanguageLabel selected = languageBox.getValue(); // preserve old selection key
        String selectedKey = selected != null ? selected.getKey() : "english";

        // Rebuild language items with translated labels
        LanguageLabel[] updatedLanguages = {
                new LanguageLabel("english", rb.getString("english")),
                new LanguageLabel("finnish", rb.getString("finnish")),
                new LanguageLabel("chinese", rb.getString("chinese")),
                new LanguageLabel("vietnamese", rb.getString("vietnamese"))
        };

        // Temporarily remove event handler to avoid recursion
        var handler = languageBox.getOnAction();
        languageBox.setOnAction(null);

        languageBox.getItems().setAll(updatedLanguages);

        // Restore selection
        for (LanguageLabel label : updatedLanguages) {
            if (label.getKey().equals(selectedKey)) {
                languageBox.setValue(label);
                break;
            }
        }

        // Restore event handler
        languageBox.setOnAction(handler);
    }

    private static void updateNameLabelIfInputExists(TextField textInput, ObservableResourceFactory RESOURCE_FACTORY, Label label, String key) {
        String input = textInput.getText();
        if (!input.isEmpty()) {
            ResourceBundle rb = RESOURCE_FACTORY.getResources();
            String result = MessageFormat.format(rb.getString(key), input);
            Platform.runLater(() -> label.setText(result));
        }
    }

}
