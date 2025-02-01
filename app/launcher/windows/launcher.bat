::[Bat To Exe Converter]
::
::YAwzoRdxOk+EWAjk
::fBw5plQjdCuDJEuB5E0jFBZdRxGSKXiyOrQM+NT37v+JoUUYRt4ocYHf1aOdHOkQ5UuqfJUitg==
::YAwzuBVtJxjWCl3EqQJgSA==
::ZR4luwNxJguZRRnk
::Yhs/ulQjdF+5
::cxAkpRVqdFKZSzk=
::cBs/ulQjdF65
::ZR41oxFsdFKZSDk=
::eBoioBt6dFKZSDk=
::cRo6pxp7LAbNWATEpCI=
::egkzugNsPRvcWATEpSI=
::dAsiuh18IRvcCxnZtBJQ
::cRYluBh/LU+EWAjk
::YxY4rhs+aU+IeA==
::cxY6rQJ7JhzQF1fEqQJhZksaHErSXA==
::ZQ05rAF9IBncCkqN+0xwdVsFAlTMbCXqZg==
::ZQ05rAF9IAHYFVzEqQIdIRBBfBGRNAs=
::eg0/rx1wNQPfEVWB+kM9LVsJDGQ=
::fBEirQZwNQPfEVWB+kM9LVsJDGQ=
::cRolqwZ3JBvQF1fEqQIDIBpGSUSRP3m/A7sP4Ofv/KqIsA0KXOMrfZneyPru
::dhA7uBVwLU+EWHSF800+MD5nfGQ=
::YQ03rBFzNR3SWATElA==
::dhAmsQZ3MwfNWATEVotweksGGUSvNWCvNqAP4Ka7zuaL4l8UU+ora8/J36eLMvYW7guE
::ZQ0/vhVqMQ3MEVWAtB9wSA==
::Zg8zqx1/OA3MEVWAtB9wSA==
::dhA7pRFwIByZRRnk
::Zh4grVQjdCuDJEuB5E0jFBZdRxGSKXiyOrQM+NT37v+JoUUYRt4ocYHf1aOdHM0a4VDUYIUlmH9Cnas=
::YB416Ek+ZG8=
::
::
::978f952a14a936cc963da21a135fa983
﻿@echo off
setlocal

:: Ruta al runtime personalizado
set JAVA_HOME=%~dp0runtime
set PATH=%JAVA_HOME%\bin;%PATH%

:: Ejecutar el programa Java
javaw -jar "%~dp0mikupush.jar" ui

endlocal
