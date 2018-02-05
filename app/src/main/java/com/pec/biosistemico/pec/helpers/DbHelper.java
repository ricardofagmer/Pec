package com.pec.biosistemico.pec.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

	public static final String TABLE_PAPERTOP = "PAPERPORT";
	public static final String TABLE_TEMP = "TEMP";
	public static final String TABLE_FINANCEIRO = "FINANCEIRO";
	public static final String TABLE_CRIATF = "CRIATF";
	public static final String COLUMN_ID = "_id";
	private static final String DATABASE_NAME = "IbsPEC.db";
	private static final int DATABASE_VERSION = 1;
	private static DbHelper instance;
	private Context mContext;
	protected SQLiteDatabase db;

	private Cursor cursor;

	public static synchronized DbHelper getHelper(Context context) {

		if (instance == null)
			instance = new DbHelper(context);
		return instance;
	}

	public void open() throws SQLException {

		if (instance == null)
			instance = DbHelper.getHelper(mContext);
		db = instance.getWritableDatabase();
	}

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.v("version ", "Version number is " + database.getVersion());
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(DbHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

		database.execSQL(updateTables());

		Log.w(DbHelper.class.getName(), "New Version updated: " + database.getVersion());
	}

	private String updateTables() {
		StringBuilder sql = new StringBuilder();

		return sql.toString();
	}


	public void createTables() {

		StringBuilder sqlVersion = new StringBuilder();
		sqlVersion.append("CREATE TABLE IF NOT EXISTS [versao](");
		sqlVersion.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sqlVersion.append("versao varchar(10)); ");
		db.execSQL(sqlVersion.toString());

		sqlVersion = new StringBuilder();
		sqlVersion.append("CREATE TABLE IF NOT EXISTS [financeiro](");
		sqlVersion.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sqlVersion.append("idProduto INTEGER,");
		sqlVersion.append("_projeto INTEGER,");
		sqlVersion.append("_grupo INTEGER,");
		sqlVersion.append("enviado varchar(5), ");
		sqlVersion.append("_propriedade INTEGER,");
		sqlVersion.append("quantidade INTEGER,");
		sqlVersion.append("descricao varchar(100), ");
		sqlVersion.append("unidadeMedida double, ");
		sqlVersion.append("valorUnitario double, ");
		sqlVersion.append("valorMaximo double, ");
		sqlVersion.append("tipo varchar(20),");
		sqlVersion.append("valorMinimo double); ");
		db.execSQL(sqlVersion.toString());

		StringBuilder sqlTouro = new StringBuilder();
		sqlTouro.append("CREATE TABLE IF NOT EXISTS [touro](");
		sqlTouro.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sqlTouro.append("id INTEGER,");
		sqlTouro.append("nome varchar(30), ");
		sqlTouro.append("sememDisponivel INTEGER, ");
		sqlTouro.append("brinco INTEGER, ");
		sqlTouro.append("rg varchar(30), ");
		sqlTouro.append("id_raca INTEGER, ");
		sqlTouro.append("raca varchar(30)); ");
		db.execSQL(sqlTouro.toString());

		StringBuilder sqlAtend01 = new StringBuilder();
		sqlAtend01.append("CREATE TABLE IF NOT EXISTS [criatf](");
		sqlAtend01.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sqlAtend01.append("id INTEGER, ");
		sqlAtend01.append("_projeto INTEGER, ");
		sqlAtend01.append("_grupo INTEGER, ");
		sqlAtend01.append("_propriedade INTEGER, ");
		sqlAtend01.append("id_criatf INTEGER,");
		sqlAtend01.append("criatf_pai INTEGER,");
		sqlAtend01.append("data_protocolo varchar(20), ");
		sqlAtend01.append("data_inseminacao varchar(20), ");
		sqlAtend01.append("data_d0 varchar(20), ");
		sqlAtend01.append("vacina varchar(10), ");
		sqlAtend01.append("_raca INTEGER, ");
		sqlAtend01.append("touro varchar(30), ");
		sqlAtend01.append("consultor varchar(20), ");
		sqlAtend01.append("enviado varchar(5), ");
		sqlAtend01.append("fez_d0 varchar(5), ");
		sqlAtend01.append("nome_vaca varchar(30), ");
		sqlAtend01.append("_id_animais INTEGER, ");
		sqlAtend01.append("id_reprodutivo INTEGER, ");
		sqlAtend01.append("id_cobertura INTEGER, ");
		sqlAtend01.append("processo_d8 varchar(10), ");
		sqlAtend01.append("processo_iatf varchar(10), ");
		sqlAtend01.append("fez_ia varchar(10), ");
		sqlAtend01.append("dg varchar(10), ");
		sqlAtend01.append("diagnostico varchar(10), ");
		sqlAtend01.append("numeroDias varchar(20), ");
		sqlAtend01.append("previsao_parto varchar(20), ");
		sqlAtend01.append("tipo_atendimento varchar(20), ");
		sqlAtend01.append("nova_ia varchar(5)); ");
		db.execSQL(sqlAtend01.toString());

		/*StringBuilder sqlAtend02 = new StringBuilder();
        sqlAtend02.append("CREATE TABLE IF NOT EXISTS [atendimento_02](");
		sqlAtend02.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sqlAtend02.append("id INTEGER, ");
		sqlAtend02.append("_projeto INTEGER, ");
		sqlAtend02.append("_grupo INTEGER, ");
		sqlAtend02.append("_propriedade INTEGER, ");
		sqlAtend02.append("data_atendimento varchar(20), ");
		sqlAtend02.append("id_vaca INTEGER, ");
		sqlAtend02.append("id_cobertura INTEGER, ");
		sqlAtend02.append("id_reprodutivo INTEGER, ");
		sqlAtend02.append("nome_vaca varchar(30), ");
		sqlAtend02.append("processo_d8 varchar(5), ");
		sqlAtend02.append("processo_iatf varchar(5), ");
		sqlAtend02.append("raca varchar(20), ");
		sqlAtend02.append("enviado varchar(5), ");
		sqlAtend02.append("dg varchar(20), ");
		sqlAtend02.append("consultor varchar(10), ");
		sqlAtend02.append("nome_touro varchar(30)); ");
	    db.execSQL(sqlAtend02.toString());

	    StringBuilder sqlAtend03 = new StringBuilder();
	    sqlAtend03.append("CREATE TABLE IF NOT EXISTS [atendimento_03](");
	    sqlAtend03.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sqlAtend03.append("id INTEGER, ");
		sqlAtend03.append("_projeto INTEGER, ");
		sqlAtend03.append("_grupo INTEGER, ");
		sqlAtend03.append("_propriedade INTEGER, ");
		sqlAtend03.append("data_atendimento varchar(20), ");
		sqlAtend03.append("nome_vaca varchar(30), ");
		sqlAtend03.append("enviado varchar(5), ");
		sqlAtend03.append("previsao_parto varchar(20), ");
		sqlAtend03.append("consultor varchar(20), ");
		sqlAtend03.append("diagnostico varchar(30)); ");
	    db.execSQL(sqlAtend03.toString());		*/

		StringBuilder sqlTemp = new StringBuilder();
		sqlTemp.append("CREATE TABLE IF NOT EXISTS [temp](");
		sqlTemp.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sqlTemp.append("id INTEGER, ");
		sqlTemp.append("nome varchar(10)); ");
		db.execSQL(sqlTemp.toString());

		StringBuilder sqlLogin = new StringBuilder();
		sqlLogin.append("CREATE TABLE IF NOT EXISTS [login](");
		sqlLogin.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sqlLogin.append("nome varchar(20), ");
		sqlLogin.append("crmv varchar(10), ");
		sqlLogin.append("usuario varchar(10));");
		db.execSQL(sqlLogin.toString());

		StringBuilder sqlCliente = new StringBuilder();
		sqlCliente.append("CREATE TABLE IF NOT EXISTS [cliente](");
		sqlCliente.append("[id] INTEGER PRIMARY KEY, ");
		sqlCliente.append("nome varchar(100));");
		db.execSQL(sqlCliente.toString());

		StringBuilder sqlEscritorio = new StringBuilder();
		sqlEscritorio.append("CREATE TABLE IF NOT EXISTS [escritorio](");
		sqlEscritorio.append("[id] INTEGER PRIMARY KEY , ");
		sqlEscritorio.append("nome varchar(100), ");
		sqlEscritorio.append("_cliente integer);");
		db.execSQL(sqlEscritorio.toString());

		StringBuilder sqlProjeto = new StringBuilder();
		sqlProjeto.append("CREATE TABLE IF NOT EXISTS [projeto](");
		sqlProjeto.append("[id] INTEGER PRIMARY KEY , ");
		sqlProjeto.append("nome varchar(100), ");
		sqlProjeto.append("_cliente integer,");
		sqlProjeto.append("_escritorio integer);");
		db.execSQL(sqlProjeto.toString());

		StringBuilder sqlGrupo = new StringBuilder();
		sqlGrupo.append("CREATE TABLE IF NOT EXISTS [grupo](");
		sqlGrupo.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sqlGrupo.append("[id] INTEGER , ");
		sqlGrupo.append("nome varchar(100), ");
		sqlGrupo.append("_projeto integer);");
		db.execSQL(sqlGrupo.toString());

		StringBuilder sqlProdutor = new StringBuilder();
		sqlProdutor.append("CREATE TABLE IF NOT EXISTS [produtor](");
		sqlProdutor.append("[_id] INTEGER PRIMARY KEY , ");
		sqlProdutor.append("[id] INTEGER  , ");
		sqlProdutor.append("nome varchar(100), ");
		sqlProdutor.append("longitude varchar(100), ");
		sqlProdutor.append("latidude varchar(100), ");
		sqlProdutor.append("_grupo integer);");
		db.execSQL(sqlProdutor.toString());

		StringBuilder sqlPaperPort = new StringBuilder();
		sqlPaperPort.append("CREATE TABLE IF NOT EXISTS [paperPort](");
		sqlPaperPort.append("[_id] INTEGER PRIMARY KEY , ");
		sqlPaperPort.append("[id] INTEGER  , ");
		sqlPaperPort.append("_projeto integer, ");
		sqlPaperPort.append("_grupo integer, ");
		sqlPaperPort.append("_propriedade integer, ");
		sqlPaperPort.append("id_criatf integer, ");
		sqlPaperPort.append("modificado varchar(3), ");
		sqlPaperPort.append("data_coleta varchar(20), ");
		sqlPaperPort.append("area_atual varchar(50), ");
		sqlPaperPort.append("brinco varchar(50), ");
		sqlPaperPort.append("enviado varchar(3), ");
		sqlPaperPort.append("nome_usual varchar(50), ");
		sqlPaperPort.append("dataCobertura varchar(20), ");
		sqlPaperPort.append("dataDiagnostico varchar(20), ");
		sqlPaperPort.append("dataParto varchar(20), ");
		sqlPaperPort.append("dataPesagem varchar(20), ");
		sqlPaperPort.append("diasPrenhez varchar(20), ");
		sqlPaperPort.append("fez_d0 varchar(5), ");
		sqlPaperPort.append("idCobertura integer, ");
		sqlPaperPort.append("idReprodutivo integer, ");
		sqlPaperPort.append("idAnimais integer, ");
		sqlPaperPort.append("numeroFetos integer, ");
		sqlPaperPort.append("numeroDias integer, ");
		sqlPaperPort.append("previsaoParto varchar(20), ");
		sqlPaperPort.append("categoria varchar(20), ");
		sqlPaperPort.append("previsaoSecagem varchar(20), ");
		sqlPaperPort.append("statusProdutivo varchar(20), ");
		sqlPaperPort.append("statusReprodutivo varchar(20), ");
		sqlPaperPort.append("manejo varchar(50), ");
		sqlPaperPort.append("peso_atual varchar(20), ");
		sqlPaperPort.append("tipoParto varchar(20), ");
		sqlPaperPort.append("status varchar(50), ");
		sqlPaperPort.append("data_secagem varchar(20), ");
		sqlPaperPort.append("obs varchar(100), ");
		sqlPaperPort.append("nome_projeto varchar(100), ");
		sqlPaperPort.append("nome_grupo varchar(100), ");
		sqlPaperPort.append("nome_propriedade varchar(100), ");
		sqlPaperPort.append("ultimo_parto varchar(20), ");
		sqlPaperPort.append("ultimo_peso varchar(20), ");
		sqlPaperPort.append("ocorrencia varchar(20), ");
		sqlPaperPort.append("referencia varchar(20));");
		db.execSQL(sqlPaperPort.toString());

		// db.execSQL("DROP TABLE CHECKLIST");

		StringBuilder sqlCheckList = new StringBuilder();
		sqlCheckList.append("CREATE TABLE IF NOT EXISTS [checklist](");
		sqlCheckList.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sqlCheckList.append("[id] INTEGER , ");
		sqlCheckList.append("_cliente integer, ");
		sqlCheckList.append("_projeto integer, ");
		sqlCheckList.append("_grupo integer, ");
		sqlCheckList.append("_produtor varchar(100), ");
		sqlCheckList.append("data varchar(20), ");
		sqlCheckList.append("enviado varchar(3), ");
		sqlCheckList.append("controle1 varchar(10), ");
		sqlCheckList.append("controle2 varchar(10), ");
		sqlCheckList.append("controle3 varchar(10), ");
		sqlCheckList.append("manejoN1 varchar(10), ");
		sqlCheckList.append("manejoN2 varchar(10), ");
		sqlCheckList.append("manejoN3 varchar(10), ");
		sqlCheckList.append("manejoN4 varchar(10), ");
		sqlCheckList.append("manejoN5 varchar(10), ");
		sqlCheckList.append("manejoN6 varchar(10), ");
		sqlCheckList.append("sanidade1 varchar(10), ");
		sqlCheckList.append("sanidade2 varchar(10), ");
		sqlCheckList.append("sanidade3 varchar(10), ");
		sqlCheckList.append("sanidade4 varchar(10), ");
		sqlCheckList.append("sanidade5 varchar(10), ");
		sqlCheckList.append("manejoR1 varchar(10), ");
		sqlCheckList.append("manejoR2 varchar(10), ");
		sqlCheckList.append("manejoR3 varchar(10), ");
		sqlCheckList.append("manejoR4 varchar(10), ");
		sqlCheckList.append("qualidade1 varchar(10), ");
		sqlCheckList.append("qualidade2 varchar(10), ");
		sqlCheckList.append("qualidade3 varchar(10), ");
		sqlCheckList.append("consultor varchar(20), ");
		sqlCheckList.append("tipoReprodutivo varchar(20), ");
		sqlCheckList.append("situacaoEncontrada varchar(999), ");
		sqlCheckList.append("recomendacoes varchar(999), ");
		sqlCheckList.append("qualidade4 varchar(10));");
		db.execSQL(sqlCheckList.toString());

		StringBuilder sqlReport = new StringBuilder();
		sqlReport.append("CREATE TABLE IF NOT EXISTS [report](");
		sqlReport.append("[_id] INTEGER PRIMARY KEY , ");
		sqlReport.append("[id] INTEGER  , ");
		sqlReport.append("_cliente integer, ");
		sqlReport.append("_projeto integer, ");
		sqlReport.append("_grupo integer, ");
		sqlReport.append("_produtor varchar(100), ");
		sqlReport.append("data varchar(20), ");
		sqlReport.append("consultor varchar(20), ");
		sqlReport.append("enviado varchar(3), ");
		sqlReport.append("tipoReprodutivo varchar(100), ");
		sqlReport.append("situacaoEncontrada varchar(2000), ");
		sqlReport.append("caminho_imagem varchar(100), ");
		sqlReport.append("recomedacoes varchar(2000));");
		db.execSQL(sqlReport.toString());

		StringBuilder sqlAnimal = new StringBuilder();
		sqlAnimal.append("CREATE TABLE IF NOT EXISTS [animal](");
		sqlAnimal.append("[_id] INTEGER PRIMARY KEY , ");
		sqlAnimal.append("[id] INTEGER  , ");
		sqlAnimal.append("_cliente integer, ");
		sqlAnimal.append("_projeto integer, ");
		sqlAnimal.append("_grupo integer, ");
		sqlAnimal.append("_produtor varchar(100), ");
		sqlAnimal.append("data varchar(20), ");
		sqlAnimal.append("consultor varchar(20), ");
		sqlAnimal.append("enviado varchar(3), ");
		sqlAnimal.append("status varchar(6), ");
		sqlAnimal.append("brinco varchar(100), ");
		sqlAnimal.append("nome varchar(50), ");
		sqlAnimal.append("numero_partos varchar(10), ");
		sqlAnimal.append("sexo varchar(10), ");
		sqlAnimal.append("origem_entrada varchar(50), ");
		sqlAnimal.append("origem_entrada_p INTEGER, ");
		sqlAnimal.append("raca varchar(50), ");
		sqlAnimal.append("categoria varchar(50), ");
		sqlAnimal.append("pelagem varchar(50), ");
		sqlAnimal.append("entrada_plantel varchar(50), ");
		sqlAnimal.append("ultimo_parto varchar(20), ");
		sqlAnimal.append("data_nascimento varchar(20));");
		db.execSQL(sqlAnimal.toString());

		StringBuilder sqlCategoria = new StringBuilder();
		sqlCategoria.append("CREATE TABLE IF NOT EXISTS [categoria](");
		sqlCategoria.append("[_id] INTEGER PRIMARY KEY , ");
		sqlCategoria.append("[id] INTEGER  , ");
		sqlCategoria.append("nome varchar(50));");
		db.execSQL(sqlCategoria.toString());

		StringBuilder sqlRaca = new StringBuilder();
		sqlRaca.append("CREATE TABLE IF NOT EXISTS [raca](");
		sqlRaca.append("[_id] INTEGER PRIMARY KEY , ");
		sqlRaca.append("[id] INTEGER  , ");
		sqlRaca.append("nome varchar(50));");
		db.execSQL(sqlRaca.toString());

		Cursor cursor1 = db.rawQuery("SELECT COUNT(*)as TOTAL FROM cliente WHERE id = 9999", null);
		cursor1.moveToFirst();
		int count1 = cursor1.getInt(cursor1.getColumnIndex("TOTAL"));

		if (count1 == 0) {

			db.execSQL("INSERT INTO cliente   (id,nome) VALUES (9999,'-SELECIONE O CLIENTE-')");
			db.execSQL("INSERT INTO projeto   (id,nome) VALUES (9999,'-SELECIONE O PROJETO-')");
			db.execSQL("INSERT INTO grupo     (id,nome) VALUES (9999,'-SELECIONE O GRUPO-')");
			db.execSQL("INSERT INTO produtor  (id,nome) VALUES (9999,'-SELECIONE O PRODUTOR-')");

			db.execSQL("INSERT INTO versao (versao) VALUES ('2')");


		}

		cursor1 = db.rawQuery("SELECT COUNT(*)as TOTAL FROM CRIATF WHERE _ID = 1", null);
		cursor1.moveToFirst();
		int count = cursor1.getInt(cursor1.getColumnIndex("TOTAL"));

		if (count == 0) {

			db.execSQL("INSERT INTO CRIATF (_id,nome_vaca) VALUES (1,'-SELECIONE O ANIMAL-')");


		}


		cursor1 = db.rawQuery("SELECT COUNT(*)as TOTAL FROM categoria", null);
		cursor1.moveToFirst();
		count = cursor1.getInt(cursor1.getColumnIndex("TOTAL"));

		if (count == 0) {

			db.execSQL("INSERT INTO categoria  (id,nome) VALUES (1,'VACA')");
			db.execSQL("INSERT INTO categoria  (id,nome) VALUES (2,'TOURO')");
			//db.execSQL("INSERT INTO categoria  (id,nome) VALUES (3,'BEZERRA')");
			//db.execSQL("INSERT INTO categoria  (id,nome) VALUES (4,'BEZERRO')");
            /*db.execSQL("INSERT INTO categoria  (id,nome) VALUES (5,'BZA DESMAMA')");
			db.execSQL("INSERT INTO categoria  (id,nome) VALUES (6,'BZP DESMAMA')");
			db.execSQL("INSERT INTO categoria  (id,nome) VALUES (7,'NOVILHA')");
			db.execSQL("INSERT INTO categoria  (id,nome) VALUES (8,'GARROTE')");
			db.execSQL("INSERT INTO categoria  (id,nome) VALUES (9,'FEMEA RECRIA')");*/
			db.execSQL("INSERT INTO categoria  (id,nome) VALUES (11,'BOI')");

			db.execSQL("INSERT INTO raca  (id,nome) VALUES (1,'SEM RAÇA DEFINIDA')");
			db.execSQL("INSERT INTO raca  (id,nome) VALUES (2,'NELORE')");
			db.execSQL("INSERT INTO raca  (id,nome) VALUES (3,'CRUZAMENTO INDUSTRIAL')");
			db.execSQL("INSERT INTO raca  (id,nome) VALUES (4,'CHAROLAIS')");
			db.execSQL("INSERT INTO raca  (id,nome) VALUES (5,'ANGUS')");
			db.execSQL("INSERT INTO raca  (id,nome) VALUES (6,'HOLANDES')");

		}

	}




	}
