## Insetos em Ordem
================
Este projeto foi desenvolvido no âmbito da Unidade Curricular de Estagio/Projeto da Licenciatura de Engenharia Informática da Escola Superior de Tecnologia e Gestão do Instituto Politécnico de Beja.

Neste projeto foi desenvolvido uma aplicação Android para identificação de insetos através de chaves dicotómicas, tornando o acesso a ferramentas científicas acessíveis ao público geral. A aplicação foi implementada em Android Studio com Kotlin, utilizando Room para persistência de dados e Google Maps para georreferenciação.

A aplicação inclui conteúdos para uma chave de identificação de insetos até ao nível da respetiva ordem.

## Funcionalidades Principais
- **Identificação de Insetos:** Chave dicotómica para classificação até à ordem, com imagens e descrições.
- **Georreferenciação:** Integração com Google Maps para localização de avistamentos, permitindo visualização de todos os registos no mapa.
- **Gestão de Registos:** Secção "Meus Insetos" para editar, apagar e partilhar identificações.
- **Geração de PDF:** Funcionalidade para gerar documentos PDF com informações completas de classificações específicas, incluindo ordem, coordenadas, data, descrição e foto, salvos na pasta Downloads.
- **Política de Privacidade:** Implementação de aceitação obrigatória de política de privacidade antes do uso da aplicação.

## Dependências e Configuração
- **Versão SDK:** Mínimo 27, alvo 34.
- **Bibliotecas Principais:**
    - Room 2.6.1 para base de dados.
    - Google Maps SDK 18.2.0 para mapas.
    - PhotoView 2.3.0 para visualização de imagens.
- **Google Maps API:** Configurada com chave restrita ao package `pt.ipbeja.pi.piproject` e SHA-1 do certificado.

## Desenvolvimento e Testes
- **Teste de Usabilidade:** Guião atualizado com as tarefas, começando pela aceitação de política de privacidade, seguido de identificação, georreferenciação, gestão de base de dados, créditos e geração de PDF.
- **Correções Implementadas:** Resolução de problemas como mapa não carregando, geração de PDF com permissões adequadas, e atualização de ordens de insetos.


## Anexos
- Guião de Teste de Usabilidade (atualizado).
- Instruções para ativação da Google Maps API.
- Código fonte disponível em: https://github.com/EsterCaetano/Insetos-em-Ordem.git</content>
  <parameter name="filePath">C:\Users\ester\Desktop\PI-master-2026-02-23-jpb\PI-master-2026-02-23-jpb\Relatorio_Atualizado.md
- Abrir o projeto com o Android Studio
- Ao compilar o projeto, as dependências necessárias serão descarregadas pelo gradle


Como Extender
-------------
A definição da chave dicotómica encontra-se no ficheiro XML em `app/src/main/assets/chave.xml`. As imagens podem ser colocadas na mesma pasta, sendo que os respetivos caminhos referidos no XML serão procurados pela aplicação relativamente a esta pasta.

Durante a edição da chave, os caminhos relativos dos assets podem ser obtidos facilmente com o menu de contexto dos mesmos na opção `Copy Relative Path Ctrl+Alt+Shift+C`.

A chave segue um formato simples em que cada nó deve estar identificado por um código id unívoco.

Créditos
--------
- Programadores: Ester A. Caetano, Rafael Conceição Narciso, José Mauricio, Vasco Flores, José Rodrigues, alunos do Instituto Politécnico de Beja
- Professores Tutores: Isabel Sofia Brito e João Paulo Barros.
- Conteúdos do livro Insetos em Ordem, P. Garcia Pereira, E. Monteiro, F. Vala, e C. Luís
- Aplicação desenvolvida em colaboração com a Dra. Patrícia Garcia Pereira e pela equipa da Plataforma de Ciência Aberta

Vídeos dos testes de usabilidade da aplicação: [Aqui](https://ipbejapt.sharepoint.com/:f:/s/ProjetoInsectoemOrdem/IgCaTSzfPrsTQJmnUDXFFMMGAYpjTfWWvyDXJieKC_HduGs?e=uJJT8w)
