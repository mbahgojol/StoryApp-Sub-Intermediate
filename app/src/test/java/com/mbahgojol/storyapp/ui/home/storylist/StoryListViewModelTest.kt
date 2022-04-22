package com.mbahgojol.storyapp.ui.home.storylist

import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.mbahgojol.storyapp.data.model.GetAllStoryResponse
import com.mbahgojol.storyapp.data.remote.Repository
import com.mbahgojol.storyapp.helper.InstantRuleExecution
import com.mbahgojol.storyapp.helper.TrampolineSchedulerRX
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.rxjava3.core.Flowable
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@RunWith(JUnitPlatform::class)
class StoryListViewModelTest : Spek({
    Feature("List Story") {
        val repository = mock<Repository>()
        val viewModel = StoryListViewModel(repository)
        val observerState = mock<Observer<PagingData<GetAllStoryResponse.Story>>>()

        beforeFeature {
            TrampolineSchedulerRX.start()
            InstantRuleExecution.start()
            viewModel.resultStory.observeForever(observerState)
        }

        Scenario("do on List Story, response success") {
            val expectedResult = PagingData.from(
                mutableListOf(GetAllStoryResponse.Story("", "", "", "", 0.0, 0.0))
            )

            Given("set success result state") {
                given(repository.getAllStory()).willReturn(
                    Flowable.just(expectedResult)
                )
            }

            When("list story") {
                viewModel.getListStory()
            }

            Then("result success and return data") {
                verify(
                    observerState, atLeastOnce()
                ).onChanged(expectedResult)
            }
        }

        Scenario("do on List Story, response success and data not found") {
            val expectedResult = PagingData.from(
                mutableListOf<GetAllStoryResponse.Story>()
            )

            Given("set success result state") {
                given(repository.getAllStory()).willReturn(
                    Flowable.just(expectedResult)
                )
            }

            When("list story") {
                viewModel.getListStory()
            }

            Then("result success and return data not found") {
                verify(
                    observerState, atLeastOnce()
                ).onChanged(expectedResult)
            }
        }

        afterFeature {
            viewModel.resultStory.removeObserver(observerState)
            TrampolineSchedulerRX.tearDown()
            InstantRuleExecution.tearDown()
        }
    }
})