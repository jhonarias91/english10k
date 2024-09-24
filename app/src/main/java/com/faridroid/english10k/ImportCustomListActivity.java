package com.faridroid.english10k;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.faridroid.english10k.data.dto.CustomListDTO;
import com.faridroid.english10k.data.dto.CustomWordDTO;
import com.faridroid.english10k.data.dto.UserDTO;
import com.faridroid.english10k.view.viewmodel.CategoryViewModel;
import com.faridroid.english10k.view.viewmodel.CustomListViewModel;
import com.faridroid.english10k.view.viewmodel.CustomWordViewModel;
import com.faridroid.english10k.view.viewmodel.UserViewModel;

import java.util.UUID;

public class ImportCustomListActivity extends AppCompatActivity {
    private EditText etListName;
    private EditText etSharedContent;
    private Button btnCreateList;

    private CustomWordViewModel customWordViewModel;
    private CustomListViewModel customListViewModel;
    private CategoryViewModel categoryViewModel;
    private UserViewModel userViewModel;
    private UserDTO user;
    private String defaultCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_custom_list);

        // Inicializar los elementos de la UI
        etListName = findViewById(R.id.et_list_name);
        etSharedContent = findViewById(R.id.et_shared_content);
        btnCreateList = findViewById(R.id.btn_create_list);

        // Inicializar los ViewModel
        customWordViewModel = new ViewModelProvider(this).get(CustomWordViewModel.class);
        customListViewModel = new ViewModelProvider(this).get(CustomListViewModel.class);
        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(UserViewModel.class);

        categoryViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(CategoryViewModel.class);

        getUserAdCategory();
        // Manejar la recepción del contenido compartido
        handleSharedIntent();

        // Configurar el botón de crear lista
        btnCreateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = etListName.getText().toString();
                String sharedContent = etSharedContent.getText().toString();

                if (!listName.isEmpty() && !sharedContent.isEmpty()) {
                    createCustomListAndWords(listName, sharedContent);
                } else {
                    Toast.makeText(ImportCustomListActivity.this, "Por favor ingrese un nombre y contenido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUserAdCategory() {
        userViewModel.checkOrCreateUser();

        try {
            userViewModel.getIsUserCreated().observe(this, isCreated -> {
                if (isCreated) {
                    userViewModel.getUserDTO().observe(this, user -> {
                        if (user != null) {
                            this.user = user;
                            categoryViewModel.setUserId(user.getId());

                            //Get the category
                            categoryViewModel.getOrCreateDefaultCategory().observe(this, categoryDTOs -> {
                                this.defaultCategoryId = categoryDTOs.getId();
                            });
                        } else {
                            Toast.makeText(this, "Error obteniendo el usuario, cierre e intente de nuevo", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error obteniendo el usuario, cierre e intente de nuevo", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSharedIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                etSharedContent.setText(sharedText);
            }
        }
    }

    private void createCustomListAndWords(String listName, String sharedContent) {

        //Validate listName not exists
        customListViewModel.getCustomListByCategoryIdAndName(this.defaultCategoryId, listName).observe(this, customListDTO -> {
            if (customListDTO != null) {
                Toast.makeText(this, "Ya existe una lista con ese nombre", Toast.LENGTH_SHORT).show();
            } else {
                createListAndWords(listName, sharedContent);
            }
        });

    }

    private void createListAndWords(String listName, String sharedContent) {

        String newListId = UUID.randomUUID().toString();
        CustomListDTO customList = new CustomListDTO(newListId, this.defaultCategoryId, listName, null);
        customListViewModel.insertCustomList(customList);

        // Procesar el contenido CSV (word,spanish)
        String[] lines = sharedContent.split("\\n");
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String word = parts[0].trim();
                String spanish = parts[1].trim();
                String customWordId = UUID.randomUUID().toString();
                // Crear el DTO para la palabra personalizada
                CustomWordDTO customWordDTO = new CustomWordDTO(customWordId, customList.getId(), word, spanish);

                // Insertar la palabra en la base de datos
                customWordViewModel.insertCustomWord(customWordDTO);
            } else {
                Toast.makeText(this, "Formato inválido en la línea: " + line, Toast.LENGTH_SHORT).show();
            }
        }

        // Confirmación de la creación de la lista
        Toast.makeText(this, "Lista y palabras creadas con éxito", Toast.LENGTH_SHORT).show();
        finish();
    }
}
