package info.puzz.a10000sentences.activities.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.From;

import info.puzz.a10000sentences.R;
import info.puzz.a10000sentences.activities.BaseActivity;
import info.puzz.a10000sentences.activities.SentenceQuizActivity;
import info.puzz.a10000sentences.databinding.SentenceBinding;
import info.puzz.a10000sentences.models.Sentence;

public class SentencesAdapter extends LoadMoreAdapter<Sentence> {

    public <T extends BaseActivity> SentencesAdapter(T activity, From select) {
        super(activity, R.layout.sentence_collection, select);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        SentenceBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.sentence, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }

        final Sentence sentence = getItemAndLoadMoreIfNeeded(position);

        binding.setSentence(sentence);

        binding.targetSentence.setTextColor(ContextCompat.getColor(getContext(), sentence.getSentenceStatus().getColor()));

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SentenceQuizActivity.startSentence((BaseActivity) getContext(), sentence.sentenceId, SentenceQuizActivity.Type.KNOWN_AND_UNKNOWN);
            }
        });

        return binding.getRoot();
    }

}
