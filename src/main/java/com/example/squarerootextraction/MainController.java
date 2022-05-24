package com.example.squarerootextraction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    /*
    Хотел сделать, чтобы текст ставился по айдишникам, но че-то не получилось и пришлось вынести все элементы.....
     */
    @FXML
    private Label precision;
    @FXML
    private Label value;
    @FXML
    private Menu language;

    private LanguageController languageController;

    @Override
    /*
    Вызывается перед показом формы
    */
    public void initialize(URL location, ResourceBundle resources) {
        languageController = new LanguageController(Strings.LanguageFolderName);
        languageController.getListOfLanguages().forEach(pair -> {
            String languageName = pair.getKey();
            String shortLanguageName = pair.getValue();

            MenuItem menuItem = new MenuItem(languageName);
            menuItem.setId(shortLanguageName);
            menuItem.setOnAction(this::onClick_LanguageChange);

            language.getItems().add(menuItem);
        });
        changeLanguage("ru");
    }

    @FXML
    /*
    Нажатие на кнопку "="
     */
    public void onClick_Calculate(ActionEvent actionEvent) {
        System.out.println("test");
    }

    @FXML
    public void onClick_LanguageChange(ActionEvent actionEvent) {
        String id = ((MenuItem)actionEvent.getSource()).getId();
        changeLanguage(id);
    }

    private void changeLanguage(String newShortLanguageName)
    {
        /*
        name=Русский
        language=Язык
        precision=Точность
        value=Значение
         */
        language.setText(languageController.getLocalizedName(newShortLanguageName, "language"));
        precision.setText(languageController.getLocalizedName(newShortLanguageName, "precision"));
        value.setText(languageController.getLocalizedName(newShortLanguageName, "value"));
    }
}
