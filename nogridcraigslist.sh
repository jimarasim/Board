#!/usr/bin/env bash
#[SOURCE OF TRUTH FOR BoardScrub#BuildPageOfFoundLinks NO GRID craigslist]
#SEATTLE BOOKS
#http://seattle.craigslist.org/search/foa?query=book
#SOFTWARE JOBS
mvn -DdefaultImplicitWaitSeconds=5 -Dbrowser="FIREFOX" -Dinput="http://seattle.craigslist.org/search/sof" -Dreport="gridcraigslisthousesbremerton" -DnoScreenShots -DnoScroll -DlinksLoadedIndicatorXpath="//a[contains(text(),'help')]" -DlinkXpath="//a[contains(@class,'result-title')]" -DnextLinkXpath="//a[contains(@class,'button next')]" -DbodyTextXpath="//section[@id='postingbody']" -DimageXpath="//div[contains(@class,'slide first')]/img" -DtitleTextXpath="//h2[@class='postingtitle']" -Dnogrid -Dtest="BoardScrub#BuildPageOfFoundLinks" test
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
#NOTE: FIREFOX NO LONGER SUPPORTED FOR LOGGING
#NOTE: FIREFOX SCREENSHOTS SHOW THE WHOLE PAGE
#NOTE: SAFARI NO LONGER SUPPORTED FOR LOGGING
#NOTE: SAFARI MUST NOT ALREADY BE RUNNING WHEN RUNNING A SAFARI AUTOMATION
#NOTE: SAFARI MUST enable the 'Allow Remote Automation' option in Safari's Develop menu to control Safari via WebDriver
#NOTE: SAFARI IS FAST BUT DOESN'T WORK CONSISTENTLY LIKE CHROME, AND FIREFOX
#CHROME("chrome","",Platform.WINDOWS),
#CHROMELINUX("chrome","",Platform.LINUX),
#CHROMELINUX32("chrome","",Platform.LINUX),
#CHROMEMAC ("chrome","",Platform.MAC),
#FIREFOX("firefox","",Platform.WINDOWS),
#FIREFOXLINUX("firefox","",Platform.LINUX),
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
