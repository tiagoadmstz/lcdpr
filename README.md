# LCDPR - Livro Caixa Digital do Produtor Rural
Versão do Leiaute 1.3
Período: Ano-Calendário e Situações Especiais a partir de 2019
Manual: Ato Declaratório Copes n° 1/2020

Este é um projeto que visa facitar a digitação dos dados e geração do arquivo LCDPR.

O Livro Caixa Digital do Produtor Rural (LCDPR) foi instituído pela Instrução NormativaRFB no1.848,
de 28 de novembro de 2018, que alterou a Instrução Normativa SRF nº 83, de 11 de outubro de 2001.
O resultado da exploração da atividade rural no Brasil deverá ser apurado mediante escrituração do LCDPR,
que deverá abranger as receitas, as despesas de custeio, os investimentos e demais valores que integram
a atividade desenvolvida no país.O produtor rural que auferir, durante o ano, receita bruta total da atividade
rural superior ao limite estabelecido, deverá entregar arquivo digital que contém o LCDPR até o final do
prazo de entrega da declaração do Imposto sobre a Renda da Pessoa Física referente ao respectivo ano-calendário.
Observa-se que o limite de receita bruta deve abranger todas asunidadesrurais exploradas pelo contribuinte,
de modo a permitir a apuração do resultado  da  atividade  ruralque  deverá  ser  informado  em  campo  próprio
na  ficha Atividade Rural  da Declaração de Ajuste  Anual  do  Imposto de Renda da Pessoa Física (DIRPF).
Os dados da atividade rural desenvolvida no exterior devem ser informados diretamente no campo Atividade Rural
da DIRPF, não sendo lançados no LCDPR do ano-calendário de 2019.O LCDPR deverá ser assinado digitalmente,
por meio de certificado digital válido, emitido por entidade credenciada pela Infraestrutura de 
Chaves Públicas Brasileira (ICP-Brasil), a fim de garantir a autoria do documento digital.

Exemplo gerado em teste:
```
0000|LCDPR|0013|11111111191|JOSÉ DA SILVA|0|0|01012019|01012019
0010|1
0030|RUA TESTE|1234|BLOCO Z SALA 301|BAIRRO LCDPR|DF|5300108|71000000|6133333333|testeLCDPR@LCDPR.com.br
0040|001|BR|BRL|12345678|12345678901234|12345678901234|Fazenda Tudo Certo|Rodovia BR 999, Km 3000|||Distrito do Meio|DF|5300108|71000000|2|05000
0045|002|3|12345678912|JOÃO DE SOUSA|00520
0050|001|BR|999|Banco LCDPR|1234|0000000123456789
Q100|02012019|001|001|2|2|Venda de 100 sacas de milho|12345678912|3|1000000|000|1100000|P
Q100|02012019|001|001|3|1|Pagamento de sementes e outros insumos|12345678000121|2|000|500000|600000|P
Q200|012019|10000000|8500000|1500000|P
Q200|022019|7000000|6000000|2500000|P
9999|JOSE DE SOUZA|12345678912|AL123456O|testeLCDPR@LCDPR.com.br|6133333333|8007
```
Saída no formato json 
````json
{
  "bloco_0":{
    "identificacao_pessoa_fisica":{
      "reg":"0000",
      "nome_esc":"LCDPR",
      "cod_ver":"0013",
      "cpf":11111111191,
      "nome":"JOSÉ DA SILVA",
      "ind_sit_ini_per":0,
      "sit_especial":"0",
      "dt_ini":"01012019",
      "dt_fin":"01012019"
    },
    "parametros_tributacao":{
      "reg":"0010",
      "forma_apur":1
    },
    "dados_cadastrais_contribuinte":{
      "reg":"0030",
      "endereco":"RUA TESTE",
      "num":"1234",
      "compl":"BLOCO Z SALA 301",
      "bairro":"BAIRRO LCDPR",
      "uf":"DF",
      "cod_mun":"5300108",
      "cep":"71000000",
      "num_tel":6133333333,
      "email":"testeLCDPR@LCDPR.com.br"
    },
    "imoveis_rurais":[{ 
      "reg":"0040",
      "cod_imovel":"001",
      "pais":"BR",
      "moeda":"BRL",
      "cad_ITR":"12345678",
      "CAEPF":"12345678901234",
      "inscr_estadual":"12345678901234",
      "nome_imovel":"Fazenda Tudo Certo",
      "endereco":"Rodovia BR 999, Km 3000",
      "num":"",
      "compl":"", 
      "bairro":"Distrito do Meio",
      "uf":"DF","cod_mun":"5300108",
      "cep":"71000000",
      "tipo_exploracao":2,
      "participacao":"05000"
    }],
    "exploracao_imoveis_rurais":[{
      "reg":"0045",
      "cod_imovel":"002",
      "tipo_contraparte":"3",
      "id_contraparte":"12345678912",
      "nome_contraparte":"JOÃO DE SOUSA",
      "perc_contraparte":"00520"
    }],
    "contas_bancarias":[{
      "reg":"0050",
      "cod_conta":"001",
      "pais_cta":"BR",
      "banco":"999",
      "nome_banco":"Banco LCDPR",
      "agencia":"1234","num_conta":"0000000123456789"
    }]},
  "bloco_Q":{
    "demonstrativo_livro_caixa":[{
      "reg":"Q100",
      "data": "02012019",
      "cod_imovel":"001",
      "cod_conta":"001",
      "num_doc":"2",
      "tipo_doc":2,
      "hist":"Venda de 100 sacas de milho",
      "id_partic":"12345678912",
      "tipo_lanc":3,
      "vl_entrada":"1000000",
      "vl_saida":"000",
      "sld_fin":"1100000",
      "nat_sld_fin":"P"
    },{
      "reg":"Q100",
      "data":"02012019",
      "cod_imovel":"001",
      "cod_conta":"001",
      "num_doc":"3",
      "tipo_doc":1,
      "hist":"Pagamento de sementes e outros insumos",
      "id_partic":"12345678000121",
      "tipo_lanc":2,
      "vl_entrada":"000",
      "vl_saida":"500000",
      "sld_fin":"600000",
      "nat_sld_fin":"P"
    }],
    "resumo_demonstrativo_livro_caixa":[{
      "reg":"Q200",
      "mes":"012019",
      "vl_entrada":"10000000",
      "vl_saida":"8500000",
      "sld_fin":"1500000",
      "nat_sld_fin":"P"
    },{
      "reg":"Q200",
      "mes":"022019",
      "vl_entrada":"7000000",
      "vl_saida":"6000000",
      "sld_fin":"2500000",
      "nat_sld_fin":"P"
    }]},
  "bloco_9":{
    "reg":"9999",
    "ident_nom":"JOSE DE SOUZA",
    "ident_cpf_cnpj":12345678912,
    "ind_crc":"AL123456O",
    "email":"testeLCDPR@LCDPR.com.br",
    "fone":6133333333,
    "qtd_lin":8007
  }
}
````

