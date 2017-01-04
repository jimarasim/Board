package com.jaemzware.board;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.jaemzware.seleniumcodebase.CodeBase;
import static com.jaemzware.seleniumcodebase.ParameterType.*;
import java.security.InvalidParameterException;

/**
 * @author jaemzware.org
 */
public class BoardScrub extends CodeBase {
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
     * This method visits each url, and puts its content into a results list using webdriver 
     */
    @Test
    public void BuildPageOfFoundLinks() {
        try {
            //get properties file information
            GetBuildPageOfFoundLinksRequiredProperties(); 
            
            //paging variables
            String currentContentPageUrl = input;  //used to navigate to next page
            int resultCountNumCurrentPageFirstResult=1; //used to track max visits
            Boolean continueProcessing = true; //used to track if we should keep paging
            List<String[]> contents = new ArrayList(); //contents collected from each page

            //go to the first page
            String driverGetWithTimeErrorCheck=driverGetWithTime(input,1);
            if(driverGetWithTimeErrorCheck.equals("ERROR")){
                throw new Exception("GETCONTENTFROM LINKS DRIVERGETWITHTIME ERROR OCCURRED SEE ABOVE FOR EXCEPTION MESSAGE");
            }

            //PAGE THROUGH ALL RESULTS
            String checkHtmlResponseForError=""; //FOR CHECKING FOR ERRORS FROM driverGetWithTime AS WE PROCESS PAGES
            boolean maximumResultsSpecified = (aNumber>0)?true:false;
            int maximumResultsToReturn = aNumber;
            //debugging variables for paging issue concerning -DnextLinkXpath, in while loop, to avoid infinite loops and unexpected paging errors
            WebElement weNextLinkXpathElement = null;
            boolean nextLinkXpathExists = false;
            boolean nextLinkXpathEnabled = false;
            boolean nextLinkXpathDisplayed = false;
            while(continueProcessing){
                //get all the links on the target url
                List<String> links = GetLinksOnPage();

                //get conent from the links
                List<String[]> contentsOnCurrentPage
                        =GetContentFromLinks(links);
                
                contents.addAll(contentsOnCurrentPage); 
                
                //PAGING LOGIC
                //go back to content page with results just collected
                checkHtmlResponseForError = driverGetWithTime(currentContentPageUrl,1);
                if(checkHtmlResponseForError.equals("ERROR")){
                    System.out.println("THERE WAS AN ERROR GETTING THE LAST PAGE. SEE ABOVE FOR EXCEPTION MESSAGE.");
                    continueProcessing=false;
                }

                //DISPLAY INFORMATIONAL DATA FOR PAGING DEBUGGING
                //variables for paging; get the next link xpath element, and SET varibles for displayed and enabled to CHECK
                //whether or not to stop trying to continue processing
                nextLinkXpathExists = IsElementPresent(By.xpath(nextLinkXpath));
                weNextLinkXpathElement = nextLinkXpathExists?driver.findElement(By.xpath(nextLinkXpath)):null;
                nextLinkXpathDisplayed = (weNextLinkXpathElement!=null)?weNextLinkXpathElement.isDisplayed():false;
                nextLinkXpathEnabled = (weNextLinkXpathElement!=null)?weNextLinkXpathElement.isEnabled():false;

                System.out.println("=============================INFORMATIONAL: URL:"+currentContentPageUrl+" NEXTLINKXPATH:"+nextLinkXpath+" EXISTS: "+nextLinkXpathExists+" DISPLAYED:"+nextLinkXpathDisplayed+" ENABLED:"+nextLinkXpathEnabled+"=============================");

                //increment the number of results by the size of them on the current page, and see if that's more
                //than the number of results maximum to return, as specified by -DaNumber on the command line
                resultCountNumCurrentPageFirstResult += contentsOnCurrentPage.size();
                if(     maximumResultsSpecified &&
                        (resultCountNumCurrentPageFirstResult >= maximumResultsToReturn)  ){
                    System.out.println("=============================INFORMATIONAL: MAX VISITS REACHED resultCountNumCurrentPageFirstResult:"+resultCountNumCurrentPageFirstResult+" contentsOnCurrentPage.size():"+contentsOnCurrentPage.size()+" maxVisits:"+aNumber);
                    continueProcessing=false;
                }
                //STOP IF THERE IS NO NEXT LINK
                else if(!nextLinkXpathExists){
                    System.out.println("=============================INFORMATIONAL: NEXT LINK XPATH:"+nextLinkXpath+" NOT PRESENT. URL:"+driver.getCurrentUrl());
                    continueProcessing=false;
                }
                //STOP IF THE NEXT LINK IS PRESENT BUT NOT DISPLAYED
                else if(!nextLinkXpathDisplayed) {
                    System.out.println("=============================INFORMATIONAL: NEXT LINK PRESENT BUT NOT DISPLAYED. URL:" + driver.getCurrentUrl());
                    continueProcessing = false;
                }
                //STOP IF THE NEXT LINK IS PRESENT BUT NOT ENABLED
                else if(!nextLinkXpathEnabled) {
                    System.out.println("=============================INFORMATIONAL: NEXT LINK PRESENT BUT NOT ENABLED. URL:" + driver.getCurrentUrl());
                    continueProcessing = false;
                }
                //OTHERWISE CONTINUE PROCESSING pages by clicking the link pointed to by -DnextLinkXpath
                else{
                    //GET NEXT page link
                    WebElement nextPageLink = driver.findElement(By.xpath(nextLinkXpath));

                    //GOING TO NEXT PAGE MESSAGE
                    System.out.println("INFORMATIONAL GOING TO NEXT PAGE:"+nextPageLink.getAttribute("href"));

                    //GO TO THE NEXT PAGE
                    nextPageLink.click();

                    //WAIT FOR NEW RESULTS PAGE TO LOAD
                    WaitForPageChange(currentContentPageUrl);

                    //update the current page url
                    currentContentPageUrl = driver.getCurrentUrl();

                }
            }
            
            //generate a page of the contents
            WriteContentsToWebPage(contents);

        } catch (Exception ex) {
            ScreenShot();
            System.out.println("BuildPageOfFoundLinks EXCEPTION MESSAGE:"+ex.getMessage());
            CustomStackTrace("BuildPageOfFoundLinks EXCEPTION TRACE", ex);
            Assert.fail("BuildPageOfFoundLinks EXCEPTION MESSAGE:"+ex.getMessage());
        }
    }
    private void GetBuildPageOfFoundLinksRequiredProperties() throws Exception{           
        // CHECK FOR REQUIRED PARAMETERS
        if (linksLoadedIndicatorXpath==null || linksLoadedIndicatorXpath.isEmpty()) {
            throw new Exception("MISSING: -DlinksLoadedIndicatorXpath");
        }
        if (linkXpath == null || linkXpath.isEmpty()) {
            throw new Exception("MISSING: -DlinkXpath");
        }
        if (imageXpath == null||imageXpath.isEmpty()) {
            throw new Exception("MISSING: -DimageXpath");
        }
        if (titleTextXpath == null || titleTextXpath.isEmpty()) {
            throw new Exception("MISSING: -DtitleTextXpath");
        }
        if (bodyTextXpath == null||bodyTextXpath.isEmpty()) {
            throw new Exception("MISSING: -DbodyTextXpath");
        }
        if (nextLinkXpath == null||nextLinkXpath.isEmpty()){                
            throw new Exception("MISSING: -DnextLinkXpath");
        }
        if (input == null || input.isEmpty()) {
            throw new Exception("URL NOT SPECIFIED -Dinput");
        }
        if (report == null || report.isEmpty()) {
            throw new Exception("URL NOT SPECIFIED -Dreport");
        }
        System.out.println("linksLoadedIndicatorXpath:"+
                linksLoadedIndicatorXpath+
                " linkXpath:"+
                linkXpath+
                " imageXpath:"+
                imageXpath+
                " titleTextXpath:"+
                titleTextXpath+
                " bodyTextXpath:"+
                bodyTextXpath+
                " nextLinkXpath:"+
                nextLinkXpath+ 
                " input:"+
                input+ 
                " report:"+
                report);
    }
        /**
     * This method visits the links and gets the content from each of them, for the html report
     * @param links
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("SleepWhileInLoop")
    private List<String[]> GetContentFromLinks(List<String> links) throws Exception{
    
        int waitTimeMillis=5000;
        
        System.out.println("VISITING RESULT LINKS");
        List<String[]> results = new ArrayList<>();

        // navigate to links and get images
        String driverGetHtmlOutput = "";
        int visitCount = 0;
        String titleText=null;
        for (String href : links) {
            try{
                driverGetHtmlOutput = driverGetWithTime(href);
                if(driverGetHtmlOutput.equals("ERROR")){
                    throw new Exception("GETCONTENTFROM LINKS DRIVERGETWITHTIME ERROR OCCURRED. LOOK ABOVE FOR EXCEPTION MESSAGE.");
                }

                System.out.println("INFORMATIONAL: GETCONTENTFROMLINKS VISITCOUNT:"+visitCount);
                Thread.sleep(waitAfterPageLoadMilliSeconds);
                
                //scroll page
                ScrollPage();
            }
            catch(Exception ex){
                System.out.println("WARNING: PAGE TOOK LONG TO LOAD:"+href+", ... MOVING ON");
                continue;
            }

            //THROTTLE DOWN IMPLICIT WAIT
            // throttle wait time when looking for elements that should already be on the page
            driver.manage().timeouts().implicitlyWait(waitTimeMillis, TimeUnit.MILLISECONDS);
            System.out.println("DECREASED IMPLICIT WAIT FROM defaultImplicitWaitSeconds:"+defaultImplicitWaitSeconds+"ms TO waitTimeMillis:"+waitTimeMillis+" ms");
            System.out.println("LOOKING FOR TITLE TEXT AT (GetContentFromLinks):"+titleTextXpath+" TIMEOUT:"+waitTimeMillis+"ms");
            //check for TITLE TEXT
            if (IsElementPresent(By.xpath(titleTextXpath), waitTimeMillis)) {
                try{            
                    System.out.println("LOOKING FOR TITLE TEXT:"+titleTextXpath+" TIMEOUT:"+waitTimeMillis+"ms");
                    titleText = driver.findElement(By.xpath(titleTextXpath)).getText();
                }
                catch(Exception ex){
                    System.out.println("WARNING: STALE ELEMENT REFERENCE ON titleTextXpath:"+titleTextXpath+" WHILE GETTING IMAGES, TITLE, BODY FROM:"+href);
                    
                    //THROTTLE UP IMPLICIT WAIT
                    // throttle implicit wait time back up
                    driver.manage().timeouts().implicitlyWait(defaultImplicitWaitSeconds, TimeUnit.SECONDS);
                    System.out.println("INCREASED IMPLICIT WAIT FROM waitTimeMillis"+waitTimeMillis+"ms TO defaultImplicitWaitSeconds:"+defaultImplicitWaitSeconds+" ms");
                    break;
                }
                if (titleText == null) {
                    System.out.println("WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" GETTEXT IS NULL");
                }
                else if (titleText.isEmpty()){
                    System.out.println("WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" GETTEXT IS EMPTY");
                }
            }
            else{
                System.out.println("WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" WAS NOT FOUND AFTER:"+waitTimeMillis +"ms");
            }

            //THROTTLE UP IMPLICIT WAIT
            driver.manage().timeouts().implicitlyWait(defaultImplicitWaitSeconds, TimeUnit.SECONDS);
            System.out.println("INCREASED IMPLICIT WAIT FROM waitTimeMillis"+waitTimeMillis+"ms TO defaultImplicitWaitSeconds:"+defaultImplicitWaitSeconds+" ms");

            //BODY TEXT
            // check for the body text
            List<WebElement> allBodyTexts = new ArrayList<>();
            
            System.out.println("IsElementPresent(By.xpath("+bodyTextXpath+"), "+quickWaitMilliSeconds+"))");

            if (IsElementPresent(By.xpath(bodyTextXpath), quickWaitMilliSeconds)) {
                allBodyTexts = driver.findElements(By.xpath(bodyTextXpath));
            }
            else{
                System.out.println("WARNING: BODYTEXT AT XPATH:"+bodyTextXpath+" WAS NOT FOUND AFTER:"+quickWaitMilliSeconds+"ms");
            }
            //add all body text into one string
            StringBuilder bodyText=new StringBuilder();
            String tempString;
            for(WebElement we: allBodyTexts){
                try{
                    tempString = we.getText();
                    bodyText.append(tempString);
                }
                catch(Exception ex){
                    System.out.println("WARNING: allBodyTexts ELEMENTS WENT STALE WHILE TRYING TO GET TEXT FROM THEM");
                    break;
                }
            }
            //IMAGES
            // check for images
            String imageSrc = "";
            if(noImages==null){
                
                System.out.println("CHECKING IMAGES IsElementPresent(By.xpath("+imageXpath+"), "+quickWaitMilliSeconds+")");
                if (IsElementPresent(By.xpath(imageXpath), quickWaitMilliSeconds)) {
                    // add images to images list
                    List<WebElement> imageElements = driver.findElements(By.xpath(imageXpath));
                    System.out.println("IMAGE COUNT:"+imageElements.size());

                    for (WebElement i : imageElements) {
                        try {
                            imageSrc=i.getAttribute("src");

                            //add result entry image
                            results.add(new String[] { href, imageSrc, titleText, LessThan1000CharString(bodyText.toString()),driverGetHtmlOutput });
                        } 
                        catch (Exception ex) {
                            System.out.println("WARNING: IMAGE WENT STALE");
                        }
                    }
                }
                else{
                    System.out.println("WARNING: IMAGE AT XPATH:"+imageXpath+" WAS NOT FOUND AFTER:"+quickWaitMilliSeconds+"ms");
                }
            }
            //add at least one result entry if no images were found
            if(imageSrc!=null && imageSrc.isEmpty()){
                results.add(new String[] { href, imageSrc, titleText, LessThan1000CharString(bodyText.toString()),driverGetHtmlOutput});
            }
            //check the desired image count, and break if it's been reached
            visitCount=visitCount+1;
            if((aNumber>0) && (visitCount>aNumber)){
                break;
            }
            //set titleText back to null for next check
            titleText=null;
        }
       return results;
    }
    /**
     * Write results from a BoardScrub to a web page
     * @param results
     * @throws Exception 
     */
    public void WriteContentsToWebPage(List<String[]> results) throws Exception
    {
        // build web page
        if(report==null){
            report = "NONAMEREPORT";
        }
        
        String fileName = "index" + report + ".htm";
        try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
            writer.println(HtmlReportHeader("From:<a href='"+input+"' target='_blank'>"+input+"</a>"));
            
            String oldHref = new String();
            //href, imageSrc, titleText, LessThan1000CharString(bodyText.toString()),driverGetHtmlOutput
            for (String[] entry : results) {
                if (!oldHref.equals(entry[0])) {
                    oldHref = entry[0];
                    writer.println("<hr />");
                    writer.println("<h2><a href='" + oldHref + "' target='_blank'>"+oldHref+"</a></h2>");
                    writer.println("<span>" + entry[2] + "</span><br />");
                    writer.println("<span>" + entry[3] + "</span><br />");
                    writer.println(entry[4]);
                }
                writer.println("<a href='"+oldHref+"' target='_blank'><img src='" + entry[1] + "' /></a><br />");
            }
            writer.println(HtmlReportFooter());
            
            writer.flush();
            
            System.out.println("open " + fileName);
        }
        catch(Exception ex){
            throw new Exception("COULD NOT USE PRINTWRITER TO STORE COLLECTED PAGE CONTENT");
        }
    }
    /**
     * This method shortens a string to 1000 characters or less.  Created to deal with error of trying to shorten a string to 1000 characters, that was shorter than 1000 characters
     * @param stringToTrim - string to trim to less than 1000 characters, if it has 1000 characters
     * @return 
     */
    private String LessThan1000CharString(String stringToTrim){
        if(stringToTrim.length()<1000){
            return stringToTrim;
        }
        else{
            return stringToTrim.substring(0,999);
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
