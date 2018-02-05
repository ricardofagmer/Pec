package com.pec.biosistemico.pec.rest;

/**
 * Created by Ricardo on 16/11/2016.
 */
import com.pec.biosistemico.pec.domain.ListGson;
import com.pec.biosistemico.pec.model.PaperTop;
import com.pec.biosistemico.pec.model.CheckList;
import com.pec.biosistemico.pec.model.Criatf;
import com.pec.biosistemico.pec.paperTop.financeiro.Produto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiInterface
{
    @GET("papertop/listaJson")
    Call<ListGson>listPaperTop(@Query("idGrupo") String grupo, @Query("idProp") int produtor);

    @GET("produto/lista")
    Call<ListGson>listProduto();

    @GET("produto/lista")
    Call<Boolean>enviarFinanceiro(@Body List<Produto> produtoList);

    @POST("papertop/enviarPaperTop")
    Call<String>enviarPaperTop(@Body List<PaperTop> paperTopList);

    @POST("checklist/enviarCheckList")
    Call<String>enviarCheckList(@Body List<CheckList> checkListList);

    @POST("criatf/enviarCriatf")
    Call<String>enviarCriatf(@Body List<Criatf> criatfList);
}
