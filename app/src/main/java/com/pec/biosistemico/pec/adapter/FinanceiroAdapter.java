package com.pec.biosistemico.pec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.helpers.DbHelper;
import com.pec.biosistemico.pec.paperTop.Vaca;
import com.pec.biosistemico.pec.paperTop.financeiro.Produto;

import java.util.ArrayList;

public class FinanceiroAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Produto> lista;
    private String modificado;
    private  int red = R.color.ligthred;

    public FinanceiroAdapter(Context context, ArrayList<Produto> lista) {
        this.context = context;
        this.lista = lista;
    }


    @Override
    public int getCount() {

        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Produto produto = lista.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.lista_despesas, null);

        TextView lbl_id = (TextView) layout.findViewById(R.id.lblid);
        lbl_id.setText(String.valueOf(produto.get_id()));

        TextView lblNome = (TextView)layout.findViewById(R.id.lblDescricao);
        lblNome.setText(produto.getDescricao());

        return layout;
    }
}
