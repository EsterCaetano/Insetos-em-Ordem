# Guião de Teste de Usabilidade - Aplicação "Insetos em Ordem"

## Introdução
Este guião descreve o processo de teste de usabilidade para a aplicação móvel "Insetos em Ordem", desenvolvida para auxiliar na identificação de insetos através de chaves dicotómicas. O teste visa avaliar a facilidade de uso, eficiência e satisfação dos utilizadores ao interagirem com a aplicação.

## Objetivos do Teste
- Avaliar a intuitividade da interface e navegação
- Identificar dificuldades na realização de tarefas principais
- Medir o tempo necessário para completar tarefas
- Avaliar a satisfação geral dos utilizadores
- Identificar problemas de usabilidade e sugestões de melhoria



## Cenários de Teste e Tarefas

### Tarefa 1: Aceitar Política de Privacidade
**Objetivo:** Garantir que o utilizador aceite a política de privacidade antes de usar a aplicação.

**Passos:**
1. Abrir a aplicação pela primeira vez.
2. Concordar com a política de privacidade: Aceitar a política e clicar no botão "Próximo" ou na parte superior direito, clicar no botão "Pular" para a página seguinte.
3. Navegar pelo ecrã de introdução (IntroActivity).
4. Explorar o menu principal (MainActivity).


### Tarefa 2: Identificação de Inseto (Conhecimento)
**Objetivo:** Identificar especificamente um gafanhoto (**Orthoptera - Caelifera**) utilizando características e chaves dicotómicas.

**Contexto do Cenário:**

Imagine que encontrou um inseto com corpo alongado, asas anteriores robustas e duras, terceiro par de patas comprido e adaptadas para saltar, antenas de tamanho claramente inferior (curtas) ao do corpo. Pretende identificá-lo através da aplicação para confirmar se é um gafanhoto (Orthoptera - Caelifera).


**Passos Detalhados:**

1. **Iniciar o Processo de Identificação:**
   - Aceda ao menu principal e selecione "Identificar"
   - Prepare a imagem ou descrição do inseto encontrado

2. **Responder às Características Principais de Orthoptera - Caelifera:**
   - **Pergunta 1:** "O inseto tem asas anteriores robustas e duras?"
     * Resposta esperada: **SIM** (Característica de Orthoptera)
   
   - **Pergunta 2:** Terceiro par de patas comprido e adaptadas para saltar?"
     * Resposta esperada: **SIM**

   - **Pergunta 3:** "As antenas de tamanho claramente inferior (curtas), menores que o comprimento do corpo?"
     * Resposta esperada: **SIM** (Diferencia Caelifera de Ensifera)

6. Clicar no botão **Guardar no meus insetos.**

### Tarefa 3: Georreferenciação e Registo
**Objetivo:** Confirmar a localização no mapa e guardar a descoberta.

**Passos:**
1. Depois de identificar um inseto, clique no **confirmar** para confirmar a localização onde encontrou o inseto e clicar em guardar.
2. Tirar ou carregar uma Foto: Após a identificação, a aplicação irá pedir-lhe para capturar uma imagem. Tire uma foto nítida do inseto que classificou e clicar no OK. Em alternativa, pode carregar uma foto que já tenha tirado, clicando no botão **Carregar Foto na Galeria.**
3. Clique em guardar para guardar o seu registo.

### Tarefa 4: Gestão da Base de Dados
**Objetivo:** Aceder à secção **Os Meus Insetos** e editar informações de um registo existente.

Todas as suas identificações ficam guardadas no menu **"Os Meus Insetos"**, que funciona como o seu caderno de campo digital.

1. Aceder à Lista: No menu principal, procure a secção **"Os Meus Insetos"**. Aqui encontrará a lista de todos os insetos que já registou.
2. **Selecionar um Registo:** Toque num dos registos da lista para ver os seus detalhes.
3. Editar o Registo: Dentro da página de detalhes, terá várias opções para gerir essa descoberta:

   I. Mais Informações: Esta funcionalidade pode permitir visualizar as informações do inseto guardado.

   II. Corrigir a Ordem: Se, com o tempo, descobrir que a classificação inicial estava incorreta, pode usar esta opção para selecionar a ordem certa.

   III. Submeter Avistamento: Esta funcionalidade permite partilhar por e-mail a sua descoberta com a comunidade científica do projeto (como a equipa da Plataforma de Ciência Aberta mencionada nos créditos), contribuindo para a ciência cidadã!

   IV. Gerar PDF: Gerar um documento PDF com todas as informações de uma classificação selecionada. O PDF será gerado com todas as informações do inseto selecionado e baixado para a área de transferência do dispositivo.

   V. Apagar o Registo: Remove permanentemente a identificação da sua lista. Use esta opção se adicionou algo por engano.

### Tarefa 5: Conhecer os Autores (Validação e Créditos)
**Objetivo:** Saber quem tornou a aplicação possível e quais as fontes do conhecimento.

1. Consultar Créditos: No menu principal ou num menu de opções, procure a secção "Créditos".
2. Explorar a Informação: Nesta secção, poderá ver:

   • Os nomes dos programadores e professores que criaram a aplicação.

   • As fontes bibliográficas usadas para o conteúdo científico (como o livro "Insetos em Ordem").

   • As entidades colaboradoras, e a Plataforma de Ciência Aberta.

__________________________________________________________________________________________________________________________________________________





















## Instruções para Participantes
1. **Antes do teste:** Leia e assine o consentimento informado
2. **Durante o teste:** Pense em voz alta enquanto realiza as tarefas
3. **Instruções gerais:**
   - "Imagine que encontrou um inseto e quer identificá-lo usando esta aplicação"
   - "Por favor, verbalize os seus pensamentos e dúvidas à medida que usa a app"
   - "Se não conseguir realizar uma tarefa, informe o moderador"
4. **Após o teste:** Preencha o questionário de satisfação

## Guia do Moderador
1. **Preparação:**
   - Instalar a aplicação no dispositivo de teste
   - Preparar cenários com imagens de insetos para teste
   - Configurar gravação de ecrã (se possível)

2. **Durante o teste:**
   - Apresentar-se e explicar o objetivo do teste
   - Dar instruções claras para cada tarefa
   - Não interferir excessivamente - deixar o utilizador explorar
   - Registar observações: erros, hesitações, comentários
   - Usar frases neutras: "O que está a tentar fazer?" em vez de "Clique aqui"

3. **Técnicas de moderação:**
   - "Pense em voz alta"
   - "Por que escolheu essa opção?"
   - "O que esperava que acontecesse?"

## Questionário Pós-Teste
Avalie os seguintes aspetos numa escala de 1 a 5 (1=Muito mau, 5=Muito bom):

1. Facilidade geral de uso da aplicação
2. Intuitividade da navegação
3. Clareza das instruções e mensagens
4. Satisfação com o design visual
5. Utilidade da aplicação para identificar insetos
6. Probabilidade de recomendar a aplicação

**Perguntas abertas:**
- Quais foram os principais problemas encontrados?
- Que funcionalidades gostaria de ver adicionadas?
- Sugestões de melhoria

## Análise dos Resultados
- Compilar tempos médios por tarefa
- Identificar padrões de erros
- Analisar comentários e sugestões
- Priorizar problemas de usabilidade
- Recomendar melhorias baseadas nos achados

## Anexos
- Formulário de consentimento
- Modelo de observação do moderador
- Questionário detalhado
- Lista de verificação para configuração do teste
