# Bora Pagar Backend

## Criando Migrations
Para criar uma nova migration, execute o seguinte commando na pasta `/backend`:

```sh
./createMigration <NOME_DA_MIGRATION>
```

Depois disso você está livre para alterar o `.sql` gerado.

## Rodando o projeto
Você precisa ter o maven instalado. Para rodar o projeto basta executar:
```sh
mvn spring-boot:run
```

## Padrões do projeto
- Todas as entidades devem ter testes.
- Todas as funções devem ser documentadas com o javadoc.
- Lembrar de rodar `mvn spotless:apply` antes de fazer commit para formatar os arquivos.

## Checando formatação
Crie o arquivo `./git/hooks/pre-commit`. Se necessário, dê permissões de execução (`chmod -x ./git/hooks/pre-commit`).
Adicione o seguinte conteúdo neste arquivo:
```sh
#!/usr/bin/sh
# Run only if had changed at backend dir
if ! git diff --cached --quiet -- backend/; then
    # If any command fails, exit immediately with that command's exit status
    set -e pipefail
    cd $(git rev-parse --show-toplevel)/backend
    exec mvn spotless:check
fi
```
Este script checa se você rodou o `mvn spotless:apply` e cancela o commit caso você não o tenha feito.
Lembre-se que depois de rodar o spotless você deve preparar as mudanças com o `git add`