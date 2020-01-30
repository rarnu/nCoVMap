package com.rarnu.ncov

fun List<DataCity>.cityToJson() = """[${joinToString(",") { """{"city":"${it.city}","confirm":${it.confirm},"dead":${it.dead},"heal":${it.heal}}""" }}]"""
fun Map<DataArea, List<DataCity>>.dataToJson() = """[${toList().joinToString(",") { """{"area":"${it.first.area}","confirm":${it.first.confirm},"dead":${it.first.dead},"heal":${it.first.heal},"cities":${it.second.cityToJson()}}""" }}]"""

fun Map<String, Int>.stringIntToJson() = """[${toList().joinToString(",") { """{"name":"${it.first}","value":${it.second}}""" }}]"""
fun List<String>.stringListToJson() = """[${joinToString(",") { "\"$it\"" }}]"""
fun List<Int>.intListToJson() = """[${joinToString(",") { "$it" }}]"""

