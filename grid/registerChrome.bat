java -Dwebdriver.chrome.driver=..\drivers\chromedriver.exe -jar selenium-server-standalone-3.6.0.jar -role node -hub http://127.0.0.1:4444/grid/register -browser browserName=chrome,version=ANY,platform=WINDOWS,maxInstances=10 -port 9021
