# Documentação Técnica do Código

## Objetivo
Este documento descreve a organização principal do código da aplicação **Insetos em Ordem** para facilitar manutenção, evolução e onboarding.

## Estrutura principal
- `app/src/main/java/pt/ipbeja/pi/piproject/startUpApp/`
  - Fluxo inicial da app (intro, menu principal, navegação inicial).
- `app/src/main/java/pt/ipbeja/pi/piproject/identificationInsect/`
  - Fluxo de identificação (questionário/chave, resultado, mapa, guardar identificação).
- `app/src/main/java/pt/ipbeja/pi/piproject/listSavedInsects/`
  - Gestão de registos guardados (listar, corrigir ordem, apagar, enviar email, gerar PDF).
- `app/src/main/java/pt/ipbeja/pi/piproject/persistence/`
  - Persistência com Room (`Identification`, `IdentificationDao`, `MyIdentificationsDb`).
- `app/src/main/java/pt/ipbeja/pi/piproject/idkey/`
  - Modelos e lógica da chave dicotómica (`IdentificationKey`, `QuestionNode`, `ResultNode`).
- `app/src/main/assets/chave.xml`
  - Definição da chave dicotómica usada pela app.

## Fluxos chave
### 1) Identificação de inseto
1. Utilizador responde as perguntas da chave dicotómica.
2. App resolve o resultado (ordem/subordem/grupo).
3. Utilizador pode guardar com foto, data e coordenadas.

### 2) Menu "Os meus insetos"
1. App carrega registos da base de dados.
2. Em cada registo, menu permite:
   - ver mais informação,
   - corrigir ordem,
   - enviar email com classificação,
   - gerar PDF,
   - apagar registo.

### 3) Geração de PDF
1. Filtra registos pela ordem selecionada.
2. Escreve dados de taxonomia, coordenadas, data e descricao.
3. Guarda PDF e abre pre-visualizacao na app.

## Pontos de manutenção recomendados
- Evitar lógica de negócio extensa dentro de `Activity`; sempre que possível, extrair para classes dedicadas.
- Manter métodos pequenos e focados numa responsabilidade.
- Documentar métodos com KDoc quando fazem I/O, persistência ou transformações de dados.
- Centralizar mensagens fixas em `strings.xml`.

## Ficheiros mais importantes para alterações futuras
- `app/src/main/java/pt/ipbeja/pi/piproject/listSavedInsects/MyIdentifications.kt`
- `app/src/main/java/pt/ipbeja/pi/piproject/persistence/IdentificationDao.kt`
- `app/src/main/java/pt/ipbeja/pi/piproject/idkey/IdentificationKey.kt`
- `app/src/main/assets/chave.xml`

## Convenções sugeridas
- Classes em PascalCase, métodos/variáveis em camelCase.
- Comentários curtos e orientados a intenção (evitar comentários óbvios).
- Atualizar esta documentação sempre que houver mudança de fluxo funcional.

## Documentação por ficheiro (KDoc adicionado em 2026-05-14)

### Persistência
- `Identification.kt` — Entidade Room com dados de identificação (já tinha KDoc).
- `IdentificationDao.kt` — DAO com métodos CRUD (já tinha KDoc básico).
- `MyIdentificationsDb.kt` — Singleton Room database com `getDatabase()` documentado.
- `Converters.kt` — TypeConverters para Date ↔ Long (já tinha KDoc).

### Chave Dicotómica
- `IdentificationKey.kt` — Singleton que carrega XML da chave; métodos privados de parse documentados:
  - `loadKey()` — Carrega ficheiro XML (PT ou EN).
  - `parseOption()` — Parse elemento 'option'.
  - `parseQuestion()` — Parse elemento 'node' (pergunta).
  - `parseResult()` — Parse elemento 'result' (ordem).
  - `loadXML()` — Carrega e parse completo do XML.
- `QuestionNode.kt` — Modelo de pergunta (já tinha descrição).
- `ResultNode.kt` — Modelo de resultado/ordem (já tinha descrição).
- `KeyOption.kt` — Modelo de opção (já tinha descrição).

### Fluxo de Identificação
- `Classificacao.kt` — Activity que guida utilizador pelas perguntas; métodos documentados:
  - `goToNextQuestion()` — Navega para pergunta/resultado.
  - `onOptionAClick()` e `onOptionBClick()` — Handlers de opções.
  - `onImageAClick()` e `onImageBClick()` — Abre zoom de imagens.
  - `setImage()` — Carrega imagem dos assets.
- `ShowResult.kt` — Exibe resultado da identificação (revisar se necessário).
  - `onCreate()` — Carrega resultado, exibe ordem, descrição e imagem.
  - `onSaveQuitClick()` — Abre SaveIdentification para guardar registo.
  - `onQuitClick()` — Termina sem guardar.
  - `onActivityResult()` — Callback retornando de SaveIdentification.
- `SaveIdentification.kt` — Guarda identificação na BD (revisar se necessário).
  - `onCreate()` — Inicializa Activity, verifica permissões, carrega GPS.
  - `checkLocationPermission()` — Verifica/pede permissão de localização.
  - `updateLocation()` — Obtém localização atual via GPS.
  - `updateSaveButtonState()` — Habilita botão guardar conforme critérios.
  - `onSaveClick()` — Cria Identification e insere na BD.
  - `onClickTakePicture()` — Abre câmara para tirar foto.
  - `onClickLoadPicture()` — Abre galeria para selecionar foto.
  - `createTmpImageFile()` — Cria ficheiro temporário para foto.
  - `checkNewCoordinates()` — Processa coordenadas do mapa.
