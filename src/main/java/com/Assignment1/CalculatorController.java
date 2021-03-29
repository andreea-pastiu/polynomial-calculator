package com.Assignment1;

import com.Assignment1.Models.CalculatorModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class CalculatorController
{
    private CalculatorModel model = new CalculatorModel();

    @FXML
    Text polynomial1Text;
    @FXML
    Text polynomial2Text;
    @FXML
    Text resultText;

    @FXML
    private void editPolynomial1()
    {
        model.setPolynomial1(true);
        model.setPolynomial2(false);
    }

    @FXML
    private void editPolynomial2()
    {
        model.setPolynomial2(true);
        model.setPolynomial1(false);
    }

    @FXML
    private void processKeyboard(ActionEvent event)
    {
        String keyPressed = ((Button) event.getSource()).getText();
        if(model.isPolynomial1())
        {
            if(!keyPressed.equals("←"))
            {
                polynomial1Text.setText(polynomial1Text.getText() + keyPressed);
            }
            else
            {
                if(polynomial1Text.getText().length() > 0)
                {
                    polynomial1Text.setText(polynomial1Text.getText().substring(0, polynomial1Text.getText().length() - 1));
                }
            }
        }
        if(model.isPolynomial2())
        {
            if(!keyPressed.equals("←"))
            {
                polynomial2Text.setText(polynomial2Text.getText() + keyPressed);
            }
            else
            {
                if(polynomial2Text.getText().length() > 0)
                {
                    polynomial2Text.setText(polynomial2Text.getText().substring(0, polynomial2Text.getText().length()-1));
                }
            }
        }
    }

    @FXML
    private void resetAll()
    {
        polynomial1Text.setText("");
        polynomial2Text.setText("");
        resultText.setText("");
    }

    @FXML
    private void resetPolynomial1()
    {
        polynomial1Text.setText("");
    }

    @FXML
    private void resetPolynomial2()
    {
        polynomial2Text.setText("");
    }

    @FXML
    private void exit()
    {
        System.exit(0);
    }

    @FXML
    private void processOperator(ActionEvent event)
    {
        String operator = ((Button) event.getSource()).getText();
        Polynomial result = new Polynomial();
        try
        {
            if (operator.equals("+"))
            {

                result = model.add(model.parsePolynomial(polynomial1Text.getText()), model.parsePolynomial(polynomial2Text.getText()));
                resultText.setText(model.toString(result));
            }
            if (operator.equals("-"))
            {
                result = model.subtract(model.parsePolynomial(polynomial1Text.getText()), model.parsePolynomial(polynomial2Text.getText()));
                resultText.setText(model.toString(result));
            }
            if (operator.equals("*"))
            {
                result = model.multiply(model.parsePolynomial(polynomial1Text.getText()), model.parsePolynomial(polynomial2Text.getText()));
                resultText.setText(model.toString(result));
            }
            if (operator.equals("/"))
            {
                DivisionResult divisionResult = model.divide(model.parsePolynomial(polynomial1Text.getText()), model.parsePolynomial(polynomial2Text.getText()));
                resultText.setText("Q: " + model.toString(divisionResult.getQuotient()) + " R: " + model.toString(divisionResult.getRemainder()));
            }
            if (operator.equals("'"))
            {
                result = model.derive(model.parsePolynomial(polynomial1Text.getText()));
                resultText.setText(model.toString(result));
            }
            if (operator.equals("∫"))
            {
                result = model.integrate(model.parsePolynomial(polynomial1Text.getText()));
                resultText.setText(model.toString(result));
            }
        }
        catch (InvalidPolynomialException exception)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
        }
    }
}
