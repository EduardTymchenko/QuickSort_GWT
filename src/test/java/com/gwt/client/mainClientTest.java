package com.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.shared.FieldVerifier;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class mainClientTest extends GWTTestCase {

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName() {
        return "com.gwt.gwtJUnit";
    }

    /**
     * Tests the FieldVerifier.
     */
    public void testFieldVerifier() {
        assertFalse(FieldVerifier.isValidNumber(null, 1, 1000));
        assertFalse(FieldVerifier.isValidNumber("", 1, 1000));
        assertFalse(FieldVerifier.isValidNumber("a", 1, 1000));
        assertFalse(FieldVerifier.isValidNumber(" ", 1, 1000));
        assertFalse(FieldVerifier.isValidNumber("0", 1, 1000));
        assertFalse(FieldVerifier.isValidNumber("1001", 1, 1000));
        assertFalse(FieldVerifier.isValidNumber("10 ", 1, 1000));
        assertFalse(FieldVerifier.isValidNumber("10g", 1, 1000));
        assertTrue(FieldVerifier.isValidNumber("1", 1, 1000));
        assertTrue(FieldVerifier.isValidNumber("5", 1, 1000));
    }

    /**
     * This test will send a request to the server using the greetServer method in
     * GreetingService and verify the response.
     */
    public void testSortService() {
//     Create the service that we will test.
        SortServiceAsync sortService = GWT.create(SortService.class);
        ServiceDefTarget target = (ServiceDefTarget) sortService;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "gwt/sort");

        // Since RPC calls are asynchronous, we will need to wait for a response
        // after this test method returns. This line tells the test runner to wait
        // up to 10 seconds before timing out.
        delayTestFinish(10000);

        // Send a request to the server.
        sortService.getNumbersArrayServer("7", new AsyncCallback<List<Integer>>() {
            @Override
            public void onFailure(Throwable throwable) {
                // The request resulted in an unexpected error.
                fail("Request failure: " + throwable.getMessage());
            }

            @Override
            public void onSuccess(List<Integer> result) {
                // Verify that the response is correct.
                assertTrue(result.size() == 7);


                // Now that we have received a response, we need to tell the test runner
                // that the test is complete. You must call finishTest() after an
                // asynchronous test finishes successfully, or the test will time out.
                finishTest();
            }
        });

        sortService.getNumbersArrayServer("word", new AsyncCallback<List<Integer>>() {
            @Override
            public void onFailure(Throwable throwable) {
                // The request resulted in an unexpected error.
                assertEquals("Name must be number from 1 to 1000 [1,1000]", throwable.getMessage());
                finishTest();
            }

            @Override
            public void onSuccess(List<Integer> result) {
                fail("result: " + result);
                finishTest();
            }
        });

    }


}
