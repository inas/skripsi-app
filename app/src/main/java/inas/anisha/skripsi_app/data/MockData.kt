package inas.anisha.skripsi_app.data

import inas.anisha.skripsi_app.data.db.entity.CycleEntity

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
    }
}