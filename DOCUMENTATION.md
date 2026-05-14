DOCUMENTATION — Insetos em Ordem
=================================

Objetivo
--------
Documentar a arquitetura, convenções e um plano de refatoração para melhorar a manutenibilidade do projeto "Insetos em Ordem".

Visão geral da arquitetura
--------------------------
A aplicação é uma app Android escrita em Kotlin com as seguintes responsabilidades principais:
- UI (Activities / Fragments) — Interação com o utilizador e fluxos de navegação.
- Data (Room) — Persistência local de observações e identificações.
- Assets (XML `chave.xml` + imagens) — Definição da chave dicotómica e imagens usadas nas perguntas.
- Integração com Google Maps — Visualização de avistamentos e georreferenciação.
- Exportação PDF — Geração de relatórios em PDF com os dados das observações.

Estrutura de pastas (recomendada)
----------------------------------
Sugestão de organização dos pacotes Kotlin (mover/ajustar conforme for conveniente):

- app/src/main/java/pt/ipbeja/pi/piproject
  - data
    - db (Room database, DAOs, entidades)
    - models (data classes separadas das entidades, DTOs)
    - repository (classes para acesso a dados, unificam fontes: Room, network)
  - ui
    - main (Activity / Fragments do menu principal)
    - insects (ecrãs "Meus Insetos", edição, visualização)
    - map (mapa e mapa-related UI)
    - pdf (geração e preview do PDF)
  - network (se necessário: APIs, Retrofit)
  - utils (helpers, extensões Kotlin, conversores, constantes)
  - di (injeção de dependências, Hilt/Koin se for adotado)

Pontos principais de código
---------------------------
- Assets chave: `app/src/main/assets/chave.xml` — contém a chave dicotómica.
- Banco de dados Room: procurar classes que definem `@Entity`, `@Dao` e `Room.databaseBuilder`.
- Activities de interesse: `HomeActivity`, `LoginActivity`, e os ecrãs de gestão dos registos.

Plano de refatoração (prioridade alta → baixa)
----------------------------------------------
1. Corrigir e unificar a estrutura de pacotes (mover ficheiros para os pacotes indicados acima).
2. Separar camadas: UI não deve aceder diretamente a fontes de dados; introduzir `Repository` e `ViewModel` (Jetpack ViewModel) para cada fluxo principal.
3. Introduzir ViewModel e LiveData/Flow para ligar UI ↔ dados, substituindo callbacks diretos ao Room/Retrofit.
4. Consolidar o acesso ao Room: definir entidades e mapeadores (DTO ↔ Entity) claros.
5. Remover código duplicado e extrair utilitários comuns (ex.: utilitários de permissões, gerador de PDF, formatação de data).
6. Adicionar testes unitários básicos para lógica da geração de PDF e parsing da `chave.xml`.
7. Avaliar e atualizar dependências (Room, PhotoView, Google Maps) para versões seguras e estáveis.
8. Opcional: adicionar DI (Hilt) para facilitar testes e gestão de dependências.

Boas práticas e convenções
--------------------------
- Nomes de classes: PascalCase (ex.: `InsectRepository`, `HomeViewModel`).
- Nomes de métodos/variáveis: camelCase.
- Evitar Activities monolíticas: preferir Fragments ou Activities pequenas com responsabilidade única.
- Usar coroutines + Flow para operações assíncronas (em vez de Callbacks) quando possível.
- Tratar permissões em tempo de execução de forma centralizada (ex.: PermissionHelper).

Documentação técnica — onde procurar e editar
--------------------------------------------
- `app/src/main/assets/chave.xml` — editar a chave dicotómica.
- `app/src/main/res/layout` — layouts XML das Activities/Fragments.
- `app/src/main/java/...` — código Kotlin.
- `README.md` — visão geral do projeto (já atualizada).

Como compilar e executar localmente
----------------------------------
1. Abrir Android Studio.
2. File → Open → apontar para a pasta do projeto.
3. Construir o projecto (Build → Make Project) — Gradle irá descarregar dependências.
4. Executar em emulador ou dispositivo físico.

Comandos úteis (PowerShell / Windows):

```powershell
cd "C:\Users\ester\Desktop\PI-master-2026-02-23-jpb\PI-master-2026-02-23-jpb"
# verificar estado git
git status
# adicionar alterações e commitar
git add README.md DOCUMENTATION.md
git commit -m "docs: cleanup README and add DOCUMENTATION.md with refactor plan"
# push (se a remote estiver configurada)
git push origin main
```

Sugestões para controlo de versões e ficheiros grandes
-----------------------------------------------------
- O repositório tem ficheiros potencialmente grandes (p.ex. imagens, zip). Para ficheiros maiores que 50MB considerar usar Git LFS: https://git-lfs.github.com/

Contribuição e revisão
----------------------
- Abrir issues para tarefas de refatoração e atribuir prioridades.
- Fazer PRs pequenos e focados para cada alteração de arquitetura (mover pacotes, introduzir ViewModel, etc.).
- Incluir descrições claras e steps to test em cada PR.

Checklist rápido para a primeira sprint de refactor
---------------------------------------------------
- [ ] Criar package `data` e mover entidades/DAOs para lá.
- [ ] Criar `repository` e um `InsectRepository` mínimo.
- [ ] Introduzir `ViewModel` para o ecrã "Meus Insetos".
- [ ] Escrever 2 testes unitários simples (parsing do XML e geração de PDF).
- [ ] Atualizar README com instruções de contribuição e padrão de commits.

Contacto e notas finais
-----------------------
Se quiser, implemento os primeiros passos do plano (mover ficheiros, criar `ViewModel` e `Repository`) e faço commits incrementais para revisão. Posso também abrir as PRs se preferires trabalhar com um fluxo de GitHub mais formal.

