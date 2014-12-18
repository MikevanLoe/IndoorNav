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
            MapsActivity.fNavigationMenu.startAnimation(showAnimation);
            MapsActivity.fNavigationMenu.setVisibility(View.VISIBLE);
        } else {
            MapsActivity.fNavigationMenu.startAnimation(hideAnimation);
            MapsActivity.fNavigationMenu.setVisibility(View.INVISIBLE);
        }
    }

    public static void visibilityMarkerInfo(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_out_bottom);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_in_bottom);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.fMarkerDisplay.getView().startAnimation(showAnimation);
            MapsActivity.fMarkerDisplay.getView().setVisibility(View.VISIBLE);
        } else {
            MapsActivity.fMarkerDisplay.getView().startAnimation(hideAnimation);
            MapsActivity.fMarkerDisplay.getView().setVisibility(View.INVISIBLE);
        }
    }

    public static void visibilityRepairList(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_out_top);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_in_top);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.fRepairList.getView().startAnimation(showAnimation);
            MapsActivity.fRepairList.getView().setVisibility(View.VISIBLE);
        } else {
            MapsActivity.fRepairList.getView().startAnimation(hideAnimation);
            MapsActivity.fRepairList.getView().setVisibility(View.INVISIBLE);
        }
    }

    public static void visibilityNavigationInfoTop(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_out_top);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_in_top);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.fNavigationInfoTop.getView().startAnimation(showAnimation);
            MapsActivity.fNavigationInfoTop.getView().setVisibility(View.VISIBLE);
        } else {
            MapsActivity.fNavigationInfoTop.getView().startAnimation(hideAnimation);
            MapsActivity.fNavigationInfoTop.getView().setVisibility(View.INVISIBLE);
        }
    }

    public static void visibilityNavigationInfoBottom(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_out_bottom);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_slide_in_bottom);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.fNavigationInfoBottom.startAnimation(showAnimation);
            MapsActivity.fNavigationInfoBottom.setVisibility(View.VISIBLE);
        } else {
            MapsActivity.fNavigationInfoBottom.startAnimation(hideAnimation);
            MapsActivity.fNavigationInfoBottom.setVisibility(View.INVISIBLE);
        }
    }

    public static void visibilityFloorNavagator(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_fade_out);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_fade_in);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.fFloorNavigator2.getView().startAnimation(showAnimation);
            MapsActivity.fFloorNavigator2.getView().setVisibility(View.VISIBLE);
        } else {
            MapsActivity.fFloorNavigator2.getView().startAnimation(hideAnimation);
            MapsActivity.fFloorNavigator2.getView().setVisibility(View.INVISIBLE);
        }
    }


    public static void visibilityCardNavigator(Visibility type) {
        Animation hideAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_fade_out);
        Animation showAnimation = AnimationUtils.loadAnimation(MapsActivity.getContext(), R.anim.abc_fade_in);

        if(type.equals(Visibility.SHOW)) {
            MapsActivity.fNavigationCard.getView().startAnimation(showAnimation);
            MapsActivity.fNavigationCard.getView().setVisibility(View.VISIBLE);
        } else {
            MapsActivity.fNavigationCard.getView().startAnimation(hideAnimation);
            MapsActivity.fNavigationCard.getView().setVisibility(View.INVISIBLE);
        }
    }
}
