package com.gwt.shared;
public class FieldVerifier {

  public static boolean isValidNumber(String inputNumber, int minNumber, int maxNumber) {
    try {
      int numbersToDisplay = Integer.parseInt(inputNumber);
      if (numbersToDisplay < minNumber ||  numbersToDisplay > maxNumber) return false;

    } catch (NumberFormatException ex){
      return false;
    }
    return true;
  }
}
