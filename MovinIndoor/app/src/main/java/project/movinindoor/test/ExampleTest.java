package project.movinindoor.test;

import android.test.InstrumentationTestCase;

import project.movinindoor.MapsActivity;

/**
 * Created by Ian on 26-11-2014.
 */


public class ExampleTest extends InstrumentationTestCase {

    MapsActivity mapsActivity = new MapsActivity();

    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }

    public void test2() throws Exception
    {
        int po = 23;
        int op = 23;
        assertEquals(po,op);
    }
}