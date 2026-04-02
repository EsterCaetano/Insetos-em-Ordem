# Como Ativar Google Maps Android API

## 📋 Pré-requisitos
- Conta Google
- Projeto Android Studio configurado
- Package name da app: `pt.ipbeja.pi.piproject`

## 🚀 Passo 1: Aceder ao Google Cloud Console

1. Abra o navegador e vá para: https://console.cloud.google.com/
2. Faça login com a sua conta Google
3. Selecione ou crie um novo projeto:
   - Clique em "Selecionar um projeto" (canto superior esquerdo)
   - Clique em "Novo Projeto"
   - Dê um nome ao projeto (ex: "Insetos em Ordem")
   - Clique em "Criar"

## 🗺️ Passo 2: Ativar a Google Maps Android API

1. No menu lateral esquerdo, clique em "APIs e Serviços" > "Biblioteca"
2. Na barra de pesquisa, digite "Maps SDK for Android"
3. Clique no resultado "Maps SDK for Android"
4. Clique no botão "Ativar"

## 🔑 Passo 3: Criar Credenciais (Chave da API)

1. No menu lateral, clique em "APIs e Serviços" > "Credenciais"
2. Clique em "+ CRIAR CREDENCIAIS" > "Chave de API"
3. A chave será criada automaticamente
4. **IMPORTANTE:** Copie a chave imediatamente (começa com "AIzaSy...")

## 🔒 Passo 4: Restringir a Chave da API (Segurança)

1. Na lista de credenciais, clique no nome da chave que acabou de criar
2. Em "Restrições de aplicações", selecione "Aplicações Android"
3. Clique em "+ Adicionar nome do pacote e impressão digital"
4. Preencha:
   - **Nome do pacote:** `pt.ipbeja.pi.piproject`
   - **Impressão digital do certificado SHA-1:** `6C:A1:96:6F:26:FF:E4:28:7B:03:C0:60:62:79:BF:7A:33:8D:03:59`
5. Em "Restrições de API", selecione "Restringir chave"
6. Marque apenas "Maps SDK for Android"
7. Clique em "Salvar"

## 🛠️ Passo 5: Obter a Impressão Digital SHA-1

### Opção A: Via Android Studio (Recomendado)
1. Abra o Android Studio
2. Abra o projeto
3. Clique em "Gradle" (painel direito)
4. Navegue: `app > Tasks > android > signingReport`
5. Execute o `signingReport`
6. Procure por "SHA1" na aba "Run"
7. Copie o valor SHA1 (para debug ou release)

### Opção B: Via Terminal
```bash
# Para debug keystore (padrão)
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

# Para release keystore (se aplicável)
keytool -list -v -keystore caminho/para/sua/release.keystore -alias seu_alias
```

## 📝 Passo 6: Configurar no Projeto

1. Abra o ficheiro `app/src/main/res/values/google_maps_api.xml`
2. Substitua `YOUR_API_KEY_HERE` pela sua chave real:
```xml
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">AIzaSySUA_CHAVE_REAL_AQUI</string>
```

## 🧪 Passo 7: Testar

1. Compile e execute a aplicação
2. Vá para "Meus Insetos"
3. Clique no botão "Mapa"
4. O mapa deve carregar corretamente

## ⚠️ Notas Importantes

- **Custos:** A Google Maps API tem um nível gratuito generoso, mas pode haver custos para uso intenso
- **Segurança:** Sempre restrinja as chaves da API para evitar uso não autorizado
- **Debug vs Release:** Use diferentes chaves para debug e release com diferentes SHA-1
- **Problemas comuns:**
  - Erro "API key not found": Verifique se a chave está correta no google_maps_api.xml
  - Erro "Invalid key": Verifique as restrições da chave
  - Mapa em branco: Verifique se a API está ativada e a chave é válida

## 📞 Suporte

Se tiver problemas:
- Verifique o [Google Maps Platform Troubleshooting](https://developers.google.com/maps/documentation/android-sdk/troubleshooting)
- Consulte a documentação oficial: https://developers.google.com/maps/documentation/android-sdk/get-api-key
