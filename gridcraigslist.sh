#!/usr/bin/env bash
#[SOURCE OF TRUTH FOR BoardScrub#BuildPageOfFoundLinks GRID craigslist]
mvn -Dbrowser="FIREFOXMAC" -Dinput="http://seattle.craigslist.org/search/rea?search_distance=20&postal=98110&min_price=1000&max_price=100000&availabilityMode=0" -Dreport="gridcraigslisthousesbainbridgeFIREFOXMAC    NOLOGGING" -DaNumber=0 -DdefaultImplicitWaitSeconds=5 -DnoScreenShots -DnoScroll -DwaitAfterPageLoadMilliSeconds=0 -DbodyTextXpath="//section[@id='postingbody']" -DimageXpath="//div[contains(@class,'slide first')]/img" -DlinksLoadedIndicatorXpath="//a[contains(text(),'help')]" -DlinkXpath="//a[contains(@class,'result-title')]" -DnextLinkXpath="//a[contains(@class,'button next')]" -DtitleTextXpath="//h2[@class='postingtitle']" -DaHubPort="4444" -DaHubServer="localhost" -Dtest="BoardScrub#BuildPageOfFoundLinks" test
#COMMAND LINE SWITCHES FOR BoardScrub#BuildPageOfFoundLinks
#-Dinput MAPS TO STARTURL (eg http://jaemzware.com)
#-Dreport appended to index in report name index___.htm
#-DbodyTextXpath="//section[@id='postingbody']" xpath for the body text of the board post to grab
#-DimageXpath="//div[contains(@class,'slide first')]/img" xpath for images to grab from the board post
#-DlinksLoadedIndicatorXpath="//a[contains(text(),'help')]"
#-DlinkXpath="//a[contains(@class,'result-title')]" xpath for links to follow from the board query
#-DnextLinkXpath="//a[contains(@class,'button next')]" xpath for the link that goes to the next page of board query results
#-DtitleTextXpath="//h2[@class='postingtitle']" xpath for the title of the board post to grab
#COMMAND LINE SWITCHES FOR CODEBASE
#-DaHubPort=4444 - port of selenium grid to use when -Dnogrid is absent
#-DaHubServer=localhost - server of selenium grid to use when -Dnogrid is absent
#-DaNumber - stop after visiting this many pages (default is 0, don't stop)
#-DdefaultImplicitWaitSeconds HOW LONG TO WAIT FOR ELEMENTS BEFORE TIMING OUT (DEFAULT 10 S)
#-Dlogging CHROME AND IE ONLY SHOWS BROWSER AND CLIENT ERRORS IF ANY. NOT SUPPORTED BY FIREFOX NOR SAFARI
#-Dnogrid LAUNCH LOCALLY, AND DON'T USE SELENIUM GRID
#-DnoImages - if DaString is //img, don't display images in the report
#-DnoScreenShots DONT TAKE SCREENSHOTS OF EACH PAGE. DEFAULT ON.
#-DnoScroll DOESN'T SCROLL THE SCREEN FOR VIEWING (DEFAULT ON)
#-DwaitAfterPageLoadMilliSeconds PAUSE EXECUTION AFTER PAGE LOAD AND EACH SCROLL (DEFAULT 0 MS)
#-Dbrowser
#NOTE: VERSION AND PLATFORM ENUMERATION VARS ONLY USED BY GRID
#NOTE: CHROMELINUX32 SPECIAL FOR RASPBERRY PI
#NOTE: FIREFOX IS NO LONGER SUPPORTED RUNNING LOCALLY AS OF WEBDRIVER 3.0
#NOTE: SAFARI MUST NOT ALREADY BE RUNNING WHEN RUNNING A SAFARI AUTOMATION
#NOTE: SAFARI MUST enable the 'Allow Remote Automation' option in Safari's Develop menu to control Safari via WebDriver
#CHROME("chrome","",Platform.WINDOWS),
#CHROMELINUX("chrome","",Platform.LINUX),
#CHROMELINUX32("chrome","",Platform.LINUX),
#CHROMEMAC ("chrome","",Platform.MAC),
#FIREFOXLINUX("firefox","",Platform.LINUX),
#FIREFOXLINUXBPT("firefox","",Platform.LINUX),
#FIREFOXMAC("firefox","",Platform.MAC),
#SAFARI("safari","10",Platform.MAC),
#IE8("InternetExplorer","8",Platform.WINDOWS),
#IE9("InternetExplorer","9",Platform.WINDOWS),
#IE10("InternetExplorer","10",Platform.WINDOWS),
#IE11("InternetExplorer","11",Platform.WINDOWS),
#CHROMEIPHONE6("IPHONE","6",Platform.MAC),
#CHROMEIPAD4("IPAD","4",Platform.MAC),
#CHROMEANDROID402("ANDROID","4.0.2",Platform.WINDOWS),
#APPIUMSAFARISIMULATOR("","",Platform.MAC),
#APPIUMAPPSIMULATOR("","",Platform.MAC),
#APPIUMAPPDEVICE("","",Platform.MAC),
#APPIUMSAFARIDEVICE("","",Platform.MAC);

