package kz.artyom.magicball2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public TextView textView2;
    public Button out_answer_button;

    private static final String PREFERENCES_KEY = "answer_preferences";
    private static final String ANSWER_SET_KEY = "answer_set";
    private ArrayList<String> answerList;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private ConstraintLayout AnswerLayout;
    private LinearLayout MainLayout;
    private EditText editTextTextPersonName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        out_answer_button = findViewById(R.id.out_answer_button);
        textView2 = findViewById(R.id.textView2);
        lvItems = findViewById(R.id.lvItems);
        AnswerLayout = findViewById(R.id.AnswerLayout);
        MainLayout = findViewById(R.id.MainLayout);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);

        answerList = new ArrayList<>(loadAnswers());
        if (answerList.isEmpty()) {
            answerList.add("Да");
            answerList.add("Нет");
            answerList.add("Скорее всего да");
            answerList.add("Скорее всего нет");
            answerList.add("Возможно");
            answerList.add("Имеются перспективы");
            answerList.add("Вопрос задан неверно");
            saveAnswers(answerList);
        }

        setupListViewListener();
    }
    public void AddAnswerOption (View v) {

        answerList = new ArrayList<>(loadAnswers());
        if (answerList.isEmpty()) {
            answerList.add("Да");
            answerList.add("Нет");
            answerList.add("Скорее всего да");
            answerList.add("Скорее всего нет");
            answerList.add("Возможно");
            answerList.add("Имеются перспективы");
            answerList.add("Вопрос задан неверно");
            saveAnswers(answerList);
        }

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, answerList);
        lvItems.setAdapter(itemsAdapter);

        MainLayout.setVisibility(v.INVISIBLE);
        AnswerLayout.setVisibility(v.VISIBLE);
    }

    public void BackToMainClick (View v) {

        MainLayout.setVisibility(v.VISIBLE);
        AnswerLayout.setVisibility(v.INVISIBLE);
    }

    public void Onclic(View view){

        randomSet();

    }

    private ArrayList<String> loadAnswers() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        Set<String> defaultSet = new HashSet<>(); // Empty set as default
        Set<String> answerSet = preferences.getStringSet(ANSWER_SET_KEY, defaultSet);
        return new ArrayList<>(answerSet);
    }

    private void saveAnswers(ArrayList<String> answerList) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> answerSet = new HashSet<>(answerList);
        editor.putStringSet(ANSWER_SET_KEY, answerSet);
        editor.apply();
    }

    public void onAddItem(View v) {
        EditText etNewItem = findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        answerList.add(itemText);
        etNewItem.setText("");

        saveAnswers(answerList);

        itemsAdapter.notifyDataSetChanged();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {

                        answerList.remove(pos);

                        saveAnswers(answerList);

                        itemsAdapter.notifyDataSetChanged();

                        return true;
                    }
                });
    }

    public void randomSet() {

        Random random = new Random();

        int randomIndex = random.nextInt(answerList.size());

        String randomAnswer = answerList.get(randomIndex);

        if (textView2 != null) {
            textView2.setText(randomAnswer);
        }

    }

}