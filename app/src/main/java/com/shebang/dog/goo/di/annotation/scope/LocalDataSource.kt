package com.shebang.dog.goo.di.annotation.scope

import javax.inject.Qualifier

@Qualifier
@Retention(value = AnnotationRetention.RUNTIME)
annotation class LocalDataSource