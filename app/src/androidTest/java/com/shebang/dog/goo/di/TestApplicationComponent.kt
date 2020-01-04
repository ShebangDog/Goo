package com.shebang.dog.goo.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestApplicationModule::class])
interface TestApplicationComponent : ApplicationComponent