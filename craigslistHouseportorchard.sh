#!/usr/bin/env bash
# -DaHubPort="4723" -DaHubServer="localhost"
mvn -DcontactButtonXpath="//section[contains(@class,'ReplyBar')]//button" -DcontactInfoAnchorXpaths="//div[contains(@class,'reply_options')]/ul/li/a" -DcontactInfoLiXpaths="//div[contains(@class,'reply_options')]/ul/li" -DnoScroll -Dreport="craigslist" -Dinput="https://seattle.craigslist.org/search/rea?query=98366&hasPic=1&search_distance=20&postal=98366" -DbodyTextXpath="//section[@id='postingbody']" -DnextLinkXpath="//a[@title='next page']" -DlinksLoadedIndicatorXpath="//input[@id='query']" -DlinkXpath="//span[@class='pl']/a" -DimageXpath="//div[contains(@class,'slide first')]/img" -DtitleTextXpath="//h2[@class='postingtitle']" -DstartRESTParm="page" -Dbrowser="CHROME" -Dnogrid -DaNumber="0" -DwaitAfterPageLoadMilliSeconds="0" -Dtest="BoardScrub#BuildPageOfFoundLinks" test



