# HotelBackend

BASE DE DADOS
Instale PostgreSQL versão 10.16-1.
Com o arquivo "init.sql" construa a base de dados usando o software pgAdmin 4(já incluso na instalação).

BUILD
Para fazer o build instale Maven versão 3.6.3;
Instale JDK8;
Execute usando CMD(CommandPrompter) o comando 'mvn package', especificando o mesmo diretorio onde se encontra o arquivo pom.xml.
Após o build execute o jar 'HotelJ-0.0.1-SNAPSHOT' encontrado na pasta target, para iniciar o servidor.

ENDPOINTS 
-PARA FAZER CADASTRO DE HOSPEDE:                    localhost:8080/cadastrar?nome=joao&doc=8989&tel=990
-PARA FAZER CHECKIN:                                localhost:8080/checkin?nome=joao&doc=8989&tel=990&entrada=2021-03-28T10:23:54&saida=2021-03-31T18:23:54&veiculo=true
-PARA CONSULTAR HOSPEDES ATUAIS:                    localhost:8080/hospedes/atuais
-PARA CONSULTAR HOSPEDES ÑÃO HOSPEDADOS ATUALMENTE: localhost:8080/hospedes/naohospedados
