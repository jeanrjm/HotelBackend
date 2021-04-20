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
-PARA FAZER CADASTRO DE HOSPEDE:                    localhost:8080/cadastrar
                                                    {
                                                        "nome":"Fulano da Silva",
                                                        "documento":"123456",
                                                        "telefone":"9925-2211"
                                                    }
                                                    
-PARA FAZER CHECKIN:                                localhost:8080/checkin
                                                      {
                                                              "hospede": {
                                                                  "nome":"Fulano da Silva",
                                                                  "documento":"123456",
                                                                  "telefone":"9925-2211"
                                                              },
                                                              "dataEntrada": "2018-03-14T08:00:00",
                                                              "dataSaida": "2018-03-16T10:17:00",
                                                              "adicionalVeiculo": "false"
                                                       }


-PARA CONSULTAR HOSPEDES ATUAIS:                    localhost:8080/hospedes/atuais
-PARA CONSULTAR HOSPEDES ÑÃO HOSPEDADOS ATUALMENTE: localhost:8080/hospedes/naohospedados


