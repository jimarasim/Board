mvn -Dtest=BoardScrub#BuildPageOfFoundLinks -Dbrowser=CHROME -Denvironment=googlenews -Dinput="https://news.google.com/" -Dnogrid -DaNumber=0 -DwaitAfterPageLoadMilliSeconds=5000 test