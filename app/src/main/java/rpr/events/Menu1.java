package rpr.events;

/**

 * Created by Vishal Singh on 31-03- 2017.

 */

import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

public class Menu1 extends Fragment {

    public static TabLayout tabLayout;

    public static ViewPager viewPager;

    public static int int_items = 4;

    @Nullable

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

/**

 *Inflate tab_layout and setup Views.

 */

        View x = inflater.inflate(R.layout.tab_layout,null);

        tabLayout = (TabLayout) x.findViewById(R.id.tabs);

        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

/**

 *Set an Apater for the View Pager

 */

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

/**

 * Now , this is a workaround ,

 * The setupWithViewPager dose&#39;t works without the runnable .

 * Maybe a Support Library Bug .

 */

        tabLayout.post(new Runnable() {

            @Override

            public void run() {

                tabLayout.setupWithViewPager(viewPager);

            }

        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {

            super(fm);

        }

        /**

         * Return fragment with respect to Position .

         */

        @Override

        public Fragment getItem(int position)

        {

            switch (position){

                case 0 : return new Primary();

                case 1 : return new Secondary();

                case 2 : return new Tertiary();

                case 3 : return new Quaternery();

            }

            return null;

        }

        @Override

        public int getCount() {

            return int_items;

        }

        /**

         * This method returns the title of the tab according to the position.

         */

        @Override

        public CharSequence getPageTitle(int position) {

            switch (position){

                case 0 :

                    return &quot;Cultural&quot;;

                case 1 :

                    return &quot;Seminar&quot;;

                case 2 :

                    return &quot;Sports&quot;;

                case 3 :

                    return &quot;Technical&quot;;

            }

            return null;

        }

    }

}