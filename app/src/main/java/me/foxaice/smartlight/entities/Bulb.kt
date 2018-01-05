package me.foxaice.smartlight.entities

import android.os.Parcel
import me.foxaice.smartlight.constants.GroupId
import me.foxaice.smartlight.utils.KParcelable
import me.foxaice.smartlight.utils.parcelableCreator

data class Bulb(@GroupId val id: Long, var name: String = "", var isOn: Boolean) : KParcelable {
    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Bulb)
    }

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    constructor(@GroupId id: Long) : this(id = id, isOn = false)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
        dest.writeByte(if (isOn) 1 else 0)
    }
}