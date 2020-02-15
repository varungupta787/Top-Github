package com.topgithub.demo;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.topgithub.demo.data.repository.RepoRepository;
import com.topgithub.demo.models.RepositoryItem;
import com.topgithub.demo.viewmodel.GithubViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.when;

public class GithubViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
    GithubViewModel viewModel;

    @Mock
    RepoRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        viewModel = new GithubViewModel(repository, Schedulers.trampoline(), Schedulers.trampoline());
    }

    @Test
    public void testGetRepositoryListData() {
        List<RepositoryItem> list = new ArrayList<>();
        RepositoryItem item = new RepositoryItem();
        item.name = "GithubTopTest";
        list.add(item);
        when(repository.getRepositoryList()).thenReturn(Single.just(list));
        viewModel.getRepositoryListData();
        TestObserver<List<RepositoryItem>> testObserver = repository.getRepositoryList().test();
        testObserver.assertNoErrors().onSuccess(list);
        Assert.assertEquals(1, viewModel.getGithubRepositoryList().getValue().size());
        Assert.assertEquals(false, viewModel.getLoadingState().getValue());
        Assert.assertEquals(false, viewModel.getRepoError().getValue());
    }

    @Test
    public void testGetRepositoryListData_failure() {
        when(repository.getRepositoryList()).thenReturn(Single.error(new RuntimeException()));
        viewModel.getRepositoryListData();
        TestObserver<List<RepositoryItem>> testObserver = repository.getRepositoryList().test();
        testObserver.assertError(RuntimeException.class);
        Assert.assertEquals(true, viewModel.getRepoError().getValue());
        Assert.assertEquals(false, viewModel.getLoadingState().getValue());
    }
}
