# Relatório Atualizado do Projeto "Insetos em Ordem"

## Resumo do Projeto
Este projeto desenvolveu duas aplicações Android para identificação de insetos e rochas através de chaves dicotómicas, tornando o acesso a ferramentas científicas acessíveis ao público geral. As aplicações foram implementadas em Android Studio com Kotlin, utilizando Room para persistência de dados e Google Maps para georreferenciação.

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
- **Teste de Usabilidade:** Guião atualizado com 6 tarefas, começando pela aceitação de política de privacidade, seguido de identificação, georreferenciação, gestão de base de dados, créditos e geração de PDF.
- **Correções Implementadas:** Resolução de problemas como mapa não carregando, geração de PDF com permissões adequadas, e atualização de ordens de insetos.

## Créditos
- Programadores: Ester A. Caetano, Rafael Conceição Narciso, José Mauricio, Vasco Flores, José Rodrigues.
- Professores Tutores: João Paulo Barros, Sofia Soares.
- Conteúdos: Livro "Insetos em Ordem" de P. Garcia Pereira et al., e colaboração com Plataforma de Ciência Aberta.

## Anexos
- Guião de Teste de Usabilidade (atualizado).
- Instruções para ativação da Google Maps API.
- Código fonte disponível em: https://github.com/NarcisoUNK/PI</content>
<parameter name="filePath">C:\Users\ester\Desktop\PI-master-2026-02-23-jpb\PI-master-2026-02-23-jpb\Relatorio_Atualizado.md
