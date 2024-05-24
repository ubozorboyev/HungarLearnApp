package info.puzz.a10000sentences.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import java.util.List;

import javax.inject.Inject;

import info.puzz.a10000sentences.Application;
import info.puzz.a10000sentences.R;
import info.puzz.a10000sentences.activities.adapters.CollectionsAdapter;
import info.puzz.a10000sentences.dao.Dao;
import info.puzz.a10000sentences.databinding.ActivityCollectionsBinding;
import info.puzz.a10000sentences.models.SentenceCollection;
import info.puzz.a10000sentences.models.SentenceHistory;

public class CollectionsActivity extends BaseActivity implements BaseActivity.OnCollectionsReloaded {

    private static final String TAG = CollectionActivity.class.getSimpleName();

    ActivityCollectionsBinding binding;

    @Inject Dao dao;

    public static <T extends BaseActivity> void start(T activity) {
        Intent intent = new Intent(activity, CollectionsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Application.COMPONENT.injectActivity(this);

        if (isTaskRoot()) {
            SentenceHistory sentenceHistory = dao.getLatestSentenceHistory();
            if (sentenceHistory != null) {
                CollectionActivity.start(this, sentenceHistory.collectionId);
                // TODO for some reason this return causes an exception:
                // return;
            }
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_collections);

        if (dao.getLanguages().size() == 0) {
            reloadLanguages();
        }

        setTitle(R.string.collections);
    }

    private void showFirstStepsIfNeeded() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String firstTimePref = "___first_time___";
        if (!prefs.getBoolean(firstTimePref, false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(firstTimePref, true);
            editor.commit();

            WebView webView = new WebView(this);
            webView.loadData(getString(R.string.first_steps_contents), "text/html; charset=UTF-8", "UTF-8");
            new AlertDialog.Builder(this)
                    .setTitle(R.string.first_steps)
                    .setView(webView)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        reloadCollections();
    }

    @Override
    public void onCollectionsReloaded() {
        reloadCollections();
        showFirstStepsIfNeeded();
    }

    private void reloadCollections() {
        List<SentenceCollection> cols = dao.getCollections();

        binding.collectionsList.setAdapter(new CollectionsAdapter(this, cols));
    }

}
