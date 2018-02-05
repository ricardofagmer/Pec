package com.pec.biosistemico.pec.dao.interfaces;

import java.util.List;

/**
 * Created by Ricardo on 08/05/2017.
 */

public interface SQLiteGenericDAO<T> {

    public static  final String table_name = "";

    public long salvar(T t) throws Exception;
    public void editar(T t) throws Exception;
    public void deletar(T t) throws Exception;
    public T buscar(long codigo);
    public List<T> bucarTodos();
}
