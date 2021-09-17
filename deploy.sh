cd /usr/local/tomcat
bin/shutdown.sh
cd webapps
rm -rf ROOT/
cp ~/build/test-app.jar ./ROOT.jar
cd ..
bin/startup.sh