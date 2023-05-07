@ECHO OFF

REM This script is a Testsuite to test 
REM It searches every file in Folder input and runs the jar with it

SETLOCAL EnableDelayedExpansion
SET APPEXEC="GroPro.jar"

IF exist %APPEXEC% GOTO APPFOUND

:APPNOTFOUND
ECHO Das Programm %APPEXEC% konnte nicht gefunden werden.
PAUSE
GOTO END

:APPFOUND

SET PARAMS="files="
for %%f in (input\*.*) do ( 
java -jar %APPEXEC% %%f 
)

PAUSE

:END