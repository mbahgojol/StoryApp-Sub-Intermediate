package com.mbahgojol.storyapp.data.remote

import com.mbahgojol.storyapp.data.model.LoginBody
import com.mbahgojol.storyapp.data.model.RegisterBody
import com.mbahgojol.storyapp.data.remote.story.FakeStoryServiceImpl
import com.mbahgojol.storyapp.data.remote.story.StoryService
import com.mbahgojol.storyapp.helper.InstantRuleExecution
import com.mbahgojol.storyapp.helper.TrampolineSchedulerRX
import com.mbahgojol.storyapp.utils.getResultAllStory
import com.mbahgojol.storyapp.utils.successResult
import com.nhaarman.mockitokotlin2.mock
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryImplTest {

    private lateinit var apiService: StoryService
    private lateinit var storRepository: Repository

    @Before
    fun startup() {
        TrampolineSchedulerRX.start()
        InstantRuleExecution.start()
        apiService = FakeStoryServiceImpl()
        storRepository = RepositoryImpl(apiService)
    }

    @Test
    fun `when login success`() {
        val expected = successResult
        val actual = apiService.login(LoginBody("email@gmail.com", "123123"))
        Assert.assertNotNull(actual)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when register success`() {
        val expected = successResult
        val actual = apiService.register(RegisterBody("email@gmail.com", "email", "123123"))
        Assert.assertNotNull(actual)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when add story success`() {
        val expected = successResult
        val actual = apiService.addStory(mock())
        Assert.assertNotNull(actual)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when get all story success`() {
        val expected = getResultAllStory
        val actual = apiService.getAllStory(hashMapOf())
        Assert.assertNotNull(actual)
        Assert.assertEquals(expected, actual)
    }

    @After
    fun teardown() {
        TrampolineSchedulerRX.tearDown()
        InstantRuleExecution.tearDown()
    }
}