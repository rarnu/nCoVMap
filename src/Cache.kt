package com.rarnu.ncov

const val DATA_EXPIRE = 2 * 60 * 60 * 1000  // 设定过期时间为 2 小时

data class DataCache(val datetime: Long, val data: String)

/**
 * 地图缓存
 */
var mapCache: DataCache? = null
/**
 * 每日疫情缓存
 */
var dailyCache: DataCache? = null
/**
 * 详情数据缓存
 */
var detailCache: DataCache? = null
