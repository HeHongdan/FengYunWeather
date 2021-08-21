package me.wsj.fengyun.db.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * 城市对象(对应数据库)。
 *
 * @property cityId String
 * @property cityName String
 * @property isLocal Boolean
 */
@Entity(tableName = "city")
class CityEntity() {

    /** 城市Id。 */
    @PrimaryKey(autoGenerate = false)
    @NonNull
    var cityId: String = ""
    /** 城市名称。 */
    var cityName: String = ""
    /** 是否本地。 */
    var isLocal: Boolean = false

    /**
     * 城市的构造方法。
     *
     * @param id 城市Id。
     * @param name 城市名称。
     * @param local 是否本地。
     * @constructor 城市对象。
     */
    @Ignore
    constructor(id: String, name: String, local: Boolean = false) : this() {
        cityId = id
        cityName = name
        isLocal = local
    }
}