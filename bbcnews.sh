mvn -Dtest=BoardScrub#BuildPageOfFoundLinks -Dbrowser=CHROME -Denvironment=bbc -Dinput="http://www.bbc.com/news" -Dnogrid -DaNumber=0 -DwaitAfterPageLoadMilliSeconds=5000 test