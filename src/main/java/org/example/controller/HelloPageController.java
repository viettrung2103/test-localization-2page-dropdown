package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.example.ObservableResourceFactory;
import org.example.model.LanguageLabel;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class HelloPageController {

    private final ObservableResourceFactory RESOURCE_FACTORY = ObservableResourceFactory.getInstance();

    @FXML private Button button;
    @FXML private Button nextBtn;
    @FXML private ComboBox<LanguageLabel> languageBox;
    @FXML private Label nameLabel;
    @FXML private TextField textInput;

    private final LanguageLabel[] supportedLanguages = {
            new LanguageLabel("english", "English"),
            new LanguageLabel("finnish", "Finnishe"),
            new LanguageLabel("chinese", "Chinese"),
            new LanguageLabel("vietnamese", "Vietnamese")
    };

    @FXML
    public void initialize() {
        RESOURCE_FACTORY.setResources(ResourceBundle.getBundle("messages", new Locale("en", "US")));
        setupLanguageBox();
        bindUIComponents();
    }

    private void bindUIComponents() {
        textInput.promptTextProperty().bind(RESOURCE_FACTORY.getStringBinding("prompt"));
        button.textProperty().bind(RESOURCE_FACTORY.getStringBinding("button"));
        nextBtn.textProperty().bind(RESOURCE_FACTORY.getStringBinding("next"));
    }

    @FXML
    void buttonClick(MouseEvent event) {
        String input = textInput.getText();
        if (!input.isEmpty()) {
            ResourceBundle rb = RESOURCE_FACTORY.getResources();
            String result = MessageFormat.format(rb.getString("name"), input);
            Platform.runLater(() -> nameLabel.setText(result));
        }
    }

    private void setupLanguageBox() {
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
                updateLanguageBoxLabels();
                updateNameLabelIfInputExists();
            }
        });

        // Initial label setup
        updateLanguageBoxLabels();
    }

    private ResourceBundle getResourceBundleFromKey(String key) {
        return switch (key) {
            case "finnish" -> ResourceBundle.getBundle("messages", new Locale("fi", "FI"));
            case "chinese" -> ResourceBundle.getBundle("messages", new Locale("cn", "CN"));
            case "vietnamese" -> ResourceBundle.getBundle("messages", new Locale("vn", "VN"));
            default -> ResourceBundle.getBundle("messages", new Locale("en", "US"));
        };
    }
    private void updateLanguageBoxLabels() {
        ResourceBundle rb = RESOURCE_FACTORY.getResources();
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

    private void updateNameLabelIfInputExists() {
        String input = textInput.getText();
        if (!input.isEmpty()) {
            ResourceBundle rb = RESOURCE_FACTORY.getResources();
            String result = MessageFormat.format(rb.getString("name"), input);
            Platform.runLater(() -> nameLabel.setText(result));
        }
    }

    @FXML
    public void nextBtnClick() {
        System.out.println("Go to next page");
    }
}
