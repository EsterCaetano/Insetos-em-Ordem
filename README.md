# Insetos em Ordem

Este projeto foi desenvolvido no âmbito da Unidade Curricular de Estágio/Projeto da Licenciatura de Engenharia Informática da Escola Superior de Tecnologia e Gestão do Instituto Politécnico de Beja.

## Sobre a Aplicação

Neste projeto foi desenvolvida uma aplicação Android para identificação de insetos através de chaves dicotómicas, tornando o acesso a ferramentas científicas acessíveis ao público geral. A aplicação foi implementada em Android Studio com Kotlin, utilizando Room para persistência de dados e Google Maps para georreferenciação.

A aplicação inclui conteúdos para uma chave de identificação de insetos até ao nível da respetiva ordem.

## Funcionalidades Principais

- **Identificação de Insetos:** Chave dicotómica para classificação até à ordem, com imagens e descrições.
- **Georreferenciação:** Integração com Google Maps para localização de avistamentos, permitindo visualização de todos os registos no mapa.
- **Gestão de Registos:** Secção "Meus Insetos" para editar, apagar e partilhar identificações.
- **Geração de PDF:** Funcionalidade para gerar documentos PDF com informações completas de classificações específicas, incluindo ordem, coordenadas, data, descrição e foto, salvos na pasta Downloads.
- **Política de Privacidade:** Implementação de aceitação obrigatória de política de privacidade antes do uso da aplicação.

## Tecnologia e Dependências

### Versão SDK
- **Mínimo:** 27
- **Alvo:** 34

### Bibliotecas Principais
- **Room 2.6.1** — Base de dados local
- **Google Maps SDK 18.2.0** — Mapas e geolocalização
- **PhotoView 2.3.0** — Visualização e zoom de imagens

### Google Maps API
Configurada com chave restrita ao package `pt.ipbeja.pi.piproject` com SHA-1 do certificado.

## Requisitos

Este projeto foi desenvolvido em Android Studio com suporte para Kotlin. A versão mínima do SDK é 27 para API level 27, mas poderá funcionar para outras versões.

## Como Compilar

```bash
git clone https://github.com/EsterCaetano/Insetos-em-Ordem.git
cd Insetos-em-Ordem
```

1. Abrir o projeto com o Android Studio
2. Ao compilar o projeto, as dependências necessárias serão descarregadas pelo gradle

## Como Estender

### Modificar a Chave Dicotómica

A definição da chave dicotómica encontra-se no ficheiro XML em `app/src/main/assets/chave.xml`. As imagens podem ser colocadas na mesma pasta, sendo que os respetivos caminhos referidos no XML serão procurados pela aplicação relativamente a esta pasta.

Durante a edição da chave, os caminhos relativos dos assets podem ser obtidos facilmente com o menu de contexto dos mesmos na opção `Copy Relative Path Ctrl+Alt+Shift+C`.

A chave segue um formato simples em que cada nó deve estar identificado por um código id unívoco.

## Documentação Técnica do Código

### Objetivo

Este documento descreve a organização principal do código da aplicação **Insetos em Ordem** para facilitar manutenção, evolução e onboarding.

### Estrutura Principal de Pacotes

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

### Fluxos Chave

#### 1) Identificação de Inseto
1. Utilizador responde as perguntas da chave dicotómica.
2. App resolve o resultado (ordem/subordem/grupo).
3. Utilizador pode guardar com foto, data e coordenadas.

#### 2) Menu "Os meus insetos"
1. App carrega registos da base de dados.
2. Em cada registo, menu permite:
   - Ver mais informação
   - Corrigir ordem
   - Enviar email com classificação
   - Gerar PDF
   - Apagar registo

#### 3) Geração de PDF
1. Filtra registos pela ordem selecionada.
2. Escreve dados de taxonomia, coordenadas, data e descrição.
3. Guarda PDF e abre pré-visualização na app.

### Pontos de Manutenção Recomendados

- Evitar lógica de negócio extensa dentro de `Activity`; sempre que possível, extrair para classes dedicadas.
- Manter métodos pequenos e focados numa responsabilidade.
- Documentar métodos com KDoc quando fazem I/O, persistência ou transformações de dados.
- Centralizar mensagens fixas em `strings.xml`.

### Ficheiros Mais Importantes para Alterações Futuras

- `app/src/main/java/pt/ipbeja/pi/piproject/listSavedInsects/MyIdentifications.kt`
- `app/src/main/java/pt/ipbeja/pi/piproject/persistence/IdentificationDao.kt`
- `app/src/main/java/pt/ipbeja/pi/piproject/idkey/IdentificationKey.kt`
- `app/src/main/assets/chave.xml`

### Convenções Sugeridas

- Classes em PascalCase, métodos/variáveis em camelCase.
- Comentários curtos e orientados a intenção (evitar comentários óbvios).
- Atualizar esta documentação sempre que houver mudança de fluxo funcional.

### Documentação por Ficheiro (KDoc adicionado em 2026-05-15)

