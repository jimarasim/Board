package com.jaemzware.board;

import com.jaemzware.seleniumcodebase.CodeBase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.jaemzware.seleniumcodebase.ParameterType.*;
import static com.jaemzware.seleniumcodebase.ParameterType.aString;

/**
 * Created by jameskarasim on 5/22/16.
 */
public class ResultPlace extends CodeBase {
    @Before
    public void BeforeTest() {
        try {

            // initialize verifification errors
            verificationErrors = new StringBuilder();

            // get input parameters HERE
            String getParameterResult = GetParameters();
            if(!getParameterResult.isEmpty()){
                System.out.println(getParameterResult);
                throw new InvalidParameterException();
            }

            //start driver
            StartDriver("../SeleniumCodeBase/SeleniumGrid/");

            //set implicit wait
            driver.manage().timeouts().implicitlyWait(defaultImplicitWaitSeconds, TimeUnit.SECONDS);
        }
        catch (InvalidParameterException ipex) {
            Assert.fail("INVALID PARAMETERS FOUND");
        }
        catch(Exception ex){
            Assert.fail(ex.getMessage());
        }
    }


    /** This test will report where in google result stats your site sits
     *
     */
    @Test
    public void ResultPlace(){

        try{
            //get properties file paths
            GetResultPlaceRequiredProperties();

            //get each place where the target url shows up
            List<Integer>resultPlacesOfTarget = GetResultPlacesOfTarget();

            //report each place where the target url shows up
            ReportPlacesOfTarget(resultPlacesOfTarget);

        }
        catch(Exception ex){
            ScreenShot();
            System.out.println("EXCEPTION MESSAGE:"+ex.getMessage());
            CustomStackTrace("EXCEPTION TRACE", ex);
            Assert.fail(ex.getMessage());
        }
    }
    /**
     * this method gets required properties from the properties file for the BuildPageOfFoundLinks test
     */
    private void GetResultPlaceRequiredProperties() throws Exception{
        // CHECK FOR REQUIRED PARAMETERS

        if (aString == null || aString.isEmpty()) {
            throw new Exception("TARGET URL NOT SPECIFIED -DaString)");
        }
        if (input == null || input.isEmpty()) {
            throw new Exception("URL NOT SPECIFIED -Dinput");
        }
        if (linkXpath == null || linkXpath.isEmpty()) {
            throw new Exception("MISSING: -DlinkXpath");
        }
        if (numResultsRESTParm == null||numResultsRESTParm.isEmpty() ){
            throw new Exception("MISSING: -DnumResultsRESTParm");
        }
        if (startRESTParm == null||startRESTParm.isEmpty() ){
            throw new Exception("MISSING: -DstartRESTParm");
        }
        if (nextLinkXpath==null ||nextLinkXpath.isEmpty()){
            throw new Exception("MISSING: -DnextLinkXpath");
        }

        System.out.println(
                "aNumber:"+aNumber+
                " linkXpath:"+
                linkXpath+
                " numResultsRESTParm:"+
                numResultsRESTParm+
                " startRESTParm:"+
                        startRESTParm+
                " aString:"+
                aString+
                " input:"+
                input+
                "nextLinkXpath:"+nextLinkXpath);
    }
    /**
     * this method gets required properties from the properties file for the BuildPageOfFoundLinks test
     */
    private List<Integer> GetResultPlacesOfTarget() throws Exception{
        List<Integer>resultPlacesOfTarget = new ArrayList<>();
        int numResultsOnPage = 100;
        int numCurrentPageFirstResult=1;  //used to track

        System.out.println("GETRESULTPLACESOFTARGET");

        //ADD NUM AND START PARMS TO SEARCH STRING (GOOGLE SPECIFIC)
        String urlWithParms = input +
                "&"+
                numResultsRESTParm+
                "="+
                Integer.toString(numResultsOnPage);

        // NAVIGATE TO URL

        String oldUrl=driver.getCurrentUrl();
        String driverGetWithTimeResponse = driverGetWithTime(urlWithParms,1);
        if(driverGetWithTimeResponse.equals("ERROR")){
            throw new Exception("GetResultPlacesOfTarget DRIVERGETWITHTIME ERROR OCCURRED. LOOK ABOVE FOR EXCEPTION MESSAGE.");
        }
        WaitForPageChange(oldUrl);

        Boolean continueProcessing = true;

//PAGE THROUGH ALL RESULTS
        while(continueProcessing){

            // make sure there are some links
            System.out.println("CHECKING FOR RESULTS");
            System.out.println("!IsElementPresent(By.xpath("+linkXpath+"), "+throttleDownWaitTimeMilliSeconds+")");
            if (!IsElementPresent(By.xpath(linkXpath), throttleDownWaitTimeMilliSeconds)) {
                throw new Exception("COULDN'T FIND ANY RESULTS");
            }

            // GET THE LINKS
            System.out.println("FINDING RESULTS");

            List<WebElement> webElements = driver.findElements(By.xpath(linkXpath));

            System.out.println("RESULT COUNT:"+webElements.size());

            // store off the hrefs
            System.out.println("REVIEWING LINKS");

//CHECK IF TARGET LINK IS IN THE LINKS
            String linkHref;
            String linkText;
            for(int i=0;i<webElements.size();i++){

                try{
                    linkHref=webElements.get(i).getAttribute("href");
                    linkText=webElements.get(i).getText();
                }
                catch(Exception ex){
                    System.out.println("WARNING: EXCEPTION WHILE GETTING HREFS:"+ex.getMessage());
                    break;
                }

                if(linkHref.contains(aString)){
                    //report the link position with the first result offset
                    resultPlacesOfTarget.add(numCurrentPageFirstResult + i);
                }

                System.out.println((i+numCurrentPageFirstResult)+":\t"+linkHref);
                System.out.println((i+numCurrentPageFirstResult)+":\t"+linkText);
            }

            //SET FIRST RESULT ON THE NEXT PAGE OF RESULTS
            numCurrentPageFirstResult += numResultsOnPage;


            if(aNumber>0 && numCurrentPageFirstResult >= aNumber){
                System.out.println("MAX VISITS REACHED numCurrentPageFirstResult:"+numCurrentPageFirstResult+" numResultsOnPage:"+numResultsOnPage+" maxVisits:"+aNumber);

                //tell the loop to stop
                continueProcessing=false;
            }
            else if(!IsElementPresent(By.xpath(nextLinkXpath),throttleDownWaitTimeMilliSeconds)){
                System.out.println("LAST PAGE REACHED: "+driver.getCurrentUrl());

                //there is no next link, we're done, tell the while loop to stop
                continueProcessing=false;
            }
            else{

                //CONSTRUCT THE NEW URL
                urlWithParms = input +
                        "&"+
                        numResultsRESTParm+
                        "="+
                        Integer.toString(numResultsOnPage)+
                        "&"+
                        startRESTParm+
                        "="+
                        Integer.toString(numCurrentPageFirstResult);

                //GO TO THE NEXT PAGE
                //get results text string, so we can tell when its changed in WaitForGoogleResultsPageChange
                oldUrl=driver.getCurrentUrl();

                // NAVIGATE TO URL
                driverGetWithTimeResponse = driverGetWithTime(urlWithParms,1);
                if(driverGetWithTimeResponse.equals("ERROR")){
                    throw new Exception("GetResultPlacesOfTarget DRIVERGETWITHTIME ERROR OCCURRED. LOOK ABOVE FOR EXCEPTION MESSAGE.");
                }

                //WAIT FOR NEW RESULTS PAGE TO LOAD
                WaitForPageChange(oldUrl);

            }
        }

        return resultPlacesOfTarget;
    }

    //ITERATE THROUGH AND REPORT THE NUMBER POSITION IN THE RESULTS OF THE QUERY WHERE THE URL SHOWED UP
    private void ReportPlacesOfTarget(List<Integer> resultPlacesOfTarget) {
        //REPORT TEST RESULTS
        if(resultPlacesOfTarget.size()<1){
            System.out.println("TARGET:"+aString+" NOT FOUND IN RESULTS");
        }
        else{
            System.out.println("TARGET:"+aString+" FOUND AT PLACE (0-based):");
            for(Integer place:resultPlacesOfTarget){
                System.out.println("PLACE:"+place);
            }
        }
    }

    @After
    public void AfterTest() {
        try {
            QuitDriver();
        } catch (Exception ex) {
            ScreenShot();
            this.CustomStackTrace("After test exception", ex);
            Assert.fail(ex.getMessage());
        }
    }

}


