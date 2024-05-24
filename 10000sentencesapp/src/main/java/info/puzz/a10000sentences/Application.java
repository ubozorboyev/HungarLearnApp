package info.puzz.a10000sentences;

import com.activeandroid.ActiveAndroid;
import com.google.firebase.FirebaseApp;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class Application extends android.app.Application {

    public static DiComponent COMPONENT;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());

        initIconify();
        initActiveAndroid();
        initDagger();
    }

    private void initDagger() {
        COMPONENT = DaggerDiComponent.builder()
                .appModule(new AppModule())
                .build();
    }

    private void initIconify() {
        //FontAwesomeIcons.fa_volume_up
        Iconify.with(new FontAwesomeModule());
    }

    private void initActiveAndroid() {
        ActiveAndroid.initialize(this);
    }

}
