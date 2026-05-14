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

