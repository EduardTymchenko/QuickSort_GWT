package com.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.shared.DTOrequest;
import com.gwt.shared.DTOresponse;

import java.util.List;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface SortServiceAsync {
  void getNumbersArrayServer(String numbers, AsyncCallback<List<Integer>> callback)
      throws IllegalArgumentException;
  void getStepsSort(DTOrequest request, AsyncCallback<List<DTOresponse>> callback);
}
