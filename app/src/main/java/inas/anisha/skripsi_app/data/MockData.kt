package inas.anisha.skripsi_app.data

import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.db.entity.CycleEntity
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.data.db.entity.SchoolClassEntity
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import java.util.*

class MockData {
    companion object {
        fun getCycles(): List<CycleEntity> {
            return mutableListOf(
                CycleEntity(
                    0,
                    1,
                    80L,
                    "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                ),
                CycleEntity(
                    0,
                    2,
                    90L,
                    "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                ),
                CycleEntity(
                    0,
                    3,
                    100L,
                    "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                ),
                CycleEntity(
                    0,
                    4,
                    30L,
                    "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                )
            )
        }

        fun getSchoolClasses(): List<SchoolClassEntity> {
            return mutableListOf(
                SchoolClassEntity(
                    0,
                    "kelas 1",
                    Calendar.getInstance().apply { set(2020, 5, 6, 8, 0) },
                    Calendar.getInstance().apply { set(2020, 5, 6, 9, 30) },
                    Calendar.MONDAY
                ),
                SchoolClassEntity(
                    0,
                    "kelas 2",
                    Calendar.getInstance().apply { set(2020, 5, 6, 9, 30) },
                    Calendar.getInstance().apply { set(2020, 5, 6, 11, 0) },
                    Calendar.MONDAY
                ),
                SchoolClassEntity(
                    0,
                    "kelas 3",
                    Calendar.getInstance().apply { set(2020, 5, 6, 12, 0) },
                    Calendar.getInstance().apply { set(2020, 5, 6, 13, 30) },
                    Calendar.MONDAY
                ),
                SchoolClassEntity(
                    0,
                    "kelas 4",
                    Calendar.getInstance().apply { set(2020, 5, 7, 8, 0) },
                    Calendar.getInstance().apply { set(2020, 5, 7, 9, 30) },
                    Calendar.TUESDAY
                ),
                SchoolClassEntity(
                    0,
                    "kelas 5",
                    Calendar.getInstance().apply { set(2020, 5, 8, 10, 0) },
                    Calendar.getInstance().apply { set(2020, 5, 8, 11, 30) },
                    Calendar.WEDNESDAY
                )
            )
        }

        fun getSchedules(): List<ScheduleEntity> {
            return mutableListOf(
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_ACTIVITY,
                    "Kegiatan 1",
                    Calendar.getInstance().apply { set(2020, 5, 6, 11, 10) },
                    Calendar.getInstance().apply { set(2020, 5, 6, 11, 50) },
                    "makan siang bawa bekel yang sehat"
                ),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_ACTIVITY,
                    "Kegiatan 2",
                    Calendar.getInstance().apply { set(2020, 5, 6, 14, 10) },
                    Calendar.getInstance().apply { set(2020, 5, 6, 14, 40) },
                    "ngaji bentar"
                ),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_ACTIVITY,
                    "Kegiatan 3",
                    Calendar.getInstance().apply { set(2020, 5, 7, 11, 10) },
                    Calendar.getInstance().apply { set(2020, 5, 7, 11, 50) }),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_TEST,
                    "Test 1",
                    Calendar.getInstance().apply { set(2020, 5, 6, 8, 10) },
                    Calendar.getInstance().apply { set(2020, 5, 6, 10, 40) },
                    "ulangan biologi aduh belom belajar",
                    5
                ),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_TEST,
                    "Test 2",
                    Calendar.getInstance().apply { set(2020, 5, 7, 10, 10) },
                    Calendar.getInstance().apply { set(2020, 5, 7, 11, 10) },
                    "ulangan ulangin",
                    3
                ),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_TASK,
                    "Tugas 1",
                    null,
                    Calendar.getInstance().apply { set(2020, 5, 5, 10, 0) },
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
                    null,
                    Calendar.getInstance().apply { set(2020, 5, 5, 11, 0) },
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
                    null,
                    Calendar.getInstance().apply { set(2020, 5, 10, 11, 10) },
                    "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequa",
                    4
                ),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_TASK,
                    "Tugas 4",
                    null,
                    Calendar.getInstance().apply { set(2020, 5, 15, 11, 10) },
                    "ulangan ulangin",
                    3,
                    "",
                    Calendar.getInstance().apply { set(2020, 5, 6, 17, 50) }),
                ScheduleEntity(
                    0,
                    SkripsiConstant.SCHEDULE_TYPE_TASK,
                    "Tugas 5",
                    null,
                    Calendar.getInstance().apply { set(2020, 5, 20, 11, 10) },
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