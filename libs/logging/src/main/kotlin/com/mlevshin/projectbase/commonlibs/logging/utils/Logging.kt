package com.mlevshin.projectbase.commonlibs.logging.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

inline fun <reified T:Any> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)
fun logger(clazz: KClass<out Any>): Logger = LoggerFactory.getLogger(clazz.java)