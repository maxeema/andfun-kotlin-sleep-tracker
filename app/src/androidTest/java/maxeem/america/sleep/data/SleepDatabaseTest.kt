/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package maxeem.america.sleep.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, logNight building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var sleepDao : NightsDao
    private lateinit var db       : NightsDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears logNight the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, NightsDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        sleepDao = db.nightsDao
    }

    @After @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test @Throws(Exception::class)
    fun checkOnEmpty() {
        val tonight = sleepDao.tonight()
        println(" tonight is $tonight")
        assertEquals(tonight, null)
    }

    @Test @Throws(Exception::class)
    fun testInsertUpdateDelete() {
        val night = Night(quality = Night.Quality.SO_SO) //so-so
        sleepDao.insert(night)
        val tonight = sleepDao.tonight()!!
        assertEquals(tonight.quality, Night.Quality.SO_SO)
        tonight.quality = Night.Quality.PRETTY_GOOD
        sleepDao.update(tonight)
        assertEquals(sleepDao.tonight()!!.quality, Night.Quality.PRETTY_GOOD)
        sleepDao.delete(tonight)
        assertEquals(sleepDao.get(tonight.id!!), null)
    }

    @Test @Throws(Exception::class)
    fun insertMultipleAndGetTonight() {
        val nightFirst  = Night(quality = Night.Quality.EXCELLENT)
        val nightSecond = Night(quality = Night.Quality.POOR)
        val nightThird  = Night(quality = Night.Quality.OK)
        sleepDao.insert(nightFirst)
        sleepDao.insert(nightSecond)
        sleepDao.insert(nightThird)
        val tonight = sleepDao.tonight()
        println("tonight is $tonight")
        assertEquals(tonight!!.quality, Night.Quality.OK)
    }

}
