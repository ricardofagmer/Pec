package com.pec.biosistemico.pec.paperTop;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.util.Global;

import javax.mail.Quota;

import static com.pec.biosistemico.pec.R.*;

/**
 * Created by ricardo on 02/04/2016.
 */
public class VacaAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Vaca> lista;
    private String modificado;
    private  int red = color.ligthred;
    private Global mDados = Global.getInstance();

    public VacaAdapter(Context context, ArrayList<Vaca> lista) {
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

        Vaca vaca = lista.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.model_listar, null);

        TextView lbl_id = (TextView) layout.findViewById(id.lbl_id);
        lbl_id.setText(vaca.get_id());

        TextView nome_usual = (TextView) layout.findViewById(id.nomeusual);
        nome_usual.setText(vaca.getNome_usual());

        TextView brinco = (TextView) layout.findViewById(id.lbl_Brinco);
        brinco.setText(vaca.getBrinco());

        TextView status = (TextView) layout.findViewById(id.lbl_Status);
        status.setText(vaca.getStatus());

        TextView data_secagem = (TextView) layout.findViewById(id.lblDataSecagem);
        data_secagem.setText(vaca.getData_secagem());

        TextView cat_atual = (TextView) layout.findViewById(id.catAtual);
        cat_atual.setText(vaca.getCat_atual());

        TextView peso_atual = (TextView) layout.findViewById(id.PesoAtual);
        peso_atual.setText(vaca.getPeso_atual());

        TextView previsao_parto = (TextView) layout.findViewById(id.lblPrevisao);
        previsao_parto.setText(vaca.getPrevisao_parto());

        TextView ocorrencio = (TextView) layout.findViewById(id.manejo);
        ocorrencio.setText(vaca.getOcorrencia());

        TextView numero_dias = (TextView) layout.findViewById(id.nDias);
        numero_dias.setText(vaca.getNumero_dias());

        TextView ultimo_parto = (TextView) layout.findViewById(id.ultimo_parto);
        ultimo_parto.setText(vaca.getUltimo_parto());

        TextView modificado = (TextView) layout.findViewById(id.modificado);
        modificado.setText(vaca.getModificado());

        TextView manejo = (TextView)layout.findViewById(R.id.lbl_menejo);
        manejo.setText(vaca.getManejo());

        if(vaca.getVerifica() != null)
        {
            layout.setBackgroundResource(color.md_green_50);
        }

        if(manejo.getText().equals("VERIFICAR PARTO. ")){

            manejo.setText(vaca.getManejo());
            layout.setBackgroundResource(color.md_yellow_200);
        }

        if (modificado.getText().toString().equals("SIM")) {

            layout.setBackgroundResource(color.back_spinner);
        }

        return layout;
    }
}
