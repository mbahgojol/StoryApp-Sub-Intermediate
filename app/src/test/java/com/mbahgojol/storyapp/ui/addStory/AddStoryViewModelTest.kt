package com.mbahgojol.storyapp.ui.addStory

import androidx.lifecycle.Observer
import com.mbahgojol.storyapp.data.model.GlobalResponse
import com.mbahgojol.storyapp.data.remote.Repository
import com.mbahgojol.storyapp.helper.InstantRuleExecution
import com.mbahgojol.storyapp.helper.TrampolineSchedulerRX
import com.mbahgojol.storyapp.utils.ResultState
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@RunWith(JUnitPlatform::class)
class AddStoryViewModelTest : Spek({
    Feature("Add Story") {
        val repository = mock<Repository>()
        val viewModel = AddStoryViewModel(repository)
        val observerState = mock<Observer<ResultState>>()

        beforeFeature {
            TrampolineSchedulerRX.start()
            InstantRuleExecution.start()
            viewModel.resultStateStory.observeForever(observerState)
        }

        Scenario("do on Upload Story, response success") {
            val expectedResult = GlobalResponse(false, "")
            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("description", "")
                .build()

            Given("set success result state") {
                given(repository.addStory(body)).willReturn(
                    Single.just(
                        ResultState.Success(expectedResult)
                    )
                )
            }

            When("upload story") {
                viewModel.upload(body)
            }

            Then("result success and return data") {
                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(true))

                verify(
                    observerState
                ).onChanged(ResultState.Success(expectedResult))

                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(false))
            }
        }

        Scenario("do on Upload Story, response error") {
            val expectedResult = Throwable("error")
            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("description", "")
                .build()

            Given("set error result state") {
                given(repository.addStory(body)).willReturn(
                    Single.just(
                        ResultState.Error(expectedResult)
                    )
                )
            }

            When("upload story") {
                viewModel.upload(body)
            }

            Then("result error and return data") {
                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(true))

                verify(
                    observerState
                ).onChanged(ResultState.Error(expectedResult))

                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(false))
            }
        }

        afterFeature {
            viewModel.resultStateStory.removeObserver(observerState)
            TrampolineSchedulerRX.tearDown()
            InstantRuleExecution.tearDown()
        }
    }
})