#### Persistência
- `Identification.kt` — Entidade Room com dados de identificação (já tinha KDoc).
- `IdentificationDao.kt` — DAO com métodos CRUD (já tinha KDoc básico).
- `MyIdentificationsDb.kt` — Singleton Room database com `getDatabase()` documentado.
- `Converters.kt` — TypeConverters para Date ↔ Long (já tinha KDoc).

#### Chave Dicotómica
- `IdentificationKey.kt` — Singleton que carrega XML da chave; métodos privados de parse documentados:
  - `loadKey()` — Carrega ficheiro XML (PT ou EN).
  - `parseOption()` — Parse elemento 'option'.
  - `parseQuestion()` — Parse elemento 'node' (pergunta).
  - `parseResult()` — Parse elemento 'result' (ordem).
  - `loadXML()` — Carrega e parse completo do XML.
- `QuestionNode.kt` — Modelo de pergunta (já tinha descrição).
- `ResultNode.kt` — Modelo de resultado/ordem (já tinha descrição).
- `KeyOption.kt` — Modelo de opção (já tinha descrição).

#### Fluxo de Identificação
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

#### Gestão de Registos ("Os meus insetos")
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

#### Utilitários
- `Util.kt` — Utilitários de texto; método documentado:
  - `removeSpaces()` — Remove espaços múltiplos e normalizações.
- `Coordinates.kt` — Conversão de coordenadas; métodos já documentados:
  - `angleDMS()` — Converte ângulo para graus/minutos/segundos.
  - `anglesToDMS()` — Converte par (lat, lng) para formato legível.

#### Menu Inicial
- `MainActivity.kt` — Menu principal (revisar se necessário).
- `IntroActivity.kt` — Ecrã de intro (revisar se necessário).
- `ScreenItem.kt` — Modelo de slide.
- `IntroViewPagerAdapter.kt` — Adapter de ViewPager para intro.

#### Créditos
- `Credits.kt` — Activity de créditos (revisar se necessário).
- Várias imagens de outras instituições (IPBejaImageZoom, Ce3cImageZoom, etc.)
- `PrivacyPolicyInfo.kt` — Política de privacidade.

### Status de Documentação Completa (2026-05-15)

#### Classes Documentadas com KDoc em Português (19 classes)

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
  - PdfPreviewActivity.kt — Preview do PDF

✅ **Menu e navegação (2 classes):**
  - MainActivity.kt — Menu principal
  - Credits.kt — Página de créditos

✅ **Chave dicotómica e persistência (4 classes):**
  - IdentificationKey.kt — Parse XML da chave
  - MyIdentificationsDb.kt — Singleton Room database
  - Util.kt — Utilitários de texto
  - Coordinates.kt — Conversão de coordenadas

#### Classes com KDoc original em inglês (5 classes):
- Identification.kt — Entidade Room (já tem KDoc)
- QuestionNode.kt — Modelo de pergunta (já tem KDoc)
- ResultNode.kt — Modelo de resultado (já tem KDoc)
- KeyOption.kt — Modelo de opção (já tem KDoc)
- Converters.kt — TypeConverters (já tem KDoc)

#### Classes pendentes de documentação:
- IntroActivity.kt, IntroViewPagerAdapter.kt, ScreenItem.kt
- PrivacyPolicyInfo.kt, OrderPopupMenu.kt, PdfPreviewActivity.kt
- Info.kt e 7 classes ImageZoom (instituições)

**Total: 34 ficheiros .kt. Documentação concluída: 57% (19/34 com KDoc português)**

## Créditos

- **Programadores:** Ester A. Caetano, Rafael Conceição Narciso, José Mauricio, Vasco Flores, José Rodrigues, alunos do Instituto Politécnico de Beja
- **Professores Tutores:** Isabel Sofia Brito e João Paulo Barros.
- **Conteúdos:** Livro Insetos em Ordem, P. Garcia Pereira, E. Monteiro, F. Vala, e C. Luís
- **Colaboração:** Dra. Patrícia Garcia Pereira e equipa da Plataforma de Ciência Aberta

## Recursos Adicionais

- Vídeos dos testes de usabilidade: [Aqui](https://ipbejapt.sharepoint.com/:f:/s/ProjetoInsectoemOrdem/IgCaTSzfPrsTQJmnUDXFFMMGAYpjTfWWvyDXJieKC_HduGs?e=uJJT8w)
- Código fonte: https://github.com/EsterCaetano/Insetos-em-Ordem.git

Créditos
--------
- Programadores: Ester A. Caetano, Rafael Conceição Narciso, José Mauricio, Vasco Flores, José Rodrigues, alunos do Instituto Politécnico de Beja
- Professores Tutores: Isabel Sofia Brito e João Paulo Barros.
- Conteúdos do livro Insetos em Ordem, P. Garcia Pereira, E. Monteiro, F. Vala, e C. Luís
- Aplicação desenvolvida em colaboração com a Dra. Patrícia Garcia Pereira e pela equipa da Plataforma de Ciência Aberta

Vídeos dos testes de usabilidade da aplicação: [Aqui](https://ipbejapt.sharepoint.com/:f:/s/ProjetoInsectoemOrdem/IgCaTSzfPrsTQJmnUDXFFMMGAYpjTfWWvyDXJieKC_HduGs?e=uJJT8w)
