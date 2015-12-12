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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.jaemzware.seleniumcodebase.CodeBase;
import static com.jaemzware.seleniumcodebase.ParameterType.*;
import java.security.InvalidParameterException;

/**
 * @author jaemzware.org
 */
public class BoardScrub extends CodeBase {
    @Before
    public void BeforeTest() {
        try {// start the webdriver

            // properties file is in same directory as pom.xml
//            properties.load(new FileInputStream(propertiesFile));

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
//        catch (FileNotFoundException fnfex){
//            Assert.fail(propertiesFile+" NOT FOUND");
//        }
//        catch(IOException ioex){
//            Assert.fail(propertiesFile+" IO EXCEPTION");
//        }
        catch(Exception ex){
            Assert.fail(ex.getMessage());
        }
    }
    
    /**
     * This method visits each url, and puts its content into a results list using webdriver
     * @return
     * @throws Exception 
     */
    @Test
    public void BuildPageOfFoundLinks() {
        try {
            //get properties file information
            GetBuildPageOfFoundLinksRequiredProperties(); 
            
            //paging variables
            String currentContentPageUrl = input;  //used to navigate to next page
            int numCurrentPageFirstResult=1; //used to track max visits
            Boolean continueProcessing = true; //used to track if we should keep paging
            List<String[]> contents = new ArrayList(); //contents collected from each page
            
            
            //go to the first page
            driverGetWithTime(input);
            Thread.sleep(waitAfterPageLoadMilliSeconds);
            
            //PAGE THROUGH ALL RESULTS
            while(continueProcessing){
                //get all the links on the target url
                List<String> links = GetLinksOnPage(); 

                //get conent from the links
                List<String[]> contentsOnCurrentPage
                        =GetContentFromLinks(links);
                
                contents.addAll(contentsOnCurrentPage); 
                
                //PAGING LOGIC
                //go back to content page with results just collected
                driverGetWithTime(currentContentPageUrl);
                
                //SET FIRST RESULT ON THE NEXT PAGE OF RESULTS
                numCurrentPageFirstResult += contentsOnCurrentPage.size();
                
                //STOP IF MAXVISITS REACHED
                System.out.println("if("+aNumber+">0 && "+numCurrentPageFirstResult+" >= "+aNumber+"){");
                System.out.println("else if(!IsElementPresent(By.xpath("+nextLinkXpath+"),"+waitAfterPageLoadMilliSeconds+")||");
                System.out.println("else");
                if(aNumber>0 && numCurrentPageFirstResult >= aNumber){
                    System.out.println("MAX VISITS REACHED numCurrentPageFirstResult:"+numCurrentPageFirstResult+" numResultsOnPage:"+contentsOnCurrentPage.size()+" maxVisits:"+aNumber);
                    continueProcessing=false;
                }
                //STOP IF THERE IS NO NEXT LINK
                else if(!IsElementPresent(By.xpath(nextLinkXpath),waitAfterPageLoadMilliSeconds)||
                        !driver.findElement(By.xpath(nextLinkXpath)).isEnabled()||
                        !driver.findElement(By.xpath(nextLinkXpath)).isDisplayed()){
                    System.out.println("LAST PAGE REACHED: "+driver.getCurrentUrl());
                    continueProcessing=false;
                }
                //OTHERWISE CONTINUE PROCESSING
                else{

                    //GET NEXT page link
                    WebElement nextPageLink = driver.findElement(By.xpath(nextLinkXpath));
                    
                    //GOING TO NEXT PAGE MESSAGE
                    System.out.println("GOING TO NEXT PAGE:"+nextPageLink.getAttribute("href"));
                    
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
    
    /**
     * This method visits each url, and puts its content into a results list, using rest calls
     * @return
     * @throws Exception 
     */
    @Test
    public void BuildPageOfFoundLinksViaRest() {
        try {
            //get properties file information
            GetBuildPageOfFoundLinksRequiredProperties(); 
            
            //go to the first page
            driverGetWithTime(input);
            
            //get all the links on the target url
            List<String> links = 
                    GetLinksOnPage(); 
            
            //get conent from the links
            List<String[]> contents = 
                    GetContentFromLinksViaRest(links); 

            //generate a page of the contents
            WriteContentsToWebPage(contents);

        } catch (Exception ex) {
            ScreenShot();
            System.out.println("BuildPageOfFoundLinksViaRest EXCEPTION MESSAGE:"+ex.getMessage());
            CustomStackTrace("BuildPageOfFoundLinksViaRest EXCEPTION TRACE", ex);
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
            GetBuildPageOfFoundLinksRequiredProperties();

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
     * This method gets links to visit from the target page
     * @return 
     */
    private List<String> GetLinksOnPage() throws Exception{
        // list for links
        List<String> urls = new ArrayList<>();
        
        // wait for links to be loaded
        (new WebDriverWait(driver, defaultImplicitWaitSeconds)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                System.out.println("IsElementPresent(By.xpath(linksLoadedIndicatorXpath))");
                System.out.println("IsElementPresent(By.xpath("+linksLoadedIndicatorXpath+"))");
                return IsElementPresent(By.xpath(linksLoadedIndicatorXpath));
            }
        });

        // make sure there are some links
        System.out.println("CHECKING FOR RESULTS");

        if (!IsElementPresent(By.xpath(linkXpath))) {
            throw new Exception("COULDNT FIND ANY RESULTS ON: "+input+" WITH XPATH:"+linkXpath);
        }

// GET THE links
        System.out.println("FINDING RESULTS");

        List<WebElement> webElements = driver.findElements(By.xpath(linkXpath));

        // store off the hrefs
        System.out.println("SAVING RESULT LINKS. COUNT:"+webElements.size());

        for (WebElement we : webElements) {
            urls.add(we.getAttribute("href"));
        }
        
        return urls;
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
        for (String href : links) {
            try{
                driverGetHtmlOutput = driverGetWithTime(href);
                Thread.sleep(waitAfterPageLoadMilliSeconds);
                
                //scroll page
                ScrollPage();
            }
            catch(Exception ex){
                System.out.println("WARNING: PAGE TOOK LONG TO LOAD:"+href+", ... MOVING ON");
                continue;
            }

            //TITLE TEXT
            // check for the title text
            String titleText="";
            
            //THROTTLE DOWN IMPLICIT WAIT //implictlywait cant' work with appium
            if(!browser.toString().contains("APPIUM")){
                // throttle wait time when looking for elements that should already be on the page
                driver.manage().timeouts().implicitlyWait(waitTimeMillis, TimeUnit.MILLISECONDS);
                
                System.out.println("DECREASED IMPLICIT WAIT FROM defaultImplicitWaitSeconds:"+defaultImplicitWaitSeconds+"ms TO waitTimeMillis:"+waitTimeMillis+" ms");
            }

            System.out.println("LOOKING FOR TITLE TEXT AT (GetContentFromLinks):"+titleTextXpath+" TIMEOUT:"+waitTimeMillis+"ms");
            
            if (IsElementPresent(By.xpath(titleTextXpath), waitTimeMillis)) {
                try{            
                        System.out.println("LOOKING FOR TITLE TEXT AT (GetContentFromLinks):"+titleTextXpath+" TIMEOUT:"+waitTimeMillis+"ms");

                        titleText = driver.findElement(By.xpath(titleTextXpath)).getText();

                }
                catch(Exception ex){
                    System.out.println("WARNING: STALE ELEMENT REFERENCE ON titleTextXpath:"+titleTextXpath+" WHILE GETTING IMAGES, TITLE, BODY FROM:"+href);
                    
                    //THROTTLE UP IMPLICIT WAIT //implictlywait cant' work with appium
                    if(!browser.toString().contains("APPIUM")){
                        // throttle implicit wait time back up
                        driver.manage().timeouts().implicitlyWait(defaultImplicitWaitSeconds, TimeUnit.SECONDS);
                        
                        System.out.println("INCREASED IMPLICIT WAIT FROM waitTimeMillis"+waitTimeMillis+"ms TO defaultImplicitWaitSeconds:"+defaultImplicitWaitSeconds+" ms");
                        
                    }
                    
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
            
            
            if(!browser.toString().contains("APPIUM")){
                //THROTTLE UP IMPLICIT WAIT //implictlywait cant' work with appium
                driver.manage().timeouts().implicitlyWait(defaultImplicitWaitSeconds, TimeUnit.SECONDS);
                
                System.out.println("INCREASED IMPLICIT WAIT FROM waitTimeMillis"+waitTimeMillis+"ms TO defaultImplicitWaitSeconds:"+defaultImplicitWaitSeconds+" ms");
                
            }

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
                System.out.println("IsElementPresent(By.xpath("+imageXpath+"), "+quickWaitMilliSeconds+")");
                if (IsElementPresent(By.xpath(imageXpath), quickWaitMilliSeconds)) {
                    // add images to images list
                    List<WebElement> imageElements = driver.findElements(By.xpath(imageXpath));
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
            if((aNumber>0) && (++visitCount>aNumber)){
                break;
            }

        }
        
       return results;
    }
     
    /**
     * This method visits each url, locally after getting it from a rest request, and puts its content into a results list
     * @return
     * @throws Exception 
     */
    private List<String[]> GetContentFromLinksViaRest(List<String> links) throws Exception{
    

        System.out.println("VISITING RESULT LINKS");
        List<String[]> results = new ArrayList<>();

        // navigate to links and get images

        int visitCount = 0;
        String rawHtml="";
        String rawHtmlLocalFile="";
        for (String href : links) {
            try{
                //do an http get of the page
                rawHtml = HttpGetReturnResponse(href);
                
                //write it to a file
                rawHtmlLocalFile = aString + WriteHtmlContentToFile(rawHtml);
                
                //load the page locally
                driverGetWithTime(rawHtmlLocalFile);
            }
            catch(Exception ex){
                System.out.println("WARNING: EXCEPTION GETTING:"+rawHtmlLocalFile+", ... MOVING ON. EXCEPTION:"+ex.getMessage());
                this.CustomStackTrace(userid, ex);
                continue;
            }

            // check for the title text
            String titleText="";

            System.out.println("LOOKING FOR TITLE TEXT AT (GetContentFromLinksViaRest):"+titleTextXpath+" TIMEOUT:"+waitAfterPageLoadMilliSeconds+"ms");
            if (IsElementPresent(By.xpath(titleTextXpath), waitAfterPageLoadMilliSeconds)) {
                try{
                    titleText = driver.findElement(By.xpath(titleTextXpath)).getText();
                }
                catch(Exception ex){
                    System.out.println("WARNING: STALE ELEMENT REFERENCE WHILE GETTING IMAGES, TITLE, BODY FROM:"+href);
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
                System.out.println("WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" WAS NOT FOUND AFTER:"+quickWaitMilliSeconds+"ms");
                ScreenShot();
            }


            // check for the body text
            List<WebElement> allBodyTexts = new ArrayList<>();

            System.out.println("IsElementPresent(By.xpath("+bodyTextXpath+"), "+quickWaitMilliSeconds+")");
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

            // check for images
            String imageSrc = "";
            if(noImages==null){
                System.out.println("IsElementPresent(By.xpath("+imageXpath+"), "+quickWaitMilliSeconds+")");
                if (IsElementPresent(By.xpath(imageXpath), quickWaitMilliSeconds)) {
                    // add images to images list
                    List<WebElement> imageElements = driver.findElements(By.xpath(imageXpath));
                    for (WebElement i : imageElements) {
                        try {
                            imageSrc=i.getAttribute("src");

                            //add result entry image
                            results.add(new String[] { href, imageSrc, titleText, LessThan1000CharString(bodyText.toString())});
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
                results.add(new String[] { href, imageSrc, titleText, LessThan1000CharString(bodyText.toString()) });
            }                   
            //check the desired image count, and break if it's been reached
            if((aNumber>0) && (++visitCount>aNumber)){
                break;
            }

        }
        
       return results;
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
       
    /**
     * this method gets required properties from the properties file for the BuildPageOfFoundLinks test
     */
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
            //num parameter name to use in url, for number of results to return (used for paging)
            if (numResultsParm == null||numResultsParm.isEmpty() ){                
                throw new Exception("MISSING: -DnumResultsParm");
            } 
            //start parameter name to use in url, for nth result to get results from (used for paging)
            if (startParm == null||startParm.isEmpty()){                
                throw new Exception("MISSING: -DstartParm");
            } 
            if (aString == null || aString.isEmpty()) {
                throw new Exception("TARGET URL NOT SPECIFIED -DaString)");
            }
            if (input == null || input.isEmpty()) {
                throw new Exception("URL NOT SPECIFIED -Dinput");
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
                    " numResultsParm:"+
                    numResultsParm+ 
                    " startParm:"+
                    startParm +
                    " aString:"+
                    aString+ 
                    " input:"+
                    input);
    }
   
    /**
     * Write results from a BoardScrub to a web page
     * @param results
     * @throws Exception 
     */
    private void WriteContentsToWebPage(List<String[]> results) throws Exception
    {
        // build web page
        if(report==null){
            report = "NONAMEREPORT";
        }
        
        String fileName = "index"+getDateStamp()+"-" + report + ".htm";
        try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
            writer.println(HtmlReportHeader("BoardScrub:<a href='"+input+"' target='_blank'>"+input+"</a>"));
            
            String oldHref = new String();
            for (String[] entry : results) {
                if (!oldHref.equals(entry[0])) {
                    oldHref = entry[0];
                    writer.println("<h2><a href='" + oldHref + "' target='_blank'>"+oldHref+"</a></h2>");
                    writer.println("<span>" + entry[2] + "</span><br />");
                    writer.println("<span>" + entry[3] + "</span><br />");
                    writer.println(entry[4]);

                }
                writer.println("<a href='"+oldHref+"' target='_blank'><img src='" + entry[1] + "' /></a><br />");

            }
            writer.println(HtmlReportFooter());
            
            writer.flush();
            
            System.out.println("INDEX FILE WRITTEN:" + fileName);
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
    
    private List<Integer> GetResultPlacesOfTarget() throws Exception{
        List<Integer>resultPlacesOfTarget = new ArrayList<>(); 
        int numResultsOnPage = 100;
        int numCurrentPageFirstResult=1;  //used to track
        
        System.out.println("GETRESULTPLACESOFTARGET");
        
        //ADD NUM AND START PARMS TO SEARCH STRING (GOOGLE SPECIFIC)
        String urlWithParms = input + 
                "&"+
                numResultsParm+
                "="+
                Integer.toString(numResultsOnPage);

        // NAVIGATE TO URL

        String oldUrl=driver.getCurrentUrl();
        driverGetWithTime(urlWithParms);
        WaitForPageChange(oldUrl);

        Boolean continueProcessing = true;

//PAGE THROUGH ALL RESULTS
        while(continueProcessing){

            // make sure there are some links
            System.out.println("CHECKING FOR RESULTS");
            System.out.println("!IsElementPresent(By.xpath("+linkXpath+"), "+quickWaitMilliSeconds+")");
            if (!IsElementPresent(By.xpath(linkXpath), quickWaitMilliSeconds)) {
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

//CHECK IF WE WANT TO KEEP GOING (DEPENDING ON HOW MANY PAGES TO VISIT)

            //SET FIRST RESULT ON THE NEXT PAGE OF RESULTS
            numCurrentPageFirstResult += numResultsOnPage;

            System.out.println("if("+aNumber+">0 && "+numCurrentPageFirstResult+" >= "+aNumber+")");
            System.out.println("else if(!IsElementPresent(By.xpath("+nextLinkXpath+"),"+quickWaitMilliSeconds+")){");
            System.out.println("else");
            if(aNumber>0 && numCurrentPageFirstResult >= aNumber){
                System.out.println("MAX VISITS REACHED numCurrentPageFirstResult:"+numCurrentPageFirstResult+" numResultsOnPage:"+numResultsOnPage+" maxVisits:"+aNumber);

                //tell the loop to stop
                continueProcessing=false;
            }
            else if(!IsElementPresent(By.xpath(nextLinkXpath),quickWaitMilliSeconds)){
                System.out.println("LAST PAGE REACHED: "+driver.getCurrentUrl());

                //there is no next link, we're done, tell the while loop to stop
                continueProcessing=false;
            }
            else{

                //CONSTRUCT THE NEW URL
                urlWithParms = input + 
                "&"+
                numResultsParm+
                "="+
                Integer.toString(numResultsOnPage)+
                "&"+
                startParm+
                "="+
                Integer.toString(numCurrentPageFirstResult);

                //GO TO THE NEXT PAGE
                //get results text string, so we can tell when its changed in WaitForGoogleResultsPageChange
                oldUrl=driver.getCurrentUrl();

                // NAVIGATE TO URL
                driverGetWithTime(urlWithParms);

                //WAIT FOR NEW RESULTS PAGE TO LOAD
                WaitForPageChange(oldUrl);

            }
        }
        
        return resultPlacesOfTarget;
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
