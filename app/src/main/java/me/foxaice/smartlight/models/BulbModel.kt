package me.foxaice.smartlight.models

import android.os.Parcel
import me.foxaice.smartlight.constants.*
import me.foxaice.smartlight.entities.Bulb
import me.foxaice.smartlight.models.interfaces.IBulbModel
import me.foxaice.smartlight.utils.parcelableCreator
import java.util.*


class BulbModel : IBulbModel {
    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::BulbModel)
        val ERROR_MESSAGE = String.format(Locale.ENGLISH, "Must be between %d and %d inclusive", 0, BULB_QUANTITY - 1)
    }

    private var mCurrentBulb: Bulb
    private val mBulbs: List<Bulb>
    private var mBulbsNames: MutableList<String> = mutableListOf()

    constructor() {
        mBulbs = listOf(
                Bulb(ALL_GROUP),
                Bulb(GROUP_1),
                Bulb(GROUP_2),
                Bulb(GROUP_3),
                Bulb(GROUP_4)
        )
        mCurrentBulb = mBulbs[0]
    }

    constructor(parcel: Parcel) {
        mCurrentBulb = parcel.readParcelable(Bulb::class.java.classLoader)
        mBulbs = parcel.createTypedArrayList(Bulb.CREATOR)
    }

    @Suppress("unused")
            //todo change this constructor to primary ???
    constructor(@GroupId groupId: Int, isOn: Boolean) : this() {
        checkGroupNumberIntoRange(groupId)
        mCurrentBulb = mBulbs[groupId]
        mCurrentBulb.isOn = isOn
    }

    override fun setCurrentGroup(@GroupId groupId: Int) {
        checkGroupNumberIntoRange(groupId)
        mCurrentBulb = mBulbs[groupId]
    }

    override fun setGroupNames(names: Array<String>) {
        for (i in names.indices) {
            mBulbs[i].name = names[i]
        }
    }

    override fun setCurrentGroupPower(powerOn: Boolean) {
        if (mCurrentBulb.id == ALL_GROUP) {
            for (item in mBulbs) {
                item.isOn = powerOn
            }
        } else {
            mCurrentBulb.isOn = powerOn
        }
    }

    override fun setSpecificGroupState(groupId: Int, powerOn: Boolean) {
        checkGroupNumberIntoRange(groupId)
        mBulbs[groupId].isOn = powerOn
    }

    override fun getCurrentGroup(): Long {
        return mCurrentBulb.id
    }

    override fun getGroupNames(): Array<String> {
        if (mBulbsNames.isEmpty()) {
            for (item in mBulbs) {
                mBulbsNames.add(item.name)
            }
        }
        return mBulbsNames.toTypedArray()
    }

    override fun getCurrentGroupName(): String {
        return mCurrentBulb.name
    }

    override fun getSpecificGroupName(groupId: Int): String {
        checkGroupNumberIntoRange(groupId)
        return mBulbs[groupId].name
    }

    override fun isCurrentGroupOn(): Boolean {
        return mCurrentBulb.isOn
    }

    override fun isSpecificGroupOn(groupId: Int): Boolean {
        checkGroupNumberIntoRange(groupId)
        return mBulbs[groupId].isOn
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(mCurrentBulb, flags)
        dest.writeTypedList(mBulbs)
    }

    private fun checkGroupNumberIntoRange(@GroupId groupId: Int) {
        if (groupId !in 0 until BULB_QUANTITY) throw IllegalArgumentException(ERROR_MESSAGE)
    }
}