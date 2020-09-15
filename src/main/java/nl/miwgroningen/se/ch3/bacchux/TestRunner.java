package nl.miwgroningen.se.ch3.bacchux;

import nl.miwgroningen.se.ch3.bacchux.test.FirstTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(FirstTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        if (result.wasSuccessful()) {
            System.out.println("Test successful");
        } else {
            System.out.println("Test unsuccesful");
        }
    }
}
