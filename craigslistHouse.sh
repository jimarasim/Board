#!/usr/bin/env bash
mvn -DcontactButtonXpath="//section[contains(@class,'ReplyBar')]//button" -DcontactInfoAnchorXpaths="//div[contains(@class,'reply_options')]/ul/li/a" -DcontactInfoLiXpaths="//div[contains(@class,'reply_options')]/ul/li" -DnoScroll -Dreport="craigslist" -Dinput="http://seattle.craigslist.org/search/rea?min_price=25000&max_price=110000&availabilityMode=0&housing_type=2&housing_type=6&housing_type=12" -DbodyTextXpath="//section[@id='postingbody']" -DnextLinkXpath="//a[@title='next page']" -DlinksLoadedIndicatorXpath="//input[@id='query']" -DlinkXpath="//span[@class='pl']/a" -DimageXpath="//div[contains(@class,'slide first')]/img" -DtitleTextXpath="//h2[@class='postingtitle']" -DstartRESTParm="page" -Dbrowser="CHROME" -DaHubPort="4723" -DaHubServer="localhost" -Dnogrid -DaNumber="100" -DwaitAfterPageLoadMilliSeconds="0" -Dtest="BoardScrub#BuildPageOfFoundLinks" test



