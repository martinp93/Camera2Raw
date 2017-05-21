@echo off

dir /s /b *.java > files.lst

echo Compiling...
echo.

javac @files.lst

javadoc @files.lst -d .\imageapi-javadoc -windowtitle "Image API" -doctitle "Image Processing API Specification" -sourcetab 2

del files.lst

@pause