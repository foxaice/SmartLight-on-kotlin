package me.foxaice.smartlight.constants

import android.support.annotation.IntDef

@IntDef(ALL_GROUP, GROUP_1, GROUP_2, GROUP_3, GROUP_4)
@Retention(AnnotationRetention.SOURCE)
annotation class GroupId

const val ALL_GROUP = 0L
const val GROUP_1 = 1L
const val GROUP_2 = 2L
const val GROUP_3 = 3L
const val GROUP_4 = 4L

const val KEY_BULB_MODEL = "KEY_BULB_MODEL"

const val BULB_QUANTITY = 5
