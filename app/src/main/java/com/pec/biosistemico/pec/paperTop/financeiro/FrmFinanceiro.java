package com.pec.biosistemico.pec.paperTop.financeiro;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.dao.FinanceiroDAO;
import com.pec.biosistemico.pec.adapter.FinanceiroAdapter;
import com.pec.biosistemico.pec.util.Global;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class FrmFinanceiro extends DialogFragment implements OnClickListener{

	private EditText txtQtadade;
	private EditText txtValor;
	private Spinner ddlItem;
	private Button btnSalvar;
	private Button btnCancelar;
	private SQLiteDatabase db;
	private FinanceiroDAO produtoFinanceiroDAO;
	private RadioButton rdDespesa;
	private RadioButton rdReceita;
	private ListView lv;
	private Global mDados = Global.getInstance();
	private long posicao = 0;
	private TextView lbl;
	private String um;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_DeviceDefault_Light_Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.financeiro, null);

		db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);

		produtoFinanceiroDAO = new FinanceiroDAO(getActivity());

		btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
		btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
		txtQtadade = (EditText) view.findViewById(R.id.txtQtdade);
		txtValor = (EditText) view.findViewById(R.id.txtValor);
		ddlItem = (Spinner)view.findViewById(R.id.ddlItem);
		lv = (ListView)view.findViewById(R.id.lv);
		rdDespesa = (RadioButton)view.findViewById(R.id.rbDespesa);
		rdReceita = (RadioButton)view.findViewById(R.id.rbReceita);
		lbl = (TextView)view.findViewById(R.id.lblTitulo);

		ddlItem.setPrompt("SELECIONE UM ITEM");

		populaProduto("DESPESA");
		lbl.setText("Itens de Despesa");
		RefreshList();

		rdDespesa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				populaProduto("DESPESA");
				RefreshList();
				lbl.setText("Itens de Despesa");

			}
		});

		rdReceita.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				populaProduto("RECEITA");
				RefreshList();
				lbl.setText("Itens de Receita");
			}
		});

		ddlItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				TextView lblid = (TextView)view.findViewById(R.id.lblId);
				TextView um = (TextView)view.findViewById(R.id.lblUM);
				posicao = Integer.parseInt(lblid.getText().toString());

				txtQtadade.setHint("Quantidade em " + um.getText());

				if(um.getText().equals("KW")){

					txtQtadade.setText("1");
					txtQtadade.setEnabled(false);
					txtValor.setHint("R$ (valor total da fatura)");
				}
				else{
					txtQtadade.setText("");
					txtQtadade.setEnabled(true);
					txtValor.setHint("R$ (valor unitário)");

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		btnSalvar.setOnClickListener(new OnClickListener() {
			@SuppressLint({ "SimpleDateFormat", "ShowToast" })
			@Override
			public void onClick(View v) {

				ArrayList<Produto> produtos = new ArrayList<Produto>();
				List<Produto> produtoList = produtoFinanceiroDAO.getAllProdutoPorID(String.valueOf(posicao));

				String un = "";

				for(Produto produto : produtoList) {

					un = produto.getUnidadeMedida();

					if (txtQtadade.getText().toString().equals("") || txtValor.getText().toString().equals(""))
					{
						Toast.makeText(getActivity(), "Quantidade e Valor devem ser informados", Toast.LENGTH_LONG).show();
					}
					else
					{
							long _id = ddlItem.getSelectedItemId();
							int idProduto = produtoFinanceiroDAO.getIdProdutoPorID(_id);
							String tipo = "";

							if (rdReceita.isChecked())
							{
								tipo = "RECEITA";
							}
							else
							{
								tipo = "DESPESA";
							}
								produto.setDescricao(produtoFinanceiroDAO.getDescricaoPorID(posicao));
								produto.setValorUnitario(Double.parseDouble(txtValor.getText().toString()));
								produto.setQuantidade(Integer.parseInt(txtQtadade.getText().toString()));
								produto.set_projeto(mDados.getProjeto());
								produto.set_grupo(Integer.parseInt(mDados.getUsuario()));
								produto.set_propriedade(mDados.getProdutor());
								produto.setIdProduto(idProduto);
								produto.setTipo(tipo);
								produto.setEnviado("NAO");

								produtoFinanceiroDAO.inserirProduto(produto);
								CleanFields();
								Toast.makeText(getActivity(), "Inserido!", Toast.LENGTH_LONG).show();
								RefreshList();
					}
				}
			}
		});

		btnCancelar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dismiss();
			}
		});

		return view;
	}

	public void RefreshList() {

		String tipo ="";

		if (rdReceita.isChecked())
		{
			tipo = "RECEITA";
		}
		else
		{
			tipo = "DESPESA";
		}

		ArrayList<Produto> produtos = new ArrayList<Produto>();
		produtos.addAll(produtoFinanceiroDAO.getAllProdutoPorPropriedadeTipo(tipo,mDados.getProdutor()));

		FinanceiroAdapter financeiroAdapter ;
		financeiroAdapter = new FinanceiroAdapter(getActivity(),produtos);

		lv.setAdapter(financeiroAdapter);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

				TextView lbl_id = (TextView) view.findViewById(R.id.lblid);
				String idSelecionado = lbl_id.getText().toString();

				produtoFinanceiroDAO.deleteProduto(Integer.parseInt(idSelecionado));

				Toast.makeText(getActivity(),"Item excluido",Toast.LENGTH_LONG).show();

				RefreshList();
			}
		});

		}

	public void CleanFields(){
		
		txtValor.setText("");
		txtQtadade.setText("");
	}

	public void populaProduto(String tipo) {

		db.isOpen();

		Cursor cursor = db.rawQuery("SELECT DISTINCT _ID, IDPRODUTO, DESCRICAO,unidadeMedida FROM FINANCEIRO WHERE QUANTIDADE = 0 AND TIPO = '"+tipo+"' GROUP BY DESCRICAO ORDER BY DESCRICAO", null);
		String[] from = {"descricao", "_id", "idProduto","unidadeMedida"};
		int[] to = {R.id.lblNome, R.id.lblId, R.id.lbl_id_FK,R.id.lblUM};

		@SuppressWarnings("deprecation")
		SimpleCursorAdapter ad1 = new SimpleCursorAdapter(getActivity(),R.layout.spinner_left, cursor, from, to);
		ddlItem.setAdapter(ad1);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View arg0) {

	}

}
