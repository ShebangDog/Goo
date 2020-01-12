package com.shebang.dog.goo.di.scope

import javax.inject.Qualifier

@Qualifier
@Retention(value = AnnotationRetention.RUNTIME)
annotation class RemoteDataSource