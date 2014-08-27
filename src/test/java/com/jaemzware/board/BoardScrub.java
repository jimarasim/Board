package com.jaemzware.board;

import com.jaemzware.seleniumcodebase.AutomationCodeBase;
import java.io.FileInputStream;
import java.io.PrintWriter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebElement;

/**
 *@author jaemzware@hotmail.com
 * 
 */
public class BoardScrub extends AutomationCodeBase
{
    static final String propertiesFile = "src/test/java/com/jaemzware/board/selenium.properties";
    static Properties properties = new Properties();
    
    @Before
    public void BeforeTest()
    {
        try{//start the webdriver
            
            //properties file is in same directory as pom.xml
            properties.load(new FileInputStream(propertiesFile));
            
            //get input parameters HERE
            GetParameters();
            
            StartDriver();
        }
        catch(Exception ex)
        {
            Assert.fail("BEFORE TESTS EXCEPTION:"+ex.getMessage());
        }
    }
    
    @Test
    public void BuildPageOfFoundLinks()
    {
        try{
            
            final String url = properties.getProperty(environment.toString()+".url");
            final String linksLoadedIndicatorXpath = properties.getProperty(environment.toString()+".linksLoadedIndicatorXpath");
            final String linkXpath = properties.getProperty(environment.toString()+".linkXpath");
            final String imageXpath = properties.getProperty(environment.toString()+".imageXpath");
            
            driver.get(url);
            
            //wait for login to complete
            (new WebDriverWait(driver,defaultImplicitWait))
                    .until(new ExpectedCondition<Boolean>(){
                    @Override
                    public Boolean apply(WebDriver d) {
                        return IsElementPresent(By.xpath(linksLoadedIndicatorXpath));
                    }});
            
            //list for links
            List<String> urls = new ArrayList<String>();
            
            //make sure there are some links
            System.out.println("CHECKING FOR LINKS");
            
            if(!IsElementPresent(By.xpath(linkXpath),10000)){
                throw new Exception("COULDN'T FIND ANY LINKS");
            }
            
            //GET THE links
            System.out.println("FINDING LINKS");
            
            List<WebElement> webElements = driver.findElements(By.xpath(linkXpath));
            
            //store off the hrefs
            System.out.println("SAVING LINKS");
            
            for(WebElement we:webElements){
                urls.add(we.getAttribute("href"));
            }
            
            System.out.println("VISITING LINKS");
            List<String[]> images = new ArrayList<String[]>();
            
            //navigate to links and get images
            for(String href:urls)
            {
                System.out.println(href);
                final String waithref = href;
                driver.get(href);
                
                //wait for page to load
                (new WebDriverWait(driver,defaultImplicitWait))
                        .until(new ExpectedCondition<Boolean>(){
                        @Override
                        public Boolean apply(WebDriver d) {
                            return driver.getCurrentUrl().contains(waithref);
                        }});
                
                //check for  images
                if(!IsElementPresent(By.xpath(imageXpath),1000)){
                    images.add(new String[]{href,"noimage.jpg"}); 
                    continue;
                }
                
                //get images
                for(WebElement i:driver.findElements(By.xpath(imageXpath)))
                {
                    images.add(new String[]{href,i.getAttribute("src")});   
                }
                
            }
            
            //build web page
            PrintWriter writer = new PrintWriter("index.htm", "UTF-8");
            writer.println("<html><head><title></title></head><body>");
            
            
            String oldHref = new String();
            for(String[] entry:images)
            {
                if(!oldHref.equals(entry[0]))
                {
                    oldHref = entry[0];
                    writer.println("<a href='"+oldHref+"' target='_blank'>"+oldHref+"</a><br />");
                }
                writer.println("<img src='"+entry[1]+"' /><br />");
            }
            writer.println("</body></html>");
        }
        catch(Exception ex)
        {
            ScreenShot();
            CustomStackTrace("Scratch exception",ex);
            Assert.fail(ex.getMessage());
        }
    }
    
    
    @After
    public void AfterTest()
    {
        try
        {
            QuitDriver();
        }
        catch(Exception ex)
        {
            ScreenShot();
            this.CustomStackTrace("After test exception",ex);
            Assert.fail(ex.getMessage());
        }
    }
    
}
