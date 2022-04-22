package com.mbahgojol.storyapp.ui.register

import androidx.lifecycle.Observer
import com.mbahgojol.storyapp.data.model.GlobalResponse
import com.mbahgojol.storyapp.data.model.RegisterBody
import com.mbahgojol.storyapp.data.remote.Repository
import com.mbahgojol.storyapp.helper.InstantRuleExecution
import com.mbahgojol.storyapp.helper.TrampolineSchedulerRX
import com.mbahgojol.storyapp.utils.ResultState
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.rxjava3.core.Single
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@RunWith(JUnitPlatform::class)
class RegisterViewModelTest : Spek({
    Feature("Register") {
        val repository = mock<Repository>()
        val viewModel = RegisterViewModel(repository)
        val observerState = mock<Observer<ResultState>>()

        beforeFeature {
            TrampolineSchedulerRX.start()
            InstantRuleExecution.start()
            viewModel.resultStateRegister.observeForever(observerState)
        }

        Scenario("do on Register Story, response success") {
            val expectedResult = GlobalResponse(false, "")
            val body = RegisterBody("ghozimahdi@gmail.com", "ghozi", "ghozi123")

            Given("set success result state") {
                given(repository.register(body)).willReturn(
                    Single.just(
                        ResultState.Success(expectedResult)
                    )
                )
            }

            When("register") {
                viewModel.register(body)
            }

            Then("result success and return data") {
                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(true))

                verify(
                    observerState
                ).onChanged(ResultState.Success(expectedResult))

                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(false))
            }
        }

        Scenario("do on Register Story, response error") {
            val expectedResult = Throwable("error")
            val body = RegisterBody("ghozimahdi@gmail.com", "ghozi", "ghozi123")

            Given("set error result state") {
                given(repository.register(body)).willReturn(
                    Single.just(
                        ResultState.Error(expectedResult)
                    )
                )
            }

            When("register") {
                viewModel.register(body)
            }

            Then("result error") {
                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(true))

                verify(
                    observerState
                ).onChanged(ResultState.Error(expectedResult))

                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(false))
            }
        }

        afterFeature {
            viewModel.resultStateRegister.removeObserver(observerState)
            TrampolineSchedulerRX.tearDown()
            InstantRuleExecution.tearDown()
        }
    }
})