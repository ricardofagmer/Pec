package com.pec.biosistemico.pec.criatf;

import com.pec.biosistemico.pec.R;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

@SuppressLint("ValidFragment")
public class ListaSemes extends DialogFragment implements
		DialogInterface.OnClickListener {

	private int numStyle;
	private int numTheme;

	@SuppressLint("ValidFragment")
	public ListaSemes(int numStyle, int numTheme) {

		this.numStyle = numStyle;
		this.numTheme = numTheme;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int style;
		int theme;

		switch (numStyle) {

			case 1:
				style = DialogFragment.STYLE_NO_TITLE;
				break;
			case 2:
				style = DialogFragment.STYLE_NO_INPUT;
				break;
			case 3:
				style = DialogFragment.STYLE_NO_FRAME;
				break;
			default:
				style = DialogFragment.STYLE_NORMAL;
				break;

		}

		switch (numTheme) {
			case 1:
				theme = android.R.style.Theme_DeviceDefault_Light_Dialog;
				break;
			case 2:
				theme = android.R.style.Theme_Holo_Dialog;
				break;
			default:
				theme = android.R.style.Theme_Holo_Light_DarkActionBar;
				break;
		}

		setStyle(style, theme);
		setCancelable(false);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
				Context.MODE_PRIVATE, null);

		View view = inflater.inflate(R.layout.listar_semem, container);

		final ListView lstSemem = (ListView) view.findViewById(R.id.lstSemem);
		final Button btnFechar = (Button) view.findViewById(R.id.btnCancelar);

		Cursor cursor1 = db
				.rawQuery("SELECT t._id, t.id, t.nome, t.raca , count(t.nome) as total FROM CRIATF c, TOURO t WHERE  c.touro = t.id  AND c.fez_ia = ''   group by nome",null);

		String[] from = {"raca", "nome", "total" };

		int[] to = {R.id.lblRaca, R.id.lblNome, R.id.lblQtdade };

		@SuppressWarnings("deprecation")
		SimpleCursorAdapter ad1 = new SimpleCursorAdapter(getActivity(),
				R.layout.semem_list, cursor1, from, to);

		lstSemem.setAdapter(ad1);

		btnFechar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dismiss();
			}
		});

		return view;

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	}

}
