package com.gwt.client;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.EntryPoint;

public class MainClient implements EntryPoint {

  public void onModuleLoad() {
    Intro intro = new Intro();
    RootPanel.get("mainContent").add(intro);
    intro.setFocusNumberField();
  }
}
