mvn -Dtest=BoardScrub#BuildPageOfFoundLinks -Dbrowser=CHROME -Denvironment=bbc -Dinput="http://www.bbc.com/news" -Dnogrid -DaNumber=20 -DwaitAfterPageLoadMilliSeconds=3000 -DlinksLoadedIndicatorXpath="//a" test