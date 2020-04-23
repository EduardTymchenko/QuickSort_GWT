package com.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwt.shared.DTOrequest;
import com.gwt.shared.DTOresponse;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Sort extends Composite {
    private final int showTimeDefault = 1000;  // Time show step Quick Sort, mc
    private FlowPanel sortContainer;
    private final Button sortButton = new Button("Sort");
    private final Button resetButton = new Button("Reset");
    private List<Button> numberButtonsList = new ArrayList<>();
    private final TextBox speedField = new TextBox();

    private final SortServiceAsync sortServiceAsync = GWT.create(SortService.class);
    private boolean isIncreasingSort;
    private boolean isSortShow;
    private static Logger logger = Logger.getLogger("");

    public Sort(List<Integer> arrNumbers) {
        buildSortWindow(arrNumbers);
    }

    private void buildSortWindow(List<Integer> arrNumbers) {
        sortContainer = new FlowPanel();
        sortContainer.addStyleName("sortContainer");
        buildNumberBlock(arrNumbers);
        buildButtonBlock();
        initWidget(sortContainer);
    }

    private void buildButtonBlock() {
        FlowPanel buttonBlockContainer = new FlowPanel();
        buttonBlockContainer.addStyleName("buttonBlock");
        sortButton.addStyleName("btnSort");
        resetButton.addStyleName("btnSort");
        buttonBlockContainer.add(sortButton);
        buttonBlockContainer.add(resetButton);
        sortContainer.add(buttonBlockContainer);
        // Button "Sort"
        sortButton.addClickHandler(clickEvent -> {
            logger.log(Level.SEVERE, "Click button \"Sort\"");
            DTOrequest request = new DTOrequest(getIntegerListFromButtonList(numberButtonsList), 0, isIncreasingSort);
            logger.log(Level.SEVERE, "Request to server:\n" + request);
            sortServiceAsync.getStepsSort(request, new AsyncCallback<List<DTOresponse>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    AllWindowsMethods.dialogBoxError(throwable);
                }

                @Override
                public void onSuccess(List<DTOresponse> dtoResponses) {
                    logger.log(Level.SEVERE, "Got array steps sort from server:\n" + dtoResponses);

                    visualSort(dtoResponses, generateTimeShow());
                    isIncreasingSort = !isIncreasingSort;
                }
            });
        });
        // Button "Reset"
        resetButton.addClickHandler(clickEvent -> {
            logger.log(Level.SEVERE, "Click button \"Reset\"");
            Intro intro = new Intro();
            AllWindowsMethods.showNewWindow(intro);
            intro.setFocusNumberField();
        });
        buttonBlockContainer.add(speedSortBuildBlock());
    }

    private Widget speedSortBuildBlock() {
        FlowPanel blockSpeedSort = new FlowPanel();
        Label labelSpeedField = new Label("Enter speed show sort [1,30] \n (default 1 s):");
        labelSpeedField.addStyleName("speedSort");
        blockSpeedSort.add(labelSpeedField);
        speedField.setMaxLength(2);
        speedField.addStyleName("speedSort");
        blockSpeedSort.add(speedField);
        return blockSpeedSort;
    }


    private void buildNumberBlock(List<Integer> arrNumbers) {
        numberButtonsList.clear();
        FlowPanel numberBlock = null;
        for (int i = 0; i < arrNumbers.size(); i++) {
            if (i % 10 == 0) {
                numberBlock = new FlowPanel();
                numberBlock.addStyleName("numberBlock");
                sortContainer.add(numberBlock);
            }
            Button numberBtn = new Button(arrNumbers.get(i).toString());
            // Block numbers buttons
            numberBtn.addClickHandler(clickEvent -> {
                if (isSortShow) return;
                resetIsisIncreasingSort();
                int numberClick = Integer.parseInt(clickEvent.getRelativeElement().getInnerText());
                logger.log(Level.SEVERE, "Click number button with number=" + numberClick);
                if (numberClick > 30) {
                    PopupPanel popupPanel = new PopupPanel(true);
                    popupPanel.add(new Label("Please select a value smaller or equal to 30."));
                    popupPanel.show();
                    int popupPanelX = clickEvent.getClientX();
                    int popupPanelY = clickEvent.getClientY() - popupPanel.getElement().getAbsoluteBottom();
                    popupPanel.setPopupPosition(popupPanelX, popupPanelY);
                } else {
                    sortServiceAsync.getNumbersArrayServer(String.valueOf(arrNumbers.size()), new AsyncCallback<List<Integer>>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            AllWindowsMethods.dialogBoxError(throwable);
                        }

                        @Override
                        public void onSuccess(List<Integer> integersList) {
                            logger.log(Level.SEVERE, "Got new array from server:\n" + integersList);
                            updateNumberButtonsAll(integersList);
                        }
                    });
                }
            });
            numberButtonsList.add(numberBtn);
            numberBlock.add(numberBtn);
        }
    }

    private void resetIsisIncreasingSort() {
        isIncreasingSort = false;
    }

    private void updateNumberButtonsAll(List<Integer> arrNumbers) {
        if (arrNumbers.size() != numberButtonsList.size()) {
            String errMess = "<p>Server returned array not equal size " + numberButtonsList.size() + " </p>";
            AllWindowsMethods.dialogBoxError(new Throwable(errMess));
            return;
        }
        for (int i = 0; i < numberButtonsList.size(); i++) {
            numberButtonsList.get(i).setText(arrNumbers.get(i).toString());
        }
    }

    private void visualSort(List<DTOresponse> dtoResponses, int timeShow) {
        Timer stepTimer = new Timer() {
            private int step = 1;

            public void run() {
                resetStyleShowSort(dtoResponses, step - 1);
                if (step >= dtoResponses.size()) {
                    disableAllButtonPage(false);
                    this.cancel();
                    return;
                }
                logger.log(Level.SEVERE, "Step sort:\n" + dtoResponses.get(step));
                setStyleShowSort(dtoResponses, step);
                step++;
            }
        };

        disableAllButtonPage(true);
        setStyleShowSort(dtoResponses, 0);
        stepTimer.scheduleRepeating(timeShow);
    }

    private void setStyleShowSort(List<DTOresponse> listSteps, int stepShow) {
        DTOresponse stepObj = listSteps.get(stepShow);
        for (int k=stepObj.getLow(); k <= stepObj.getHigh(); k++){
            numberButtonsList.get(k).addStyleName("sortArray");
        }

        numberButtonsList.get(stepObj.getIndexPaviot()).addStyleName("paviot");
        numberButtonsList.get(stepObj.getI()).addStyleName("pointer");
        numberButtonsList.get(stepObj.getJ()).addStyleName("pointer");
        if (stepObj.isSwap()) {
            updateNumberButtonsAll(stepObj.getIntegerList());
            numberButtonsList.get(stepObj.getI()).addStyleName("swap");
            numberButtonsList.get(stepObj.getJ()).addStyleName("swap");
        }
    }

    private void resetStyleShowSort(List<DTOresponse> listSteps, int stepShow) {
        DTOresponse stepObj = listSteps.get(stepShow);
        for (int k=stepObj.getLow(); k <= stepObj.getHigh(); k++){
            numberButtonsList.get(k).removeStyleName("sortArray");
        }
        numberButtonsList.get(stepObj.getI()).removeStyleName("pointer");
        numberButtonsList.get(stepObj.getI()).removeStyleName("swap");
        numberButtonsList.get(stepObj.getJ()).removeStyleName("pointer");
        numberButtonsList.get(stepObj.getJ()).removeStyleName("swap");
        numberButtonsList.get(stepObj.getIndexPaviot()).removeStyleName("paviot");

    }


    private void disableAllButtonPage(boolean isBlock) {
        sortButton.setEnabled(!isBlock);
        resetButton.setEnabled(!isBlock);
        isSortShow = isBlock;
    }

    private List<Integer> getIntegerListFromButtonList(List<Button> list) {
        return list.stream()
                .map(el -> Integer.parseInt(el.getText()))
                .collect(Collectors.toList());
    }

    public int generateTimeShow() {
        try {
            int speedForm = Integer.parseInt(speedField.getText());
            if (speedForm > 0 && speedForm <= 30) return speedForm * 1000;
        } catch (NumberFormatException ex) {
            return showTimeDefault;
        }
        return showTimeDefault;
    }

}
