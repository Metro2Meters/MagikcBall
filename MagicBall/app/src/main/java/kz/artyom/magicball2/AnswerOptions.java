package kz.artyom.magicball2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class AnswerOptions extends AppCompatActivity {

    private static final String PREFERENCES_KEY = "answer_preferences";
    private static final String ANSWER_SET_KEY = "answer_set";
    private ArrayList<String> answerList;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    public TextView textView2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_options);

        lvItems = findViewById(R.id.lvItems);



        // Load answers from SharedPreferences
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

        // Create ArrayAdapter for the ListView
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, answerList);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }


    public void randomSet() {
        answerList = new ArrayList<>(loadAnswers());

        // Создаем экземпляр Random для генерации случайного числа
        Random random = new Random();

        // Получаем случайный индекс из списка
        int randomIndex = random.nextInt(answerList.size());

        // Получаем элемент из списка по случайному индексу
        String randomAnswer = answerList.get(randomIndex);

        // Устанавливаем текст в textView2
        if (textView2 != null) {
            textView2.setText(randomAnswer);

        } else {
            Log.e("AnswerOptions", "textView2 is null");
        }

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

        // Save the updated list to SharedPreferences
        saveAnswers(answerList);
        // Notify the adapter that the dataset has changed
        itemsAdapter.notifyDataSetChanged();
    }


    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        // Remove the item from the list
                        answerList.remove(pos);
                        // Save the updated list to SharedPreferences
                        saveAnswers(answerList);
                        // Notify the adapter that the dataset has changed
                        itemsAdapter.notifyDataSetChanged();
                        // Indicate that the long click is handled
                        return true;
                    }
                });
    }



}