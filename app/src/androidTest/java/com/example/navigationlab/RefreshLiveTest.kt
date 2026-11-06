package com.example.navigationlab

import android.os.ParcelFileDescriptor
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RefreshLiveTest {
    @get:Rule val rule = createAndroidComposeRule<MainActivity>()
    private fun waitText(text: String) {
        rule.waitUntil(45000) { rule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText(text).assertIsDisplayed()
    }
    private fun search(query: String) {
        rule.onNodeWithTag("search_query").performTextReplacement(query)
        closeSoftKeyboard()

    }
    @Test fun searchDetailBackAndEmptyQueryUseRealApi() {
        waitText("Daftar awal (10 profil)")
        screenshot("01_sebelum_refresh_awal")
        rule.onNodeWithTag("refresh_button").performClick()
        waitText("Daftar awal (10 profil)")
        screenshot("01_sesudah_refresh_awal")
        rule.onNodeWithTag("search_query").performTextReplacement("John")
        closeSoftKeyboard()


        waitText("John Doe")
        rule.onNodeWithText("Emily Johnson").assertIsDisplayed()
        screenshot("02_sebelum_refresh_pencarian")
        rule.onNodeWithTag("refresh_button").performClick()
        waitText("John Doe")
        screenshot("03_sesudah_refresh_pencarian")
        rule.onNodeWithTag("student_102").performClick()
        waitText("Nama: John Doe")
        rule.onNodeWithText("Email: john.doe@x.dummyjson.com").assertIsDisplayed()
        rule.onNodeWithText("University: Brown University").assertIsDisplayed()
        screenshot("03_detail_hasil_pencarian")
        rule.onNodeWithTag("back_button").performClick()
        waitText("John Doe")
        rule.onNodeWithTag("search_query").assertTextContains("John")
        screenshot("04_kembali_hasil_pencarian")
        search("   ")
        waitText("Daftar awal (10 profil)")
        screenshot("05_query_kosong")
        rule.onNodeWithText("Profile").performClick()
        waitText("NIM: 245150207111102")
        rule.onNodeWithText("Kembali").performClick()
        rule.onNodeWithText("About").performClick()
        waitText("NavigationLab")
        rule.onNodeWithText("Kembali").performClick()
        waitText("Daftar awal (10 profil)")
    }
    @Test fun noMatchesIsNotANetworkError() {
        waitText("Daftar awal (10 profil)")
        search("zzznoprofile987654")
        waitText("Tidak ada profil yang cocok. Coba kata lain.")
        rule.onNodeWithTag("home_error").assertDoesNotExist()
        screenshot("06_hasil_kosong")
    }
    @Test fun offlineSearchAndRetryRecoverSameQuery() {
        waitText("Daftar awal (10 profil)")
        try {
            shell("svc wifi disable"); shell("svc data disable")
            Thread.sleep(1800)
            search("John")
            waitText("Gagal memuat profil")
            rule.onNodeWithTag("search_query").assertTextContains("John")
            screenshot("07_pencarian_offline")
            shell("svc wifi enable"); shell("svc data enable")
            Thread.sleep(6000)
            rule.onNodeWithTag("refresh_button").performClick()
            waitText("John Doe")
            screenshot("08_retry_berhasil")
        } finally {
            shell("svc wifi enable"); shell("svc data enable")
        }
    }
    private fun shell(command: String) {
        ParcelFileDescriptor.AutoCloseInputStream(InstrumentationRegistry.getInstrumentation()
            .uiAutomation.executeShellCommand(command)).use { it.readBytes() }
    }
    private fun screenshot(name: String) {
        rule.waitForIdle()
        shell("mkdir -p /sdcard/Pictures/NavLabPraktikum08")
        shell("screencap -p /sdcard/Pictures/NavLabPraktikum08/$name.png")
    }
}