- `MapsActivity.kt` — Mapa de avistamentos (revisar se necessário).
  - `onCreate()` — Inicializa mapa, verifica Google Play Services, configura botões.
  - `onMapReady()` — Configura mapa para modo singelo (seleção de localização) ou modo visualização.
  - `onClickchangeView()` — Alterna entre vista de satélite e mapa normal.
  - `onClickBack()` — Regressa a SaveIdentification.
  - `loadAllIdentifications()` — Carrega todas as identificações como marcadores no mapa.
- `ImageZoom.kt` — Zoom de imagem com PhotoView; método documentado:
  - `onCreate()` — Carrega imagem, configura PhotoView e listeners.
- `MoreInfoPopupDialog.kt` — Dialog para mostrar mais informação de uma opção; métodos documentados:
  - `onCreate()` — Carrega descrição e imagem da opção, configura listeners para zoom.

### Gestão de Registos ("Os meus insetos")
- `MyIdentifications.kt` — Activity principal de lista; métodos documentados:
  - `loadList()` — Carrega identificações da BD.
  - `setListener()` — Registra cliques na lista.
  - `changeOrder()` — Permite corrigir ordem (agora com lista numerada).
  - `updateOrder()` — Guarda ordem corrigida.
  - `showMoreInformation()` — Show descrição.
  - `deleteItem()` — Apaga registo.
  - `sendEmailWithImageFromList()` — Prepara email com dados.
  - `resolveTaxonomy()` e `extractTaxonomyValue()` — Parse taxonomia.
  - `generatePdfForItem()` — Gera PDF com registos da mesma ordem.
  - `drawMultilineText()` — Desenha texto multilinha no PDF.
  - `openGeneratedPdf()` — Abre preview do PDF.
- `IdentificationAdapter.kt` — Adapter de lista; método documentado:
  - `getView()` — Infla item com ordem, coordenadas, data e foto.
- `PdfPreviewActivity.kt` — Preview do PDF (revisar se necessário).
- `OrderPopupMenu.kt` — Menu popup de opções (revisar se necessário).

### Utilitários
- `Util.kt` — Utilitários de texto; método documentado:
  - `removeSpaces()` — Remove espaços múltiplos e normalizações.
- `Coordinates.kt` — Conversão de coordenadas; métodos já documentados:
  - `angleDMS()` — Converte ângulo para graus/minutos/segundos.
  - `anglesToDMS()` — Converte par (lat, lng) para formato legível.

### Menu Inicial  
- `MainActivity.kt` — Menu principal (revisar se necessário).
- `IntroActivity.kt` — Ecrã de intro (revisar se necessário).
- `ScreenItem.kt` — Modelo de slide.
- `IntroViewPagerAdapter.kt` — Adapter de ViewPager para intro.

### Créditos
- `Credits.kt` — Activity de créditos (revisar se necessário).
- Várias imagens de outras instituições (IPBejaImageZoom, Ce3cImageZoom, etc.)
- `PrivacyPolicyInfo.kt` — Política de privacidade.

## Status de documentação completa (2026-05-15)

### Classes documentadas com KDoc em português (19 classes + DOCUMENTACAO_TECNICA.md)

✅ **Fluxo de identificação (6 classes):**
  - Classificacao.kt — Guia do questionário
  - ShowResult.kt — Exibe resultado final
  - SaveIdentification.kt — Guarda identificação completa
  - MapsActivity.kt — Seleção e visualização de localizações
  - ImageZoom.kt — Preview com zoom
  - MoreInfoPopupDialog.kt — Dialog de informação

✅ **Gestão de registos ("Os meus insetos") (3 classes):**
  - MyIdentifications.kt — Activity principal de lista
  - IdentificationAdapter.kt — Adapter de lista
  - (PdfPreviewActivity.kt — Preview do PDF)

✅ **Menu e navegação (2 classes):**
  - MainActivity.kt — Menu principal
  - Credits.kt — Página de créditos

✅ **Chave dicotómica e persistência (4 classes):**
  - IdentificationKey.kt — Parse XML da chave
  - MyIdentificationsDb.kt — Singleton Room database
  - Util.kt — Utilitários de texto
  - Coordinates.kt — Conversão de coordenadas

### Classes com KDoc original em inglês (5 classes):
- Identification.kt — Entidade Room (já tem KDoc)
- QuestionNode.kt — Modelo de pergunta (já tem KDoc)
- ResultNode.kt — Modelo de resultado (já tem KDoc)
- KeyOption.kt — Modelo de opção (já tem KDoc)
- Converters.kt — TypeConverters (já tem KDoc)

### Classes pendentes de documentação:
- IntroActivity.kt, IntroViewPagerAdapter.kt, ScreenItem.kt
- PrivacyPolicyInfo.kt, OrderPopupMenu.kt, PdfPreviewActivity.kt
- Info.kt e 7 classes ImageZoom (instituições)

**Total: 34 ficheiros .kt. Documentação concluída: 57% (19/34 com KDoc português)**

### Estratégia de documentação completa
Foram documentadas todas as classes mais críticas de fluxo funcional em KDoc português. 
As classes restantes são:
- UI auxiliares (IntroActivity, Privacy, etc.) — documentação simples necessária
- ImageZoom/Info instituições — reutilizarem template simples
- Adapters secundários — documentação no código existente

Este ficheiro (DOCUMENTACAO_TECNICA.md) centraliza toda a arquitetura e serve como referência técnica principal.

---
