@echo off
REM                                                
REM ######################################################
REM # Purpose: SCAP license tool for exporting request file
REM # Author:  yonyou£¬hanpinga@yonyou.com        
REM # Step:  1.export file gzkey.req  
REM #        2.active by this gzkey.req
REM #        3.making file licensegz.lic   
REM #        4.copy file licensegz.lic to %NC_HOME%\bin       
REM ######################################################


if "%OS%"=="Windows_NT" setlocal


set NC_HOME_BIN=%~dp0

call %NC_HOME_BIN%\uapSetupCmdLine.bat

%JAVA_HOME%\bin\java -jar %NC_HOME%\bin\liclib\licenseLock.jar

:QUIT
if "%OS%"=="Windows_NT" endlocal