package inas.anisha.srl_app.data

import inas.anisha.srl_app.constant.SkripsiConstant
import inas.anisha.srl_app.data.db.entity.CycleEntity
import inas.anisha.srl_app.data.db.entity.ScheduleEntity
import inas.anisha.srl_app.data.db.entity.SchoolClassEntity
import inas.anisha.srl_app.data.db.entity.TargetPendukungEntity
import java.util.*

class MockData {
    companion object {
        fun getCycles(): List<CycleEntity> {
            return mutableListOf(
                CycleEntity(
                    0,
                    1,
                    80,
                    "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                ),
                CycleEntity(
                    0,
                    2,
                    90,
                    "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                ),
                CycleEntity(
                    0,
                    3,
                    100,
                    "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                ),
                CycleEntity(
                    0,
                    4,
                    30,
                    "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                )
            )
        }

        fun getSchoolClasses(): List<SchoolClassEntity> {
            return mutableListOf(
                SchoolClassEntity(
                    0,
                    "kelas 1",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 4, 1, 17, 0)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 4, 1, 17, 30)
                    },
                    Calendar.SATURDAY
                ),
                SchoolClassEntity(
                    0,
                    "kelas 2",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 17, 30)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 18, 0)
                    },
                    Calendar.SATURDAY
                ),
                SchoolClassEntity(
                    0,
                    "kelas 3",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 18, 0, 0)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 19, 0, 0)
                    },
                    Calendar.SATURDAY
                ),
                SchoolClassEntity(
                    0,
                    "kelas 4",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 20, 0, 0)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 21, 0, 0)
                    },
                    Calendar.SATURDAY
                ),
                SchoolClassEntity(
                    0,
                    "kelas 5",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 22, 0, 0)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 23, 0, 0)
                    },
                    Calendar.SATURDAY
                ),
                SchoolClassEntity(
                    0,
                    "kelas 6",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 26, 16, 0, 0)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 26, 17, 0, 0)
                    },
                    Calendar.SUNDAY
                )
            )
        }

        fun getSchedules(): List<ScheduleEntity> {
            return mutableListOf(
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_ACTIVITY,
                    "Kegiatan 1",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 16, 0, 0)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 16, 10, 0)
                    },
                    "makan siang bawa bekel yang sehat"
                ),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_ACTIVITY,
                    "Kegiatan 2",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 16, 10, 0)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 17, 10, 0)
                    },
                    "ngaji bentar"
                ),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_ACTIVITY,
                    "Kegiatan 3",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 16, 0, 0)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 17, 0, 0)
                    }),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_ACTIVITY,
                    "Kegiatan 4",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 18, 10, 0)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 17, 30, 0)
                    }),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_TEST,
                    "Test 1",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 18, 10, 0)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 20, 40, 0)
                    },
                    "ulangan biologi aduh belom belajar",
                    5
                ),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_TEST,
                    "Test 2",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 10, 10)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 3, 25, 11, 10)
                    },
                    "ulangan ulangin",
                    3
                ),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_TASK,
                    "Tugas 1",
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 4, 1, 10, 0)
                    },
                    Calendar.getInstance().apply {
                        set(Calendar.MILLISECOND, 0)
                        set(2020, 4, 1, 10, 0)
                    },
                    "Tugas 1",
                    4,
                    "marugame",
                    null,
                    true,
                    true
                ),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_TASK,
                    "Tugas 2",
                    Calendar.getInstance().apply { set(2020, 4, 1, 21, 0) },
                    Calendar.getInstance().apply { set(2020, 4, 1, 21, 0) },
                    "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequa",
                    5,
                    "main harvest moon",
                    null,
                    true,
                    false
                ),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_TASK,
                    "Tugas 3",
                    Calendar.getInstance().apply { set(2020, 4, 3, 21, 40) },
                    Calendar.getInstance().apply { set(2020, 4, 3, 21, 40) },
                    "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequa",
                    4
                ),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_TASK,
                    "Tugas 4",
                    Calendar.getInstance().apply { set(2020, 4, 4, 13, 10) },
                    Calendar.getInstance().apply { set(2020, 4, 4, 13, 10) },
                    "ulangan ulangin",
                    3,
                    "",
                    Calendar.getInstance().apply { set(2020, 3, 30, 17, 50) }),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_TASK,
                    "Tugas 5",
                    Calendar.getInstance().apply { set(2020, 4, 2, 11, 10) },
                    Calendar.getInstance().apply { set(2020, 4, 2, 11, 10) },
                    "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequa",
                    0
                )

            )
        }

        fun getSupportingTargets(): List<TargetPendukungEntity> {
            return mutableListOf(
                TargetPendukungEntity(
                    0,
                    "Belajar tiap hari senin",
                    "bentar aja gapapa yang penting buka buku",
                    "selasa malam"
                ),
                TargetPendukungEntity(
                    0,
                    "Dapat nilai 60 sekali aja",
                    "biasanya 0 terus :( ",
                    "sekali",
                    true
                ),
                TargetPendukungEntity(
                    0,
                    "Tidur 10 jam sehari",
                    "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitat",
                    "tiap malam"
                )
            )
        }
    }
}