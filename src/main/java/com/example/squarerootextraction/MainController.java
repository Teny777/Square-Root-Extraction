package com.example.squarerootextraction;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @FXML
    private Spinner<Integer> precisionValue;
    @FXML
    private Button button;
    @FXML
    private TextField inputValue;



    private static final String realNumberRegEx = "^\\d+(\\.\\d+)?$";
    private static final String complexNumberRegEx1 = "^(-?\\d+(\\.\\d+)?)([+-](\\d+(\\.\\d+)?)?)[i$]";
    private static final String complexNumberRegEx2 = "^(-?\\d+(\\.\\d+)?)";
    private static final String complexNumberRegEx3 = "^(-?(\\d+(\\.\\d+)?)?)[i$]";

    private static final String complexNumberRegEx4 = "^(-?(\\d+(\\.\\d+)?)?)[i]([+-](\\d+(\\.\\d+)?))";

    private LanguageController languageController;
    private Object number;
    private String currentLanguage;
    @FXML
    private TextField resultValue;
    @FXML
    private Menu help;
    @FXML
    private MenuItem aboutItem;
    @FXML
    private MenuItem helpItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLanguages();
        initializeInputValues();
    }

    //Инициализация допустимых языков
    private void initializeLanguages(){
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
        currentLanguage = "ru";
    }

    //Инициализация диапазона значений точности
    private void initializeInputValues(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        valueFactory.setValue(1);

        precisionValue.setValueFactory(valueFactory);
        inputValue.textProperty().addListener(this::inputValueChanged);
    }

    @FXML
    /*
    Нажатие на кнопку "="
     */
    public void onClick_Calculate(ActionEvent actionEvent) {
        if (number instanceof BigDecimal)
            resultValue.setText((SqrtSolver.sqrt((BigDecimal)number, precisionValue.getValue()).setScale(10, RoundingMode.HALF_UP)).toString());
        else{
            Complex result = SqrtSolver.sqrt((Complex) number, precisionValue.getValue());
            if (result.getImaginary().abs().compareTo(BigDecimal.valueOf(Math.pow(0.1, 7))) < 0) resultValue.setText(result.toString());
            else resultValue.setText("±(" + result.toString() + ")");
        }
    }

    @Deprecated
    public void onClick_LanguageChange(ActionEvent actionEvent) {
        String id = ((MenuItem)actionEvent.getSource()).getId();
        changeLanguage(id);
        currentLanguage = id;
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
        help.setText(languageController.getLocalizedName(newShortLanguageName, "help"));
        helpItem.setText(languageController.getLocalizedName(newShortLanguageName, "reference"));
        aboutItem.setText(languageController.getLocalizedName(newShortLanguageName, "about"));


    }

    //Проверка вводимого значения
    private void inputValueChanged(ObservableValue<? extends String> observable, String oldValue, String newValue){
        String trimmedValue = newValue.trim();

        if (trimmedValue.matches(realNumberRegEx))
            number = new BigDecimal(trimmedValue);
        else
            number = checkComplexValue(trimmedValue);

        checkDisableButton();
    }

    private void checkDisableButton(){
        button.setDisable(number == null);
    }

    //Проверка комплексного числа
    private Complex checkComplexValue(String value){

        Complex result = null;
        if (value.matches(complexNumberRegEx1)){
            int indexOfOperation = getFirstOperationIndex(value);

            String real = "", imaginary = "";

            for (int i = 0; i < value.length() - 1; ++i){
                if (i < indexOfOperation)
                    real += value.charAt(i);
                if (i >= indexOfOperation)
                    imaginary += value.charAt(i);
            }
            if (imaginary.equals("-")) imaginary = "-1";
            if (imaginary.equals("+")) imaginary = "1";
            result = new Complex(new BigDecimal(real), new BigDecimal(imaginary));
        }

        else if(value.matches(complexNumberRegEx2)){
            result = new Complex(new BigDecimal(value), BigDecimal.valueOf(0));
        }

        else if(value.matches(complexNumberRegEx3)){
            if (value.equals("-i")) value = "-1i";
            if (value.equals("i")) value = "1i";
            result = new Complex(BigDecimal.valueOf(0), new BigDecimal(value.substring(0, value.length() - 1)));
        }

        else if(value.matches(complexNumberRegEx4)){
            int indexOfOperation = getFirstOperationIndex(value);

            String real = "", imaginary = "";

            for (int i = 0; i < value.length(); ++i){
                if (i < indexOfOperation - 1)
                    imaginary += value.charAt(i);
                if (i >= indexOfOperation)
                    real += value.charAt(i);
            }
            if (imaginary.equals("-")) imaginary = "-1";
            if (imaginary.equals("")) imaginary = "1";
            result = new Complex(new BigDecimal(real), new BigDecimal(imaginary));
        }

        return result;
    }

    private int getFirstOperationIndex(String value){
        int indexOfOperation = - 1;
        for (int i = value.length() - 1; i >= 0; --i)
            if (value.charAt(i) == '+' || value.charAt(i) == '-'){
                indexOfOperation = i;
                break;
            }
        return indexOfOperation;
    }


    @FXML
    public void showHelpMessage(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, languageController.getLocalizedName(currentLanguage, "referenceText"));
        alert.setHeaderText(languageController.getLocalizedName(currentLanguage, "reference"));
        alert.show();
    }

    @FXML
    public void showAboutMessage(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, languageController.getLocalizedName(currentLanguage,"aboutText"));
        alert.setHeaderText(languageController.getLocalizedName(currentLanguage,"about"));
        alert.show();
    }
}
