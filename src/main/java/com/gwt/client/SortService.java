package com.gwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.gwt.shared.DTOrequest;
import com.gwt.shared.DTOresponse;

import java.util.List;
@RemoteServiceRelativePath("sort")
public interface SortService extends RemoteService {
  List<Integer> getNumbersArrayServer(String numbers) throws IllegalArgumentException;
  List<DTOresponse> getStepsSort(DTOrequest request);

}
