# Simulador de Sistema de Arquivos em Memória (VFS)

Este projeto consiste na implementação de uma **Simulação de Gerenciamento de Arquivos** com interpretador de comandos, desenvolvido como parte do 2º Trabalho Prático da disciplina de Sistemas Operacionais do **Instituto Ferral**.

O objetivo é simular um sistema de arquivos estilo Unix/Linux operando inteiramente em memória (RAM), permitindo a criação, manipulação e navegação em uma estrutura hierárquica de diretórios e arquivos através de um terminal virtual.

## Arquitetura do Sistema

O sistema foi projetado utilizando Programação Orientada a Objetos (POO) em Java, adotando padrões de projeto como **Command Pattern** para a execução de operações e **Composite/Strategy** para a estrutura de dados dos arquivos.

### Como funciona o `Inode`?

Inspirado nos sistemas de arquivos Unix, a estrutura central deste projeto é o **Inode**.

No sistema real, um inode guarda os metadados, mas não o nome do arquivo. Neste simulador, seguimos uma lógica similar para separar a responsabilidade de "quem é o arquivo" de "o que o arquivo contém".

1. **Classe `Inode**`: Representa a entidade do sistema de arquivos. Ela armazena **apenas metadados**:
* `id`: Identificador único.
* `permissions`: Permissões de leitura/escrita/execução (rwx) para dono, grupo e outros.
* `owner`: Nome do proprietário do arquivo (ex: "root", "user").
* `dates`: Data de criação e última modificação.
* **Referência para Conteúdo**: Possui um objeto do tipo `Content`.


2. **Interface `Content**`: Define o comportamento genérico do conteúdo. Possui duas implementações:
* **`FileContent`**: Armazena o texto do arquivo em uma lista de strings (`List<String> lines`). Simula os blocos de dados de um arquivo.
* **`DirectoryContent`**: Armazena um mapa (`Map<String, Inode> entries`). Este mapa associa um **nome** (string) a um **Inode**. É assim que a árvore de diretórios é construída.



> **Nota:** Quando você executa um comando como `mkdir pasta`, o sistema cria um novo `Inode` contendo um `DirectoryContent` e adiciona uma entrada no mapa do diretório pai apontando para este novo Inode.

---

## Estrutura de Classes

### Camada Core (`src/core`)

| Classe | Descrição |
| --- | --- |
| **`Shell`** | Ponto de entrada (`main`). Inicializa o sistema, exibe o prompt colorido (`user@path >`) e processa o loop de entrada do usuário. |
| **`VirtualFileSystem`** | Inicializa a estrutura raiz (`/`). Cria as pastas padrão como `/home/user`, `/bin`, `/etc` ao iniciar o programa. |

### Camada de Comandos (`src/commands`)

| Classe | Descrição |
| --- | --- |
| **`Command`** (Interface) | Contrato que todos os comandos devem seguir. Possui o método `execute(args, context)`. |
| **`CommandParser`** | Funciona como um registro. Recebe a string digitada pelo usuário (ex: "mkdir teste"), identifica qual classe `Command` deve ser instanciada e delega a execução. |
| **`CommandContext`** | Mantém o estado da sessão do usuário: quem é o diretório atual (`currentDirectory`), a pilha de caminho para navegação e referência ao sistema de arquivos. |
| **`CommandException`** | Exceção personalizada para tratar erros de lógica (arquivo não encontrado, argumentos inválidos) sem derrubar o sistema. |

### Camada de Implementação (`src/commands/implementations`)

Contém a lógica específica de cada comando (ex: `LsCommand`, `CdCommand`, `TouchCommand`). Cada classe implementa a interface `Command`.

### Camada de Dados (`src/inode`)

* **`Inode`**: Fábrica e container de metadados.
* **`Permissions` / `PermissionBits**`: Lógica para tratar permissões octais (ex: 755) e conversão para símbolos (rwxr-xr-x).
* **`enums/ContentType`**: Enumeração simples (`FILE`, `DIRECTORY`).

---

## Comandos Implementados

Atualmente, o sistema suporta as seguintes operações baseadas na especificação do trabalho:

**Manipulação de Arquivos e Diretórios:**

* `mkdir`: Cria novos diretórios (suporta caminhos aninhados).
* `rmdir`: Remove diretórios (apenas se estiverem vazios).
* `touch`: Cria arquivos (atualmente força extensão `.txt` se não especificado).
* `rm`: Remove arquivos ou diretórios (recursivo lógico).
* `rename`: Renomeia arquivos ou diretórios no mesmo local.

**Visualização e Edição:**

* `ls`: Lista conteúdo (suporta flag `-l` para detalhes de permissões/tamanho/data).
* `tree`: Exibe a hierarquia de diretórios em formato de árvore visual.
* `cat`: Exibe o conteúdo de um arquivo.
* `head`: Exibe as primeiras N linhas de um arquivo.
* `tail`: Exibe as últimas N linhas de um arquivo.
* `echo`: Cria ou sobrescreve conteúdo (`>`) e adiciona ao final (`>>`).
* `wc`: Conta linhas, palavras e caracteres.

**Navegação e Sistema:**

* `cd`: Navega entre diretórios (suporta caminhos absolutos `/`, relativos `..` e nomes).
* `pwd`: Exibe o caminho absoluto atual.
* `chmod`: Altera permissões (modo octal).
* `chown`: Altera o proprietário do arquivo.
* `exit`: Encerra o simulador.

---

## Funcionalidades Faltantes (To-Do List)

De acordo com o documento de requisitos (`2ºTrabalhoPraticoSO - GerArquivos.pdf`), os seguintes comandos ainda precisam ser implementados para completar 100% da especificação:

### Busca e Filtragem

* [ ] **`find <diretorio> -name <nome>`**: Procurar arquivos ou diretórios recursivamente em uma hierarquia.


* [ ] **`grep <termo> <arquivo>`**: Procurar por uma palavra ou frase dentro do conteúdo de um arquivo.



### ℹInformações Detalhadas

* [ ] **`stat <nome>`**: Exibir informações completas (inode ID, links, datas detalhadas, blocos, etc).


* [ ] **`du <diretorio>`**: Exibir o tamanho total do diretório em bytes (somatório recursivo).



### Operações Avançadas

* [ ] **`cp <origem> <destino>`**: Copiar arquivos ou diretórios (requer clonagem profunda do conteúdo).


* [ ] **`mv <origem> <destino>`**: Mover arquivos ou diretórios para outra pasta (atualmente só temos `rename` que atua localmente).


* [ ] **`diff <arq1> <arq2>`**: Comparar dois arquivos linha a linha e exibir diferenças.



### Extras

* [ ] **`zip` / `unzip**`: Simulação de compactação (pode ser simplificada apenas agrupando nós).


* [ ] **`history`**: Exibir a lista dos últimos comandos digitados na sessão.



---

## Como Executar

1. **Pré-requisitos**: Java JDK 11 ou superior.
2. **Compilar**:
```bash
javac -d out -sourcepath src src/core/Shell.java

```


3. **Rodar**:
```bash
java -cp out core.Shell

```