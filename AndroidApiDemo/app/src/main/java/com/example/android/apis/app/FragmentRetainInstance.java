/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apis.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.android.apis.R;

/**
 * This example shows how you can use a Fragment to easily propagate state
 * (such as threads) across activity instances when an activity needs to be
 * restarted due to, for example, a configuration change.  This is a lot
 * easier than using the raw Activity.onRetainNonConfiguratinInstance() API.
 */
public class FragmentRetainInstance extends Activity {

    public static final String TAG = "FragLife";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onCreate(savedInstanceState);
        
        // First time init, create the UI.
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(android.R.id.content,
                    new UiFragment()).commit();
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onRestart();
    }

    @Override
    public void onStateNotSaved() {
        Log.d(TAG, "onStateNotSaved() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onStateNotSaved();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        Log.d(TAG, "onPostResume() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onPostResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        Log.d(TAG, "onRetainNonConfigurationInstance() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        return super.onRetainNonConfigurationInstance();
    }

    @Override
    public void onContentChanged() {
        Log.d(TAG, "onContentChanged() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onContentChanged();
    }

    @Override
    public void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
        super.onDetachedFromWindow();
    }

    /**
     * This is a fragment showing UI that will be updated from work done
     * in the retained fragment.
     */
    public static class UiFragment extends Fragment {

        RetainedFragment mWorkFragment;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            Log.d(TAG, "onCreateView() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            View v = inflater.inflate(R.layout.fragment_retain_instance, container, false);

            // Watch for button clicks.
            Button button = (Button)v.findViewById(R.id.restart);
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    mWorkFragment.restart();
                }
            });

            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            Log.d(TAG, "onActivityCreated() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onActivityCreated(savedInstanceState);

            FragmentManager fm = getFragmentManager();

            // Check to see if we have retained the worker fragment.
            mWorkFragment = (RetainedFragment)fm.findFragmentByTag("work");

            // If not retained (or first time running), we need to create it.
            if (mWorkFragment == null) {
                mWorkFragment = new RetainedFragment();
                // Tell it who it is working with.
                mWorkFragment.setTargetFragment(this, 0);
                fm.beginTransaction().add(mWorkFragment, "work").commit();
            }

            mWorkFragment.setInstanceIdentifier(new Object());
        }

        @Override
        public void onAttachFragment(Fragment childFragment) {
            Log.d(TAG, "onAttachFragment() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onAttachFragment(childFragment);
        }

        @Override
        public void onAttach(Context context) {
            Log.d(TAG, "onAttach() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onAttach(context);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            Log.d(TAG, "onCreate() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            Log.d(TAG, "onViewCreated() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onViewCreated(view, savedInstanceState);
        }

        @Nullable
        @Override
        public View getView() {
            Log.d(TAG, "getView() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            return super.getView();
        }

        @Override
        public void onStart() {
            Log.d(TAG, "onStart() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onStart();
        }

        @Override
        public void onResume() {
            Log.d(TAG, "onResume() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onResume();
        }

        @Override
        public void onPause() {
            Log.d(TAG, "onPause() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onPause();
        }

        @Override
        public void onStop() {
            Log.d(TAG, "onStop() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onStop();
        }

        @Override
        public void onDestroyView() {
            Log.d(TAG, "onDestroyView() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onDestroyView();
        }

        @Override
        public void onDestroy() {
            Log.d(TAG, "onDestroy() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onDestroy();
        }

        @Override
        public void onDetach() {
            Log.d(TAG, "onDetach() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onDetach();
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            Log.d(TAG, "onCreateOptionsMenu() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public void onPrepareOptionsMenu(Menu menu) {
            Log.d(TAG, "onPrepareOptionsMenu() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onPrepareOptionsMenu(menu);
        }

        @Override
        public void onDestroyOptionsMenu() {
            Log.d(TAG, "onDestroyOptionsMenu() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onDestroyOptionsMenu();
        }


    }

    /**
     * This is the Fragment implementation that will be retained across
     * activity instances.  It represents some ongoing work, here a thread
     * we have that sits around incrementing a progress indicator.
     */
    public static class RetainedFragment extends Fragment {
        ProgressBar mProgressBar;
        int mPosition;
        boolean mReady = false;
        boolean mQuiting = false;
        Object mInstanceIdentifier = null;

        public RetainedFragment(){
            Log.e(TAG, "new RetainedFragment created@{"+System.identityHashCode(RetainedFragment.this)+"}");
        }

        /**
         * This is the thread that will do our work.  It sits in a loop running
         * the progress up until it has reached the top, then stops and waits.
         */
        final Thread mThread = new Thread() {
            @Override
            public void run() {
                // We'll figure the real value out later.
                int max = 10000;

                // This thread runs almost forever.
                while (true) {

                    // Update our shared state with the UI.
                    synchronized (this) {
                        // Our thread is stopped if the UI is not ready
                        // or it has completed its work.
                        while (!mReady || mPosition >= max) {
                            if (mQuiting) {
                                return;
                            }
                            try {
                                wait();
                            } catch (InterruptedException e) {
                            }
                        }

                        // Now update the progress.  Note it is important that
                        // we touch the progress bar with the lock held, so it
                        // doesn't disappear on us.
                        mPosition++;
                        max = mProgressBar.getMax();
                        mProgressBar.setProgress(mPosition);
                    }

                    // Normally we would be doing some work, but put a kludge
                    // here to pretend like we are.
                    synchronized (this) {
                        try {
                            wait(50);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        };

        /**
         * Fragment initialization.  We way we want to be retained and
         * start our thread.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            Log.d(TAG, "onCreate() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onCreate(savedInstanceState);
            
            // Tell the framework to try to keep this fragment around
            // during a configuration change.
            setRetainInstance(true);
            
            // Start up the worker thread.
            mThread.start();
        }

        /**
         * This is called when the Fragment's Activity is ready to go, after
         * its content view has been installed; it is called both after
         * the initial fragment creation and after the fragment is re-attached
         * to a new activity.
         */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            
            // Retrieve the progress bar from the target's view hierarchy.
            mProgressBar = (ProgressBar)getTargetFragment().getView().findViewById(
                    R.id.progress_horizontal);
            
            // We are ready for our thread to go.
            synchronized (mThread) {
                mReady = true;
                mThread.notify();
            }
        }

        /**
         * This is called when the fragment is going away.  It is NOT called
         * when the fragment is being propagated between activity instances.
         */
        @Override
        public void onDestroy() {
            Log.d(TAG, "onDestroy() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            // Make the thread go away.
            synchronized (mThread) {
                mReady = false;
                mQuiting = true;
                mThread.notify();
            }
            
            super.onDestroy();
        }

        /**
         * This is called right before the fragment is detached from its
         * current activity instance.
         */
        @Override
        public void onDetach() {
            Log.d(TAG, "onDetach() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            // This fragment is being detached from its activity.  We need
            // to make sure its thread is not going to touch any activity
            // state after returning from this function.
            synchronized (mThread) {
                mProgressBar = null;
                mReady = false;
                mThread.notify();
            }
            
            super.onDetach();
        }

        /**
         * API for our UI to restart the progress thread.
         */
        public void restart() {
            synchronized (mThread) {
                mPosition = 0;
                mThread.notify();
            }
        }

        public void setInstanceIdentifier(Object object){
            if(this.mInstanceIdentifier == null) {
                this.mInstanceIdentifier = object;
                Log.e(TAG, "setInstanceIdentifier() accepted Obj "+this.getClass().getSimpleName()+"" +
                        "-InstanceIdentifier@{"+System.identityHashCode(this.mInstanceIdentifier)+"}");
            }else{
                Log.e(TAG, "setInstanceIdentifier() cannot accept " +
                        "new Obj@{"+System.identityHashCode(object)+"} since already " +
                        "old obj exist@{"+System.identityHashCode(this.mInstanceIdentifier)+"}");
            }
        }

        @Override
        public void onAttachFragment(Fragment childFragment) {
            Log.d(TAG, "onAttachFragment() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onAttachFragment(childFragment);
        }

        @Override
        public void onAttach(Context context) {
            Log.d(TAG, "onAttach() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onAttach(context);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            Log.d(TAG, "onViewCreated() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onViewCreated(view, savedInstanceState);
        }

        @Nullable
        @Override
        public View getView() {
            Log.d(TAG, "getView() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            return super.getView();
        }

        @Override
        public void onStart() {
            Log.d(TAG, "onStart() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onStart();
        }

        @Override
        public void onResume() {
            Log.d(TAG, "onResume() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onResume();
        }

        @Override
        public void onPause() {
            Log.d(TAG, "onPause() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onPause();
        }

        @Override
        public void onStop() {
            Log.d(TAG, "onStop() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onStop();
        }

        @Override
        public void onDestroyView() {
            Log.d(TAG, "onDestroyView() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onDestroyView();
        }


        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            Log.d(TAG, "onCreateOptionsMenu() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public void onPrepareOptionsMenu(Menu menu) {
            Log.d(TAG, "onPrepareOptionsMenu() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onPrepareOptionsMenu(menu);
        }

        @Override
        public void onDestroyOptionsMenu() {
            Log.d(TAG, "onDestroyOptionsMenu() "+this.getClass().getSimpleName()+"@{"+System.identityHashCode(this)+"}");
            super.onDestroyOptionsMenu();
        }


    }
}
