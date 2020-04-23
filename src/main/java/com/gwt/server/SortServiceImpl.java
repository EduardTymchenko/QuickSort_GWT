package com.gwt.server;

import com.gwt.client.SortService;
import com.gwt.shared.DTOrequest;
import com.gwt.shared.DTOresponse;
import com.gwt.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class SortServiceImpl extends RemoteServiceServlet implements SortService {
    private Random random = new Random();
    private List<DTOresponse> dtoSortList;
    private static Logger logger = Logger.getLogger("");

    @Override
    public List<Integer> getNumbersArrayServer(String numbers) {
        logger.log(Level.INFO, String.format("Got a number to generate random numbers: %s", numbers));
        if (!FieldVerifier.isValidNumber(numbers, 1, 1000)) {
            throw new IllegalArgumentException("Name must be number from 1 to 1000 [1,1000]");
        }
        return generateArrayInt(Integer.parseInt(numbers), 0, 1000);
    }

    private List<Integer> generateArrayInt(int length, int rangeMin, int rangeMax) {
        logger.log(Level.INFO, String.format("Generate integer array length: %d, range [%d,%d]",length,rangeMin,rangeMax));
        //At least one value should be equal or less than 30
        boolean isOkArray = false;
        List<Integer> arrayInt = new ArrayList<>();
        while (!isOkArray) {
            arrayInt.clear();
            for (int i = 0; i < length; i++) {
                int randomNumber = random.nextInt(rangeMax - rangeMin + 1) + rangeMin;
                arrayInt.add(randomNumber);
                if (randomNumber <= 30) isOkArray = true;
            }
        }
        logger.log(Level.INFO, String.format("Generated integer array: %s", arrayInt));
        return arrayInt;
    }


    @Override
    public List<DTOresponse> getStepsSort(DTOrequest request) {
        dtoSortList = new ArrayList<>();
        int high = request.getIntegerList().size() - 1;
        sortIntArray(request.getIntegerList(),0, high, request.getNumberAlgorithm(),request.isIncreasing());
        logger.log(Level.INFO, String.format("Return list steps QuickSort to client:\n %s", dtoSortList));
        return dtoSortList;
    }


    public void sortIntArray(List<Integer> integerList, int low, int high, int numberAlgorithmForPaviot, boolean isIncreasing) {
        logger.log(Level.INFO, String.format("Array %s will be sorted. Start index=%d, end index=%d, " +
                        "number algorithm for pivot=%d, increasing order sort=%s",
                integerList, low, high, numberAlgorithmForPaviot,isIncreasing));
        if (integerList.isEmpty() || low >= high)
            return;
        // select pivot element
        int indexPaviot = indexPivot(low, high, numberAlgorithmForPaviot);
        int pivot = integerList.get(indexPaviot);
        // split into subarrays that are larger and smaller than the Paviot element
        int i = low;
        int j = high;
        logger.log(Level.INFO, "Step sort: " + integerList + " i=" + i + " j=" + j + " pivot=" + pivot
                + " indexPivot=" + indexPaviot);
        dtoSortList.add(new DTOresponse(low, high, i, j, indexPaviot, integerList));
        while (i <= j) {

            while ((integerList.get(i) > pivot ^ isIncreasing) && integerList.get(i) != pivot) {
                i++;
                logger.log(Level.INFO, "Step sort: " + integerList + " i=" + i + " j=" + j);
                dtoSortList.add(new DTOresponse(low, high, i, j, indexPaviot, integerList));
            }

            while ((integerList.get(j) < pivot ^ isIncreasing) && integerList.get(j) != pivot) {
                j--;
                logger.log(Level.INFO, "Step sort: " + integerList + " i=" + i + " j=" + j);
                dtoSortList.add(new DTOresponse(low, high, i, j, indexPaviot, integerList));
            }
            if (i < j) {
                // swap for Paviot==low
                if (i == indexPaviot) {
                    integerList.add(i, integerList.get(j));
                    integerList.remove(j + 1);
                    logger.log(Level.INFO, "Step sort: " + integerList + " i=" + i + " j=" + j + " swap low");
                    dtoSortList.add(new DTOresponse(low, high, i, j, indexPaviot, true, integerList));
                    indexPaviot = ++i;
                    // swap for Paviot==high
                } else if (j == indexPaviot) {
                    integerList.add(j + 1, integerList.get(i));
                    integerList.remove(i);
                    logger.log(Level.INFO, "Step sort: " + integerList + " i=" + i + " j=" + j + " swap high");
                    dtoSortList.add(new DTOresponse(low, high, i, j, indexPaviot, true, integerList));
                    indexPaviot = --j;
                    // swap for Paviot in the middle
                } else {
                    Collections.swap(integerList, i, j);
                    logger.log(Level.INFO, "Step sort: " + integerList + " i=" + i + " j=" + j + " swap");
                    dtoSortList.add(new DTOresponse(low, high, i, j, indexPaviot, true, integerList));
                    i++;
                    j--;

                }
                logger.log(Level.INFO, "Step sort: " + integerList + " i=" + i + " j=" + j);
                dtoSortList.add(new DTOresponse(low, high, i, j, indexPaviot, integerList));
            } else {
                i++;
                logger.log(Level.INFO, "Step sort: " + "stop");
            }
//            for Paviot==high if sorted
            if (j == indexPaviot && j == high)
                j--;
        }
        // recursion call to sort left and right
        if (low < j) //left
            sortIntArray(integerList, low, j, numberAlgorithmForPaviot, isIncreasing);

        if (high > i) //right
            sortIntArray(integerList, i, high, numberAlgorithmForPaviot, isIncreasing);


    }

    /**
     * select index pivot element
     *
     * @param numberAlgorithm = 1 - Paviot = low
     * @param numberAlgorithm = 2 - Paviot = high
     * @param numberAlgorithm = 3 - Paviot = random
     * @param numberAlgorithm = any - Paviot = middle, it is default
     * @return int index Paviot
     */
    private int indexPivot(int low, int high, int numberAlgorithm) {
        switch (numberAlgorithm) {
            case 1:
                return low;
            case 2:
                return high;
            case 3:
                return new Random().nextInt(high - low + 1) + low;
            default:
                return low + (high - low) / 2;
        }
    }
}