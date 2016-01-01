package com.jaemzware.board;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jaemzware.seleniumcodebase.CodeBase;
import static com.jaemzware.seleniumcodebase.ParameterType.*;
import java.security.InvalidParameterException;

/**
 * @author jaemzware.org
 */
public class BoardScrubREST extends CodeBase {
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

    
    /**
     * This method visits each url, and puts its content into a results list, using rest calls
     * @return 
     */
    @Test
    public void BuildPageOfFoundLinksViaRest() {
        try {
            //get properties file information
            GetBuildPageOfFoundLinksRequiredPropertiesREST(); 
            
            //go to the first page
            driverGetWithTime(input,1);
            
            //get all the links on the target url
            List<String> links = 
                   GetLinksOnPage(); 
            
            //get conent from the links
            List<String> contents = 
                    GetContentFromLinksViaRest(links); 

            //generate a page of the contents
            WriteMultipleWebPagesToOneWebPage(contents);

        } catch (Exception ex) {
            ScreenShot();
            System.out.println("BuildPageOfFoundLinksViaRest EXCEPTION MESSAGE:"+ex.getMessage());
            CustomStackTrace("BuildPageOfFoundLinksViaRest EXCEPTION TRACE", ex);
            Assert.fail(ex.getMessage());
        }
    }
    private void GetBuildPageOfFoundLinksRequiredPropertiesREST() throws Exception{           
            // CHECK FOR REQUIRED PARAMETERS
            
            if (aString == null || aString.isEmpty()) {
                throw new Exception("TARGET URL NOT SPECIFIED -DaString)");
            }
            if (input == null || input.isEmpty()) {
                throw new Exception("URL NOT SPECIFIED -Dinput");
            }
            if (report == null || report.isEmpty()) {
                throw new Exception("URL NOT SPECIFIED -Dreport");
            }
            if (linksLoadedIndicatorXpath == null || linksLoadedIndicatorXpath.isEmpty()) {
                throw new Exception("URL NOT SPECIFIED -DlinksLoadedIndicatorXpath");
            }
            if (aNumber == -1 ) {
                aNumber=0;
                throw new Exception("URL NOT SPECIFIED -aNumber");
            }
            if (linkXpath == null || linkXpath.isEmpty()) {
                throw new Exception("URL NOT SPECIFIED -DlinkXpath");
            }
            
            
            System.out.println("aString:"+
                    aString+ 
                    " input:"+
                    input+ 
                    " report:"+
                    report+ 
                    " aNumber:"+
                    aNumber+ 
                    " linksLoadedIndicatorXpath:"+
                    linksLoadedIndicatorXpath+ 
                    " linkXpath:"+
                    linkXpath);
    }
    /**
     * This method visits each url, locally after getting it from a rest request, and puts its content into a results list
     * @return
     * @throws Exception 
     */
    private List<String> GetContentFromLinksViaRest(List<String> links) throws Exception{
    
        List<String> results = new ArrayList<>();

        int visitCount = 0;
        
        for (String href : links) {
            try{
                //do an http get of the page
                String rawHtml = HttpGetReturnResponse(href);
                
                //return in results
                results.add(rawHtml);
                
            }
            catch(Exception ex){
                System.out.println("WARNING: EXCEPTION GETTING:"+href+", ... MOVING ON. EXCEPTION:"+ex.getMessage());
                this.CustomStackTrace("CUSTOM STACK TRACE", ex);
                continue;
            }
                    
            //check the desired image count, and break if it's been reached
            if((aNumber>0) && (visitCount++>aNumber)){
                break;
            }

        }
        
       return results;
    }
   
    /**
     * Write results from a BoardScrub to a web page
     * @param results
     * @throws Exception 
     */
    public static void WriteMultipleWebPagesToOneWebPage(List<String> results) throws Exception
    {
        // build web page
        if(report==null){
            report = "NONAMEREPORT";
        }
        
        String fileName = "index"+getDateStamp()+"-" + report + ".htm";
        try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
            
            HtmlReportHeader(report);
            for (String entry : results) {
                writer.println(entry);
            }
            
            writer.flush();
            
            System.out.println("open " + fileName);
        }
        catch(Exception ex){
            throw new Exception("COULD NOT USE PRINTWRITER TO STORE COLLECTED PAGE CONTENT");
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
