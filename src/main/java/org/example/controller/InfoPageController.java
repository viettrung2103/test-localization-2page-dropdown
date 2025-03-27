package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.example.ObservableResourceFactory;
import org.example.model.LanguageLabel;
import org.example.utils.Utils;

public class InfoPageController extends PageController {


    @FXML
    private Button backBtn;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private ComboBox<LanguageLabel> languageBox;

    private  ObservableResourceFactory RESOURCE_FACTORY;
    private final LanguageLabel[] supportedLanguages = new LanguageLabel[4];


    @FXML
    public void initialize(){
        RESOURCE_FACTORY = ObservableResourceFactory.getInstance();
        RESOURCE_FACTORY.getResources();
//        setupLanguageBox();
        Utils.setupLanguageBox(
                languageBox,
                supportedLanguages,
                RESOURCE_FACTORY,
                this
        );
        bindUIComponents();
        super.updateDisplay();

    }

    // this is to update the UI components that require the data input
    // for example my name is {0} and I am {1} years old
    @Override
    public void updateAllUIComponents() {

    }

    @Override
    public void bindUIComponents() {
//        textInput.promptTextProperty().bind(RESOURCE_FACTORY.getStringBinding("prompt"));
        backBtn.textProperty().bind(RESOURCE_FACTORY.getStringBinding("back"));
        label1.textProperty().bind(RESOURCE_FACTORY.getStringBinding("continue"));
        label2.textProperty().bind(RESOURCE_FACTORY.getStringBinding("job"));

    }

    @FXML
    void backBtnClick() {
        String pageName = "/fxml/hello-page.fxml";
        Utils.gotoPage(pageName, backBtn);
    }
}
