package co.ld.codechallenge.core;

import android.util.Log;
import android.widget.ImageView;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.NoActivityResumedException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.Suppress;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;

import co.ld.codechallenge.*;
import co.ld.codechallenge.ui.GithubListFragment;

@RunWith(AndroidJUnit4.class)
public class UITest {


    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    private final String HTTP_ERROR_403 = "HTTP 403 Error";

    private int numRepos;


    @Before
    public void setup() throws InterruptedException {
        Thread.sleep(4000);
        numRepos = GithubListFragment.data.size();

        /*
            //Unable to get EspressoIdlingResource to work as expected. Use Thread.sleep() to load list
            EspressoIdlingResource.setDefaultIdlingResource();
            IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
        */
    }

    @Test //pass
    public void clickRepoList_openDetailFragment() throws Exception {
        //Find repo list
        onView(withId(R.id.repo_list))
                //Click first item
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Check if detailGithubFragment is showing
        checkGithubDetailFragmentFields();

    }

    @Suppress
    @Test //fail
    //Test returns an HTTP 403 Error when clicking on 12 repos in succession. Repo list will be been seen.
    public void githubListFragment_githubDetailFragment_githubListFragment_allRepos() throws Exception {
        for (int i = 0; i < numRepos; i++) {
            Log.d(HTTP_ERROR_403, ""+i);

            //Click i element on list
            onView(withId(R.id.repo_list))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

            //Check if githubDetailFragment is displayed
            checkGithubDetailFragmentFields();

            //Press Back button
            onView(isRoot()).perform(ViewActions.pressBack());

            //Check if githubListFragment is displayed
            onView(withId(R.id.repo_list))
                    .check(matches(isDisplayed()));

        }
    }

    @Test //pass
    public void backPressFromGithubDetailFragment_githubListFragment() throws Exception {
        onView(withId(R.id.repo_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(isRoot()).perform(ViewActions.pressBack());

        onView(withId(R.id.repo_list))
                .check(matches(isDisplayed()));
    }

    @Test //pass
    public void githubListFragment_githubDetailFragment_ofListBoundries() throws Exception {
        if (numRepos != 0) {
            onView(withId(R.id.repo_list))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            checkGithubDetailFragmentFields();

            onView(isRoot()).perform(ViewActions.pressBack());

            onView(withId(R.id.repo_list))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(numRepos - 1, click()));

            checkGithubDetailFragmentFields();
        }
    }

    @Test //pass
    public void exitAppOnBackPressFromGithubListFragment() {
        try {
            pressBack();
            fail("Should have thrown NoActivityResumedException");
        } catch (NoActivityResumedException expected) {
        }
    }

    //Check if githubDetailFragment is showing and all fields are not blank.
    public void checkGithubDetailFragmentFields() {
        onView(withId(R.id.githubDetailFragmentLayout))
                .check(matches(isDisplayed()));

        onView(withId(R.id.full_name)).check(matches(not(withText(""))));
        onView(withId(R.id.desc)).check(matches(not(withText(""))));
        onView(withId(R.id.url)).check(matches(not(withText(""))));
        onView(withId(R.id.username)).check(matches(not(withText(""))));
        onView(withId(R.id.stars)).check(matches(not(withText(""))));
    }


}
