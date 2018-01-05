package me.foxaice.smartlight.models.interfaces

import me.foxaice.smartlight.constants.GroupId
import me.foxaice.smartlight.utils.KParcelable

interface IBulbModel : KParcelable {
    fun setCurrentGroup(@GroupId groupId: Int)
    fun setGroupNames(names: Array<String>)
    fun setCurrentGroupPower(powerOn: Boolean)
    fun setSpecificGroupState(@GroupId groupId: Int, powerOn: Boolean)
    fun getCurrentGroup(): Long
    fun getGroupNames(): Array<String>
    fun getCurrentGroupName(): String
    fun getSpecificGroupName(@GroupId groupId: Int): String
    fun isCurrentGroupOn(): Boolean
    fun isSpecificGroupOn(@GroupId groupId: Int): Boolean
}