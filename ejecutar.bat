@echo off
setlocal
cd /d "%~dp0"
chcp 65001 >nul
where javac >nul 2>nul
if errorlevel 1 (
    echo Instala un JDK 17 o superior y agrega su carpeta bin al PATH.
    pause
    exit /b 1
)
if not exist out mkdir out
javac -encoding UTF-8 --release 17 -d out @fuentes.txt
if errorlevel 1 (
    pause
    exit /b 1
)
java -Dfile.encoding=UTF-8 -cp out vista.Principal %*
pause
