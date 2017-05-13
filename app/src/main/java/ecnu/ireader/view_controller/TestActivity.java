package ecnu.ireader.view_controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import ecnu.ireader.R;
import ecnu.ireader.function_module.Dictionary;
import ecnu.ireader.function_module.PassageFilter;
import ecnu.ireader.model.Passage;
import ecnu.ireader.model.Word;

public class TestActivity extends AppCompatActivity implements PassageFilter.OnWordClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextView tv = (TextView)findViewById(R.id.test_text);
        PassageFilter filter = new PassageFilter(this);
        filter.setMode(PassageFilter.MODE_CLICK);
        filter.setLevel(Dictionary.LEVEL_GK);
        filter.setOnWordClickListener(this);
        String content = "The friend called the police, Mr Pappas added.\n" +
                "\n" +
                "Police also discovered a black backpack at the scene containing jewellery, which authorities believe belonged to Dr Bolanos.\n" +
                "\n" +
                "A motive for the attack is still unclear and Mr Pappas did not say how Mr Teixeira was able to get through tight security to reach the top levels of the residential complex.\n" +
                "\n" +
                "Mr Teixeira was recently released from prison after serving nine months for bank robbery.\n" +
                "\n" +
                "His next hearing is set for 8 June.\n" +
                "\n" +
                "Dr Bolanos was a paediatric anaesthesiologist at Massachusetts Eye and Ear Infirmary.\n" +
                "\n" +
                "Dr Field worked at the Brigham and Women's Hospital, an affiliate of Harvard Medical School.\n" +
                "\n" +
                "Dr Bolanos had cancelled plans on Friday night to invite her boss for dinner because Dr Field was not feeling well, the Boston Globe reports.\n" +
                "\n" +
                "In a strange twist, it emerged that the intended dinner guest - Dr Sunil Eappen - was the father of a toddler shaken to death by a British nanny two decades ago.\n" +
                "\n" +
                "Louise Woodward was convicted of the involuntary manslaughter of eight-month-old Matthew Eappen in Boston in 1997. ";
        Passage passage = new Passage(null,"hello",content);
        PassageFilter.FilteredPassage p = filter.filterPassage(passage);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(p.getSpannableContent());
    }

    @Override
    public void onWordClick(Word word){
        if(word.getMeaning() == null){
            Toast.makeText(this,word.getEnglish()+"be clicked!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,word.getEnglish()+" "+word.getMeaning()+" be clicked!",Toast.LENGTH_SHORT).show();
        }
    }
}
