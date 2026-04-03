@echo off

if exist bin (
    rmdir /s /q bin
)

mkdir bin

echo Compilando...

javac -d bin -cp "lib/sqlite-jdbc-3.51.3.0.jar" src\Banco.java src\CadVeiculos.java src\Conexao.java src\IO.java src\Input.java
if errorlevel 1 (
    echo Erro na compilacao!
    pause
    exit /b
)

echo Executando...
java --enable-native-access=ALL-UNNAMED -cp "bin;lib/sqlite-jdbc-3.51.3.0.jar" src.CadVeiculos
pause