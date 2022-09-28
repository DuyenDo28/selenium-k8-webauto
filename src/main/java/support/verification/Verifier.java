package support.verification;

public class Verifier {

    public static void verifyEquals(String actualResult, String expectedResult){
        if(!actualResult.equals(expectedResult)){
            System.out.printf("Actual is: %s\n Expected is: %s\n", actualResult, expectedResult);
            throw new AssertionError("Actual results and expected result is different");
        }
    }
}
