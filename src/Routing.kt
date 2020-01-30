package com.rarnu.ncov

import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import org.json.JSONArray
import org.json.JSONObject

private val dataUrl: String get() = "https://view.inews.qq.com/g2/getOnsInfo?name=wuwei_ww_area_counts&callback=&_=${System.currentTimeMillis()}"
private val dailyUrl: String get() = "https://view.inews.qq.com/g2/getOnsInfo?name=wuwei_ww_cn_day_counts&callback=&_=${System.currentTimeMillis()}"

fun Routing.nCoVMapRouting() {

    /**
     * 获取中国地图数据
     */
    get("/map") {
        call.respondText {
            try {
                JSONArray(JSONObject(HttpClient().get<String>(dataUrl)).optString("data")).filter { country ->
                    (country as JSONObject).optString("country") == "中国"
                }.groupBy { area ->
                    (area as JSONObject).optString("area")
                }.mapValues { confirm ->
                    confirm.value.sumBy { item ->
                        (item as JSONObject).optInt("confirm", 0)
                    }
                }.stringIntToJson()
            } catch (th: Throwable) {
                println("error => $th")
                "[]"
            }
        }
    }

    /**
     * 获取每日疫情数据
     */
    get("/daily") {
        call.respondText {
            try {
                val mDate = mutableListOf<String>()
                val mConfirm = mutableListOf<Int>()
                val mSuspect = mutableListOf<Int>()
                val mDead = mutableListOf<Int>()
                val mHeal = mutableListOf<Int>()
                JSONArray(JSONObject(HttpClient().get<String>(dailyUrl)).optString("data")).sortedBy { item ->
                    (item as JSONObject).getString("date")
                }.forEach { item ->
                    with(item as JSONObject) {
                        mDate.add(getString("date").trim())
                        mConfirm.add(getString("confirm").trim().toInt())
                        mSuspect.add(getString("suspect").trim().toInt())
                        mDead.add(getString("dead").trim().toInt())
                        mHeal.add(getString("heal").trim().toInt())
                    }
                }
                """{"date":${mDate.stringListToJson()},"confirm":${mConfirm.intListToJson()},"suspect":${mSuspect.intListToJson()},"dead":${mDead.intListToJson()},"heal":${mHeal.intListToJson()}}"""
            } catch (th: Throwable) {
                println("error => $th")
                """{"date":[],"confirm":[],"suspect":[],"dead":[],"heal":[]}"""
            }
        }
    }

    /**
     * 获取全部详情数据
     */
    get("/detail") {
        call.respondText {
            try {
                JSONArray(JSONObject(HttpClient().get<String>(dataUrl)).optString("data")).filter { country ->
                    (country as JSONObject).optString("country") == "中国"
                }.groupBy { area ->
                    (area as JSONObject).optString("area")
                }.mapKeys { item ->
                    val sumConfirm = item.value.sumBy { i -> (i as JSONObject).optInt("confirm", 0) }
                    val sumDead = item.value.sumBy { i -> (i as JSONObject).optInt("dead", 0) }
                    val sumHeal = item.value.sumBy { i -> (i as JSONObject).optInt("heal", 0) }
                    DataArea(item.key, sumConfirm, sumDead, sumHeal)
                }.mapValues { item ->
                    item.value.sortedByDescending { i -> (i as JSONObject).optInt("confirm") }.map { i ->
                        with(i as JSONObject) {
                            DataCity(getString("city"), getInt("confirm"), getInt("dead"), getInt("heal"))
                        }
                    }
                }.dataToJson()
            } catch (th: Throwable) {
                "[]"
            }
        }

    }
}