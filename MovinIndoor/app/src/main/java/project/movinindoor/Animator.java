package project.movinindoor;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Davey on 9-12-2014.
 */
public class Animator {

    public enum Visibility {
        SHOW, HIDE
    }

    public static void visibilityNavigationMenu(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_out_top);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_in_top);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.getfNavigationMenu().startAnimation(showAnimation);
            MapsActivity.getfNavigationMenu().setVisibility(View.VISIBLE);
        } else {
            MapsActivity.getfNavigationMenu().startAnimation(hideAnimation);
            MapsActivity.getfNavigationMenu().setVisibility(View.INVISIBLE);
        }
    }

    public static void visibilityMarkerInfo(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_out_bottom);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_in_bottom);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.getfMarkerDisplay().getView().startAnimation(showAnimation);
            MapsActivity.getfMarkerDisplay().getView().setVisibility(View.VISIBLE);
        } else {
            MapsActivity.getfMarkerDisplay().getView().startAnimation(hideAnimation);
            MapsActivity.getfMarkerDisplay().getView().setVisibility(View.INVISIBLE);
        }
    }

    public static void visibilityRepairList(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_out_top);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_in_top);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.getfRepairList().getView().startAnimation(showAnimation);
            MapsActivity.getfRepairList().getView().setVisibility(View.VISIBLE);
        } else {
            MapsActivity.getfRepairList().getView().startAnimation(hideAnimation);
            MapsActivity.getfRepairList().getView().setVisibility(View.INVISIBLE);
        }
    }

    public static void visibilityNavigationInfoTop(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_out_top);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_in_top);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.getfNavigationInfoTop().getView().startAnimation(showAnimation);
            MapsActivity.getfNavigationInfoTop().getView().setVisibility(View.VISIBLE);
        } else {
            MapsActivity.getfNavigationInfoTop().getView().startAnimation(hideAnimation);
            MapsActivity.getfNavigationInfoTop().getView().setVisibility(View.INVISIBLE);
        }
    }

    public static void visibilityNavigationInfoBottom(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_out_bottom);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_in_bottom);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.getfNavigationInfoBottom().startAnimation(showAnimation);
            MapsActivity.getfNavigationInfoBottom().setVisibility(View.VISIBLE);
        } else {
            MapsActivity.getfNavigationInfoBottom().startAnimation(hideAnimation);
            MapsActivity.getfNavigationInfoBottom().setVisibility(View.INVISIBLE);
        }
    }

    public static void visibilityFloorNavagator(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_fade_out);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_fade_in);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.getfFloorNavigator2().getView().startAnimation(showAnimation);
            MapsActivity.getfFloorNavigator2().getView().setVisibility(View.VISIBLE);
        } else {
            MapsActivity.getfFloorNavigator2().getView().startAnimation(hideAnimation);
            MapsActivity.getfFloorNavigator2().getView().setVisibility(View.INVISIBLE);
        }
    }


    public static void visibilityCardNavigator(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_fade_out);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_fade_in);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.getfNavigationCard().getView().startAnimation(showAnimation);
            MapsActivity.getfNavigationCard().getView().setVisibility(View.VISIBLE);
        } else {
            MapsActivity.getfNavigationCard().getView().startAnimation(hideAnimation);
            MapsActivity.getfNavigationCard().getView().setVisibility(View.INVISIBLE);
        }
    }
}
