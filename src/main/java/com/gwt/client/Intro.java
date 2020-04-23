package com.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwt.shared.FieldVerifier;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Intro extends Composite {
    private final SortServiceAsync sortServiceAsync = GWT.create(SortService.class);
    private FlowPanel introContainer = new FlowPanel();
    private final Label questionLabel = new Label();
    private final TextBox numberField = new TextBox();
    private final Label errorLabel = new Label();
    private final Button enterButton = new Button("Enter");
    private static Logger logger = Logger.getLogger("");

    public Intro() {
        buildIntroWindow();
    }

    private void buildIntroWindow() {
        introContainer.addStyleName("introContainer");
        questionLabel.setText("How many numbers to display?");
        questionLabel.addStyleName("questionLabel");
        numberField.setMaxLength(4);
        errorLabel.addStyleName("errorLabel");

        introContainer.add(questionLabel);
        introContainer.add(numberField);
        introContainer.add(errorLabel);
        introContainer.add(enterButton);
        initWidget(introContainer);

        enterButton.addClickHandler(clickEvent -> {
            errorLabel.setText("");
            String numbersToDisplay = numberField.getText();
            logger.log(Level.SEVERE, "Enter numbers of display: " + numbersToDisplay);
            if (FieldVerifier.isValidNumber(numbersToDisplay, 1, 1000)) {
                logger.log(Level.SEVERE, "Send to server number to display: " + numbersToDisplay);
                sortServiceAsync.getNumbersArrayServer(numbersToDisplay, new AsyncCallback<List<Integer>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        AllWindowsMethods.dialogBoxError(caught);
                    }
                    @Override
                    public void onSuccess(List<Integer> resultArray) {
                        logger.log(Level.SEVERE, "Got array from server:" + "\n" + resultArray);
                        Sort sortWindow = new Sort(resultArray);
                        AllWindowsMethods.showNewWindow(sortWindow);
                    }
                });

            } else {
                errorLabel.setText("Please enter number from 1 to 1000 [1,1000]");
                numberField.setFocus(true);
                numberField.selectAll();
            }
        });
    }

    public void setFocusNumberField() {
        numberField.setFocus(true);
    }
}
