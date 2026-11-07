package com.aura.tv.domain.models

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class InstalledAppTest {

    @Test
    fun testInstalledAppProperties() {
        val app = InstalledApp(
            packageName = "com.test.app",
            activityName = "com.test.app.MainActivity",
            label = "Test App",
            isLeanback = true,
            isFavorite = true
        )

        assertEquals("com.test.app", app.packageName)
        assertEquals("com.test.app.MainActivity", app.activityName)
        assertEquals("Test App", app.label)
        assertTrue(app.isLeanback)
        assertTrue(app.isFavorite)
    }
}
