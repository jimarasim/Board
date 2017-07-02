#!/usr/bin/env bash
#-DaHubPort="4444" -DaHubServer="localhost"
mvn -DnoScreenShots -DnoScroll -DaNumber="200" -DdefaultImplicitWaitSeconds=60 -Dtest="BoardScrub#BuildPageOfFoundLinks" -Dreport="tnaboardbellvue" -Dinput="https://www.tnaboard.com/forumdisplay.php?98-WA-Provider-Posts&s=&pp=75&prefixid=bellevue&field_dvb_service_type=Escort" -DnextLinkXpath="//a[@rel='next']/img[@alt='Next' and @title='Next']" -DlinksLoadedIndicatorXpath="//span[@class='forumtitle']" -DlinkXpath="//a[contains(@class,'title')]" -DimageXpath="//*[contains(@id,'post_message_')]/blockquote//img" -DtitleTextXpath="//span[@class='threadtitle']/a" -DbodyTextXpath="//div[@class='postrow']" -Dbrowser="CHROME" -Dnogrid -DwaitAfterPageLoadMilliSeconds="0" -Dnogrid test
