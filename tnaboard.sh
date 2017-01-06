#!/usr/bin/env bash
#-DaHubPort="4444" -DaHubServer="localhost"
mvn -DnoScreenShots -DaNumber="100" -DdefaultImplicitWaitSeconds=60 -Dtest="BoardScrub#BuildPageOfFoundLinks" -Dreport="top100alerts20160920" -Dinput="https://www.tnaboard.com/forumdisplay.php?102-WA-Alerts!" -DnextLinkXpath="//a[@rel='next']/img[@alt='Next' and @title='Next']" -DlinksLoadedIndicatorXpath="//span[@class='forumtitle']" -DlinkXpath="//a[@class='title']" -DimageXpath="//*[contains(@id,'post_message_')]/blockquote//img" -DtitleTextXpath="//span[@class='threadtitle']/a" -DbodyTextXpath="//div[@class='postrow']" -Dbrowser="CHROME" -Dnogrid -DwaitAfterPageLoadMilliSeconds="0" -Dnogrid test
