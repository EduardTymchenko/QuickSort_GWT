package com.gwt.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class AllWindowsMethods {
    private static final String SERVER_ERROR = "<p>An error occurred while "
            + "attempting to contact the server.<br> Please check your network "
            + "connection and try again.<p>";

    private AllWindowsMethods() {
    }

    public static void showNewWindow(Composite showWindow) {
        RootPanel.get("mainContent").remove(0);
        RootPanel.get("mainContent").add(showWindow);
    }

    public static void dialogBoxError(Throwable throwable) {
        final DialogBox dialog = new DialogBox(false, true);
        dialog.setText("Error");
        VerticalPanel massageContainer = new VerticalPanel();
        HTML errorMessage = new HTML();
        if (throwable.getMessage().equals("0  ")) errorMessage.setHTML(SERVER_ERROR);
        else errorMessage.setHTML(throwable.getMessage());
        Button buttonOk = new Button("OK");
        buttonOk.addClickHandler(clickEvent -> dialog.hide());
        massageContainer.add(errorMessage);
        massageContainer.add(buttonOk);
        dialog.setWidget(massageContainer);
        dialog.show();

        int popupDialogX = (Window.getClientWidth() - dialog.getElement().getAbsoluteRight()) / 2;
        int popupDialogY = 10;
        dialog.setPopupPosition(popupDialogX, popupDialogY);
    }


}
