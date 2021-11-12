package com.mishasho.tienda.moda.search;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.Util.Constants;
import com.mishasho.tienda.moda.model.ProductCategory;
import com.mishasho.tienda.moda.model.ProductSize;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFiltreActivity extends DialogFragment {


    // private ProductDialogListener productDialogListener;

    Context context;
    ArrayList<ProductCategory> categoryList;
    TextView categoryName;

    public Button btnDialogOk;
    // public FiltreDialogListener filtreDialogListener;
    private ArrayList<ProductSize> productSizeList;

    private Spinner spinner;
    public ArrayList<String> tallas;

    private CircleImageView img_relevantes;
    private CircleImageView img_menor_precio;
    private CircleImageView img_mayor_precio;

    private DialogSearchFragment dialogSearchFragment;


    EditText et_precio;
    EditText et_hasta;
    private String item;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = (Context) activity;
        // productDialogListener = (ProductDialogListener) activity;
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dialogSearchFragment = (DialogSearchFragment) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement DialogSinglePickerFragment");
        }
    }

    public static final String CATEGORIA_ARRAYLIST = "categoriayArrayList";

    public static SearchFiltreActivity newInstance() {
        Bundle args = new Bundle();
      //  args.putParcelableArrayList(CATEGORIA_ARRAYLIST, categoryList);
        SearchFiltreActivity fragment = new SearchFiltreActivity();
        fragment.setArguments(args);
        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search_product_dialog, container, false);

        //categoryList = getArguments().getParcelableArrayList(CATEGORIA_ARRAYLIST);

        btnDialogOk = rootView.findViewById(R.id.btn_aceptar_ok);
        btnDialogOk.setOnClickListener(btnFiltreDialogCencelListener);
        spinner = rootView.findViewById(R.id.sp_tallas);
        CargarProductSize();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(parent.getItemAtPosition(position).equals("[Selecionar]")){
                        item = parent.getItemAtPosition(position).toString();
                    }else {
                         item = parent.getItemAtPosition(position).toString();
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
        img_relevantes = rootView.findViewById(R.id.mas_relevantes);
        img_relevantes.setOnClickListener(nuevo);
        img_menor_precio = rootView.findViewById(R.id.menor_precio);
        img_menor_precio.setOnClickListener(nuevo);
        img_mayor_precio = rootView.findViewById(R.id.mayor_precio);
        img_mayor_precio.setOnClickListener(nuevo);

        et_precio = rootView.findViewById(R.id.et_precio);
        et_hasta = rootView.findViewById(R.id.et_hasta);

        ImageView btnclose=  rootView.findViewById(R.id.btn_close);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });




        return rootView;

    }



    /*START OF DIALOG BUTTON CANCEL LISTENER*/
    private Button.OnClickListener btnFiltreDialogCencelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(et_precio.getText().toString().length() > 0 && et_hasta.getText().toString().length() > 0 ) {
                int num1 = Integer.parseInt(et_precio.getText().toString());
                int num2 = Integer.parseInt(et_hasta.getText().toString());
                if( num1 <= num2){
                    dialogSearchFragment.getRango(num1,num2);
                    dismiss();
                }else {
                    Toast.makeText(context, "El precio 2 es menor" , Toast.LENGTH_LONG).show();
                }
            }else if(item.equals("[Selecionar]")) {
                Toast.makeText(context, "Selecionar Talla" , Toast.LENGTH_LONG).show();
            }else {
                dialogSearchFragment.getTallas(item);
                dismiss();

            }


        }
    };


    private void CargarProductSize(){
        String JSON_URL = Constants.PRODUCT_SIZE;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<List<ProductSize>>() {
                }.getType();

                productSizeList = new Gson().fromJson(response, listType);
                tallas = new ArrayList<>();
                for (ProductSize pImg : productSizeList) {
                    tallas.add(pImg.getSize_name());
                }
                tallas.add(0,"[Selecionar]");
               ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, tallas);
               spinner.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);
    }


    private CircleImageView.OnClickListener nuevo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mas_relevantes:
                    String relevantes="relevantes";
                    dialogSearchFragment.getRelevantes(relevantes);
                    dismiss();
                    break;
                case R.id.menor_precio:
                    String menoramayor="menoramayor";
                    dialogSearchFragment.getMenorMayor(menoramayor);
                    dismiss();
                    break;
                case R.id.mayor_precio:
                    String mayoramenor="mayoramenor";
                    dialogSearchFragment.getMayorMenor(mayoramenor);
                    dismiss();
                    break;
                default:
                    break;
            }

        }
    };


}

