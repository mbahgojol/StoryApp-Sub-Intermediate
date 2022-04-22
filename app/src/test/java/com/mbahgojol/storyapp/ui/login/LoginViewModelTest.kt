package com.mbahgojol.storyapp.ui.login

import androidx.lifecycle.Observer
import com.mbahgojol.storyapp.data.model.GlobalResponse
import com.mbahgojol.storyapp.data.model.LoginBody
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
class LoginViewModelTest : Spek({
    Feature("Login") {
        val repository = mock<Repository>()
        val viewModel = LoginViewModel(repository)
        val observerState = mock<Observer<ResultState>>()

        beforeFeature {
            TrampolineSchedulerRX.start()
            InstantRuleExecution.start()
            viewModel.resultStateLogin.observeForever(observerState)
        }

        Scenario("do on Login Story, response success") {
            val expectedResult = GlobalResponse(false, "")
            val body = LoginBody("ghozimahdi@gmail.com", "ghozi123")

            Given("set success result state") {
                given(repository.login(body)).willReturn(
                    Single.just(
                        ResultState.Success(expectedResult)
                    )
                )
            }

            When("login") {
                viewModel.login(body)
            }

            Then("result success and return data") {
                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(true))

                verify(
                    observerState
                ).onChanged(ResultState.Success(expectedResult))

                verify(observerState, atLeastOnce()).onChanged(ResultState.Loading(false))
            }
        }

        Scenario("do on Login Story, response error") {
            val expectedResult = Throwable("error")
            val body = LoginBody("ghozimahdi@gmail.com", "ghozi123")

            Given("set error result state") {
                given(repository.login(body)).willReturn(
                    Single.just(
                        ResultState.Error(expectedResult)
                    )
                )
            }

            When("login") {
                viewModel.login(body)
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
            viewModel.resultStateLogin.removeObserver(observerState)
            TrampolineSchedulerRX.tearDown()
            InstantRuleExecution.tearDown()
        }
    }
})