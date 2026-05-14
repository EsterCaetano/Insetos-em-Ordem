# Documentacao Tecnica do Codigo

## Objetivo
Este documento descreve a organizacao principal do codigo da aplicacao **Insetos em Ordem** para facilitar manutencao, evolucao e onboarding.

## Estrutura principal
- `app/src/main/java/pt/ipbeja/pi/piproject/startUpApp/`
  - Fluxo inicial da app (intro, menu principal, navegacao inicial).
- `app/src/main/java/pt/ipbeja/pi/piproject/identificationInsect/`
  - Fluxo de identificacao (questionario/chave, resultado, mapa, guardar identificacao).
- `app/src/main/java/pt/ipbeja/pi/piproject/listSavedInsects/`
  - Gestao de registos guardados (listar, corrigir ordem, apagar, enviar email, gerar PDF).
- `app/src/main/java/pt/ipbeja/pi/piproject/persistence/`
  - Persistencia com Room (`Identification`, `IdentificationDao`, `MyIdentificationsDb`).
- `app/src/main/java/pt/ipbeja/pi/piproject/idkey/`
  - Modelos e logica da chave dicotomica (`IdentificationKey`, `QuestionNode`, `ResultNode`).
- `app/src/main/assets/chave.xml`
  - Definicao da chave dicotomica usada pela app.

## Fluxos chave
### 1) Identificacao de inseto
1. Utilizador responde as perguntas da chave dicotomica.
2. App resolve o resultado (ordem/subordem/grupo).
3. Utilizador pode guardar com foto, data e coordenadas.

### 2) Menu "Os meus insetos"
1. App carrega registos da base de dados.
2. Em cada registo, menu permite:
   - ver mais informacao,
   - corrigir ordem,
   - enviar email com classificacao,
   - gerar PDF,
   - apagar registo.

### 3) Geracao de PDF
1. Filtra registos pela ordem selecionada.
2. Escreve dados de taxonomia, coordenadas, data e descricao.
3. Guarda PDF e abre pre-visualizacao na app.

## Pontos de manutencao recomendados
- Evitar logica de negocio extensa dentro de `Activity`; sempre que possivel, extrair para classes dedicadas.
- Manter metodos pequenos e focados numa responsabilidade.
- Documentar metodos com KDoc quando fazem I/O, persistencia ou transformacoes de dados.
- Centralizar mensagens fixas em `strings.xml`.

## Ficheiros mais importantes para alteracoes futuras
- `app/src/main/java/pt/ipbeja/pi/piproject/listSavedInsects/MyIdentifications.kt`
- `app/src/main/java/pt/ipbeja/pi/piproject/persistence/IdentificationDao.kt`
- `app/src/main/java/pt/ipbeja/pi/piproject/idkey/IdentificationKey.kt`
- `app/src/main/assets/chave.xml`

## Convencoes sugeridas
- Classes em PascalCase, metodos/variaveis em camelCase.
- Comentarios curtos e orientados a intencao (evitar comentarios obvios).
- Atualizar esta documentacao sempre que houver mudanca de fluxo funcional.

## Documentacao por ficheiro (KDoc adicionado em 2026-05-14)

### Persistencia
- `Identification.kt` — Entidade Room com dados de identificacao (ja tinha KDoc).
- `IdentificationDao.kt` — DAO com metodos CRUD (ja tinha KDoc basico).
- `MyIdentificationsDb.kt` — Singleton Room database com `getDatabase()` documentado.
- `Converters.kt` — TypeConverters para Date ↔ Long (ja tinha KDoc).

### Chave Dicotomica
- `IdentificationKey.kt` — Singleton que carrega XML da chave; metodos privados de parse documentados:
  - `loadKey()` — Carrega ficheiro XML (PT ou EN).
  - `parseOption()` — Parse elemento 'option'.
  - `parseQuestion()` — Parse elemento 'node' (pergunta).
  - `parseResult()` — Parse elemento 'result' (ordem).
  - `loadXML()` — Carrega e parse completo do XML.
- `QuestionNode.kt` — Modelo de pergunta (ja tinha descricao).
- `ResultNode.kt` — Modelo de resultado/ordem (ja tinha descricao).
- `KeyOption.kt` — Modelo de opcao (ja tinha descricao).

### Fluxo de Identificacao
- `Classificacao.kt` — Activity que guida utilizador pelas perguntas; metodos documentados:
  - `goToNextQuestion()` — Navega para pergunta/resultado.
  - `onOptionAClick()` e `onOptionBClick()` — Handlers de opcoes.
  - `onImageAClick()` e `onImageBClick()` — Abre zoom de imagens.
  - `setImage()` — Carrega imagem dos assets.
- `ShowResult.kt` — Exibe resultado da identificacao (revisar se necessario).
- `SaveIdentification.kt` — Guarda identificacao na BD (revisar se necessario).
- `MapsActivity.kt` — Mapa de avistamentos (revisar se necessario).
- `ImageZoom.kt` — Preview de imagem.

### Gestao de Registos ("Os meus insetos")
- `MyIdentifications.kt` — Activity principal de lista; metodos documentados:
  - `loadList()` — Carrega identificacoes da BD.
  - `setListener()` — Registra cliques na lista.
  - `changeOrder()` — Permite corrigir ordem (agora com lista numerada).
  - `updateOrder()` — Guarda ordem corrigida.
  - `showMoreInformation()` — Show descricao.
  - `deleteItem()` — Apaga registo.
  - `sendEmailWithImageFromList()` — Prepara email com dados.
  - `resolveTaxonomy()` e `extractTaxonomyValue()` — Parse taxonomia.
  - `generatePdfForItem()` — Gera PDF com registos da mesma ordem.
  - `drawMultilineText()` — Desenha texto multilinea no PDF.
  - `openGeneratedPdf()` — Abre preview do PDF.
- `IdentificationAdapter.kt` — Adapter de lista; metodo documentado:
  - `getView()` — Infla item com ordem, coordenadas, data e foto.
- `PdfPreviewActivity.kt` — Preview do PDF (revisar se necessario).
- `OrderPopupMenu.kt` — Menu popup de opcoes (revisar se necessario).

### Utilidades
- `Util.kt` — Utilitarios de texto; metodo documentado:
  - `removeSpaces()` — Remove espacos multiplos e normalizacoes.
- `Coordinates.kt` — Conversao de coordenadas; metodos ja documentados:
  - `angleDMS()` — Converte angulo para graus/minutos/segundos.
  - `anglesToDMS()` — Converte par (lat, lng) para formato legivel.

### Menu Inicial  
- `MainActivity.kt` — Menu principal (revisar se necessario).
- `IntroActivity.kt` — Ecrã de intro (revisar se necessario).
- `ScreenItem.kt` — Modelo de slide.
- `IntroViewPagerAdapter.kt` — Adapter de ViewPager para intro.

### Creditos
- `Credits.kt` — Activity de creditos (revisar se necessario).
- Varias imagens de outras instituicoes (IPBejaImageZoom, Ce3cImageZoom, etc.)
- `PrivacyPolicyInfo.kt` — Politica de privacidade.


