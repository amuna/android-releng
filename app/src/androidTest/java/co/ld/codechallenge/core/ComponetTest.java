package co.ld.codechallenge.core;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.Suppress;
import androidx.test.rule.ActivityTestRule;
import co.ld.codechallenge.MainActivity;
import co.ld.codechallenge.model.search.Owner;
import co.ld.codechallenge.model.search.Repo;
import co.ld.codechallenge.network.Response;
import co.ld.codechallenge.ui.GithubListFragment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class ComponetTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity = mainActivityActivityTestRule.getActivity();

    private Response response = mock(Response.class);
    private List<Repo> singleList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        Thread.sleep(5000);
        Owner itemOwner = new Owner();
        itemOwner.setId(97088);
        itemOwner.setLogin("mrdoob");

        Repo item = new Repo();
        item.setId(576201);
        item.setFullName("mrdoob/three.js");
        item.setName("three.js");
        item.setDescription("JavaScript 3D library.");
        item.setOwner(itemOwner);
        item.setUrl("https://api.github.com/repos/mrdoob/three.js ");
        item.setStargazersCount(50605);

        singleList.add(item);

    }

    //@Suppress
    @Test
    public void test() throws InterruptedException {

        //When response.getData is called in GithubListFragment, return a List<Repo> of one object
        //Not sure how to inject this into the code
        doReturn(singleList).when(response).getData();

        //Check if size of list is 1.
        assertEquals(1, GithubListFragment.data.size());
        //
    }

}
