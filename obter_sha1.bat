@echo off
echo ============================================
echo OBTENCAO DA IMPRESSAO DIGITAL SHA-1
echo PARA GOOGLE MAPS API
echo ============================================
echo.

echo Verificando se keytool esta disponivel...
keytool -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: keytool nao encontrado. Certifique-se de que o Java JDK esta instalado.
    echo.
    echo Pressione qualquer tecla para sair...
    pause >nul
    exit /b 1
)

echo.
echo ============================================
echo IMPRESSAO DIGITAL PARA DEBUG (padrao):
echo ============================================
keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android 2>nul

if %errorlevel% neq 0 (
    echo.
    echo AVISO: Keystore de debug nao encontrado.
    echo Execute o Android Studio primeiro para criar o keystore de debug.
    echo.
)

echo.
echo ============================================
echo INSTRUCOES:
echo ============================================
echo 1. Copie a linha "SHA1:" mostrada acima
echo 2. Va para https://console.cloud.google.com/
echo 3. APIs e Servicos ^> Credenciais
echo 4. Clique na sua chave de API
echo 5. Adicione o SHA1 e package name: pt.ipbeja.pi.piproject
echo 6. Salve as alteracoes
echo.

echo Pressione qualquer tecla para continuar...
pause >nul
