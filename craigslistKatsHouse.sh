#!/usr/bin/env bash
mvn -DcontactButtonXpath="//section[contains(@class,'ReplyBar')]//button" -DcontactInfoAnchorXpaths="//div[contains(@class,'reply_options')]/ul/li/a" -DcontactInfoLiXpaths="//div[contains(@class,'reply_options')]/ul/li" -DnoScroll -Dreport="craigslistKatsHouse" -Dinput="http://seattle.craigslist.org/search/apa?sort=pricedsc&hasPic=1&max_price=2000&bedrooms=3&bathrooms=2&pets_cat=1&housing_type=6" -DbodyTextXpath="//section[@id='postingbody']" -DnextLinkXpath="//a[@title='next page']" -DlinksLoadedIndicatorXpath="//input[@id='query']" -DlinkXpath="//span[@class='pl']/a" -DimageXpath="//div[contains(@class,'slide first')]/img" -DtitleTextXpath="//h2[@class='postingtitle']" -DstartRESTParm="page" -Dbrowser="CHROME" -DaHubPort="4723" -DaHubServer="localhost" -Dnogrid -DaNumber="0" -DwaitAfterPageLoadMilliSeconds="0" -Dtest="BoardScrub#BuildPageOfFoundLinks" test



