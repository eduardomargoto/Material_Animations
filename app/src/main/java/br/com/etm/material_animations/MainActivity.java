package br.com.etm.material_animations;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    View imageView;
    ViewGroup background, view_rl;
    Scene scene1, scene2;
    boolean side = true;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            explode.setDuration(2000);
            Slide slide = new Slide();
            slide.setDuration(2000);
            getWindow().setReenterTransition(explode);
            getWindow().setExitTransition(slide);
        }
        background = (ViewGroup) findViewById(R.id.activity_main);
        view_rl = (ViewGroup) findViewById(R.id.view_rl);
        if (Build.VERSION.SDK_INT >= 19) {
            scene1 = Scene.getSceneForLayout(view_rl, R.layout.activity_scene_1, this);
            scene2 = Scene.getSceneForLayout(view_rl, R.layout.activity_scene_2, this);


        }

    }

    public void moveImage(View view) {
        imageView = (View) findViewById(R.id.ic_launcher);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            TransitionManager.beginDelayedTransition(background);

        if (side) {
            imageView.setLayoutParams(alignRight());
            this.side = false;
        } else {
            imageView.setLayoutParams(alignLeft());
            this.side = true;
        }

    }

    public void startActivity(View View) {
        Intent it = new Intent(this, ActivityB.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView = (View) findViewById(R.id.ic_launcher);

            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, getResources().getString(R.string.circle_name));

            this.startActivity(it, optionsCompat.toBundle());
        } else {
            startActivity(it);
        }
    }


    public void clickChangeAnimations(View v) {
        if (Build.VERSION.SDK_INT >= 19) {
            switch (count) {
                case 0:
                    TransitionManager.go(scene1, new ChangeBounds());
                    break;
                case 1:
                    TransitionManager.go(scene2, new ChangeBounds());
                    break;
            }
            count++;
            if(count == 2)
                count = 0;
        }
    }

    public RelativeLayout.LayoutParams alignLeft() {
        RelativeLayout.LayoutParams options = new RelativeLayout.LayoutParams(192, 192);
        options.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        return options;
    }

    public RelativeLayout.LayoutParams alignRight() {
        RelativeLayout.LayoutParams options = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        options.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        return options;
    }
}
